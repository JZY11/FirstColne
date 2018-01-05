package com.btb.entity;

import java.math.BigDecimal;
import java.util.List;

import com.btb.util.StringUtil;

public class MarketDepth {
	List<BigDecimal[]> bids;//[买1价,买1量]
	List<BigDecimal[]> asks;//[卖1价,卖1量]
	Long ts;
	public List<BigDecimal[]> getBids() {
		return bids;
	}
	public void setBids(List<BigDecimal[]> bids) {
		this.bids = bids;
	}
	public List<BigDecimal[]> getAsks() {
		return asks;
	}
	public void setAsks(List<BigDecimal[]> asks) {
		this.asks = asks;
	}
	public Long getTs() {
		return ts;
	}
	public void setTs(Long ts) {
		this.ts = ts;
	}
	
}
