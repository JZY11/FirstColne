package com.btb.dao;

import com.btb.entity.Markethistory;

import tk.mybatis.mapper.common.Mapper;

public interface MarketHistoryMapper extends Mapper<Markethistory> {
	
	Long getMaxTimeId(Markethistory marketHistory);
	
}
