package com.btb.tasks;

import java.io.IOException;
import java.util.Map;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.btb.dao.MarketMapper;
import com.btb.entity.Market;
import com.btb.tasks.service.BaseJob;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.SpringUtil;

public class MarketJob extends BaseJob {
	//platformId, 交易对
	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		JobDetail jobDetail = job.getJobDetail();
		JobDataMap dataMap = jobDetail.getJobDataMap();
		String platformId = dataMap.get("platformId").toString();//平台id
		String moneypair = dataMap.get("moneypair").toString();//交易对
		System.out.println("job-"+platformId+"  采集交易对");
		BaseHttp baseHttp = CacheData.httpBeans.get(platformId);
		try {
			Market market = baseHttp.detailMerged(moneypair);
			MarketMapper marketMapper = SpringUtil.getBean(MarketMapper.class);
			int updateCount = marketMapper.updateByPrimaryKeySelective(market);
			if (updateCount != 1) {
				marketMapper.insert(market);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public Integer getSeconds() {
		// TODO Auto-generated method stub
		return 1;//秒为单位
	}

	@Override
	public Map<String, Object> getParam() {
		// TODO Auto-generated method stub
		return this.param;
	}

	@Override
	public Class<? extends Job> getC() {
		// TODO Auto-generated method stub
		return this.getClass();
	}
	
}
