package com.btb.huobi.vo;

import com.btb.entity.MarketDepth;

public class MarketDepthVo1 {
	String ch;
	long ts;
	MarketDepth tick;
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
	public MarketDepth getTick() {
		return tick;
	}
	public void setTick(MarketDepth tick) {
		this.tick = tick;
	}
	
	
}
