package com.btb.entity;

import java.math.BigDecimal;

import javax.persistence.Id;
/**
 * 银行汇率
 *
 */
public class Rate {
	@Id
	String moneycode;
	String moneyname;
	BigDecimal rate;
	String updatetime;
	public String getMoneycode() {
		return moneycode;
	}
	public void setMoneycode(String moneycode) {
		this.moneycode = moneycode;
	}
	public String getMoneyname() {
		return moneyname;
	}
	public void setMoneyname(String moneyname) {
		this.moneyname = moneyname;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	
	
}
