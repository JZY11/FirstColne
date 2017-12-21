package com.btb.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.btb.dao.ThirdpartysupportmoneyMapper;
import com.btb.entity.QueryVo;
import com.btb.entity.Thirdpartysupportmoney;
import com.btb.tasks.service.BaseJob;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.SpringUtil;
import com.btb.util.TaskUtil;

public class BuyParmeterJob extends BaseJob {
	
	
	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDetail jobDetail = job.getJobDetail();
		JobDataMap dataMap = jobDetail.getJobDataMap();
		String platformId = dataMap.get("platformId").toString();//平台id
		System.out.println("job-"+platformId+"  采集交易对");
		BaseHttp baseHttp = CacheData.httpBeans.get(platformId);
		List<Thirdpartysupportmoney> thirdpartysupportmoneys = new ArrayList<>();
		baseHttp.geThirdpartysupportmoneys(thirdpartysupportmoneys );
		ThirdpartysupportmoneyMapper mapper = SpringUtil.getBean(ThirdpartysupportmoneyMapper.class);
		
		QueryVo vo = new QueryVo();
		vo.setPlatformid(platformId);
		List<String> moneypairs = new ArrayList<>();
		for (Thirdpartysupportmoney thirdpartysupportmoney : thirdpartysupportmoneys) {
			moneypairs.add(thirdpartysupportmoney.getMoneypair());
			int insertCount = mapper.updateByPrimaryKey(thirdpartysupportmoney);
			if (insertCount != 1) {
				mapper.insert(thirdpartysupportmoney);
			}
		}
		vo.setMoneypairs(moneypairs);
		//删除不存在的
		mapper.deleteParam(vo);
		//重新加载数据
		CacheData.moneyPairs.put(platformId, moneypairs);
	}

	@Override
	public Integer getSeconds() {
		// TODO Auto-generated method stub
		return 60*60;//秒为单位,每小时采集一次 每个平台支持的交易对
	}

	@Override
	public Map<String, Object> getParam() {
		// TODO Auto-generated method stub
		return this.param;
	}
	public void setParam(Map<String, Object> param) {
		this.param=param;
	}
	
	@Override
	public Class<? extends Job> getC() {
		// TODO Auto-generated method stub
		return this.getClass();
	}
	
}
