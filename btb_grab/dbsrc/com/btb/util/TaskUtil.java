package com.btb.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
	
	public static void initStartAll() {
		//初始化所有的HttpUtil,方便采集k线图和交易对
		System.out.println("正在初始化HttpUtils");
		InitAllHttpUtils();
		//采集所有平台中的交易对,同步//从数据库里面加载防止采集交易对不成功
		System.out.println("正在从数据库加载交易对");
		TaskUtil.initMoneypairToDB();
		//同步到数据里面获取外汇利率
		System.out.println("正在从数据库加载外汇汇率");
		TaskUtil.initWaiHuiToDB();
		//btc和ehc,用于采集btc和ehc的最新价格,需要最新价格计算人民币
		System.out.println("采集btc和ehc的最新价格,需要最新价格计算人民币");
		TaskUtil.initBaseBtcAndEthKline();
		//启动加载每个平台的btc,eth价格, 每1.5分钟执行一次,从数据库采集
		System.out.println("从数据库抓取btc和ehc的最新价格,上一步是先下载到数据库");
		TaskUtil.initBtcEthNowMoney();
		//同步加载k线图数据
		System.out.println("加载其他价格的k线图数据");
		TaskUtil.initKline(false);
		//获取每个平台,每个交易对的 今日开盘价格, 从k线图里面获取,每1.5分钟跑一次
		System.out.println("正在加载今日每种交易对的开盘价格,也就是0晨的收盘价格");
		TaskUtil.initTodayOpen();
		//初始化所有实时行情信息,到h2数据库中
		System.out.println("将实时行情信息的基础信息加载到h2中");
		TaskUtil.initMarketAllToH2DB();
		//加载今日实时行情数据,每1.5钟跑一次,更改h2内存数据,最高价,最低价,交易量,交易额
		System.out.println("将今日相关数据放入h2内存数据中");
		TaskUtil.initTodayNewData();
		//从数据库里面加载最新的比特币数量,这个不需要做成任务
		System.out.println("从数据库加载比特币的数量,用于计算流通量");
		TaskUtil.initBtcCountByDb();
		//定时计算全网平均数据,每分钟计算一次,并推送到服务器端
		System.out.println("计算全网的平均数据");
		TaskUtil.initBxsMarket();
		System.out.println("启动成功,开始启动定时器");
	}
	
	//定时计算全网平均数据,每分钟计算一次,并推送到服务器端
	public static void initBxsMarket() {
		String sql = "select 'bxs_all' as platformid,moneytype,moneytypeName,";
		sql+="avg(openrmb) as openrmb,avg(closermb) as closermb,";
		sql+="avg(buyrmb) as buyrmb,avg(sellrmb) as sellrmb,max(allMoneyrmb) as allMoneyrmb,";
		sql+="min(lowrmb) as lowrmb,max(highrmb) as highrmb,";
		sql+="sum(volrmb) as volrmb,sum(amount) as amount,";
		sql+="avg(zhangfuMoneyrmb) as zhangfuMoneyrmb,avg(zhangfu) as zhangfu ";
		sql+="FROM market group by moneytype,moneytypeName";
		List<Map<String, Object>> list = H2Util.select(sql);
		for (Map<String, Object> map : list) {
			CacheData.bxsMarket.put(map.get("moneytype").toString(), map);
		}
	}
	
	//加载实时行情数据,每分钟跑一次,更改h2内存数据
	public static void initTodayNewData() {
		MarketMapper marketMapper = SpringUtil.getBean(MarketMapper.class);
		List<Market> markets = marketMapper.findMarketByThisDayTask();
		for (Market market : markets) {
			H2Util.insertOrUpdate(market);
		}
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
		//每次采集交易对后,将新的币种,放入bitbinfo表里面
		TaskUtil.insertIntoBitbInfo();
	}
	//初始化所有的HttpUtil,启动执行一次
	public static void InitAllHttpUtils() {
		Map<String, BaseHttp> beanHttpMap = SpringUtil.context.getBeansOfType(BaseHttp.class);
		for (BaseHttp baseHttp : beanHttpMap.values()) {
			CacheData.httpBeans.put(baseHttp.getPlatformId(), baseHttp);
		}
	}
	//从db里面加载所有交易对
	public static void initMoneypairToDB() {
		ThirdpartysupportmoneyMapper thirdpartysupportmoneyMapper = SpringUtil.getBean(ThirdpartysupportmoneyMapper.class);
		List<Map<String, String>> findplatformidAll = thirdpartysupportmoneyMapper.findplatformidAll();
		for (Map<String, String> map : findplatformidAll) {
			String platformid = map.get("platformid");
			List<Thirdpartysupportmoney> findmoneypairByplatformid = thirdpartysupportmoneyMapper.findmoneypairByplatformid(platformid);
			CacheData.moneyPairs.put(platformid, findmoneypairByplatformid);
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
			CacheData.bitbCountMap.put(bitbInfo.getBitbcode(), bitbInfo.getCurrentcount());
		}
	}
	
	/*
	 * 获取每一种币的流通数量,5分钟一次
	 */
	public static void initBtcCount() {
		BitbinfoMapper bitbinfoMapper = SpringUtil.getBean(BitbinfoMapper.class);
		bitbinfoMapper.deleteByExample(null);
		Document document=null;
		document = Jsoup.parse(WebClientUtil.getHtml("https://coinmarketcap.com/all/views/all/"));
		Elements elements = document.select("#currencies-all tbody tr");
		for (Element element : elements) {
			Bitbinfo bitbInfo = new Bitbinfo();
			bitbInfo.setBitbcode(element.select("td.col-symbol").text());
			bitbInfo.setBitbname(element.select("td.currency-name").text());
			Elements elements2 = element.select("td.circulating-supply > a");
			if (elements2 != null && elements2.size()>0) {
				String currentCount = elements2.get(0).attr("data-supply").trim();
				bitbInfo.setCurrentcount(new BigDecimal(currentCount.equals("None") || currentCount.equals("")?"0":currentCount));
			}else {
				bitbInfo.setCurrentcount(new BigDecimal(0));
			}
			
			try {
				int updateByPrimaryKeySelective = bitbinfoMapper.updateByPrimaryKeySelective(bitbInfo);
				if (updateByPrimaryKeySelective==0) {
					//放到集合里面
					CacheData.bitbCountMap.put(bitbInfo.getBitbcode(), bitbInfo.getCurrentcount());
					bitbinfoMapper.insertSelective(bitbInfo);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
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
				CacheData.rateMap.put(rate.getMoneycode(), rate.getRate());
			}
		}
		
	}
	
	/*
	 * 每天跑一次欧洲下午4点开始跑
	 */
	public static void initWaiHui() {
		Connection connect = Jsoup.connect("https://api.fixer.io/latest?base=CNY");
		connect.ignoreContentType(true);
		Map<String, BigDecimal> map = null;
		try {
			map = ((Map<String,Map<String,BigDecimal>>)JSON.parseObject(connect.get().text(), Map.class)).get("rates");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//持久化到数据库
		RateMapper rateMapper = SpringUtil.getBean(RateMapper.class);
		
		for (String moneyCode : map.keySet()) {
			Rate rate = new Rate();
			rate.setMoneycode(moneyCode);
			rate.setRate(map.get(moneyCode));
			rate.setUpdatetime(DateUtil.dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
			rateMapper.updateByPrimaryKeySelective(rate);
		}
		initWaiHuiToDB();
	}
	//加载k线图数据
	public static void initKline(Boolean isTask) {
		Map<String, List<Thirdpartysupportmoney>> moneyPairsMap = CacheData.moneyPairs;
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
		ThirdpartysupportmoneyMapper thirdpartysupportmoneyMapper = SpringUtil.getBean(ThirdpartysupportmoneyMapper.class);
		List<Thirdpartysupportmoney> selectAll = thirdpartysupportmoneyMapper.selectAll();
		MarketHistoryMapper marketHistoryMapper = SpringUtil.getBean(MarketHistoryMapper.class);
		long time=0;
		try {
			time = new SimpleDateFormat("yyyy-MM-dd").parse(DateUtil.getDateNoTime()).getTime()/1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Thirdpartysupportmoney thirdpartysupportmoney : selectAll) {
			Markethistory markethistory=new Markethistory();
			markethistory.setPlatformid(thirdpartysupportmoney.getPlatformid());
			markethistory.setMoneypair(thirdpartysupportmoney.getMoneypair());
			markethistory.setTimeid(time);
			BigDecimal open = marketHistoryMapper.findTodayOpenMoney(markethistory);
			if (open==null) {
				open=new BigDecimal(0);
			}
			CacheData.todayOpen.put(markethistory.getPlatformid()+"."+markethistory.getMoneypair(), open);
		}
	}
	
	//加载行情数据到h2数据库中
	public static void initMarketAllToH2DB() {
		MarketMapper marketMapper = SpringUtil.getBean(MarketMapper.class);
		List<Market> marketInfo = marketMapper.findAllMarketInfo();
		for (Market market : marketInfo) {
			H2Util.insertOrUpdate(market);
		}
	}
	
	//启动加载每个平台的btc,eth价格, 每1.5分钟执行一次
	public static void initBtcEthNowMoney() {
		MarketHistoryMapper marketMapper = SpringUtil.getBean(MarketHistoryMapper.class);
		List<QueryVo> list = marketMapper.findBestNewRmbByBtcAndEth();
		for (QueryVo queryVo : list) {
			CacheData.nowBtcEthRmb.put(queryVo.getPlatformid(), queryVo.getClosermb());
		}
	}
	/**
	 * 加载所有平台的btc_usdt,和eth_usdt,这两个是最基础的需要先计算
	 * 只在线下计算,不放在在启动中
	 */
	public static void initBaseBtcAndEthKline() {
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
	}
	//每次采集交易对后,将新的币种,放入bitbinfo表里面
	public static void insertIntoBitbInfo() {
		ThirdpartysupportmoneyMapper thirdpartysupportmoneyMapper= SpringUtil.getBean(ThirdpartysupportmoneyMapper.class);
		thirdpartysupportmoneyMapper.insertIntoBitbInfo();
	}
	
	public static void main(String[] args) {
		initBtcCount();
	}
	
	/*Connection connect = Jsoup.connect("http://www.webmasterhome.cn/huilv/huobidaima.asp");
	try {
		Document document = connect.get();
		Elements elements = document.select("#huobi > a");
		for (Element element : elements) {
			System.out.println(element.text()+element.attr("title").replace("汇率", ""));
			Rate rate = new Rate();
			rate.setMoneycode(element.text());
			rate.setMoneyname(element.attr("title").replace("汇率", ""));
			RateMapper rateMapper = SpringUtil.getBean(RateMapper.class);
			rateMapper.insertSelective(rate);
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	
	/**
	 
	 			try {
				if (kaishi && !bitbInfo.getBitbcode().equals("COSS")) {
					Connection connect = Jsoup.connect(element.select("td.currency-name a").get(0).absUrl("href"));
					Document document2 = connect.get();
					Elements elements2 = document2.getElementsContainingOwnText("Max Supply");
					if (elements2 != null && elements2.size()>0) {
						Element element3 = elements2.get(0);
						bitbInfo.setAllcount(new BigDecimal(element3.parent().nextElementSibling().text().split(" ")[0].replaceAll(",", "")));
					}
					bitbinfoMapper.insertSelective(bitbInfo);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

	 */
}
