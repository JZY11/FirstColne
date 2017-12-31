package com.btb.tasks;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.btb.tasks.service.BaseJob;
import com.btb.util.TaskUtil;

public class CheckWebSocketStatusJob extends BaseJob {
	
	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		// TODO Auto-generated method stub
		Map<String, WebSocketClient> webSocketClientMap = TaskUtil.webSocketClientMap;
		int i=0;
		int ii=0;
		for (String key : webSocketClientMap.keySet()) {
			WebSocketClient webSocketClient = webSocketClientMap.get(key);
			if (webSocketClient==null) {
				System.out.println("平台ID:"+key+" - websocketClient没有链接上");
			}else {
				if (webSocketClient.getReadyState() == READYSTATE.OPEN) {
					i++;
				}
				if (webSocketClient.getReadyState() == READYSTATE.CONNECTING) {
					ii++;
				}
				//如果不是开启状态,和正在开启状态,尝试链接
				if (webSocketClient.getReadyState()!=READYSTATE.OPEN && webSocketClient.getReadyState() != READYSTATE.CONNECTING) {
					Class<? extends WebSocketClient> webSocketUtil = webSocketClient.getClass();
					try {
						Method method = webSocketUtil.getMethod("executeWebSocket");
						WebSocketClient webSocketClient2 = (WebSocketClient)method.invoke(null, null);
						TaskUtil.webSocketClientMap.put(webSocketUtil.getMethod("getPlatFormId").invoke(null, null).toString(), webSocketClient2);
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("一共:"+webSocketClientMap.size()+"个, 正常链接:"+i+"个,正在链接:"+ii+"个");
	}

	@Override
	public Integer getSeconds() {
		// TODO Auto-generated method stub
		return 30;//秒为单位30秒检查一次
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
