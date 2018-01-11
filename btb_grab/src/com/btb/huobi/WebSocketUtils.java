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
import com.btb.entity.Market;
import com.btb.entity.MarketDepth;
import com.btb.entity.MarketOrder;
import com.btb.entity.PlatformSupportmoney;
import com.btb.huobi.vo.MarketDepthVo1;
import com.btb.huobi.vo.MarketVo1;
import com.btb.huobi.vo.MarketVo1.MarketVo2;
import com.btb.huobi.vo.MarketVo1.MarketVo3;
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
	private static final String url = "wss://api.huobi.pro/ws";
	
	private static WebSocketUtils chatclient = null;
	private static String platformid=new HttpUtil().getPlatformId();
	public WebSocketUtils(URI serverUri, Map<String, String> headers, int connecttimeout) {
		super(serverUri, new Draft_17(), headers, connecttimeout);
	}
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("开流--opened connection");
		List<PlatformSupportmoney> jiaoyiduis = BaseDaoSql.findList("select * from platformsupportmoney where platformid='"+platformid+"'", PlatformSupportmoney.class);
		for (PlatformSupportmoney thirdpartysupportmoney : jiaoyiduis) {
			SubModel subModel = new SubModel();
			//打开后添加实时行情订阅
			String chId = "market."+thirdpartysupportmoney.getMoneypair()+".trade.detail";
			subModel.setId(chId);
			subModel.setSub(chId);
			chatclient.send(JSON.toJSONString(subModel));
		}
		
		
	}
	//需要改这里或者另外一个OnMessage重载方法{改}
	@Override
	public void onMessage(ByteBuffer socketBuffer) {
		try {
			String marketStr = CommonUtils.byteBufferToString(socketBuffer);
			String marketJsonStr = CommonUtils.uncompress(marketStr);
			if (marketJsonStr.contains("ping")) {
				//System.out.println(marketJsonStr);
				// Client 心跳
				chatclient.send(marketJsonStr.replace("ping", "pong"));
			} else {
				if (marketJsonStr.contains("trade.detail")) {
					//实时行情数据
					MarketVo1 vo1 = JSON.parseObject(marketJsonStr, MarketVo1.class);
					MarketVo3 vo3=null;
					try {
						vo3 = vo1.getTick().getData().get(0);
					} catch (Exception e) {}
					if (vo3!=null && vo3.getPrice() != null) {//如果是订阅的行情数据
						Market market = new Market();
						market.setPlatformid(platformid);//平台id 必填
						market.setMoneypair(vo1.getCh().split("\\.")[1]);//交易对 必填
						String[] strings = StringUtil.getHuobiBuyMoneytype(market.getMoneypair());
						market.setBuymoneytype(strings[0]);
						market.setMoneytype(strings[1]);
						market.setClose(vo3.getPrice());//最新价格 必填
						if (vo3.getDirection().equals("sell")) {
							market.setSell(vo3.getPrice());
						}else {
							market.setBuy(vo3.getPrice());
						}
						//添加或者更新行情数据
						MongoDbUtil.insertOrUpdate(market);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//{改}
	@Override
	public void onMessage(String message) {
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
		chatclient = new WebSocketUtils(new URI(url), getWebSocketHeaders(), 1000);
		trustAllHosts(chatclient);//添加ssh安全信任
		chatclient.connect();//异步链接
		TaskUtil.webSocketClientMap.put(platformid, chatclient);
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