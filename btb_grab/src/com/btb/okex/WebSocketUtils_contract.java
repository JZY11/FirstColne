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
import com.btb.entity.Market;
import com.btb.entity.MarketOrder;
import com.btb.entity.PlatformSupportmoney;
import com.btb.okex.vo.MarketContractVo1;
import com.btb.okex.vo.MarketDepthVo1;
import com.btb.util.MongoDbUtil;
import com.btb.util.TaskUtil;
import com.btb.util.dao.BaseDaoSql;
public class WebSocketUtils_contract extends WebSocketClient {
	//{改}
	private static final String url = "wss://real.okex.com:10440/websocket/okexapi";
	
	private static WebSocketUtils_contract chatclient = null;
	private static String platformid_this_week="OKEx_this_week";
	private static String platformid_next_week="OKEx_next_week";
	private static String platformid_okex_quarter="OKEx_quarter";
	public WebSocketUtils_contract(URI serverUri, Map<String, String> headers, int connecttimeout) {
		super(serverUri, new Draft_17(), headers, connecttimeout);
	}
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("开流--opened connection");
		//当周,下周,本季度 的交易对都是一样,通过本周平台id获取即可
		List<PlatformSupportmoney> jiaoyiduis = BaseDaoSql.findList("select * from platformsupportmoney where platformid='"+platformid_this_week+"'", PlatformSupportmoney.class);
		for (PlatformSupportmoney PlatformSupportmoney : jiaoyiduis) {
			String moneyType = PlatformSupportmoney.getMoneypair().split("_")[0];
			SubModel subModel = new SubModel();
			//本周合约行情订阅
			String chId = "ok_sub_futureusd_"+moneyType+"_trade_this_week";
			subModel.setChannel(chId);
			chatclient.send(JSON.toJSONString(subModel));
			
			//下周合约行情订阅
			chId = "ok_sub_futureusd_"+moneyType+"_trade_next_week";
			subModel.setChannel(chId);
			chatclient.send(JSON.toJSONString(subModel));
			
			//本季度合约行情订阅
			chId = "ok_sub_futureusd_"+moneyType+"_trade_quarter";
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
		String pid=null;
		String startStr=null;
		String endStr=null;
		if (message.contains("_trade_") && !message.contains("addChannel")) {//行情
			if (message.contains("_trade_this_week")) {//本周行情
				pid=platformid_this_week;
				endStr="_trade_this_week";
			}else if (message.contains("_trade_next_week")) {//下周行情
				pid=platformid_next_week;
				endStr="_trade_next_week";
			}else if (message.contains("_trade_quarter")) {//本季度行情
				pid=platformid_okex_quarter;
				endStr="_trade_quarter";
			}
			if (pid != null) {
				startStr="ok_sub_futureusd_";
				MarketContractVo1 marketContractVo1 = null;
				try {
					marketContractVo1 = JSON.parseArray(message, MarketContractVo1.class).get(0);
					List<Object[]> data = marketContractVo1.getData();
					Market market = new Market();
					market.setMoneypair(marketContractVo1.getChannel().replace(startStr, "").replace(endStr, "")+"_usd");
					String[] split = market.getMoneypair().split("_");
					market.setMoneytype(split[0]);
					market.setBuymoneytype(split[1]);
					market.setPlatformid(pid);
					for (Object[] order : data) {
						market.setClose(new BigDecimal(order[1].toString()));
						if (order[4].equals("ask")) {
							market.setSell(new BigDecimal(order[1].toString()));//主动方
						}else {
							market.setBuy(new BigDecimal(order[1].toString()));//主动方
						}
					}
					//添加或者更新行情数据
					MongoDbUtil.insertOrUpdate(market);
				} catch (Exception e) {}
			}
		}
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
		chatclient = new WebSocketUtils_contract(new URI(url), getWebSocketHeaders(), 1000);
		trustAllHosts(chatclient);//添加ssh安全信任
		chatclient.connect();//异步链接
		TaskUtil.webSocketClientMap.put(platformid_this_week, chatclient);
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