package com.btb.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.java_websocket.client.WebSocketClient;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSON;
import com.btb.entity.Bitbinfo;
import com.btb.entity.BuyMoneyTypeRate;
import com.btb.entity.MarketDepth;
import com.btb.entity.MarketOrder;
import com.btb.entity.Markethistory;
import com.btb.entity.Rate;
import com.btb.entity.PlatformInfo;
import com.btb.entity.PlatformSupportmoney;
import com.btb.entity.TodayOpenMoney;
import com.btb.tasks.BitbCountJob;
import com.btb.tasks.BuyParmeterJob;
import com.btb.tasks.CheckWebSocketStatusJob;
import com.btb.tasks.InitBuyMonetyTypeRate;
import com.btb.tasks.InitTodayOpenJob;
import com.btb.tasks.MarketHistoryKlineJob;
import com.btb.tasks.RateJob;
import com.btb.tasks.service.JobManager;
import com.btb.tasks.threads.BuyParmeterThread;
import com.btb.tasks.threads.MarketHistoryKlineTread;
import com.btb.util.dao.BaseDaoSql;
import com.btb.util.thread.ThreadPoolManager;

public class TaskUtil {
	//汇率表缓存,每隔一天更新一次
	public static Map<String, BigDecimal> rateMap = new HashMap<>();
	//http, <平台id,采集类>
	public static Map<String,BaseHttp> httpBeans=new HashMap<>();
	//交易对<平台id,交易对集合>
	public static Map<String,List<PlatformSupportmoney>> moneyPairs=new HashMap<>();
	//每个平台的websocketClient链接
	public static Map<String, WebSocketClient> webSocketClientMap = new HashMap<>();
	//每个交易对,今日开盘价格Map<平台id.交易对,最新价格>
	public static Map<String, BigDecimal> todayOpen = new HashMap<>();
	//获取每个平台的btc,eth实时价格,用于转换成人民币
	//Map<交易平台id.btc/eth/等等>
	public static Map<String, BigDecimal> buyMonetyTypeRate=new HashMap<>();
	//买卖盘Map<交易平台id.交易对,top10订单集合>
	public static Map<String, MyList<MarketOrder>> orders = new HashMap<>();
	//比特币当前数量:用于计算流通市值bitbCountMap
	public static Map<String, BigDecimal> bitbCountMap = new HashMap<>();
	//手动填写,所有平台的id集合
	public static Set<String> platformids=new HashSet<>();
	public static void main(String[] args) {
		//initStartAll();
		//taskList();
		InitAllHttpUtils();
		initWaiHuiToDB();
		httpBuyMoneyTypeKline(null);
		
	}
	
	public static void initStartAll() {
		//初始化所有的HttpUtil,方便采集k线图和交易对
		System.out.println("正在初始化HttpUtils");
		InitAllHttpUtils();
		
		//采集所有平台中的交易对,同步//从数据库里面加载防止采集交易对不成功
		System.out.println("正在从数据库加载交易对");
		TaskUtil.initMoneypairByDB();
		
		//同步到数据里面获取外汇利率
		System.out.println("正在从数据库加载外汇汇率");
		TaskUtil.initWaiHuiToDB();
		
		//启动加载每个平台的btc,eth价格, 每1.5分钟执行一次,从数据库采集
		System.out.println("从数据库抓取btc和ehc的最新价格");
		TaskUtil.initBuyMonetyTypeRate();
		//获取每个平台,每个交易对的 今日开盘价格, 从k线图里面获取,每1.5分钟跑一次
		System.out.println("正在加载今日每种交易对的开盘价格,也就是0晨的收盘价格");
		TaskUtil.initTodayOpen();
		
		System.out.println("每一小时获取一次流通量");
		TaskUtil.getBitbCount();
	}
	
