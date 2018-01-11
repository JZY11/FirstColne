package com.btb.binance.vo;

import java.math.BigDecimal;
import java.util.List;

public class MarketVo1 {
	BigDecimal c;//价格
	BigDecimal b;//最新的买价格
	BigDecimal a;//最新的卖价格
	String s;//交易对
	public BigDecimal getC() {
		return c;
	}
	public void setC(BigDecimal c) {
		this.c = c;
	}
	public BigDecimal getB() {
		return b;
	}
	public void setB(BigDecimal b) {
		this.b = b;
	}
	public BigDecimal getA() {
		return a;
	}
	public void setA(BigDecimal a) {
		this.a = a;
	}
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	
}


