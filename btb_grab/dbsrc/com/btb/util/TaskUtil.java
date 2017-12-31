package com.btb.util;

import java.io.IOException;
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
import com.btb.dao.BitbinfoMapper;
import com.btb.dao.MarketHistoryMapper;
import com.btb.dao.MarketMapper;
import com.btb.dao.RateMapper;
import com.btb.dao.ThirdpartyplatforminfoMpper;
import com.btb.dao.ThirdpartysupportmoneyMapper;
import com.btb.entity.Bitbinfo;
import com.btb.entity.Market;
import com.btb.entity.MarketDepthVo;
import com.btb.entity.Markethistory;
import com.btb.entity.QueryVo;
import com.btb.entity.Rate;
import com.btb.entity.Thirdpartyplatforminfo;
import com.btb.entity.Thirdpartysupportmoney;
import com.btb.huobi.HttpUtil;
import com.btb.tasks.threads.BuyParmeterThread;
import com.btb.tasks.threads.MarketHistoryKlineTread;
import com.btb.util.thread.ThreadPoolManager;

public class TaskUtil {
	//汇率表缓存,每隔一天更新一次
	public static Map<String, BigDecimal> rateMap = new HashMap<>();
	//http, <平台id,采集类>
	public static Map<String,BaseHttp> httpBeans=new HashMap<>();
	//交易对<平台id,交易对集合>
	public static Map<String,List<Thirdpartysupportmoney>> moneyPairs=new HashMap<>();
	//每个平台的websocketClient链接
	public static Map<String, WebSocketClient> webSocketClientMap = new HashMap<>();
	//每个交易对,今日开盘价格Map<平台id.交易对,最新价格>
	public static Map<String, BigDecimal> todayOpen = new HashMap<>();
	//获取比特币流通量,每隔一小时采集
	public static Map<String, BigDecimal> bitbCountMap = new HashMap<>();
	//获取每个平台的btc,eth实时价格,用于转换成人民币
	//Map<交易平台id.btc/eth>
	public static Map<String, BigDecimal> nowBtcEthRmb=new HashMap<>();
	//买卖盘Map<交易平台id.交易对,买卖盘>
	public static Map<String, MarketDepthVo> sellBuyDisk = new HashMap<>();
	//全网数据,定时1分钟执行一次
	public static Map<String, Map<String, Object>> bxsMarket = new HashMap<>();
	//手动填写,所有平台的id集合
	public static Set<String> platformids=new HashSet<>();
	static{
		//从config.properties里面读取 平台id
		String[] string = MyConfig.get("platformids").split(",");
		for (String string2 : string) {
			platformids.add(string2);
		}
	}
	public static void main(String[] args) {
		initStartAll();
	}
	
