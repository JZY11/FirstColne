package com.btb.entity;

import java.util.List;

public class QueryVo {
	String platformid;
	List<String> moneypairs;
	
	
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
