package com.btb.tasks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.btb.entity.Tasklog;
import com.btb.tasks.service.BaseJob;
import com.btb.util.TaskUtil;

public class RateJob extends BaseJob {
	
	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("银行利率每天执行一次");
		Tasklog.save(new Tasklog("银行利率每天执行一次"));
		TaskUtil.initWaiHuiToDB();
	}

	@Override
	public Integer getSeconds() {
		// TODO Auto-generated method stub
		return 60*60*24;//秒为单位
	}

	@Override
	public Map<String, Object> getParam() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends Job> getC() {
		// TODO Auto-generated method stub
		return this.getClass();
	}
	
}
