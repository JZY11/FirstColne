package com.btb.entity;

import javax.persistence.Id;

public class MarketDepthAndOrdersTop10 {
	
	String _id;
	String platformid;//平台id
	String moneypair;//交易对比如btcuatf
	String moneytype;
	String buymoneytype;
	Object data;//这里面放入买卖盘数据,和订单集合
	public String get_Id() {
		_id=platformid+"."+moneytype+"_"+buymoneytype;
		return _id;
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
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
	
}
