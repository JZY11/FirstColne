import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;

import com.btb.dao.ThirdpartyplatforminfoMpper;
import com.btb.dao.ThirdpartysupportmoneyMapper;
import com.btb.entity.Thirdpartyplatforminfo;
import com.btb.entity.Thirdpartysupportmoney;
import com.btb.tasks.BuyParmeterJob;
import com.btb.tasks.MarketHistoryKlineJob;
import com.btb.tasks.RateJob;
import com.btb.tasks.service.JobManager;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.SpringUtil;

public class Main {
	public static void main(String[] args) {
		//初始化所有的HttpUtil,启动执行一次
		Map<String, BaseHttp> beanHttpMap = SpringUtil.context.getBeansOfType(BaseHttp.class);
		for (BaseHttp baseHttp : beanHttpMap.values()) {
			CacheData.httpBeans.put(baseHttp.getPlatformId(), baseHttp);
		}
		
		//银行利率每天执行一次,1个任务
		try {
			JobManager.addJob(new RateJob());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//采集每个平台支持的交易对, 多少平台多少任务,大概200多个任务
		//获取所有平台
		ThirdpartyplatforminfoMpper thirdpartyplatforminfoMpper = SpringUtil.getBean(ThirdpartyplatforminfoMpper.class);
		List<Thirdpartyplatforminfo> thirdpartyplatfos = thirdpartyplatforminfoMpper.selectAll();
		//循环添加所有平台的交易对采集,利用的是HttpUtil的getPlatformId()方法和geThirdpartysupportmoneys方法
		for (Thirdpartyplatforminfo thirdpartyplatforminfo : thirdpartyplatfos) {
			Map<String, Object> map = new HashMap<>();
			map.put("platformId", thirdpartyplatforminfo.getId());
			try {
				BuyParmeterJob buyParmeterJob = new BuyParmeterJob();
				buyParmeterJob.setParam(map);
				JobManager.addJob(buyParmeterJob);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//所有平台的行情数据采集,,由于需要实时性,所以只能使用websoket
		//获取所有平台的websoket类
		
		
		
		//每隔一分钟检查一次所有websoket的链接状态,如果断链,重新链接
		
		//k线图任务添加,所有平台1分钟采集一次k线历史分钟图数据
		
		
		
	}
	
}