	public static void initStartAll() {
		//初始化所有的HttpUtil,方便采集k线图和交易对
		System.out.println("正在初始化HttpUtils");
		InitAllHttpUtils();
		
		//采集所有平台中的交易对,同步//从数据库里面加载防止采集交易对不成功
		System.out.println("正在从数据库加载交易对");
		TaskUtil.initMoneypairByDB();
		System.out.println(TaskUtil.moneyPairs);
		/*
		//同步到数据里面获取外汇利率
		System.out.println("正在从数据库加载外汇汇率");
		TaskUtil.initWaiHuiToDB();
		//启动加载每个平台的btc,eth价格, 每1.5分钟执行一次,从数据库采集
		System.out.println("从数据库抓取btc和ehc的最新价格");
		TaskUtil.initBtcEthNowMoney();
		//获取每个平台,每个交易对的 今日开盘价格, 从k线图里面获取,每1.5分钟跑一次
		System.out.println("正在加载今日每种交易对的开盘价格,也就是0晨的收盘价格");
		TaskUtil.initTodayOpen();
		//初始化所有实时行情信息,到h2数据库中
		//System.out.println("将实时行情信息的基础信息加载到h2中");不需要
		//TaskUtil.initMarketAllToH2DB();
		//加载今日实时行情数据,每1.5钟跑一次,更改h2内存数据,最高价,最低价,交易量,交易额
		//System.out.println("将今日相关数据放入h2内存数据中");
		//TaskUtil.initTodayNewData();
		//从数据库里面加载最新的比特币数量,这个不需要做成任务
		System.out.println("从数据库加载比特币的数量,用于计算流通量");
		TaskUtil.initBtcCountByDb();
		//定时计算全网平均数据,每分钟计算一次,并推送到服务器端
		System.out.println("计算全网的平均数据");*/
	}
	

	

	
	/**
	 * 初始化CacheData.moneyPairs,所有平台的交易对
	 */
	public static void initMoneypair(Boolean isTask) {
		ThirdpartyplatforminfoMpper thirdpartyplatforminfoMpper = SpringUtil.getBean(ThirdpartyplatforminfoMpper.class);
		List<Thirdpartyplatforminfo> thirdpartyplatfos = thirdpartyplatforminfoMpper.selectAll();
		//循环多编程,采集每个平台的交易对
		for (Thirdpartyplatforminfo thirdpartyplatforminfo : thirdpartyplatfos) {
			BuyParmeterThread buyParmeterThread = new BuyParmeterThread(thirdpartyplatforminfo.getId());
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
		}
	}
	//从db里面加载所有交易对
	public static void initMoneypairByDB() {
		for (String platformid : platformids) {
			ThirdpartysupportmoneyMapper thirdpartysupportmoneyMapper=SpringUtil.getBean(ThirdpartysupportmoneyMapper.class);
			List<Thirdpartysupportmoney> findmoneypairByplatformid = thirdpartysupportmoneyMapper.findmoneypairByplatformid(platformid);
			TaskUtil.moneyPairs.put(platformid, findmoneypairByplatformid);
		}
 	}
	/**
	 * 从数据库里面加载最新的比特币数量
	 */
	public static void initBtcCountByDb() {
		BitbinfoMapper bitbinfoMapper = SpringUtil.getBean(BitbinfoMapper.class);
		List<Bitbinfo> selectAll = bitbinfoMapper.selectAll();
		for (Bitbinfo bitbInfo : selectAll) {
			//放到集合里面
			TaskUtil.bitbCountMap.put(bitbInfo.getBitbcode(), bitbInfo.getCurrentcount());
		}
	}
	
	
	/**
	 * 从数据库里面获取外汇数据防止不能使用
	 */
	public static void initWaiHuiToDB() {
		RateMapper rateMapper = SpringUtil.getBean(RateMapper.class);
		List<Rate> selectAll = rateMapper.selectAll();
		for (Rate rate : selectAll) {
			if (rate.getRate() != null) {
				TaskUtil.rateMap.put(rate.getMoneycode(), rate.getRate());
			}
		}
		
	}
	
	
	//加载k线图数据
	public static void initKline(Boolean isTask) {
		Map<String, List<Thirdpartysupportmoney>> moneyPairsMap = TaskUtil.moneyPairs;
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
		
	}
	
/*	//加载行情数据到h2数据库中
	public static void initMarketAllToH2DB() {
		MarketMapper marketMapper = SpringUtil.getBean(MarketMapper.class);
		List<Market> marketInfo = marketMapper.findAllMarketInfo();
		for (Market market : marketInfo) {
			H2Util.insertOrUpdate(market);
		}
	}*/
	
	//启动加载每个平台的btc,eth价格, 每1.5分钟执行一次
	public static void initBtcEthNowMoney() {
		MarketHistoryMapper marketMapper = SpringUtil.getBean(MarketHistoryMapper.class);
		List<QueryVo> list = marketMapper.findBestNewRmbByBtcAndEth();
		for (QueryVo queryVo : list) {
			TaskUtil.nowBtcEthRmb.put(queryVo.getPlatformid(), queryVo.getClosermb());
		}
	}
	/**
	 * 加载所有平台的btc_usdt,和eth_usdt,这两个是最基础的需要先计算
	 * 只在线下计算,不放在在启动中
	 */
	/*public static void initBaseBtcAndEthKline() {
		Map<String, BaseHttp> beanHttpMap = SpringUtil.context.getBeansOfType(BaseHttp.class);
		for (BaseHttp baseHttp : beanHttpMap.values()) {
			CacheData.httpBeans.put(baseHttp.getPlatformId(), baseHttp);
		}
		MarketHistoryMapper marketHistoryMapper = SpringUtil.getBean(MarketHistoryMapper.class);
		List<Thirdpartysupportmoney> moneyPairs = marketHistoryMapper.findAllPingTaiEthAndBtc();
		for (Thirdpartysupportmoney thirdpartysupportmoney : moneyPairs) {
			Markethistory marketHistory = new Markethistory();
			marketHistory.setPlatformid(thirdpartysupportmoney.getPlatformid());
			marketHistory.setMoneypair(thirdpartysupportmoney.getMoneypair());
			marketHistory.setMoneytype(thirdpartysupportmoney.getMoneytype());
			marketHistory.setBuymoneytype(thirdpartysupportmoney.getBuymoneytype());
			long currentTime = System.currentTimeMillis()/1000;//换算成秒级
			Long dbCurrentTime=marketHistoryMapper.getMaxTimeId(marketHistory);
			if (dbCurrentTime == null) {//如果第一次获取数据,直接过去一天数据
				dbCurrentTime=currentTime-24*60*60;
			}
			long size = (currentTime-dbCurrentTime)/60;
			if (size > 1440) {//大于一天就取一天的数据
				size=1440;
			}
			if (size != 0) {
				BaseHttp baseHttp = CacheData.httpBeans.get(marketHistory.getPlatformid());
				baseHttp.getKLineData(marketHistory, marketHistoryMapper, size, dbCurrentTime);
			}
		}
	}*/
	
}
