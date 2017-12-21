package com.btb.tasks.service;

import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;

import com.btb.util.IdManage;

public abstract class BaseJob implements Job {
	public String groupId="public";//默认公共的
	public String jobId=IdManage.getTimeUUid();
	public Class<? extends Job> c;
	public Integer seconds;
	
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Map<String, Object> param;
	
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public abstract Integer getSeconds();
	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}
	public abstract Map<String, Object> getParam() ;
	public void setParam(Map<String, Object> param) {
		this.param = param;
	}
	public abstract Class<? extends Job> getC();
	public void setC(Class<? extends Job> c) {
		this.c = c;
	}
	
	
}
