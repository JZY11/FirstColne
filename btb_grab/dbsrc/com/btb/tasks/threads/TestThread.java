package com.btb.tasks.threads;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.btb.entity.AllPlatformKline;
import com.btb.util.DateUtil;
import com.btb.util.JsoupUtil;
import com.btb.util.StringUtil;
import com.btb.util.dao.BaseDaoSql;

public class TestThread extends Thread {
	Map<String, Object> map;
	public TestThread(Map<String, Object> map) {
		// TODO Auto-generated constructor stub
		this.map=map;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		AllPlatformKline aPlatformKline = new AllPlatformKline();
		aPlatformKline.setBitbcode(map.get("bitbcode").toString());
		aPlatformKline.setBitbname(map.get("ccid").toString());
		while (true) {
			String result = BaseDaoSql.findSingleResult("select min(timeid) from allplatformkline where bitbname = '"+aPlatformKline.getBitbname()+"'");
			long end = new Date().getTime();
			long start = DateUtil.dateMath(new Date(), DateUtil.Date, -1).getTime();
			if (StringUtil.hashText(result)) {//如果不为空
				end = Long.valueOf(result)-1;
				start=DateUtil.dateMath(new Date(end), DateUtil.Date, -1).getTime();
			}
			String url = "https://graphs.coinmarketcap.com/currencies/"+aPlatformKline.getBitbname()+"/"+start+"/"+end+"/";
			System.out.println(url);
			String string = JsoupUtil.getJson(url);
			Map map2 = JSON.parseObject(string, Map.class);
			List<List<Object>> object = (List<List<Object>>)map2.get("market_cap_by_available_supply");
			List<List<Object>> object2 = (List<List<Object>>)map2.get("price_btc");
			List<List<Object>> object3 = (List<List<Object>>)map2.get("price_usd");
			List<List<Object>> object4 = (List<List<Object>>)map2.get("volume_usd");
			if (!string.equals("error") && object.isEmpty()) {//如果没有数据了
				break;
			}
			for (int i=0;i<object.size();i++) {
				aPlatformKline.setTimeid(Long.valueOf(StringUtil.valueOf(object.get(i).get(0))));
				aPlatformKline.setCapSuppy(StringUtil.toBigDecimal(object.get(i).get(1)));
				aPlatformKline.setPrice_btc(StringUtil.toBigDecimal(object2.get(i).get(1)));
				aPlatformKline.setPrice_usd(StringUtil.toBigDecimal(object3.get(i).get(1)));
				aPlatformKline.setVol_usd(StringUtil.toBigDecimal(object4.get(i).get(1)));
				BaseDaoSql.save(aPlatformKline);
			}
		}
	}
}
