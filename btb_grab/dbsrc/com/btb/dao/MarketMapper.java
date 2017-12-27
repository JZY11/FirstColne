package com.btb.dao;

import java.util.List;

import com.btb.entity.Market;

import tk.mybatis.mapper.common.Mapper;

public interface MarketMapper extends Mapper<Market> {
	int updateByTowIds(Market market);
	
	List<Market> findAllMarketInfo();
}
