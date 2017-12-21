package com.btb.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheData  {
	//汇率表缓存,每隔一天更新一次
	public static Map<String, BigDecimal> rateMap = new HashMap<>();
	//http, <平台id,采集类>
	public static Map<String,BaseHttp> httpBeans=new HashMap<>();
	//交易对<平台id,交易对集合>
	public static Map<String,List<String>> moneyPairs=new HashMap<>();
	static{
		rateMap.put("USD", new BigDecimal("0.1513200000"));
	}
}
