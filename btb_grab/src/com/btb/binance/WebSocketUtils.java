package com.btb.binance;

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
import com.btb.binance.vo.MarketVo1;
import com.btb.entity.Market;
import com.btb.entity.MarketDepth;
import com.btb.entity.MarketOrder;
import com.btb.entity.PlatformSupportmoney;
import com.btb.util.BaseHttp;
import com.btb.util.CommonUtils;
import com.btb.util.H2Util;
import com.btb.util.MongoDbUtil;
import com.btb.util.SpringUtil;
import com.btb.util.StringUtil;
import com.btb.util.TaskUtil;
import com.btb.util.dao.BaseDaoSql;

public class WebSocketUtils extends WebSocketClient {
	
	//{改}
	private static final String url = "wss://stream.binance.com:9443/ws/{moneypair}@ticker";
	
	private static WebSocketUtils chatclient = null;
	private static String platformid=new HttpUtil().getPlatformId();
	public WebSocketUtils(URI serverUri, Map<String, String> headers, int connecttimeout) {
		super(serverUri, new Draft_17(), headers, connecttimeout);
	}
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("开流--opened connection");
	}
	//需要改这里或者另外一个OnMessage重载方法{改}
	@Override
	public void onMessage(ByteBuffer socketBuffer) {
	}
	
	//{改}
	@Override
	public void onMessage(String message) {
		Map vo1 = JSON.parseObject(message, Map.class);
		Market market = new Market();
		market.setPlatformid(platformid);//平台id 必填
		market.setMoneypair(vo1.get("s").toString());//交易对 必填
		String[] strings = StringUtil.getHuobiBuyMoneytype(market.getMoneypair());
		market.setBuymoneytype(strings[0]);
		market.setMoneytype(strings[1]);
		market.setClose(StringUtil.toBigDecimal(vo1.get("c")));//最新价格 必填
		market.setSell(StringUtil.toBigDecimal(vo1.get("a")));
		market.setBuy(StringUtil.toBigDecimal(vo1.get("b")));
		//添加或者更新行情数据
		MongoDbUtil.insertOrUpdate(market);
	}
	
	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("关流--Connection closed by " + (remote ? "remote peer" : "us"));
		try {
			executeWebSocket();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//不需要动
	public static void executeWebSocket() throws Exception {
		//WebSocketImpl.DEBUG = true;
		List<PlatformSupportmoney> jiaoyiduis = BaseDaoSql.findList("select * from platformsupportmoney where platformid='"+platformid+"'", PlatformSupportmoney.class);
		for (PlatformSupportmoney thirdpartysupportmoney : jiaoyiduis) {
			chatclient = new WebSocketUtils(new URI(url.replace("{moneypair}", thirdpartysupportmoney.getMoneypair().toLowerCase())), getWebSocketHeaders(), 1000);
			trustAllHosts(chatclient);//添加ssh安全信任
			chatclient.connect();//异步链接
			TaskUtil.webSocketClientMap.put(platformid+"_"+thirdpartysupportmoney.getMoneypair().toLowerCase(), chatclient);
		}
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
	public static String getPlatFormId() {
		return platformid;	
	}
}