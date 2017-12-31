package com.btb.tasks;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.btb.dao.MarketMapper;
import com.btb.entity.Market;
import com.btb.entity.Markethistory;
import com.btb.entity.Tasklog;
import com.btb.tasks.service.BaseJob;
import com.btb.tasks.threads.MarketHistoryKlineTread;
import com.btb.util.BaseHttp;
import com.btb.util.SpringUtil;
import com.btb.util.TaskUtil;
import com.btb.util.thread.ThreadPoolManager;

public class MarketHistoryKlineJob extends BaseJob {
	//platformId, 交易对
	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		System.out.println("采集k线图分钟数据,每1.5分钟执行一次, 每个平台一个线程");
		Tasklog.save(new Tasklog("采集k线图分钟数据,每1.5分钟执行一次, 每个平台一个线程"));
		TaskUtil.initKline(true);
	}

	@Override
	public Integer getSeconds() {
		// TODO Auto-generated method stub
		return 90;//秒为单位
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
