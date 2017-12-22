package com.btb.util;

import java.util.List;

import com.btb.dao.ThirdpartysupportmoneyMapper;
import com.btb.entity.Thirdpartysupportmoney;

public class DBUtil {
	/**
	 * 根据平台id获取交易对
	 * @param platformId
	 * @return
	 */
	public static List<Thirdpartysupportmoney> getJiaoyidui(String platformId) {
		ThirdpartysupportmoneyMapper thirdpartysupportmoneyMapper = SpringUtil.getBean(ThirdpartysupportmoneyMapper.class);
		Thirdpartysupportmoney thirdpartysupportmoney = new Thirdpartysupportmoney();
		thirdpartysupportmoney.setPlatformid(platformId);
		List<Thirdpartysupportmoney> select = thirdpartysupportmoneyMapper.select(thirdpartysupportmoney);
		return select;
	}
}
