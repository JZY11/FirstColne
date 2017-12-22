package com.btb.huobi;

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
import com.btb.huobi.vo.Vo1;
import com.btb.huobi.vo.Vo2;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.DBUtil;
import com.btb.util.H2Util;
import com.btb.util.SpringUtil;

public class WebSocketUtils extends WebSocketClient {
	//{改}
	private static final String url = "wss://api.huobi.pro/ws";
	
	private static WebSocketUtils chatclient = null;
	HttpUtil httpUtil;
	public WebSocketUtils(URI serverUri, Map<String, String> headers, int connecttimeout) {
		super(serverUri, new Draft_17(), headers, connecttimeout);
		httpUtil=new HttpUtil();
		//添加到webclient集合中
		CacheData.webSocketClientMap.put(httpUtil.getPlatformId(), chatclient);
	}
	
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("开流--opened connection");
		//打开后添加订阅
		List<Thirdpartysupportmoney> jiaoyiduis = DBUtil.getJiaoyidui(httpUtil.getPlatformId());
		for (Thirdpartysupportmoney thirdpartysupportmoney : jiaoyiduis) {
			SubModel subModel = new SubModel();
			subModel.setId(thirdpartysupportmoney.getMoneypair());
			//这一句需要进行修改{改}
			subModel.setSub("market."+thirdpartysupportmoney.getMoneypair()+".detail");
			chatclient.send(JSON.toJSONString(subModel));
		}
		
	}
	//需要改这里或者另外一个OnMessage重载方法{改}
	@Override
	public void onMessage(ByteBuffer socketBuffer) {
		try {
			String marketStr = CommonUtils.byteBufferToString(socketBuffer);
			String marketJsonStr = CommonUtils.uncompress(marketStr);
			//System.out.println(marketJsonStr);
			if (marketJsonStr.contains("ping")) {
				// Client 心跳
				chatclient.send(marketJsonStr.replace("ping", "pong"));
			} else {
				Vo1 vo1 = JSON.parseObject(marketJsonStr, Vo1.class);
				Vo2 vo2 = vo1.getTick();
				if (vo1.getCh() != null && vo2 != null && vo2.getClose() != null) {//如果是订阅的行情数据
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
	
	//{改}
	@Override
	public void onMessage(String message) {
		System.out.println("接收--received: " + message);
	}
	
	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("关流--Connection closed by " + (remote ? "remote peer" : "us"));
		chatclient.connect();//进行重连一次
	}

	//不需要动
	public static void executeWebSocket() throws Exception {
		//WebSocketImpl.DEBUG = true;
		chatclient = new WebSocketUtils(new URI(url), getWebSocketHeaders(), 1000);
		trustAllHosts(chatclient);//添加ssh安全信任
		chatclient.connect();//异步链接
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
}