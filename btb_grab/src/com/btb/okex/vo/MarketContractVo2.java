package com.btb.okex.vo;

import java.math.BigDecimal;
import java.util.List;

public class MarketContractVo2 {
	Long timestamp;
	BigDecimal high;//24小时最高价格
	BigDecimal limitLow;//最低卖出限制价格
	BigDecimal vol;//24小时成交量
	BigDecimal last;//最新价格
	BigDecimal low;//24小时最低价格
	BigDecimal buy;
	BigDecimal hold_amount;//当前持仓量
	BigDecimal sell;//卖一价格
	BigDecimal unitAmount;//合约价值
	BigDecimal limitHigh;//最高买入限制价格
	Long contractId;//合约ID
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public BigDecimal getHigh() {
		return high;
	}
	public void setHigh(BigDecimal high) {
		this.high = high;
	}
	public BigDecimal getLimitLow() {
		return limitLow;
	}
	public void setLimitLow(BigDecimal limitLow) {
		this.limitLow = limitLow;
	}
	public BigDecimal getVol() {
		return vol;
	}
	public void setVol(BigDecimal vol) {
		this.vol = vol;
	}
	public BigDecimal getLast() {
		return last;
	}
	public void setLast(BigDecimal last) {
		this.last = last;
	}
	public BigDecimal getLow() {
		return low;
	}
	public void setLow(BigDecimal low) {
		this.low = low;
	}
	public BigDecimal getBuy() {
		return buy;
	}
	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}
	public BigDecimal getHold_amount() {
		return hold_amount;
	}
	public void setHold_amount(BigDecimal hold_amount) {
		this.hold_amount = hold_amount;
	}
	public BigDecimal getSell() {
		return sell;
	}
	public void setSell(BigDecimal sell) {
		this.sell = sell;
	}
	public BigDecimal getUnitAmount() {
		return unitAmount;
	}
	public void setUnitAmount(BigDecimal unitAmount) {
		this.unitAmount = unitAmount;
	}
	public BigDecimal getLimitHigh() {
		return limitHigh;
	}
	public void setLimitHigh(BigDecimal limitHigh) {
		this.limitHigh = limitHigh;
	}
	public Long getContractId() {
		return contractId;
	}
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}
}
