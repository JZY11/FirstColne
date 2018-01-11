package com.btb.huobi;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.btb.entity.Market;
import com.btb.entity.Markethistory;
import com.btb.entity.PlatformSupportmoney;
import com.btb.huobi.vo.MarketHistoryVo1;
import com.btb.huobi.vo.MarketHistoryVo2;
import com.btb.okex.HttpUtil_okex_quarter;
import com.btb.util.BaseHttp;
import com.btb.util.JsoupUtil;
import com.btb.util.SpringUtil;
import com.btb.util.StringUtil;
import com.btb.util.dao.BaseDaoSql;
/*
 * 行情数据
 * https://api.huobi.pro/market/detail?symbol=ethusdt
 * 交易对查询
 * https://api.huobi.pro/v1/common/symbols
 * 查询所有支持的币种
 * https://api.huobi.pro/v1/common/currencys
 */
@Service("huobihttp")
public class HttpUtil extends BaseHttp {
	
	public String getPlatformId() {
		//必须跟数据库的平台id一致
		return "Huobi";
	}
	
	/**
	 * 获取第三方交易对
	 * @return
	 */
	public void geThirdpartysupportmoneys(List<PlatformSupportmoney> thirdpartysupportmoneys) {
		String url="https://api.huobi.pro/v1/common/symbols";
		String result = JsoupUtil.getJson(url);
		if (result != null) {
			Map map = JSON.parseObject(result, Map.class);
			List<Map<String, String>> list = (List<Map<String, String>>)map.get("data");
			for (Map<String, String> map2 : list) {
				PlatformSupportmoney thirdpartysupportmoney = new PlatformSupportmoney();
				thirdpartysupportmoney.setPlatformid(getPlatformId());
				thirdpartysupportmoney.setMoneytype(map2.get("base-currency"));
				thirdpartysupportmoney.setBuymoneytype(map2.get("quote-currency"));
				thirdpartysupportmoney.setMoneypair(map2.get("base-currency")+map2.get("quote-currency"));
				thirdpartysupportmoneys.add(thirdpartysupportmoney);
			}
		}
	}
	
	/**
	 * marketHistory:有两个数据, 平台id,和交易对
	 * marketHistoryMapper: 保存数据的dao层对象
	 * size: 要查询的size, 通过当前数据最大时间,和当前时间差计算而来
	 * dbCurrentTime: 数据库当前最大时间,long类型
	 */
	public void getKLineData(Markethistory marketHistory,Long size,Long dbCurrentTime) {
			try {
				String url="https://api.huobi.pro/market/history/kline?period=1min&size="+size+"&symbol="+marketHistory.getMoneypair();
				String text = JsoupUtil.getJson(url);
				if (text != null) {
					MarketHistoryVo1 marketHistoryVo1 = JSON.parseObject(text, MarketHistoryVo1.class);
					List<MarketHistoryVo2> data = marketHistoryVo1.getData();
					////去除最新值,因为最新值,当前分钟还没有统计完整,
					if (!data.isEmpty()) {
						data.remove(0);
					}


					for (MarketHistoryVo2 marketHistoryVo2:data) {
						if (marketHistoryVo2.getId()<=dbCurrentTime) {
							continue;//如果小于数据库最大时间,说明数据库已经存在,不需要再添加
						}else {
							marketHistory.setTimeid(marketHistoryVo2.getId());//这里需要注意,long类型时间必须为10位的,msql数据库才支持
							marketHistory.setAmount(marketHistoryVo2.getAmount());
							marketHistory.setClose(marketHistoryVo2.getClose());
							marketHistory.setCount(marketHistoryVo2.getCount());
							marketHistory.setHigh(marketHistoryVo2.getHigh());
							marketHistory.setLow(marketHistoryVo2.getLow());
							marketHistory.setOpen(marketHistoryVo2.getOpen());
							marketHistory.setVol(marketHistoryVo2.getVol());
							BaseDaoSql.save(marketHistory);//保存到数据库
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	public static void main(String[] args) {
		BaseHttp.testLoadKline(HttpUtil.class);
		//BaseHttp.testLoadMoneyPair(HttpUtil.class);
	}
	
}