	public static void taskList() {
		//每隔30秒检查一次所有websoket的链接状态,如果断链,重新链接
		System.out.println("每隔30秒检查一次所有websoket的链接状态,如果断链,重新链接");
		JobManager.addJob(new CheckWebSocketStatusJob());
		
		//所有平台的行情数据采集,,由于需要实时性,所以只能使用websoket
		//获取所有平台的websoket类
		System.out.println("启动所有平台的websoket");
		enableWebSocket();
		
		//每隔1小时获取一次,比特币的数量
		System.out.println("每隔1小时获取一次,比特币的数量");
		JobManager.addJob(new BitbCountJob());
		
		//获取每个平台,每个交易对的 今日开盘价格, 从k线图里面获取,每1.5分钟跑一次
		System.out.println("获取每个平台,每个交易对的 今日开盘价格, 从k线图里面获取,每1.5分钟跑一次");
		JobManager.addJob(new InitTodayOpenJob());
		
		//启动加载每个平台的btc,eth价格, 每1.5分钟执行一次
		System.out.println("启动加载每个平台的btc,eth价格也就是buymonetyType, 每1.5分钟执行一次");
		JobManager.addJob(new InitBuyMonetyTypeRate());
		
		//--采集每个平台支持的交易对, 多少平台多少线程
		System.out.println("采集每个平台支持的交易对, 多少平台多少线程,调用httpUtil的采集交易对");
		JobManager.addJob(new BuyParmeterJob());
		
		
		//采集k线图分钟数据,每1.5分钟执行一次, 每个平台一个线程,大概200个线程
		//获取平台所有交易对
		System.out.println("采集k线图分钟数据,每1.5分钟执行一次, 每个平台一个线程,大概200个线程");
		JobManager.addJob(new MarketHistoryKlineJob());
		
		//银行利率每天执行一次
		System.out.println("//银行利率每天执行一次");
		JobManager.addJob(new RateJob());
		
	}
	
	//每隔1小时获取一次,比特币的数量
	public static void getBitbCount() {
		//比特币流通量
		List<Bitbinfo> selectAll2 = BaseDaoSql.findAll(Bitbinfo.class);
		for (Bitbinfo bitbInfo : selectAll2) {
			//放到集合里面
			if (bitbInfo.getCurrentcount() != null) {
				TaskUtil.bitbCountMap.put(bitbInfo.getBitbcode().toLowerCase(), bitbInfo.getCurrentcount());
			}
		}
	}
	
	/**
	 * 初始化CacheData.moneyPairs,所有平台的交易对
	 */
	public static void initMoneypair(Boolean isTask) {
		Set<String> platformids = TaskUtil.platformids;
		for (String platformid : platformids) {
			BuyParmeterThread buyParmeterThread = new BuyParmeterThread(platformid);
			if (isTask) {
				ThreadPoolManager.workNoResult(buyParmeterThread);
			}else {
				buyParmeterThread.run();//同步运行
			}
		}
	}
	//初始化所有的HttpUtil,启动执行一次
	public static void InitAllHttpUtils() {
		Map<String, BaseHttp> beanHttpMap = SpringUtil.context.getBeansOfType(BaseHttp.class);
		for (BaseHttp baseHttp : beanHttpMap.values()) {
			TaskUtil.httpBeans.put(baseHttp.getPlatformId(), baseHttp);
			for (BaseHttp baseHttp2 : TaskUtil.httpBeans.values()) {
				platformids.add(baseHttp2.getPlatformId());
			}
		}
	}
	//从db里面加载所有交易对
	public static void initMoneypairByDB() {
		for (String platformid : platformids) {
			List<PlatformSupportmoney> findmoneypairByplatformid = BaseDaoSql.selectList("findmoneypairByplatformid", platformid);
			TaskUtil.moneyPairs.put(platformid, findmoneypairByplatformid);
		}
 	}
	
	
	/**
	 * 从数据库里面获取外汇数据防止不能使用
	 */
	public static void initWaiHuiToDB() {
		List<Rate> selectAll = BaseDaoSql.findAll(Rate.class);
		for (Rate rate : selectAll) {
			if (rate.getRate() != null) {
				TaskUtil.rateMap.put(rate.getMoneycode(), rate.getRate());
			}
		}
		
	}
	
	
	//加载k线图数据
	public static void initKline(Boolean isTask) {
		Map<String, List<PlatformSupportmoney>> moneyPairsMap = TaskUtil.moneyPairs;
		for (String platformid : moneyPairsMap.keySet()) {
			MarketHistoryKlineTread marketHistoryKlineTread = new MarketHistoryKlineTread(platformid);
			if (isTask) {
				ThreadPoolManager.workNoResult(marketHistoryKlineTread);
			}else {
				marketHistoryKlineTread.run();
			}
		}
	}
	
	//加载数据库今日开盘价格
	public static void initTodayOpen() {
		//今日开盘价格
		for (String platformid : platformids) {
			TodayOpenMoney todayOpenMoney = new TodayOpenMoney();
			todayOpenMoney.setPlatformid(platformid);
			List<TodayOpenMoney> list = BaseDaoSql.findList("select * from todayopenmoney where platformid = '"+platformid+"'", TodayOpenMoney.class);
			for (TodayOpenMoney todayOpenMoney2 : list) {
				todayOpen.put(todayOpenMoney2.getId(), todayOpenMoney2.getOpen());
			}
		}
		
		
	}
	
