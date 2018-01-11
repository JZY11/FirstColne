package com.btb.binance;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.btb.binance.vo.MarketHistoryVo1;
import com.btb.binance.vo.MarketHistoryVo2;
import com.btb.entity.Markethistory;
import com.btb.entity.PlatformSupportmoney;
import com.btb.util.BaseHttp;
import com.btb.util.JsoupUtil;
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
@Service("Binancehttp")
public class HttpUtil extends BaseHttp {
	
	public String getPlatformId() {
		//必须跟数据库的平台id一致
		return "Binance";
	}
	
	/**
	 * 获取第三方交易对
	 * @return
	 */
	public void geThirdpartysupportmoneys(List<PlatformSupportmoney> thirdpartysupportmoneys) {
		String url="https://api.binance.com/api/v3/ticker/bookTicker";
		String result = JsoupUtil.getJson(url);
		if (result != null) {
			List<Map> list = JSON.parseArray(result, Map.class);
			for (Map<String, String> map2 : list) {
				PlatformSupportmoney thirdpartysupportmoney = new PlatformSupportmoney();
				thirdpartysupportmoney.setPlatformid(getPlatformId());
				String[] strings = StringUtil.getHuobiBuyMoneytype(map2.get("symbol"));
				if (StringUtil.hashText(strings[0])) {
					thirdpartysupportmoney.setMoneytype(strings[1]);
					thirdpartysupportmoney.setBuymoneytype(strings[0]);
					thirdpartysupportmoney.setMoneypair(map2.get("symbol"));
					thirdpartysupportmoneys.add(thirdpartysupportmoney);
				}
			}
		}
	}
	
	/**
	 * marketHistory:有两个数据, 平台id,和交易对
	 * marketHistoryMapper: 保存数据的dao层对象
	 * size: 要查询的size, 通过当前数据最大时间,和当前时间差计算而来
	 * dbCurrentTime: 数据库当前最大时间,long类型
	 *  0	1515643800000 开盘时间
		1	"0.09110400" 开盘价格
		2	"0.09110400" 最高价格
		3	"0.09060100" 最低价格
		4	"0.09070700" 收盘价格
		5	"448.96300000" 
		6	1515643859999 统计结束时间,最后一个不算,没有统计完全
		7	"40.79885869"
		8	590 	交易笔数
		9	"186.08000000" 成交量
		10	"16.91935453"  成交额
		11	"0"
	 */
	public void getKLineData(Markethistory marketHistory,Long size,Long dbCurrentTime) {
			try {
				String url="https://api.binance.com/api/v1/klines?interval=1m&limit="+size+"&symbol="+marketHistory.getMoneypair();
				String text = JsoupUtil.getJson(url);
				if (text != null) {
					LinkedList<List<Object>> data = JSON.parseObject(text, LinkedList.class);
					////去除最新值,因为最新值,当前分钟还没有统计完整,
					if (!data.isEmpty()) {
						data.removeLast();
					}
					
					for (List<Object> marketHistoryVo2:data) {
						Long timeid = Long.valueOf(String.valueOf(marketHistoryVo2.get(0)))/1000;
						if (timeid<=dbCurrentTime) {
							continue;//如果小于数据库最大时间,说明数据库已经存在,不需要再添加
						}else {
							marketHistory.setTimeid(timeid);//这里需要注意,long类型时间必须为10位的,msql数据库才支持
							marketHistory.setAmount(StringUtil.toBigDecimal(marketHistoryVo2.get(9)));
							marketHistory.setClose(StringUtil.toBigDecimal(marketHistoryVo2.get(4)));
							marketHistory.setCount(StringUtil.toBigDecimal(marketHistoryVo2.get(8)));
							marketHistory.setHigh(StringUtil.toBigDecimal(marketHistoryVo2.get(2)));
							marketHistory.setLow(StringUtil.toBigDecimal(marketHistoryVo2.get(3)));
							marketHistory.setOpen(StringUtil.toBigDecimal(marketHistoryVo2.get(1)));
							marketHistory.setVol(StringUtil.toBigDecimal(marketHistoryVo2.get(10)));
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
