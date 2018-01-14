package com.btb.zb.vo;

import java.math.BigDecimal;

/**
 * Created by zhenya.1291813139.com
 * on 2018/1/15.
 * btb_grab.
 */
public class MarketVo1 {
    String channel;
    MarketVo2 ticker;
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public MarketVo2 getTicker() {
        return ticker;
    }

    public void setTicker(MarketVo2 ticker) {
        this.ticker = ticker;
    }

    public static class MarketVo2{
        BigDecimal buy;
        BigDecimal last;
        BigDecimal sell;
        public BigDecimal getBuy() {
            return buy;
        }
        public void setBuy(BigDecimal buy) {
            this.buy = buy;
        }
        public BigDecimal getLast() {
            return last;
        }
        public void setLast(BigDecimal last) {
            this.last = last;
        }
        public BigDecimal getSell() {
            return sell;
        }
        public void setSell(BigDecimal sell) {
            this.sell = sell;
        }

    }
}
