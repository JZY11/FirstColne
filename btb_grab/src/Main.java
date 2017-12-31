import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.java_websocket.client.WebSocketClient;
import org.quartz.SchedulerException;

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
		
		
		
		
	}
	
}


