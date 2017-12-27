package com.btb.dao;

import java.util.List;
import java.util.Map;

import com.btb.entity.QueryVo;
import com.btb.entity.Thirdpartysupportmoney;

import tk.mybatis.mapper.common.Mapper;

public interface ThirdpartysupportmoneyMapper extends Mapper<Thirdpartysupportmoney> {
	
	void deleteParam(QueryVo vo);
	
	List<Map<String, String>> findplatformidAll();
	
	List<Thirdpartysupportmoney> findmoneypairByplatformid(String platformid);
	
}
