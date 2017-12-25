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
import com.btb.entity.Thirdpartysupportmoney;
import com.btb.okex.vo.MarketVo1;
import com.btb.okex.vo.MarketVo2;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.DBUtil;
import com.btb.util.H2Util;
import com.btb.util.SpringUtil;

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
			String chId = "ok_sub_spot_"+thirdpartysupportmoney.getMoneypair()+"_ticker";
			subModel.setChannel(chId);
			chatclient.send(JSON.toJSONString(subModel));
			
			//添加买卖盘行情订阅
			/*chId="market."+thirdpartysupportmoney.getMoneypair()+".depth.step1";
			subModel.setChannel(chId);
			chatclient.send(JSON.toJSONString(subModel));*/
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
		List<MarketVo1> list = JSON.parseArray(message, MarketVo1.class);
		if (list != null && !list.isEmpty()) {
			for (MarketVo1 marketVo1 : list) {
				if (marketVo1.getChannel().startsWith("ok_")) {//正常数据数据
					MarketVo2 marketVo2 = marketVo1.getData();
					Market market = new Market();
					market.setAmount(marketVo2.getVol());
					market.setClose(marketVo2.getClose());
					market.setHigh(marketVo2.getHigh());
					market.setLow(marketVo2.getLow());
					market.setMoneypair(marketVo1.getChannel().replace("ok_sub_spot_", "").replace("_ticker", ""));
					market.setOpen(marketVo2.getOpen());
					market.setPlatformid(platformid);
					//添加或者更新行情数据
					System.out.println(JSON.toJSONString(market));
					H2Util.insertOrUpdate(market);
				}
			}
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