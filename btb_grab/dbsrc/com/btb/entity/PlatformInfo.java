package com.btb.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;

import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;
/**
 * 第三方交易平台信息
 */
@NameStyle(Style.normal)
public class PlatformInfo {
	@Id
	String id;//平台id
	String name;//平台名称
	String urlicon;//平台图标地址
	String url;//平台url官网
	@Column(name="description")
	String desc;//平台描述
	String apiurl;//平台api的url地址
	String company;//公司名称
	String companydesc;//标签描述
	BigDecimal btc_vol;//24小时交易额
	BigDecimal usd_vol;//24小时交易额
	BigDecimal rmb_vol;//24小时交易额
	String coinmarketcapurl;//接口url
	Integer volrank;//交易额排名
	
	
	public Integer getVolrank() {
		return volrank;
	}
	public void setVolrank(Integer volrank) {
		this.volrank = volrank;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompanydesc() {
		return companydesc;
	}
	public void setCompanydesc(String companydesc) {
		this.companydesc = companydesc;
	}
	public BigDecimal getBtc_vol() {
		return btc_vol;
	}
	public void setBtc_vol(BigDecimal btc_vol) {
		this.btc_vol = btc_vol;
	}
	public BigDecimal getUsd_vol() {
		return usd_vol;
	}
	public void setUsd_vol(BigDecimal usd_vol) {
		this.usd_vol = usd_vol;
	}
	public BigDecimal getRmb_vol() {
		return rmb_vol;
	}
	public void setRmb_vol(BigDecimal rmb_vol) {
		this.rmb_vol = rmb_vol;
	}
	public String getCoinmarketcapurl() {
		return coinmarketcapurl;
	}
	public void setCoinmarketcapurl(String coinmarketcapurl) {
		this.coinmarketcapurl = coinmarketcapurl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrlicon() {
		return urlicon;
	}
	public void setUrlicon(String urlicon) {
		this.urlicon = urlicon;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getApiurl() {
		return apiurl;
	}
	public void setApiurl(String apiurl) {
		this.apiurl = apiurl;
	}
	
	
	
}
