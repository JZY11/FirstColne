package com.btb.tasks.threads;

import com.btb.dao.MarketHistoryMapper;
import com.btb.entity.Markethistory;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.SpringUtil;

public class MarketHistoryKlineTread extends Thread {
	
	public MarketHistoryKlineTread(Markethistory marketHistory) {
		this.marketHistory=marketHistory;
	}
	
	Markethistory marketHistory;
	
	@Override
	public void run() {
		System.out.println("正在采集k线图平台:"+marketHistory.getPlatformid()+" 交易对:"+marketHistory.getMoneypair());
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
			BaseHttp baseHttp = CacheData.httpBeans.get(marketHistory.getPlatformid());
			baseHttp.getKLineData(marketHistory, marketHistoryMapper, size, dbCurrentTime);
		}
		System.out.println("采集完毕k线图平台:"+marketHistory.getPlatformid()+" 交易对:"+marketHistory.getMoneypair());
	}
	
}
