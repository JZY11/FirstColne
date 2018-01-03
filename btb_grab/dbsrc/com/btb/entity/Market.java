package com.btb.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.btb.util.StringUtil;
import com.btb.util.TaskUtil;

import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

@NameStyle(Style.normal)
public class Market {
	public Market() {
		// TODO Auto-generated constructor stub
	}
	public Market(String platformid,String buymoneytype) {
		// TODO Auto-generated constructor stub
		this.buymoneytype=buymoneytype;
		this.platformid=platformid;
	}
	String _id;//自动计算  平台id.交易对
	//必填
	String platformid;//平台id  
	String platformName;//平台名称
	String platformIcon;//平台图标url
	String platformUrl;//平台url
	String moneypair;//交易对比如btcuatf
	//必填
	String moneytype;//币种btcusdt,就是btc 
	String moneytypeName;//币种btcusdt,就是btc的名字比特币
	String company;//公司名称
	String moneytypeIcon;//币种图标
	String moneytypeUrl;//币种url描述
	//必填
	String buymoneytype;//可以使用的币种btcusdt,就是usdt,
	BigDecimal allMoneyCount;//流通量
	
	
	BigDecimal open;//当日凌晨0点开盘价格,定时凌晨0点在k线图取出最新开盘价格
	BigDecimal close;//最新成交价	//行情数据实时
	BigDecimal buy;//买一价格   		//深度定时更改,实时
	BigDecimal sell;//卖一价格  		//深度定时更改,实时
	
	BigDecimal low;//今日最低价		//k线图计算,1.5分钟
	BigDecimal high;//今日最高价		//k线图计算,1.5分钟
	BigDecimal vol;//今日成交额		//k线图计算,1.5分钟
	BigDecimal amount;//今日成交量  //k线图计算,1.5分钟
	BigDecimal count;//今日成笔数     //k线图计算,1.5分钟,这个必须要
	
	public static void main(String[] args) {
		System.out.println((new BigDecimal("0.00003686").subtract(new BigDecimal("0.00003781"))).multiply(new BigDecimal("104813.4411784000")));
	}
	
	
	BigDecimal allMoneyrmb;//流通市值 	//币数量乘以最新价格 ,不用设置
	BigDecimal buyrmb;//买一价格,人民币
	BigDecimal sellrmb;//卖以价格,人民币
	BigDecimal volrmb;//24小时成交额,人民币, 自动生成
	BigDecimal lowrmb;//人民币 , 自动生成
	BigDecimal highrmb;//人民币, 自动生成
	BigDecimal closermb;//最新成交价人民币, 自动生成
	BigDecimal zhangfu;//今日跌涨幅, 自动生成
	BigDecimal zhangfuMoneyrmb;//今日涨幅额度,自动生成
	BigDecimal openrmb;//人民币, 自动生成
	
	Long ts=System.currentTimeMillis();
	public Long getTs() {
		return ts;
	}
	public BigDecimal getAllMoneyCount() {
		allMoneyCount=TaskUtil.bitbCountMap.get(moneytype);
		return allMoneyCount;
	}
	
	public String get_id() {
		_id=getPlatformid()+"."+getMoneytype()+"_"+getBuymoneytype()+".market";
		return _id;
	}
	public BigDecimal getBuy() {
		return buy;
	}
	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}
	public BigDecimal getSell() {
		return sell;
	}
	public void setSell(BigDecimal sell) {
		this.sell = sell;
	}
	public BigDecimal getBuyrmb() {
		buyrmb=StringUtil.ToRmb(buy, platformid, buymoneytype);
		return buyrmb;
	}
	public BigDecimal getSellrmb() {
		sellrmb=StringUtil.ToRmb(sell, platformid, buymoneytype);
		return sellrmb;
	}
	public BigDecimal getZhangfuMoneyrmb() {
		if (getOpenrmb() != null && getClosermb()!=null) {
			zhangfuMoneyrmb=getClosermb().subtract(getOpenrmb());
		}
		return zhangfuMoneyrmb;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getZhangfu() {
		zhangfu=StringUtil.getbaifenbi(getOpen(), getClose());
		return zhangfu;
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
	public BigDecimal getOpen() {
		open=TaskUtil.todayOpen.get(platformid+"."+moneytype+"_"+buymoneytype);
		return open;
	}
	public BigDecimal getOpenrmb() {
		openrmb=StringUtil.ToRmb(getOpen(), platformid, buymoneytype);
		return openrmb;
	}
	public BigDecimal getClose() {
		return close;
	}
	public void setClose(BigDecimal close) {
		this.close = close;
	}
	public BigDecimal getClosermb() {
		closermb=StringUtil.ToRmb(close, platformid, buymoneytype);
		return closermb;
	}
	public BigDecimal getCount() {
		return count;
	}
	public void setCount(BigDecimal count) {
		this.count = count;
	}
	public BigDecimal getLow() {
		return low;
	}
	public void setLow(BigDecimal low) {
		this.low = low;
	}
	public BigDecimal getHigh() {
		return high;
	}
	public void setHigh(BigDecimal high) {
		this.high = high;
	}
	public BigDecimal getLowrmb() {
		lowrmb=StringUtil.ToRmb(low, platformid, buymoneytype);
		return lowrmb;
	}
	public BigDecimal getHighrmb() {
		highrmb=StringUtil.ToRmb(high, platformid, buymoneytype);
		return highrmb;
	}
	public BigDecimal getVol() {
		return vol;
	}
	public void setVol(BigDecimal vol) {
		this.vol = vol;
	}
	public BigDecimal getVolrmb() {
		volrmb=StringUtil.ToRmb(vol, platformid, buymoneytype);
		return volrmb;
	}
	public BigDecimal getAllMoneyrmb() {
		if (getAllMoneyCount() != null && getClosermb() != null) {
			allMoneyrmb = getAllMoneyCount().multiply(getClosermb());
		}
		return allMoneyrmb;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public String getPlatformIcon() {
		return platformIcon;
	}
	public void setPlatformIcon(String platformIcon) {
		this.platformIcon = platformIcon;
	}
	public String getPlatformUrl() {
		return platformUrl;
	}
	public void setPlatformUrl(String platformUrl) {
		this.platformUrl = platformUrl;
	}
	public String getMoneytype() {
		return moneytype;
	}
	public void setMoneytype(String moneytype) {
		this.moneytype = moneytype;
	}
	public String getMoneytypeName() {
		return moneytypeName;
	}
	public void setMoneytypeName(String moneytypeName) {
		this.moneytypeName = moneytypeName;
	}
	public String getMoneytypeIcon() {
		return moneytypeIcon;
	}
	public void setMoneytypeIcon(String moneytypeIcon) {
		this.moneytypeIcon = moneytypeIcon;
	}
	public String getMoneytypeUrl() {
		return moneytypeUrl;
	}
	public void setMoneytypeUrl(String moneytypeUrl) {
		this.moneytypeUrl = moneytypeUrl;
	}
	public String getBuymoneytype() {
		return buymoneytype;
	}
	public void setBuymoneytype(String buymoneytype) {
		this.buymoneytype = buymoneytype;
	}
	
	
}
