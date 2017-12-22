package com.btb.huobi.vo;

public class MarketVo1 {
	String ch;
	Long ts;
	MarketVo2 tick;
	
	
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
	public MarketVo2 getTick() {
		return tick;
	}
	public void setTick(MarketVo2 tick) {
		this.tick = tick;
	}
	
	
}
