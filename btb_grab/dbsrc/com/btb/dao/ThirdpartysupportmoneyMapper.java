package com.btb.dao;

import com.btb.entity.QueryVo;
import com.btb.entity.Thirdpartysupportmoney;

import tk.mybatis.mapper.common.Mapper;

public interface ThirdpartysupportmoneyMapper extends Mapper<Thirdpartysupportmoney> {
	
	void deleteParam(QueryVo vo);
	
}
