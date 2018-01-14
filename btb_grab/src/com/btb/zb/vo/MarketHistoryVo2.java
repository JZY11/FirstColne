package com.btb.zb.vo;

import java.math.BigDecimal;

/**
 * Created by zhenya.1291813139.com
 * on 2018/1/15.
 * btb_grab.
 */
public class MarketHistoryVo2 {
    Long id;
    BigDecimal amount;
    BigDecimal open;
    BigDecimal close;
    BigDecimal high;
    BigDecimal count;
    BigDecimal vol;
    BigDecimal low;

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public BigDecimal getOpen() {
        return open;
    }
    public void setOpen(BigDecimal open) {
        this.open = open;
    }
    public BigDecimal getClose() {
        return close;
    }
    public void setClose(BigDecimal close) {
        this.close = close;
    }
    public BigDecimal getHigh() {
        return high;
    }
    public void setHigh(BigDecimal high) {
        this.high = high;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public BigDecimal getCount() {
        return count;
    }
    public void setCount(BigDecimal count) {
        this.count = count;
    }
    public BigDecimal getVol() {
        return vol;
    }
    public void setVol(BigDecimal vol) {
        this.vol = vol;
    }
    public BigDecimal getLow() {
        return low;
    }
    public void setLow(BigDecimal low) {
        this.low = low;
    }
}
