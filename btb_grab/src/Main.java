import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.quartz.SchedulerException;

import com.btb.dao.ThirdpartyplatforminfoMpper;
import com.btb.dao.ThirdpartysupportmoneyMapper;
import com.btb.entity.Thirdpartyplatforminfo;
import com.btb.entity.Thirdpartysupportmoney;
import com.btb.tasks.BtbConutJob;
import com.btb.tasks.BuyParmeterJob;
import com.btb.tasks.CheckWebSocketStatusJob;
import com.btb.tasks.MarketHistoryKlineJob;
import com.btb.tasks.RateJob;
import com.btb.tasks.service.JobManager;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.H2Util;
import com.btb.util.SpringUtil;
import com.btb.util.StringUtil;
import com.btb.util.TaskUtil;

public class Main {
	public static void main(String[] args) {
		//h2创建表
		H2Util.initTable();
		//初始化所有的HttpUtil,方便采集k线图和交易对
		InitAllHttpUtils();
		//采集所有平台中的交易对,同步
		TaskUtil.initMoneypairToDB();//从数据库里面加载防止采集交易对不成功
		//同步到数据里面获取外汇利率
		TaskUtil.initWaiHuiToDB();
		//btc和ehc
		TaskUtil.initBaseBtcAndEthKline();
		//启动加载每个平台的btc,eth价格, 每1.5分钟执行一次,这个是执行一次,任务放在k线图里面
		TaskUtil.initBtcEthNowMoney();
		//同步加载k线图数据
		TaskUtil.initKline(false);
		//获取每个平台,每个交易对的 今日开盘价格, 从k线图里面获取
		TaskUtil.initTodayOpen();
		//初始化所有实时行情信息,到h2数据库中
		TaskUtil.initMarketAllToH2DB();
		
		//银行利率每天执行一次,1个任务
		//addJobRate();
		
		//--采集每个平台支持的交易对, 多少平台多少线程,大概200多个线程
		//addMoneyDuiJobs();
		
		//采集k线图分钟数据,每1.5分钟执行一次, 每个平台一个线程,大概200个线程
		//获取平台所有交易对
		//addKlineJobs();
		
		//采集比特币流通数量,暂时先关闭,接口采集来源需要优化
		//addMoneyCountJob();
		
		//每隔30秒检查一次所有websoket的链接状态,如果断链,重新链接
		//addCheckWebSocketStatusJob();
		
		//所有平台的行情数据采集,,由于需要实时性,所以只能使用websoket
		//获取所有平台的websoket类
		//enableWebSocket();
	}
	
	//初始化所有的HttpUtil,启动执行一次
	public static void InitAllHttpUtils() {
		Map<String, BaseHttp> beanHttpMap = SpringUtil.context.getBeansOfType(BaseHttp.class);
		for (BaseHttp baseHttp : beanHttpMap.values()) {
			CacheData.httpBeans.put(baseHttp.getPlatformId(), baseHttp);
		}
	}
	//银行利率每天执行一次,1个任务
	public static void addJobRate() {
		try {
			JobManager.addJob(new RateJob());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//--采集每个平台支持的交易对, 多少平台多少线程,大概200多个线程
	public static void addMoneyDuiJobs() {
		try {
			BuyParmeterJob buyParmeterJob = new BuyParmeterJob();
			JobManager.addJob(buyParmeterJob);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//采集k线图分钟数据,每1.5分钟执行一次, 每个平台一个线程,大概200个线程
	//加载每个平台的btc,eth价格, 每1.5分钟执行一次,这个是执行一次,任务放在k线图里面
	public static void addKlineJobs() {
		try {
			MarketHistoryKlineJob marketHistoryKlineJob = new MarketHistoryKlineJob();
			JobManager.addJob(marketHistoryKlineJob);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取比特币数量
	public static void addMoneyCountJob() {
		try {
			JobManager.addJob(new BtbConutJob());
		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	//检查websocket任务的状态,关闭的重连
	public static void addCheckWebSocketStatusJob() {
		try {
			JobManager.addJob(new CheckWebSocketStatusJob());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//开启websocket服务
	public static void enableWebSocket() {
		List<Class<WebSocketClient>> webSocketUtils = StringUtil.getAllWebSocketUtils();
		for (Class<WebSocketClient> webSocketUtil : webSocketUtils) {
			try {
				Method method = webSocketUtil.getMethod("executeWebSocket");
				WebSocketClient webSocketClient = (WebSocketClient)method.invoke(null, null);
				CacheData.webSocketClientMap.put(webSocketUtil.getMethod("getPlatFormId").invoke(null, null).toString(), webSocketClient);
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
	
}


