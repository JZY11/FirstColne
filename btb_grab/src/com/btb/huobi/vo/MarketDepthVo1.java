package com.btb.huobi.vo;

import com.btb.entity.MarketDepthVo;

public class MarketDepthVo1 {
	String ch;
	long ts;
	MarketDepthVo tick;
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
	public MarketDepthVo getTick() {
		return tick;
	}
	public void setTick(MarketDepthVo tick) {
		this.tick = tick;
	}
	
	
}
