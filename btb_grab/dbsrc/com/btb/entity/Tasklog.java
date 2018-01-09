package com.btb.entity;

import com.btb.util.DateUtil;
import com.btb.util.SpringUtil;
import com.btb.util.dao.BaseDaoSql;

public class Tasklog {
	
	public static void save(Tasklog tasklog) {
		BaseDaoSql.save(tasklog);
	}
	
	String name;
	String updatetime=DateUtil.getDate();
	String appname="okhuobi";
	
	public Tasklog(String name) {
		// TODO Auto-generated constructor stub
		this.name=name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	
	
}
