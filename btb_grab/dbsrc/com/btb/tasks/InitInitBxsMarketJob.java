package com.btb.tasks;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.btb.tasks.service.BaseJob;
import com.btb.util.TaskUtil;

//定时计算全网平均数据,每分钟计算一次,并推送到服务器端
public class InitInitBxsMarketJob extends BaseJob{
	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		// TODO Auto-generated method stub
		TaskUtil.initBxsMarket();
	}

	@Override
	public Integer getSeconds() {
		// TODO Auto-generated method stub
		return 60;//秒为单位
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
