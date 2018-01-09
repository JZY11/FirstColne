package com.btb.entity;

import java.math.BigDecimal;

import javax.persistence.Id;

public class AllPlatformKline {
	@Id
	String bitbname;
	String bitbcode;
	Long timeid;
	BigDecimal capSuppy;//市值
	BigDecimal price_btc;
	BigDecimal price_usd;
	BigDecimal vol_usd;
	public String getBitbname() {
		return bitbname;
	}
	public void setBitbname(String bitbname) {
		this.bitbname = bitbname;
	}
	public String getBitbcode() {
		return bitbcode;
	}
	public void setBitbcode(String bitbcode) {
		this.bitbcode = bitbcode;
	}
	public Long getTimeid() {
		return timeid;
	}
	public void setTimeid(Long timeid) {
		this.timeid = timeid;
	}
	public BigDecimal getCapSuppy() {
		return capSuppy;
	}
	public void setCapSuppy(BigDecimal capSuppy) {
		this.capSuppy = capSuppy;
	}
	public BigDecimal getPrice_btc() {
		return price_btc;
	}
	public void setPrice_btc(BigDecimal price_btc) {
		this.price_btc = price_btc;
	}
	public BigDecimal getPrice_usd() {
		return price_usd;
	}
	public void setPrice_usd(BigDecimal price_usd) {
		this.price_usd = price_usd;
	}
	public BigDecimal getVol_usd() {
		return vol_usd;
	}
	public void setVol_usd(BigDecimal vol_usd) {
		this.vol_usd = vol_usd;
	}
	
	
	
}
