package com.btb.entity;

import java.math.BigDecimal;

import javax.persistence.Id;

public class MarketHistory {
	@Id
	String platformid;//平台id
	@Id
	String moneypair;//交易对比如btcuatf
	@Id
	Long timeId;//timelongid
	BigDecimal open;//24小时前价格
	BigDecimal openrmb;//24小时前价格人民币
	BigDecimal close;//最新成交价
	BigDecimal closermb;//最新成交价人民币
	BigDecimal low;//24小时内最低价
	BigDecimal high;//24小时内最低价
	BigDecimal lowrmb;//24小时内最低价,人民币
	BigDecimal highrmb;//24小时内最低价,人民币
	BigDecimal vol;//24小时成交额,原始
	BigDecimal volrmb;//24小时成交额,人民币
	BigDecimal count;//24小时成笔数
	BigDecimal amount;//成交量
	
	
	public String getPlatformid() {
		return platformid;
	}
	public void setPlatformid(String platformid) {
		this.platformid = platformid;
	}
	public String getMoneypair() {
		return moneypair;
	}
	public void setMoneypair(String moneypair) {
		this.moneypair = moneypair;
	}
	public Long getTimeId() {
		return timeId;
	}
	public void setTimeId(Long timeId) {
		this.timeId = timeId;
	}
	public BigDecimal getOpen() {
		return open;
	}
	public void setOpen(BigDecimal open) {
		this.open = open;
	}
	public BigDecimal getOpenrmb() {
		return openrmb;
	}
	public void setOpenrmb(BigDecimal openrmb) {
		this.openrmb = openrmb;
	}
	public BigDecimal getClose() {
		return close;
	}
	public void setClose(BigDecimal close) {
		this.close = close;
	}
	public BigDecimal getClosermb() {
		return closermb;
	}
	public void setClosermb(BigDecimal closermb) {
		this.closermb = closermb;
	}
	public BigDecimal getLow() {
		return low;
	}
	public void setLow(BigDecimal low) {
		this.low = low;
	}
	public BigDecimal getHigh() {
		return high;
	}
	public void setHigh(BigDecimal high) {
		this.high = high;
	}
	public BigDecimal getLowrmb() {
		return lowrmb;
	}
	public void setLowrmb(BigDecimal lowrmb) {
		this.lowrmb = lowrmb;
	}
	public BigDecimal getHighrmb() {
		return highrmb;
	}
	public void setHighrmb(BigDecimal highrmb) {
		this.highrmb = highrmb;
	}
	public BigDecimal getVol() {
		return vol;
	}
	public void setVol(BigDecimal vol) {
		this.vol = vol;
	}
	public BigDecimal getVolrmb() {
		return volrmb;
	}
	public void setVolrmb(BigDecimal volrmb) {
		this.volrmb = volrmb;
	}
	public BigDecimal getCount() {
		return count;
	}
	public void setCount(BigDecimal count) {
		this.count = count;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/*
	 * 
	 * "id":1513785600,
	 * "open":16631.280000000000000000,
	 * "close":16961.790000000000000000,
	 * "low":16000.000000000000000000,
	 * "high":17200.000000000000000000,
	 * "amount":1953.466880230603314954,
	 * "vol":32251924.863476369298610826670000000000000000,
	 * "count":40525}
	 */
	
	
}
