package com.btb.entity;

import java.math.BigDecimal;

import javax.persistence.Column;

public class Bitbinfo {
	String bitbcode;//比特币缩写
	String bitbname;//英文名称
	String bitbcnname;//中文名称
	BigDecimal currentcount;//当前量
	BigDecimal allcount;//总量
	String iconurl;//图片地址
	@Column(name="description")
	String desc;//描述
	
	
	public String getBitbcode() {
		return bitbcode;
	}
	public void setBitbcode(String bitbcode) {
		this.bitbcode = bitbcode;
	}
	public String getBitbname() {
		return bitbname;
	}
	public void setBitbname(String bitbname) {
		this.bitbname = bitbname;
	}
	public String getBitbcnname() {
		return bitbcnname;
	}
	public void setBitbcnname(String bitbcnname) {
		this.bitbcnname = bitbcnname;
	}
	public BigDecimal getCurrentcount() {
		return currentcount;
	}
	public void setCurrentcount(BigDecimal currentcount) {
		this.currentcount = currentcount;
	}
	public BigDecimal getAllcount() {
		return allcount;
	}
	public void setAllcount(BigDecimal allcount) {
		this.allcount = allcount;
	}
	public String getIconurl() {
		return iconurl;
	}
	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "Bitbinfo [bitbcode=" + bitbcode + ", bitbname=" + bitbname + ", bitbcnname=" + bitbcnname
				+ ", currentcount=" + currentcount + ", allcount=" + allcount + ", iconurl=" + iconurl + ", desc="
				+ desc + "]";
	}
	
	
	
	
}
