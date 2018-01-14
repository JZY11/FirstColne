package com.btb.zb.vo;

import java.util.LinkedList;

/**
 * Created by zhenya.1291813139.com
 * on 2018/1/15.
 * btb_grab.
 */
public class MarketHistoryVo1 {
    String moneyType;
    String symbol;
    LinkedList<Object[]> data;
    public String getMoneyType() {
        return moneyType;
    }
    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public LinkedList<Object[]> getData() {
        return data;
    }
    public void setData(LinkedList<Object[]> data) {
        this.data = data;
    }
}
