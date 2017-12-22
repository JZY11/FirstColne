package com.btb.tasks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket.READYSTATE;
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
				System.out.println("平台ID:"+key+" - websocketClient没有链接上");
			}else {
				//如果不是开启状态,和正在开启状态,尝试链接
				if (webSocketClient.getReadyState()!=READYSTATE.OPEN && webSocketClient.getReadyState() != READYSTATE.CONNECTING) {
					webSocketClient.connect();//尝试链接
				}
			}
		}
	}

	@Override
	public Integer getSeconds() {
		// TODO Auto-generated method stub
		return 60;//秒为单位,1分钟检查一次
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
