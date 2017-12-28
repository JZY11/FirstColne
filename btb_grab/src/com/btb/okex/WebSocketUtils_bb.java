package com.btb.okex;

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

import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;
import com.alibaba.fastjson.JSON;
import com.btb.entity.Market;
import com.btb.entity.Thirdpartysupportmoney;
import com.btb.okex.vo.MarketDepthVo1;
import com.btb.okex.vo.MarketVo1;
import com.btb.util.CacheData;
import com.btb.util.DBUtil;
import com.btb.util.H2Util;
import com.btb.util.TaskUtil;

public class WebSocketUtils_bb extends WebSocketClient {
	//{改}
	private static final String url = "wss://real.okex.com:10441/websocket";
	
	private static WebSocketUtils_bb chatclient = null;
	private static String platformid;
	public WebSocketUtils_bb(URI serverUri, Map<String, String> headers, int connecttimeout) {
		super(serverUri, new Draft_17(), headers, connecttimeout);
		platformid=new HttpUtil_okex().getPlatformId();
	}
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("开流--opened connection");
		List<Thirdpartysupportmoney> jiaoyiduis = DBUtil.getJiaoyidui(platformid);
		for (Thirdpartysupportmoney thirdpartysupportmoney : jiaoyiduis) {
			SubModel subModel = new SubModel();
			//打开后添加实时行情订阅
			String chId = "ok_sub_spot_"+thirdpartysupportmoney.getMoneypair()+"_deals";
			subModel.setChannel(chId);
			chatclient.send(JSON.toJSONString(subModel));
			
			//添加买卖盘行情订阅
			chId="ok_sub_spot_"+thirdpartysupportmoney.getMoneypair()+"_depth_10";
			subModel.setChannel(chId);
			chatclient.send(JSON.toJSONString(subModel));
		}
		
		
	}
	//需要改这里或者另外一个OnMessage重载方法{改}
	@Override
	public void onMessage(ByteBuffer socketBuffer) {
		System.out.println("流数据");
	}
	
	//{改}
	@Override
	public void onMessage(String message) {
		if (message.contains("_deals")) {//行情数据
			MarketVo1 marketVo1 = null;
			try {
				marketVo1 = JSON.parseArray(message, MarketVo1.class).get(0);
				List<Object[]> data = marketVo1.getData();
				Market market = new Market();
				market.setMoneypair(marketVo1.getChannel().replace("ok_sub_spot_", "").replace("_deals", ""));
				String[] split = market.getMoneypair().split("_");
				market.setMoneytype(split[0]);
				market.setBuymoneytype(split[1]);
				market.setPlatformid(platformid);
				for (Object[] order : data) {
					market.setClose(new BigDecimal(order[1].toString()));
					if (order[4].equals("ask")) {
						market.setSell(new BigDecimal(order[1].toString()));
					}else {
						market.setBuy(new BigDecimal(order[1].toString()));
					}
				}
				//添加或者更新行情数据
				H2Util.insertOrUpdate(market);
			} catch (Exception e) {}
		}else if (message.contains("_depth_10")) {
			MarketDepthVo1 marketDepthVo1 = JSON.parseArray(message, MarketDepthVo1.class).get(0);
			String moneypair = marketDepthVo1.getChannel().replace("ok_sub_spot_", "").replace("_depth_10", "");
			CacheData.sellBuyDisk.put(platformid, marketDepthVo1.getData());
		}
	}
	
	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("关流--Connection closed by " + (remote ? "remote peer" : "us"));
		try {
			WebSocketClient webSocketClient = executeWebSocket();
			CacheData.webSocketClientMap.put(platformid, webSocketClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//不需要动
	public static WebSocketClient executeWebSocket() throws Exception {
		//WebSocketImpl.DEBUG = true;
		chatclient = new WebSocketUtils_bb(new URI(url), getWebSocketHeaders(), 1000);
		trustAllHosts(chatclient);//添加ssh安全信任
		chatclient.connect();//异步链接
		return chatclient;
		//System.out.println(chatclient.getReadyState());// 获取链接状态,OPEN是链接状态,CONNECTING: 正在链接状态
	}
	public static void main(String[] args) throws Exception {
		TaskUtil.initStartAll();
		executeWebSocket();
	}
	
	public void onFragment(Framedata fragment) {
		System.out.println("片段--received fragment: " + new String(fragment.getPayloadData().array()));
	}
	@Override
	public void onError(Exception ex) {
		System.out.println("WebSocket 连接异常: " + ex);
		ex.printStackTrace();
	}
	private static void trustAllHosts(WebSocketUtils_bb appClient) {
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
	public static String getPlatFormId() {
		return platformid;	
	}
}