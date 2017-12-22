package com.btb.huobi;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
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
import com.btb.entity.Market;
import com.btb.huobi.vo.Vo1;
import com.btb.huobi.vo.Vo2;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.H2Util;

public class WebSocketUtils extends WebSocketClient {
	private static final String url = "wss://api.huobi.pro/ws";

	private static WebSocketUtils chatclient = null;
	HttpUtil httpUtil;
	public WebSocketUtils(URI serverUri, Map<String, String> headers, int connecttimeout) {
		super(serverUri, new Draft_17(), headers, connecttimeout);
		httpUtil=new HttpUtil();
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("开流--opened connection");
	}

	@Override
	public void onMessage(ByteBuffer socketBuffer) {
		try {
			String marketStr = CommonUtils.byteBufferToString(socketBuffer);
			String marketJsonStr = CommonUtils.uncompress(marketStr);
			if (marketJsonStr.contains("ping")) {
				System.out.println(marketJsonStr.replace("ping", "pong"));
				// Client 心跳
				chatclient.send(marketJsonStr.replace("ping", "pong"));
			} else {
				Vo1 vo1 = JSON.parseObject(marketJsonStr, Vo1.class);
				if (vo1.getCh() != null) {//如果是订阅的行情数据
					Vo2 vo2 = vo1.getTick();
					Market market = new Market();
					market.setPlatformid(httpUtil.getPlatformId());//平台id
					market.setMoneypair(vo1.getCh().split("\\.")[1]);//交易对
					market.setAmount(vo2.getAmount());//24小时成交量
					market.setClose(vo2.getClose());//最新价格
					market.setCount(vo2.getCount());//24小时成交笔数
					market.setHigh(vo2.getHigh());//24小时最高价
					market.setLow(vo2.getLow());//24小时最低价
					market.setOpen(vo2.getOpen());//24小时前开盘价格
					market.setVol(vo2.getVol());//24小时成交额
					//添加或者更新行情数据
					H2Util.insertOrUpdate(market);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(String message) {
		System.out.println("接收--received: " + message);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("关流--Connection closed by " + (remote ? "remote peer" : "us"));
	}

	@Override
	public void onError(Exception ex) {
		System.out.println("WebSocket 连接异常: " + ex);
	}

	private static void trustAllHosts(WebSocketUtils appClient) {
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
	public static void executeWebSocket() throws Exception {
		//WebSocketImpl.DEBUG = true;
		chatclient = new WebSocketUtils(new URI(url), getWebSocketHeaders(), 1000);
		trustAllHosts(chatclient);
		chatclient.connectBlocking();
		
		SubModel subModel = new SubModel();
		subModel.setId("btcusdt");
		subModel.setSub("market.btcusdt.detail");
		
		chatclient.send(JSONObject.toJSONString(subModel));
		Thread.sleep(100000);
	}
	public static void main(String[] args) throws Exception {
		executeWebSocket();
	}
}