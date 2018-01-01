package com.btb.entity;

import java.math.BigDecimal;

import javax.persistence.Id;

import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

@NameStyle(Style.normal)
public class TodayOpenMoney {
	@Id
	String id;
	BigDecimal open;
	String today;
	String platformid;
	String moneypair;
	String moneytype;
	String buymoneytype;
	
	
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
	public String getMoneytype() {
		return moneytype;
	}
	public void setMoneytype(String moneytype) {
		this.moneytype = moneytype;
	}
	public String getBuymoneytype() {
		return buymoneytype;
	}
	public void setBuymoneytype(String buymoneytype) {
		this.buymoneytype = buymoneytype;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public BigDecimal getOpen() {
		return open;
	}
	public void setOpen(BigDecimal open) {
		this.open = open;
	}
	public String getToday() {
		return today;
	}
	public void setToday(String today) {
		this.today = today;
	}
	
	
	
}
