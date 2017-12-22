package com.btb.entity;

import java.math.BigDecimal;

import javax.persistence.Id;

import com.btb.util.StringUtil;

public class Markethistory {
	@Id
	String platformid;//平台id
	@Id
	String moneypair;//交易对比如btcuatf
	@Id
	Long timeid;//timelongid
	BigDecimal open;//24小时前价格
	BigDecimal openrmb;//24小时前价格人民币, 自动生成
	BigDecimal close;//最新成交价
	BigDecimal closermb;//最新成交价人民币, 自动生成
	BigDecimal low;//1分钟内最低价
	BigDecimal high;//1分钟内最低价
	BigDecimal lowrmb;//1分钟内最低价,人民币 , 自动生成
	BigDecimal highrmb;//1分钟内最低价,人民币, 自动生成
	BigDecimal vol;//1分钟成交额,原始
	BigDecimal volrmb;//1分钟成交额,人民币, 自动生成
	BigDecimal count;//1分钟成笔数
	BigDecimal amount;//1分钟成交量
	
	
	public Long getTimeid() {
		return timeid;
	}
	public void setTimeid(Long timeid) {
		this.timeid = timeid;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
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
	public BigDecimal getOpen() {
		return open;
	}
	public void setOpen(BigDecimal open) {
		this.open = open;
	}
	public BigDecimal getOpenrmb() {
		openrmb=StringUtil.UsdToRmb(open);
		return openrmb;
	}
	public BigDecimal getClose() {
		return close;
	}
	public void setClose(BigDecimal close) {
		this.close = close;
	}
	public BigDecimal getClosermb() {
		closermb=StringUtil.UsdToRmb(close);
		return closermb;
	}
	public BigDecimal getCount() {
		return count;
	}
	public void setCount(BigDecimal count) {
		this.count = count;
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
		lowrmb=StringUtil.UsdToRmb(low);
		return lowrmb;
	}
	public BigDecimal getHighrmb() {
		highrmb=StringUtil.UsdToRmb(high);
		return highrmb;
	}
	public BigDecimal getVol() {
		return vol;
	}
	public void setVol(BigDecimal vol) {
		this.vol = vol;
	}
	public BigDecimal getVolrmb() {
		volrmb=StringUtil.UsdToRmb(vol);
		return volrmb;
	}
	
}
