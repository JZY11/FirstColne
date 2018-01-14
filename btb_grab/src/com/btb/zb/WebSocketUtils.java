package com.btb.zb;

import com.alibaba.fastjson.JSON;
import com.btb.entity.Market;
import com.btb.entity.PlatformSupportmoney;
import com.btb.util.MarketUtil;
import com.btb.util.StringUtil;
import com.btb.util.TaskUtil;
import com.btb.util.dao.BaseDaoSql;
import com.btb.zb.vo.MarketVo1;
import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhenya.1291813139.com
 * on 2018/1/15.
 * btb_grab.
 */
public class WebSocketUtils extends WebSocketClient {
    //{改}

    private static final String url = "wss://api.zb.com:9999/websocket";

    private static WebSocketUtils chatclient = null;
    private static String platformid=new HttpUtil().getPlatformId();
    public WebSocketUtils(URI serverUri, Map<String, String> headers, int connecttimeout) {
        super(serverUri, new Draft_17(), headers, connecttimeout);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("开流--opened connection");
        List<PlatformSupportmoney> jiaoyiduis = BaseDaoSql.findList("select * from platformsupportmoney where platformid='"+platformid+"'", PlatformSupportmoney.class);
        for (PlatformSupportmoney thirdpartysupportmoney : jiaoyiduis) {
            SubModel subModel = new SubModel();
            //打开后添加实时行情订阅

            String chId = thirdpartysupportmoney.getMoneypair().replace("_", "")+"_ticker";
            subModel.setChannel(chId);
            chatclient.send(JSON.toJSONString(subModel));
        }

    }

    //需要改这里或者另外一个OnMessage重载方法{改}
    @Override
    public void onMessage(ByteBuffer socketBuffer) {

    }

    //{改}

    @Override
    public void onMessage(String message) {
        MarketVo1 vo1 = JSON.parseObject(message, MarketVo1.class);
        System.out.println(vo1.getTicker());

        Market market = new Market();
        market.setPlatformid(platformid);//平台id 必填

        String moneypair = vo1.getChannel().replace("_ticker", "");
        String[] strings = StringUtil.getHuobiBuyMoneytype(moneypair);
        market.setMoneypair(strings[1]+"_"+strings[0]);//交易对 必填

        market.setBuymoneytype(strings[0]);
        market.setMoneytype(strings[1]);
        MarketVo1.MarketVo2 marketVo2 = vo1.getTicker();
        market.setClose(marketVo2.getLast());//最新价格 必填

        market.setSell(marketVo2.getSell());
        market.setBuy(marketVo2.getBuy());
        System.out.println(JSON.toJSON(market));
        //添加或者更新行情数据

        MarketUtil.changeMarket(market);

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
