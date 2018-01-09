package com.btb.tasks.threads;

import java.util.List;

import com.btb.entity.Markethistory;
import com.btb.entity.PlatformSupportmoney;
import com.btb.util.BaseHttp;
import com.btb.util.SpringUtil;
import com.btb.util.TaskUtil;
import com.btb.util.dao.BaseDaoSql;

public class MarketHistoryKlineTread extends Thread {
	String platformid;
	public MarketHistoryKlineTread(String platformid) {
		this.platformid=platformid;
	}
	
	@Override
	public void run() {
		
		List<PlatformSupportmoney> moneyPairs = TaskUtil.moneyPairs.get(platformid);
		
		for (PlatformSupportmoney thirdpartysupportmoney : moneyPairs) {
			Markethistory marketHistory = new Markethistory();
			marketHistory.setPlatformid(platformid);
			marketHistory.setMoneypair(thirdpartysupportmoney.getMoneypair());
			marketHistory.setMoneytype(thirdpartysupportmoney.getMoneytype());
			marketHistory.setBuymoneytype(thirdpartysupportmoney.getBuymoneytype());
			long currentTime = System.currentTimeMillis()/1000;//换算成秒级
			Long dbCurrentTime=BaseDaoSql.selectOne("getMaxTimeId", marketHistory);
			if (dbCurrentTime == null) {//如果第一次获取数据,直接过去一天数据
				dbCurrentTime=currentTime-24*60*60;
			}
			long size = (currentTime-dbCurrentTime)/60;
			if (size > 1440) {//大于一天就取一天的数据
				size=1440;
			}
			if (size != 0) {
				BaseHttp baseHttp = TaskUtil.httpBeans.get(marketHistory.getPlatformid());
				baseHttp.getKLineData(marketHistory, size, dbCurrentTime);
			}
		}
	}
	
}
