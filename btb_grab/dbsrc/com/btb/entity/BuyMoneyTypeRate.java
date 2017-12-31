package com.btb.entity;

import java.math.BigDecimal;

import javax.persistence.Id;

import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

@NameStyle(Style.normal)
public class BuyMoneyTypeRate {
	@Id
	String id;
	BigDecimal closermb;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getClosermb() {
		return closermb;
	}
	public void setClosermb(BigDecimal closermb) {
		this.closermb = closermb;
	}
	
	
}
