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
import com.btb.huobi.vo.MarketDepthVo1;
import com.btb.huobi.vo.MarketVo1;
import com.btb.huobi.vo.MarketVo2;
import com.btb.okex.vo.MarketContractVo1;
import com.btb.okex.vo.MarketContractVo2;
import com.btb.okex.vo.MarketDepthVo2;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.DBUtil;
import com.btb.util.H2Util;
import com.btb.util.SpringUtil;
public class WebSocketUtils_contract extends WebSocketClient {
	//{改}
	private static final String url = "wss://real.okex.com:10440/websocket/okexapi";
	
	private static WebSocketUtils_contract chatclient = null;
	private static String platformid_this_week="okex_this_week";
	private static String platformid_next_week="okex_next_week";
	private static String platformid_okex_quarter="okex_quarter";
	public WebSocketUtils_contract(URI serverUri, Map<String, String> headers, int connecttimeout) {
		super(serverUri, new Draft_17(), headers, connecttimeout);
	}
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("开流--opened connection");
		//当周,下周,本季度 的交易对都是一样,通过本周平台id获取即可
		List<Thirdpartysupportmoney> jiaoyiduis = DBUtil.getJiaoyidui(platformid_this_week);
		for (Thirdpartysupportmoney thirdpartysupportmoney : jiaoyiduis) {
			String moneyType = thirdpartysupportmoney.getMoneypair().split("_")[0];
			SubModel subModel = new SubModel();
			//本周合约行情订阅
			String chId = "ok_sub_futureusd_"+moneyType+"_ticker_this_week";
			subModel.setChannel(chId);
			chatclient.send(JSON.toJSONString(subModel));
			//本周合约深度订阅
			chId="ok_sub_futureusd_"+moneyType+"_depth_this_week_10";
			subModel.setChannel(chId);
			//chatclient.send(JSON.toJSONString(subModel));
			
			//下周合约行情订阅
			chId = "ok_sub_futureusd_"+moneyType+"_ticker_next_week";
			subModel.setChannel(chId);
			//chatclient.send(JSON.toJSONString(subModel));
			//下周合约深度订阅
			chId="ok_sub_futureusd_"+moneyType+"_depth_next_week_10";
			subModel.setChannel(chId);
			//chatclient.send(JSON.toJSONString(subModel));
			
			//本季度合约行情订阅
			chId = "ok_sub_futureusd_"+moneyType+"_ticker_quarter";
			subModel.setChannel(chId);
			//chatclient.send(JSON.toJSONString(subModel));
			//本季度合约深度订阅
			chId="ok_sub_futureusd_"+moneyType+"_depth_quarter_10";
			subModel.setChannel(chId);
			//chatclient.send(JSON.toJSONString(subModel));
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
		System.out.println("接收--received: " + message);
		String pid=null;
		String startStr=null;
		String endStr=null;
		if (message.contains("_ticker_")) {//行情
			if (message.contains("_ticker_this_week")) {//本周行情
				pid=platformid_this_week;
				endStr="_ticker_this_week";
			}else if (message.contains("_ticker_next_week")) {//下周行情
				pid=platformid_next_week;
				endStr="_ticker_next_week";
			}else if (message.contains("_ticker_quarter")) {//本季度行情
				pid=platformid_okex_quarter;
				endStr="_ticker_quarter";
			}
			if (pid != null) {
				startStr="ok_sub_futureusd_";
				MarketContractVo1 marketContractVo1 = JSON.parseArray(message, MarketContractVo1.class).get(0);
				MarketContractVo2 marketContractVo2 = marketContractVo1.getData();
				
				Market market = new Market();
				market.setAmount(marketContractVo2.getVol());
				market.setClose(marketContractVo2.getLast());
				market.setHigh(marketContractVo2.getHigh());
				market.setLow(marketContractVo2.getLow());
				market.setMoneypair(marketContractVo1.getChannel().replace(startStr, "").replace(endStr, "")+"_usd");
				
			}
		}else if (message.contains("_depth_")) {//深度
			if (message.contains("_depth_this_week_10")) {//本周深度
				pid=platformid_this_week;
			}else if (message.contains("_depth_next_week_10")) {//下周深度
				pid=platformid_next_week;
			}else if (message.contains("_depth_quarter_10")) {//本季度深度
				pid=platformid_okex_quarter;
			}
		}
		
	}
	
	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("关流--Connection closed by " + (remote ? "remote peer" : "us"));
		try {
			WebSocketClient webSocketClient = executeWebSocket();
			CacheData.webSocketClientMap.put(platformid_this_week, webSocketClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//不需要动
	public static WebSocketClient executeWebSocket() throws Exception {
		//WebSocketImpl.DEBUG = true;
		chatclient = new WebSocketUtils_contract(new URI(url), getWebSocketHeaders(), 1000);
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
	private static void trustAllHosts(WebSocketUtils_contract appClient) {
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
		return platformid_this_week;	
	}
}