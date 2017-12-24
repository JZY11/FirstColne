package com.btb.tasks.threads;

import java.util.ArrayList;
import java.util.List;

import com.btb.dao.ThirdpartysupportmoneyMapper;
import com.btb.entity.QueryVo;
import com.btb.entity.Thirdpartysupportmoney;
import com.btb.util.BaseHttp;
import com.btb.util.CacheData;
import com.btb.util.SpringUtil;

/**
 * 采集每个平台的交易对,每隔1小时执行一次
 * @author Administrator
 *
 */
public class BuyParmeterThread extends Thread {
	String platformId;
	public BuyParmeterThread(String platformId) {
		this.platformId=platformId;
	}
	
	@Override
	public void run() {
		//根据平台id获取httpUtil对象
		BaseHttp baseHttp = CacheData.httpBeans.get(platformId);
		List<Thirdpartysupportmoney> thirdpartysupportmoneys = new ArrayList<>();
		baseHttp.geThirdpartysupportmoneys(thirdpartysupportmoneys );
		ThirdpartysupportmoneyMapper mapper = SpringUtil.getBean(ThirdpartysupportmoneyMapper.class);
		
		QueryVo vo = new QueryVo();
		
		vo.setPlatformid(platformId);
		List<String> moneypairs = new ArrayList<>();
		
		for (Thirdpartysupportmoney thirdpartysupportmoney : thirdpartysupportmoneys) {
			moneypairs.add(thirdpartysupportmoney.getMoneypair());
			int insertCount = mapper.updateByPrimaryKey(thirdpartysupportmoney);
			if (insertCount != 1) {
				mapper.insert(thirdpartysupportmoney);
			}
		}
		vo.setMoneypairs(moneypairs);
		//删除不存在的
		if (moneypairs != null && !moneypairs.isEmpty()) {
			mapper.deleteParam(vo);
			//重新加载数据
			CacheData.moneyPairs.put(platformId, moneypairs);
		}
	}
}
