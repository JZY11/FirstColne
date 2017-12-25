package com.btb.okex.vo;

import java.math.BigDecimal;

public class MarketVo2 {
	Long timestamp;
	BigDecimal high;
	BigDecimal low;
	BigDecimal vol;//24小时的成交量
	BigDecimal close;
	BigDecimal open;
	BigDecimal last;
	BigDecimal buy;
	BigDecimal change;
	BigDecimal sell;
	BigDecimal dayLow;
	BigDecimal dayHigh;
	
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public BigDecimal getLast() {
		return last;
	}
	public void setLast(BigDecimal last) {
		this.last = last;
	}
	public BigDecimal getBuy() {
		return buy;
	}
	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}
	public BigDecimal getChange() {
		return change;
	}
	public void setChange(BigDecimal change) {
		this.change = change;
	}
	public BigDecimal getSell() {
		return sell;
	}
	public void setSell(BigDecimal sell) {
		this.sell = sell;
	}
	public BigDecimal getDayLow() {
		return dayLow;
	}
	public void setDayLow(BigDecimal dayLow) {
		this.dayLow = dayLow;
	}
	public BigDecimal getDayHigh() {
		return dayHigh;
	}
	public void setDayHigh(BigDecimal dayHigh) {
		this.dayHigh = dayHigh;
	}
	public BigDecimal getOpen() {
		return open;
	}
	public void setOpen(BigDecimal open) {
		this.open = open;
	}
	public BigDecimal getClose() {
		return close;
	}
	public void setClose(BigDecimal close) {
		this.close = close;
	}
	public BigDecimal getHigh() {
		return high;
	}
	public void setHigh(BigDecimal high) {
		this.high = high;
	}
	public BigDecimal getVol() {
		return vol;
	}
	public void setVol(BigDecimal vol) {
		this.vol = vol;
	}
	public BigDecimal getLow() {
		return low;
	}
	public void setLow(BigDecimal low) {
		this.low = low;
	}
	
}
