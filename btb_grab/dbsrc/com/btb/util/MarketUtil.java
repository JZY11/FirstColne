package com.btb.util;

import com.alibaba.fastjson.JSON;
import com.btb.entity.Market;

public class MarketUtil {
	
	//更改行情信息
	public static void changeMarket(Market market) {
		Market market2 = TaskUtil.marketMap.get(market.get_id());
		if (market2==null) {
			TaskUtil.marketMap.put(market.get_id(), market);
		}else {
			if (market.getAmount() != null) {
				market2.setAmount(market.getAmount());//今日成交量刷新
			}
			if (market.getVol() != null) {
				market2.setVol(market.getVol());//今日成交额刷新
			}
			if (market.getHigh() != null) {
				market2.setHigh(market.getHigh());//今日最高刷新
			}
			if (market.getLow() != null) {
				market2.setLow(market.getLow());//今日最低刷新
			}
			if (market.getOpen() != null) {
				market2.setOpen(market.getOpen());//今日开盘价格刷新
			}
			if (market.getAllMoneyCount() != null) {
				market2.setAllMoneyCount(market.getAllMoneyCount());//币种流通量
			}
			if (market.getClose() != null) {
				market2.setClose(market.getClose());//结束价格
			}
			if (market.getBuy() != null) {
				market2.setBuy(market.getBuy());//购一价格
			}
			if (market.getSell() != null) {
				market2.setSell(market.getSell());//卖一价格
			}
		}
	}
	
}
