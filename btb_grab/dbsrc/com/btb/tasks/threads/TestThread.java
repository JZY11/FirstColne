package com.btb.tasks.threads;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.btb.entity.AllPlatformKline;
import com.btb.util.DateUtil;
import com.btb.util.JsoupUtil;
import com.btb.util.StringUtil;
import com.btb.util.dao.BaseDaoSql;

public class TestThread extends Thread {
	Map<String, Object> map;
	public TestThread(Map<String, Object> map) {
		// TODO Auto-generated constructor stub
		this.map=map;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
