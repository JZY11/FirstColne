package com.btb.tasks.threads;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.btb.entity.QueryVo;
import com.btb.entity.PlatformSupportmoney;
import com.btb.util.BaseHttp;
import com.btb.util.SpringUtil;
import com.btb.util.TaskUtil;
import com.btb.util.dao.BaseDaoSql;

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
		BaseHttp baseHttp = TaskUtil.httpBeans.get(platformId);
		if (baseHttp != null) {
			List<PlatformSupportmoney> thirdpartysupportmoneys = new ArrayList<>();
			baseHttp.geThirdpartysupportmoneys(thirdpartysupportmoneys );
			
			QueryVo vo = new QueryVo();
			
			vo.setPlatformid(platformId);
			List<String> moneypairs = new ArrayList<>();
			
			for (PlatformSupportmoney thirdpartysupportmoney : thirdpartysupportmoneys) {
				moneypairs.add(thirdpartysupportmoney.getMoneypair());
				int insertCount = BaseDaoSql.update(thirdpartysupportmoney);
				if (insertCount != 1) {
					BaseDaoSql.save(thirdpartysupportmoney);
				}
			}
			vo.setMoneypairs(moneypairs);
			//删除不存在的
			if (moneypairs != null && !moneypairs.isEmpty()) {
				BaseDaoSql.excByMybatis("deleteParam", vo);
				//重新加载数据
				TaskUtil.moneyPairs.put(platformId, thirdpartysupportmoneys);
			}
		}
	}
}
