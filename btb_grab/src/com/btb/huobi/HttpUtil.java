package com.btb.huobi;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.btb.entity.Market;
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
@Service
public class HttpUtil extends BaseHttp {
	
	public static void main(String[] args) throws IOException {
		/*List<Thirdpartysupportmoney> thirdpartysupportmoneys=new ArrayList<>();
		new HttpUtil().geThirdpartysupportmoneys(thirdpartysupportmoneys);
		System.out.println(JSON.toJSONString(thirdpartysupportmoneys));*/
		Market market = new HttpUtil().detailMerged("omgusdt");
		System.out.println(JSON.toJSONString(market));
	}
	
	public String getPlatformId() {
		return "100000000";
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
	 * 获取实时行情数据
	 * 参数交易对,自动传参调用
	 * @return
	 */
	public Market detailMerged(String moneypair) {
		String result=null;
		try {
			Connection connect = Jsoup.connect("https://api.huobi.pro/market/detail?symbol="+moneypair);
			result = connect.get().body().text();
			Map map = JSON.parseObject(result, Map.class);
			Map map2 = (Map) map.get("tick");
			
			BigDecimal close=(BigDecimal) map2.get("close");//最新价格
			BigDecimal open=(BigDecimal) map2.get("open");//24小时前开盘价格
			BigDecimal vol=(BigDecimal) map2.get("vol");//24小时成交额
			BigDecimal amount=(BigDecimal) map2.get("amount");//24小时成交量
			
			Market market = new Market();
			market.setPlatformid(getPlatformId());
			market.setMoneypair(moneypair);
			
			
			
			return market;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
}
