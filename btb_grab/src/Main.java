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
import com.btb.util.SpringUtil;
import com.btb.util.StringUtil;
import com.btb.util.TaskUtil;

public class Main {
	public static void main(String[] args) {
		//初始化所有的HttpUtil,启动执行一次
		Map<String, BaseHttp> beanHttpMap = SpringUtil.context.getBeansOfType(BaseHttp.class);
		for (BaseHttp baseHttp : beanHttpMap.values()) {
			CacheData.httpBeans.put(baseHttp.getPlatformId(), baseHttp);
		}
		
		//从数据库获取所有交易对,只有启动的时候使用
		TaskUtil.initMoneypair();
		
		//银行利率每天执行一次,1个任务
		try {
			JobManager.addJob(new RateJob());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//--采集每个平台支持的交易对, 多少平台多少线程,大概200多个线程
		try {
			BuyParmeterJob buyParmeterJob = new BuyParmeterJob();
			JobManager.addJob(buyParmeterJob);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//采集k线图分钟数据,每分钟执行一次,大概200多*20交易对,4000千多任务
		//获取平台所有交易对
		try {
			MarketHistoryKlineJob marketHistoryKlineJob = new MarketHistoryKlineJob();
			JobManager.addJob(marketHistoryKlineJob);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//采集比特币流通数量,暂时先关闭,接口采集来源需要优化
		/*try {
			JobManager.addJob(new BtbConutJob());
		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		
	}
	
}


