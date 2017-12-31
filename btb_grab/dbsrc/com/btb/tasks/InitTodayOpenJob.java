package com.btb.tasks;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.btb.entity.Tasklog;
import com.btb.tasks.service.BaseJob;
import com.btb.util.TaskUtil;

//获取每个平台,每个交易对的 今日开盘价格, 从k线图里面获取,每1.5分钟跑一次
public class InitTodayOpenJob extends BaseJob{
	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("获取每个平台,每个交易对的 今日开盘价格, 从k线图里面获取,每1.5分钟跑一次");
		Tasklog.save(new Tasklog("获取每个平台,每个交易对的 今日开盘价格, 从k线图里面获取,每1.5分钟跑一次"));
		TaskUtil.initTodayOpen();
	}

	@Override
	public Integer getSeconds() {
		// TODO Auto-generated method stub
		return 90;//秒为单位
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
