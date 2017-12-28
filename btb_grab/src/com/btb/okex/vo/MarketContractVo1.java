package com.btb.okex.vo;

import java.util.List;

public class MarketContractVo1 {
	String channel;
	List<Object[]> data;
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public List<Object[]> getData() {
		return data;
	}
	public void setData(List<Object[]> data) {
		this.data = data;
	}
	
}
