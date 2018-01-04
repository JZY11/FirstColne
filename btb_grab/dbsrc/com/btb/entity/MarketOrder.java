package com.btb.entity;

import java.math.BigDecimal;

public class MarketOrder {
	BigDecimal price;//交易价格
	Long ts;//交易时间
	BigDecimal amount;//交易量
	String type;//buy(买)/sell(卖)
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Long getTs() {
		return ts;
	}
	public void setTs(Long ts) {
		this.ts = ts;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
