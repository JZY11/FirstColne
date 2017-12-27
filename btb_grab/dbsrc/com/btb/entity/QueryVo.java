package com.btb.entity;

import java.math.BigDecimal;
import java.util.List;

public class QueryVo {
	String platformid;
	BigDecimal closermb;
	List<String> moneypairs;
	
	
	public BigDecimal getClosermb() {
		return closermb;
	}
	public void setClosermb(BigDecimal closermb) {
		this.closermb = closermb;
	}
	public String getPlatformid() {
		return platformid;
	}
	public void setPlatformid(String platformid) {
		this.platformid = platformid;
	}
	public List<String> getMoneypairs() {
		return moneypairs;
	}
	public void setMoneypairs(List<String> moneypairs) {
		this.moneypairs = moneypairs;
	}
	
}
