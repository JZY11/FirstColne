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
import org.quartz.SchedulerException;

import com.btb.dao.ThirdpartyplatforminfoMpper;
import com.btb.dao.ThirdpartysupportmoneyMapper;
import com.btb.entity.QueryVo;
import com.btb.entity.Thirdpartyplatforminfo;
import com.btb.entity.Thirdpartysupportmoney;
import com.btb.tasks.service.BaseJob;
import com.btb.tasks.service.JobManager;
import com.btb.tasks.threads.BuyParmeterThread;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.SpringUtil;
import com.btb.util.TaskUtil;
import com.btb.util.thread.ThreadPoolManager;

/**
 * 每小时采集一次所有平台的交易对
 * @author Administrator
 *
 */
public class BuyParmeterJob extends BaseJob {
	
	
	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("job-  采集交易对");
		//获取所有平台
		ThirdpartyplatforminfoMpper thirdpartyplatforminfoMpper = SpringUtil.getBean(ThirdpartyplatforminfoMpper.class);
		List<Thirdpartyplatforminfo> thirdpartyplatfos = thirdpartyplatforminfoMpper.selectAll();
		//循环多编程,采集每个平台的交易对
		for (Thirdpartyplatforminfo thirdpartyplatforminfo : thirdpartyplatfos) {
			BuyParmeterThread buyParmeterThread = new BuyParmeterThread(thirdpartyplatforminfo.getId());
			ThreadPoolManager.workNoResult(buyParmeterThread);
		}
	}

	@Override
	public Integer getSeconds() {
		// TODO Auto-generated method stub
		return 60;//秒为单位,每小时采集一次 每个平台支持的交易对
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

	
	public static void main(String[] args) {
		//--采集每个平台支持的交易对, 多少平台多少任务,大概200多个任务
		try {
			SpringUtil.testinitSpring();
			BuyParmeterJob buyParmeterJob = new BuyParmeterJob();
			JobManager.addJob(buyParmeterJob);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
