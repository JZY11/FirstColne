package com.btb.entity;

import java.math.BigDecimal;

import javax.persistence.Id;

import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

/*
 * 第三方平台,交易对信息管理
 */
@NameStyle(Style.normal)
public class PlatformSupportmoney {
	@Id
	String id;
	String platformid;//平台id
	String moneypair;//交易对比如btcuatf
	String moneytype;//购买的币种比如btcuatf,应该是btc
	String buymoneytype;//使用的币种比如btcuatf,应该是uatf
	String ishave;//是否有效数据,1:有  0:没有
	BigDecimal vol24;//24小时成交额
	BigDecimal pricenow;//现在的价格
	BigDecimal orderBaifubi;//这个交易对 占用 平台交易额的百分比
	String updatetime;//最后更新时间
	String moneypairurl;//moneypairurl,交易对的url,跳转到平台的官网查看某个交易对信息
	
	
	public String getId() {
		id=getPlatformid()+"."+getMoneytype()+"_"+getBuymoneytype();
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMoneypairurl() {
		return moneypairurl;
	}
	public void setMoneypairurl(String moneypairurl) {
		this.moneypairurl = moneypairurl;
	}
	
	public BigDecimal getVol24() {
		return vol24;
	}
	public void setVol24(BigDecimal vol24) {
		this.vol24 = vol24;
	}
	
	public BigDecimal getPricenow() {
		return pricenow;
	}
	public void setPricenow(BigDecimal pricenow) {
		this.pricenow = pricenow;
	}
	
	public BigDecimal getOrderBaifubi() {
		return orderBaifubi;
	}
	public void setOrderBaifubi(BigDecimal orderBaifubi) {
		this.orderBaifubi = orderBaifubi;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getIshave() {
		return ishave;
	}
	public void setIshave(String ishave) {
		this.ishave = ishave;
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
		if (moneytype != null) {
			moneytype=moneytype.toUpperCase();
		}
		return moneytype;
	}
	public void setMoneytype(String moneytype) {
		this.moneytype = moneytype;
	}
	public String getBuymoneytype() {
		if (buymoneytype!=null) {
			buymoneytype=buymoneytype.toUpperCase();
		}
		return buymoneytype;
	}
	public void setBuymoneytype(String buymoneytype) {
		this.buymoneytype = buymoneytype;
	}
	
}
