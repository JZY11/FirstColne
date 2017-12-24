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
import com.btb.dao.MarketHistoryMapper;
import com.btb.entity.Market;
import com.btb.entity.Markethistory;
import com.btb.entity.Thirdpartysupportmoney;
import com.btb.huobi.vo.MarketHistoryVo1;
import com.btb.huobi.vo.MarketHistoryVo2;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.SpringUtil;
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
	
	
	public void getKLineData(Markethistory marketHistory) {
		MarketHistoryMapper marketHistoryMapper = SpringUtil.getBean(MarketHistoryMapper.class);
		long currentTime = System.currentTimeMillis()/1000;//换算成秒级
		Long dbCurrentTime=marketHistoryMapper.getMaxTimeId(marketHistory);
		if (dbCurrentTime == null) {//如果第一次获取数据,直接过去一天数据
			dbCurrentTime=currentTime-24*60*60;
		}
		long size = (currentTime-dbCurrentTime)/60;
		if (size > 1440) {//大于一天就取一天的数据
			size=1440;
		}
		if (size != 0) {
			String url="https://api.huobi.pro/market/history/kline?period=1min&size="+size+"&symbol="+marketHistory.getMoneypair();
			Connection connect = Jsoup.connect(url);
			//System.out.println(url);
			connect.ignoreContentType(true);
			try {
				String text = connect.get().body().text();
				
				//数据整改开始
				MarketHistoryVo1 marketHistoryVo1 = JSON.parseObject(text, MarketHistoryVo1.class);
				List<MarketHistoryVo2> data = marketHistoryVo1.getData();
				//这里是大于0,因为第一个值是实时变化的,当前分钟还没有统计完整,
				//倒序从最小时间开始添加数据库,方便k线图计算
				for (int i=data.size()-1;i>0;i--) {
					MarketHistoryVo2 marketHistoryVo2 = data.get(i);
					if (marketHistoryVo2.getId()<=dbCurrentTime) {
						continue;//如果小于数据库最大时间,说明数据库已经存在,不需要再添加
					}else {
						marketHistory.setTimeid(marketHistoryVo2.getId());
						marketHistory.setAmount(marketHistoryVo2.getAmount());
						marketHistory.setClose(marketHistoryVo2.getClose());
						marketHistory.setCount(marketHistoryVo2.getCount());
						marketHistory.setHigh(marketHistoryVo2.getHigh());
						marketHistory.setLow(marketHistoryVo2.getLow());
						marketHistory.setOpen(marketHistoryVo2.getOpen());
						marketHistory.setVol(marketHistoryVo2.getVol());
						marketHistoryMapper.insert(marketHistory);
						//数据整改结束
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		SpringUtil.testinitSpring();
		//CacheData.httpBeans.get("100000000");
		
		HttpUtil httpUtil = new HttpUtil();
		
		Markethistory marketHistory=new Markethistory();
		marketHistory.setPlatformid(httpUtil.getPlatformId());
		marketHistory.setMoneypair("btcusdt");
		httpUtil.getKLineData(marketHistory);
	}
	
}
