package com.btb.tasks;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.btb.tasks.service.BaseJob;
import com.btb.util.TaskUtil;
//启动加载每个平台的btc,eth价格, 每1.5分钟执行一次
public class InitBtcEthNowMoney extends BaseJob {

	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		// TODO Auto-generated method stub
		TaskUtil.initBtcEthNowMoney();
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
