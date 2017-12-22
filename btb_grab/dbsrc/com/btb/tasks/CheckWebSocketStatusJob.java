package com.btb.tasks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.btb.tasks.service.BaseJob;
import com.btb.util.CacheData;
import com.btb.util.TaskUtil;

public class CheckWebSocketStatusJob extends BaseJob {
	
	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		// TODO Auto-generated method stub
		Map<String, WebSocketClient> webSocketClientMap = CacheData.webSocketClientMap;
		for (String key : webSocketClientMap.keySet()) {
			WebSocketClient webSocketClient = webSocketClientMap.get(key);
			if (webSocketClient==null) {
				System.out.println(webSocketClient);
			}else {
				System.out.println(webSocketClient.getReadyState());
			}
		}
	}

	@Override
	public Integer getSeconds() {
		// TODO Auto-generated method stub
		return 2;//秒为单位
	}

	@Override
	public Map<String, Object> getParam() {
		// TODO Auto-generated method stub
		return new HashMap<>();
	}

	@Override
	public Class<? extends Job> getC() {
		// TODO Auto-generated method stub
		return this.getClass();
	}
	
}
