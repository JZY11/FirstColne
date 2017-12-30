package com.btb.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.btb.dao.ThirdpartysupportmoneyMapper;
import com.btb.entity.Market;
import com.btb.entity.MarketDepthVo;
import com.btb.entity.Thirdpartysupportmoney;
import com.btb.huobi.vo.MarketDepthVo1;
import com.btb.huobi.vo.MarketVo1;
import com.btb.huobi.vo.MarketVo1.MarketVo2;
import com.btb.huobi.vo.MarketVo1.MarketVo3;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.DBUtil;
import com.btb.util.H2Util;
import com.btb.util.SpringUtil;
import com.btb.util.StringUtil;
import com.btb.util.TaskUtil;

public class MyWebSocketClient extends WebSocketClient {
	//{改}
	private static final String url = MyConfig.get("wsurl");
	
	private static MyWebSocketClient chatclient = null;
	public MyWebSocketClient(URI serverUri, Map<String, String> headers, int connecttimeout) {
		super(serverUri, new Draft_17(), headers, connecttimeout);
	}
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("我已经链接了");
	}
	//{改}
	@Override
	public void onMessage(String message) {
		System.out.println(message);
	}
	
	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("关流 bxsAdmin--Connection closed by " + (remote ? "remote peer" : "us"));
		try {
			WebSocketClient webSocketClient = executeWebSocket();
			CacheData.webSocketClientMap.put("bxsAdmin", webSocketClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//不需要动
	public static WebSocketClient executeWebSocket() throws Exception {
		//WebSocketImpl.DEBUG = true;
		chatclient = new MyWebSocketClient(new URI(url), getWebSocketHeaders(), 1000);
		//trustAllHosts(chatclient);//添加ssh安全信任
		chatclient.connect();//异步链接
		return chatclient;
		//System.out.println(chatclient.getReadyState());// 获取链接状态,OPEN是链接状态,CONNECTING: 正在链接状态
	}
	public static void main(String[] args) throws Exception {
		executeWebSocket();
	}
	
	@Override
	public void onError(Exception ex) {
		System.out.println("WebSocket 连接异常: " + ex);
		ex.printStackTrace();
	}
	private static void trustAllHosts(MyWebSocketClient appClient) {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		} };

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			appClient.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sc));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Map<String, String> getWebSocketHeaders() throws IOException {
		Map<String, String> headers = new HashMap<String, String>();
		return headers;
	}
	
	
	public static void sendMessage(String message) {
		chatclient.send(message);
	}
	
}