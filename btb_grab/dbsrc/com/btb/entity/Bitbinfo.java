package com.btb.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;

import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

@NameStyle(Style.normal)
public class Bitbinfo {
	String ccid;
	@Id
	String bitbcode;//比特币缩写
	String bitbname;//英文名称
	String bitbcnname;//中文名称
	String bitbfullname;//比特币fullname
	BigDecimal currentcount;//当前量
	BigDecimal allcount;//总量
	String iconurl;//图片地址
	@Column(name="description")
	String desc;//描述
	Integer rank;//排序
	String checkerror;//验证是否有效时是否出错
	String ccoverviewurl;//cc详情页
	String Features;//特性
	String Technology;
	String AffiliateUrl;//官网
	String moneyurl;//币种url
	BigDecimal priceRmb;//人民币,当前全网价格
	BigDecimal vol24Rmb;//24小时成交额
	BigDecimal marketCapRmb;//当前市值
	Long last_updated;//最后更新时间
	BigDecimal percent_change_1h;//1小时涨幅
	BigDecimal percent_change_24h;//24小时涨幅
	BigDecimal percent_change_7d;//7天涨幅
	
	
	public BigDecimal getPriceRmb() {
		return priceRmb;
	}
	public void setPriceRmb(BigDecimal priceRmb) {
		this.priceRmb = priceRmb;
	}
	public BigDecimal getVol24Rmb() {
		return vol24Rmb;
	}
	public void setVol24Rmb(BigDecimal vol24Rmb) {
		this.vol24Rmb = vol24Rmb;
	}
	public BigDecimal getMarketCapRmb() {
		return marketCapRmb;
	}
	public void setMarketCapRmb(BigDecimal marketCapRmb) {
		this.marketCapRmb = marketCapRmb;
	}
	public Long getLast_updated() {
		return last_updated;
	}
	public void setLast_updated(Long last_updated) {
		this.last_updated = last_updated;
	}
	public BigDecimal getPercent_change_1h() {
		return percent_change_1h;
	}
	public void setPercent_change_1h(BigDecimal percent_change_1h) {
		this.percent_change_1h = percent_change_1h;
	}
	public BigDecimal getPercent_change_24h() {
		return percent_change_24h;
	}
	public void setPercent_change_24h(BigDecimal percent_change_24h) {
		this.percent_change_24h = percent_change_24h;
	}
	public BigDecimal getPercent_change_7d() {
		return percent_change_7d;
	}
	public void setPercent_change_7d(BigDecimal percent_change_7d) {
		this.percent_change_7d = percent_change_7d;
	}
	public String getMoneyurl() {
		return moneyurl;
	}
	public void setMoneyurl(String moneyurl) {
		this.moneyurl = moneyurl;
	}
	public String getCcoverviewurl() {
		return ccoverviewurl;
	}
	public void setCcoverviewurl(String ccoverviewurl) {
		this.ccoverviewurl = ccoverviewurl;
	}
	public String getFeatures() {
		return Features;
	}
	public void setFeatures(String features) {
		Features = features;
	}
	public String getTechnology() {
		return Technology;
	}
	public void setTechnology(String technology) {
		Technology = technology;
	}
	public String getAffiliateUrl() {
		return AffiliateUrl;
	}
	public void setAffiliateUrl(String affiliateUrl) {
		AffiliateUrl = affiliateUrl;
	}
	public String getCheckerror() {
		return checkerror;
	}
	public void setCheckerror(String checkerror) {
		this.checkerror = checkerror;
	}
	
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public String getCcid() {
		return ccid;
	}
	public void setCcid(String ccid) {
		this.ccid = ccid;
	}
	public String getBitbfullname() {
		return bitbfullname;
	}
	public void setBitbfullname(String bitbfullname) {
		this.bitbfullname = bitbfullname;
	}
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
