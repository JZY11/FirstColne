package com.btb.util;

import java.io.IOException;
import java.util.List;

import com.btb.entity.Market;
import com.btb.entity.Markethistory;
import com.btb.entity.PlatformSupportmoney;
import com.btb.tasks.threads.BuyParmeterThread;
import com.btb.tasks.threads.MarketHistoryKlineTread;

public abstract class BaseHttp {
	public abstract String getPlatformId();
	public abstract void geThirdpartysupportmoneys(List<PlatformSupportmoney> thirdpartysupportmoneys);
	public abstract void getKLineData(Markethistory marketHistory,Long size,Long dbCurrentTime);
	
	public static void testLoadMoneyPair(Class<? extends BaseHttp> c) {
		//初始化所有的HttpUtil,方便采集k线图和交易对
		System.out.println("正在初始化HttpUtils");
		TaskUtil.InitAllHttpUtils();
		try {
			BuyParmeterThread buyParmeterThread = new BuyParmeterThread(c.newInstance().getPlatformId());
			buyParmeterThread.run();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void testLoadKline(Class<? extends BaseHttp> c) {
		//初始化所有的HttpUtil,方便采集k线图和交易对
		System.out.println("正在初始化HttpUtils");
		TaskUtil.InitAllHttpUtils();
		//采集所有平台中的交易对,同步//从数据库里面加载防止采集交易对不成功
		System.out.println("正在从数据库加载交易对");
		TaskUtil.initMoneypairByDB();
		//同步到数据里面获取外汇利率
		System.out.println("正在从数据库加载外汇汇率");
		TaskUtil.initWaiHuiToDB();
		//启动加载每个平台的btc,eth价格, 每1.5分钟执行一次,从数据库采集
		System.out.println("从数据库抓取btc和ehc的最新价格");
		TaskUtil.initBuyMonetyTypeRate();
		try {
			MarketHistoryKlineTread marketHistoryKlineTread = new MarketHistoryKlineTread(c.newInstance().getPlatformId());
			marketHistoryKlineTread.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
