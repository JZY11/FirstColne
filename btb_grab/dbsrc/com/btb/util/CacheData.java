package com.btb.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Spring;

import org.java_websocket.client.WebSocketClient;

import com.btb.dao.MarketHistoryMapper;
import com.btb.dao.ThirdpartysupportmoneyMapper;
import com.btb.entity.Bitbinfo;
import com.btb.entity.MarketDepthVo;
import com.btb.entity.Markethistory;
import com.btb.entity.Thirdpartysupportmoney;

public class CacheData  {
	//汇率表缓存,每隔一天更新一次
	public static Map<String, BigDecimal> rateMap = new HashMap<>();
	//http, <平台id,采集类>
	public static Map<String,BaseHttp> httpBeans=new HashMap<>();
	//交易对<平台id,交易对集合>
	public static Map<String,List<Thirdpartysupportmoney>> moneyPairs=new HashMap<>();
	//每个平台的websocketClient链接
	public static Map<String, WebSocketClient> webSocketClientMap = new HashMap<>();
	//每个交易对,今日开盘价格Map<平台id.交易对,最新价格>
	public static Map<String, BigDecimal> todayOpen = new HashMap<>();
	//获取比特币流通量,每隔一小时采集
	public static Map<String, BigDecimal> bitbCountMap = new HashMap<>();
	//获取每个平台的btc,eth实时价格,用于转换成人民币
	//Map<交易平台id.btc/eth>
	public static Map<String, BigDecimal> nowBtcEthRmb=new HashMap<>();
	//买卖盘Map<交易平台id.交易对,买卖盘>
	public static Map<String, MarketDepthVo> sellBuyDisk = new HashMap<>();
	
	static{
		rateMap.put("USD", new BigDecimal("0.1513200000"));
	}
	public static void main(String[] args) throws ParseException {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse(DateUtil.getDateNoTime()).getTime());
	}
}
