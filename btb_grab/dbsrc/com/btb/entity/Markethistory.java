package com.btb.entity;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.btb.util.DateUtil;
import com.btb.util.StringUtil;

public class Markethistory {
	public Markethistory() {
		// TODO Auto-generated constructor stub
	}
	public Markethistory(String platformid,String buymoneytype) {
		// TODO Auto-generated constructor stub
		this.buymoneytype=buymoneytype;
		this.platformid=platformid;
	}
	@Id
	String id;
	String platformid;//平台id
	String moneypair;//交易对比如btcuatf
	@Id
	Long timeid;//timelongid
	String moneytype;
	String buymoneytype;
	@Transient
	String today;//不在数据库中
	String time_min;
	String time_hour;
	String time_week;
	String time_month;
	String time_day;
	
	
	BigDecimal open;//1分钟时前价格
	BigDecimal openrmb;//1分钟前价格人民币, 自动生成
	BigDecimal close;//最新成交价
	BigDecimal closermb;//最新成交价人民币, 自动生成
	BigDecimal low;//1分钟内最低价
	BigDecimal high;//1分钟内最低价
	BigDecimal lowrmb;//1分钟内最低价,人民币 , 自动生成
	BigDecimal highrmb;//1分钟内最低价,人民币, 自动生成
	BigDecimal vol;//1分钟成交额,原始
	BigDecimal volrmb;//1分钟成交额,人民币, 自动生成
	BigDecimal count;//1分钟成笔数
	BigDecimal amount;//1分钟成交量
	
	
	public String getId() {
		id=getPlatformid()+"."+getMoneytype()+"_"+getBuymoneytype();
		return id;
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
	public Long getTimeid() {
		return timeid;
	}
	public void setTimeid(Long timeid) {
		this.timeid = timeid;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
		return open;
	}
	public void setOpen(BigDecimal open) {
		this.open = open;
	}
	public BigDecimal getOpenrmb() {
		openrmb=StringUtil.ToRmb(open, platformid, buymoneytype);
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
		if (vol==null) {
			if (amount != null) {
				vol=getClosermb().multiply(amount);
			}
		}
		return vol;
	}
	public void setVol(BigDecimal vol) {
		this.vol = vol;
	}
	public BigDecimal getVolrmb() {
		volrmb=StringUtil.ToRmb(vol, platformid, buymoneytype);
		return volrmb;
	}
	public String getTime_min() {
		time_min = DateUtil.dateFormat(new Date(timeid*1000), "yyyy-MM-dd HH:mm:ss");
		return time_min;
	}
	public String getTime_hour() {
		time_hour = DateUtil.dateFormat(new Date(timeid*1000), "yyyy-MM-dd-HH");
		return time_hour;
	}
	public String getTime_week() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(timeid*1000));
		int weekInt = calendar.get(Calendar.WEEK_OF_YEAR);//解决周跨年问题
		if (weekInt==52) {
			int month = calendar.get(Calendar.MONTH)+1;
			if (month==1) {//如果是下年1月,应该取上年的数据
				time_week=(calendar.getWeekYear()-1) +"-"+ calendar.getWeeksInWeekYear()+"week";
			}else {//如果是上年12月份,正常输出
				time_week=calendar.getWeekYear() +"-"+ calendar.getWeeksInWeekYear()+"week";
			}
		}else {
			time_week=calendar.getWeekYear() +"-"+ calendar.getWeeksInWeekYear()+"week";
		}
		return time_week;
	}
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int weekInt = calendar.getWeeksInWeekYear();//解决周跨年问题
		if (weekInt==52) {
			int month = calendar.get(Calendar.MONTH)+1;
			System.out.println(month);
		}
	}
	public String getTime_month() {
		time_month = DateUtil.dateFormat(new Date(timeid*1000), "yyyy-MM");
		return time_month;
	}
	public String getTime_day() {
		time_day = DateUtil.dateFormat(new Date(timeid*1000), "yyyy-MM-dd");
		return time_day;
	}
	
	
}
