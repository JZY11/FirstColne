package com.btb.huobi.vo;

public class MarketDepthVo1 {
	String ch;
	long ts;
	MarketDepthVo2 tick;
	public String getCh() {
		return ch;
	}
	public void setCh(String ch) {
		this.ch = ch;
	}
	public long getTs() {
		return ts;
	}
	public void setTs(long ts) {
		this.ts = ts;
	}
	public MarketDepthVo2 getTick() {
		return tick;
	}
	public void setTick(MarketDepthVo2 tick) {
		this.tick = tick;
	}
	
}
