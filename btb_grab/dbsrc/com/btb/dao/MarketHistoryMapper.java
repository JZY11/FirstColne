package com.btb.dao;

import java.math.BigDecimal;
import java.util.List;

import com.btb.entity.Markethistory;
import com.btb.entity.QueryVo;
import com.btb.entity.Thirdpartysupportmoney;

import tk.mybatis.mapper.common.Mapper;

public interface MarketHistoryMapper extends Mapper<Markethistory> {
	
	Long getMaxTimeId(Markethistory marketHistory);
	List<Markethistory> findTodayOpenMoney();
	
	List<QueryVo> findBestNewRmbByBtcAndEth();
	List<Thirdpartysupportmoney> findAllPingTaiEthAndBtc();
}
