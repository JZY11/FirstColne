import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;

import com.btb.dao.ThirdpartyplatforminfoMpper;
import com.btb.dao.ThirdpartysupportmoneyMapper;
import com.btb.entity.Thirdpartyplatforminfo;
import com.btb.entity.Thirdpartysupportmoney;
import com.btb.tasks.BuyParmeterJob;
import com.btb.tasks.MarketJob;
import com.btb.tasks.RateJob;
import com.btb.tasks.service.JobManager;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.SpringUtil;

public class Main {
	public static void main(String[] args) {
		//初始化所有的HttpUtil
		Map<String, BaseHttp> beanHttpMap = SpringUtil.context.getBeansOfType(BaseHttp.class);
		for (BaseHttp baseHttp : beanHttpMap.values()) {
			CacheData.httpBeans.put(baseHttp.getPlatformId(), baseHttp);
		}
		
		//银行利率
		try {
			JobManager.addJob(new RateJob());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//采集每个平台支持的交易对
		ThirdpartyplatforminfoMpper thirdpartyplatforminfoMpper = SpringUtil.getBean(ThirdpartyplatforminfoMpper.class);
		List<Thirdpartyplatforminfo> thirdpartyplatfos = thirdpartyplatforminfoMpper.selectAll();
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
		
		//每隔1秒钟请求一次
		ThirdpartysupportmoneyMapper thirdpartysupportmoneyMapper = SpringUtil.getBean(ThirdpartysupportmoneyMapper.class);
		List<Thirdpartysupportmoney> selectAll = thirdpartysupportmoneyMapper.selectAll();
		for (Thirdpartysupportmoney thirdpartysupportmoney : selectAll) {
			String jobGroupId = "market-"+thirdpartysupportmoney.getPlatformid();
			String jobId = thirdpartysupportmoney.getMoneypair();
			MarketJob marketJob = new MarketJob();
			marketJob.setGroupId(jobGroupId);
			marketJob.setJobId(jobId);
			Map<String, Object> param=new HashMap<>();
			param.put("platformId", thirdpartysupportmoney.getPlatformid());
			param.put("moneypair", thirdpartysupportmoney.getMoneypair());
			marketJob.setParam(param);
			try {
				JobManager.addJob(marketJob);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/*
	 * 行情任务
	 */
	public static void addMarketJob(MarketJob marketJob) {
		String jobId = marketJob.jobId;
	}
	
}


