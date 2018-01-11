package com.btb.binance.vo;

import java.math.BigDecimal;
import java.util.List;

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
	public static class MarketVo2{
		Long id;
		Long ts;
		List<MarketVo3> data;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Long getTs() {
			return ts;
		}
		public void setTs(Long ts) {
			this.ts = ts;
		}
		public List<MarketVo3> getData() {
			return data;
		}
		public void setData(List<MarketVo3> data) {
			this.data = data;
		}
		
		
	}
	
	public static class MarketVo3{
		long id;
		BigDecimal amount;
		BigDecimal price;
		String direction;
		Long ts;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public BigDecimal getAmount() {
			return amount;
		}
		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
		public String getDirection() {
			return direction;
		}
		public void setDirection(String direction) {
			this.direction = direction;
		}
		public Long getTs() {
			return ts;
		}
		public void setTs(Long ts) {
			this.ts = ts;
		}
		
	}
}


