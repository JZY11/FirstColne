package com.btb.dao;

import com.btb.entity.TodayOpenMoney;

import tk.mybatis.mapper.common.Mapper;

public interface TodayOpenMoneyMapper extends Mapper<TodayOpenMoney> {
	
	Integer checkTodayOpenMoneyLess();
	
	
}
