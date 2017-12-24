package com.btb.tasks.threads;

import java.util.List;

import com.btb.dao.MarketHistoryMapper;
import com.btb.entity.Markethistory;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.SpringUtil;

public class MarketHistoryKlineTread extends Thread {
	String platformid;
	public MarketHistoryKlineTread(String platformid) {
		this.platformid=platformid;
	}
	
	@Override
	public void run() {
		MarketHistoryMapper marketHistoryMapper = SpringUtil.getBean(MarketHistoryMapper.class);
		List<String> moneyPairs = CacheData.moneyPairs.get(platformid);
		Markethistory marketHistory = new Markethistory();
		marketHistory.setPlatformid(platformid);
		
		for (String moneyPair : moneyPairs) {
			marketHistory.setMoneypair(moneyPair);
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
				BaseHttp baseHttp = CacheData.httpBeans.get(marketHistory.getPlatformid());
				baseHttp.getKLineData(marketHistory, marketHistoryMapper, size, dbCurrentTime);
			}
		}
	}
	
}
