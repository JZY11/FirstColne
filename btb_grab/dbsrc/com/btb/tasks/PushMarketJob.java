package com.btb.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;
import com.btb.util.H2Util;
import com.btb.util.MyWebSocketClient;

public class PushMarketJob implements Job {
	public static int ts=1;
	public static Map<String, Object> message = new HashMap<>(); 
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = H2Util.select("select * from market");
		for (Map<String, Object> map : list) {
			String ch = map.get("platformid")+"."+map.get("moneytype")+"_"+map.get("buymoneytype")+".market";
			message.put("ch", ch);
			message.put("data", map);
			MyWebSocketClient.sendMessage(JSON.toJSONString(message));
		}
	}

}
