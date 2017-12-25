package com.btb.okex.vo;

import java.math.BigDecimal;
import java.util.List;

public class MarketDepthVo2 {
	List<BigDecimal[]> bids;
	List<BigDecimal[]> asks;
	Long timestamp;
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
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	
}
