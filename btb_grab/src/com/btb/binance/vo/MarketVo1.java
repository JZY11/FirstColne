package com.btb.binance.vo;

import java.math.BigDecimal;
import java.util.List;

public class MarketVo1 {
	BigDecimal p;//价格
	Boolean m;//是否是buy
	String s;//交易对
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	public BigDecimal getP() {
		return p;
	}
	public void setP(BigDecimal p) {
		this.p = p;
	}
	public Boolean getM() {
		return m;
	}
	public void setM(Boolean m) {
		this.m = m;
	}
	
}


