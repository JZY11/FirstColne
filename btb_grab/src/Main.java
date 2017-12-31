import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.java_websocket.client.WebSocketClient;
import org.quartz.SchedulerException;

import com.btb.tasks.BtbConutJob;
import com.btb.tasks.BuyParmeterJob;
import com.btb.tasks.CheckWebSocketStatusJob;
import com.btb.tasks.InitBtcEthNowMoney;
//import com.btb.tasks.InitInitBxsMarketJob;
//import com.btb.tasks.InitMarketAllToH2DBJob;
//import com.btb.tasks.InitTodayNewDataJob;
import com.btb.tasks.InitTodayOpenJob;
import com.btb.tasks.MarketHistoryKlineJob;
//import com.btb.tasks.PushMarketJob;
import com.btb.tasks.RateJob;
import com.btb.tasks.service.JobManager;
import com.btb.util.StringUtil;
import com.btb.util.TaskUtil;

public class Main {
	public static void main(String[] args) {
		
		TaskUtil.initStartAll();
		
		
		//每隔30秒检查一次所有websoket的链接状态,如果断链,重新链接
		System.out.println("每隔30秒检查一次所有websoket的链接状态,如果断链,重新链接");
		JobManager.addJob(new CheckWebSocketStatusJob());
		
		//所有平台的行情数据采集,,由于需要实时性,所以只能使用websoket
		//获取所有平台的websoket类
		System.out.println("启动所有平台的websoket");
		enableWebSocket();
		
		//获取每个平台,每个交易对的 今日开盘价格, 从k线图里面获取,每1.5分钟跑一次
		System.out.println("获取每个平台,每个交易对的 今日开盘价格, 从k线图里面获取,每1.5分钟跑一次");
		JobManager.addJob(new InitTodayOpenJob());
		
		//启动加载每个平台的btc,eth价格, 每1.5分钟执行一次
		System.out.println("启动加载每个平台的btc,eth价格, 每1.5分钟执行一次");
		JobManager.addJob(new InitBtcEthNowMoney());
		
		//加载今日实时行情数据,每1.5钟跑一次,更改h2内存数据,最高价,最低价,交易量,交易额
		//System.out.println("加载今日实时行情数据,每1.5钟跑一次,更改h2内存数据,最高价,最低价,交易量,交易额");
		//JobManager.addJob(new InitTodayNewDataJob());
		
		//--采集每个平台支持的交易对, 多少平台多少线程,大概200多个线程
		System.out.println("采集每个平台支持的交易对, 多少平台多少线程,大概200多个线程");
		JobManager.addJob(new BuyParmeterJob());
		
		//采集k线图分钟数据,每1.5分钟执行一次, 每个平台一个线程,大概200个线程
		//获取平台所有交易对
		System.out.println("采集k线图分钟数据,每1.5分钟执行一次, 每个平台一个线程,大概200个线程");
		JobManager.addJob(new MarketHistoryKlineJob());
		
		//银行利率每天执行一次,1个任务
		System.out.println("//银行利率每天执行一次,1个任务");
		JobManager.addJob(new RateJob());
		
		//采集比特币流通数量,暂时先关闭,接口采集来源需要优化
		System.out.println("//采集比特币流通数量,暂时先关闭,接口采集来源需要优化");
		JobManager.addJob(new BtbConutJob());
		
		//初始化所有实时行情信息,到h2数据库中
		//System.out.println("//初始化所有实时行情信息,到h2数据库中");
		//JobManager.addJob(new InitMarketAllToH2DBJob());
		
		//定时计算全网平均数据,每分钟计算一次,并推送到服务器端
		//System.out.println("定时计算全网平均数据,每分钟计算一次,并推送到服务器端");
		//JobManager.addJob(new InitInitBxsMarketJob());
		
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
	
}