	//启动加载每个平台的btc,eth价格, 每1小时执行一次
	public static void initBuyMonetyTypeRate() {
		//加载每个平台所有buymoneytype的人民币价格汇率
		String platformidsStr="";
		for (String string : platformids) {
			platformidsStr+="'"+string+"',";
		}
		if (StringUtil.hashText(platformidsStr)) {
			platformidsStr=platformidsStr.substring(0, platformidsStr.length()-1);
		}
		List<BuyMoneyTypeRate> selectAll3 = BaseDaoSql.selectList("findBuyMoneyTypeRate",platformidsStr);
		for (BuyMoneyTypeRate buyMoneyTypeRate1 : selectAll3) {
			int update = BaseDaoSql.update(buyMoneyTypeRate1);
			if (update==0) {
				BaseDaoSql.save(buyMoneyTypeRate1);
			}
			buyMonetyTypeRate.put(buyMoneyTypeRate1.getId(), buyMoneyTypeRate1.getClosermb());
		}
	}
	
	//开启websocket服务
	public static void enableWebSocket() {
		List<Class<WebSocketClient>> webSocketUtils = StringUtil.getAllWebSocketUtils();
		for (Class<WebSocketClient> webSocketUtil : webSocketUtils) {
			try {
				Method method = webSocketUtil.getMethod("executeWebSocket");
				WebSocketClient webSocketClient = (WebSocketClient)method.invoke(null, null);
				TaskUtil.webSocketClientMap.put(webSocketUtil.getMethod("getPlatFormId").invoke(null, null).toString(), webSocketClient);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//采集订单的时候调用这个
	public static void putOrders(String platformId,String moneytype,String buymoneytype,MarketOrder marketOrder) {
		String _id = platformId+"."+moneytype+"_"+buymoneytype;
		marketOrder.setPrice(StringUtil.ToRmb(marketOrder.getPrice(), platformId, buymoneytype));
		MyList<MarketOrder> myList = orders.get(_id);
		if (myList == null) {//不存在创建
			myList = new MyList<>();
			myList.add(marketOrder);
			orders.put(_id, myList);
		}else {
			myList.add(marketOrder);
		}
		if (myList.size()>=10) {//够10个存储到mongodb
			MongoDbUtil.insertOrUpdateOrderTab(_id, JSON.toJSONString(myList));
		}
	}
	//采集订单的时候调用这个
	public static void putBuySellDisk(String platformId,String moneytype,String buymoneytype,MarketDepth marketDepth) {
		if (marketDepth != null) {
			String _id = platformId+"."+moneytype+"_"+buymoneytype;
			//价格转人民币
			for (BigDecimal[] ask : marketDepth.getAsks()) {
				ask[0]=StringUtil.ToRmb(ask[0], platformId, buymoneytype);
			}
			for (BigDecimal[] bid : marketDepth.getBids()) {
				bid[0]=StringUtil.ToRmb(bid[0], platformId, buymoneytype);
			}
			MongoDbUtil.insertOrUpdateBuySellDiskTab(_id, JSON.toJSONString(marketDepth));
		}
	}
		
	/**
	 * 加载所有平台的btc_usdt,和eth_usdt,这两个是最基础的需要先计算
	 * 只在线下计算,不放在在启动中
	 */
	public static void httpBuyMoneyTypeKline(String platformidTest) {
		for (String platformid : platformids) {
			if (platformidTest!=null && !platformid.equals(platformidTest)) {
				continue;
			}
			List<PlatformSupportmoney> moneyPairs = BaseDaoSql.findList("select * from platformsupportmoney where platformid='"+platformid+"' and buymoneytype='usdt'", PlatformSupportmoney.class);
			for (PlatformSupportmoney thirdpartysupportmoney : moneyPairs) {
				Markethistory marketHistory = new Markethistory();
				marketHistory.setPlatformid(platformid);
				marketHistory.setMoneypair(thirdpartysupportmoney.getMoneypair());
				marketHistory.setMoneytype(thirdpartysupportmoney.getMoneytype());
				marketHistory.setBuymoneytype(thirdpartysupportmoney.getBuymoneytype());
				long currentTime = System.currentTimeMillis()/1000;//换算成秒级
				Long dbCurrentTime=BaseDaoSql.selectOne("getMaxTimeId", marketHistory);
				if (dbCurrentTime == null) {//如果第一次获取数据,直接过去一天数据
					dbCurrentTime=currentTime-24*60*60;
				}
				long size = (currentTime-dbCurrentTime)/60;
				if (size > 1440) {//大于一天就取一天的数据
					size=1440;
				}
				if (size != 0) {
					BaseHttp baseHttp = TaskUtil.httpBeans.get(marketHistory.getPlatformid());
					baseHttp.getKLineData(marketHistory, size, dbCurrentTime);
				}
			}
			
		}
	}
	
}
