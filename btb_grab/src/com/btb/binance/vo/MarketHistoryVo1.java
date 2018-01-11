package com.btb.binance.vo;

import java.util.List;

public class MarketHistoryVo1 {
	String ch;
	Long ts;
	String status;
	List<MarketHistoryVo2> data;
	
	public String getCh() {
		return ch;
	}
	public void setCh(String ch) {
		this.ch = ch;
	}
	public Long getTs() {
		return ts;
	}
	public void setTs(Long ts) {
		this.ts = ts;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<MarketHistoryVo2> getData() {
		return data;
	}
	public void setData(List<MarketHistoryVo2> data) {
		this.data = data;
	}
	
	
}
