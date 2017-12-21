package com.btb.entity;
/*
 * 第三方平台,交易对信息管理
 */
public class Thirdpartysupportmoney {
	String platformid;//平台id
	String moneypair;//交易对比如btcuatf
	String moneytype;//购买的币种比如btcuatf,应该是btc
	String buymoneytype;//使用的币种比如btcuatf,应该是uatf
	
	
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
	
}
