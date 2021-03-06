package com.btb.okex;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.btb.entity.Market;
import com.btb.entity.Markethistory;
import com.btb.entity.PlatformSupportmoney;
import com.btb.huobi.vo.MarketHistoryVo1;
import com.btb.huobi.vo.MarketHistoryVo2;
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
//@1
@Service("HttpUtil_okex_next_week")
public class HttpUtil_okex_next_week extends BaseHttp {
	
	public String getPlatformId() {
		//@2 必须跟数据库的平台id一致
		return "OKEx_next_week";
	}
	
	/**
	 * 获取第三方交易对
	 * @return
	 */
	//@3
	@Override
	public void geThirdpartysupportmoneys(List<PlatformSupportmoney> PlatformSupportmoneys) {
			//获取合约的交易对
			Document document = JsoupUtil.getElement("https://www.okex.com/ws_api.html");
			Element element = document.getElementsContainingOwnText("订阅合约K线数据").get(0).nextElementSibling();
			String text = element.select("div.page-header > h1 > small").get(0).text();
			text=text.substring(text.indexOf("：")+1);
			String[] split = text.split(", ");
			for (String string : split) {
				PlatformSupportmoney PlatformSupportmoney = new PlatformSupportmoney();
				PlatformSupportmoney.setPlatformid(getPlatformId());
				PlatformSupportmoney.setMoneytype(string);
				PlatformSupportmoney.setBuymoneytype("usd");
				PlatformSupportmoney.setMoneypair(string+"_usd");
				PlatformSupportmoneys.add(PlatformSupportmoney);
			}
	}
	
	/**
	 * marketHistory:有两个数据, 平台id,和交易对
	 * marketHistoryMapper: 保存数据的dao层对象
	 * size: 要查询的size, 通过当前数据最大时间,和当前时间差计算而来
	 * dbCurrentTime: 数据库当前最大时间,long类型
	 */
	//@4
	public void getKLineData(Markethistory marketHistory,Long size,Long dbCurrentTime) {
		String url="https://www.okex.com/api/v1/future_kline.do?type=1min&contract_type=next_week&size="+size+"&symbol="+marketHistory.getMoneypair();
		String text = JsoupUtil.getJson(url);
		if (text != null) {
			List<BigDecimal[]> data = JSON.parseArray(text, BigDecimal[].class);
			////去除最新值,因为最新值,当前分钟还没有统计完整,
			if (!data.isEmpty()) {
				data.remove(data.size()-1);//ok的最新值是最后一个
			}
			
			for (BigDecimal[] marketHistoryList:data) {
				Long ts = marketHistoryList[0].longValue();//交易时间
				BigDecimal open = marketHistoryList[1];//开盘
				BigDecimal high = marketHistoryList[2];//1分钟最高
				BigDecimal low = marketHistoryList[3];//1分钟最低
				BigDecimal close = marketHistoryList[4];//收盘
				BigDecimal amount =marketHistoryList[5];//1分钟交易量
				
				if (ts/1000 <=dbCurrentTime) {//数据库时间是秒级别
					continue;//如果小于数据库最大时间,说明数据库已经存在,不需要再添加
				}else {
					marketHistory.setTimeid(ts/1000);//这里需要注意,long类型时间必须为10位的,msql数据库才支持
					marketHistory.setAmount(amount);
					marketHistory.setClose(close);
					//marketHistory.setCount(0);
					marketHistory.setHigh(high);
					marketHistory.setLow(low);
					marketHistory.setOpen(open);
					//marketHistory.setVol(marketHistoryVo2.getVol());
					BaseDaoSql.save(marketHistory);//保存到数据库
				}
			}
		}
	}
	public static void main(String[] args) {
		BaseHttp.testLoadMoneyPair(HttpUtil_okex_next_week.class);
		BaseHttp.testLoadKline(HttpUtil_okex_next_week.class);
	}

}
