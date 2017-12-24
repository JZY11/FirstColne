package com.btb.okex;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.btb.dao.MarketHistoryMapper;
import com.btb.entity.Market;
import com.btb.entity.Markethistory;
import com.btb.entity.Thirdpartysupportmoney;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.StringUtil;
/*
 * 行情数据
 * https://api.huobi.pro/market/detail?symbol=ethusdt
 * 交易对查询
 * https://api.huobi.pro/v1/common/symbols
 * 查询所有支持的币种
 * https://api.huobi.pro/v1/common/currencys
 */
@Service("ikexHttpUtil")
public class HttpUtil extends BaseHttp {
	
	public String getPlatformId() {
		return "100000001";
	}
	
	/**
	 * 获取第三方交易对
	 * @return
	 */
	public void geThirdpartysupportmoneys(List<Thirdpartysupportmoney> thirdpartysupportmoneys) {
		Connection connect = Jsoup.connect("https://api.huobi.pro/v1/common/symbols");
		connect.ignoreContentType(true);
		String result = null;
		try {
			result = connect.get().body().text();
			Map map = JSON.parseObject(result, Map.class);
			List<Map<String, String>> list = (List<Map<String, String>>)map.get("data");
			for (Map<String, String> map2 : list) {
				Thirdpartysupportmoney thirdpartysupportmoney = new Thirdpartysupportmoney();
				thirdpartysupportmoney.setPlatformid(getPlatformId());
				thirdpartysupportmoney.setMoneytype(map2.get("base-currency"));
				thirdpartysupportmoney.setBuymoneytype(map2.get("quote-currency"));
				thirdpartysupportmoney.setMoneypair(thirdpartysupportmoney.getMoneytype()+thirdpartysupportmoney.getBuymoneytype());
				thirdpartysupportmoneys.add(thirdpartysupportmoney);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取火币k线数据
	 * @return
	 */
	public static String historyKline() {
		String result=null;
		try {
			Connection connect = Jsoup.connect("https://api.huobi.pro/market/history/kline");
			result = connect.get().body().text();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void getKLineData(Markethistory marketHistory, MarketHistoryMapper marketHistoryMapper, Long size,
			Long dbCurrentTime) {
		// TODO Auto-generated method stub
		
	}

	
}
