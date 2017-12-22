package com.btb.util;

import java.io.IOException;
import java.util.List;

import com.btb.entity.Market;
import com.btb.entity.Thirdpartysupportmoney;

public abstract class BaseHttp {
	public abstract String getPlatformId();
	public abstract void geThirdpartysupportmoneys(List<Thirdpartysupportmoney> thirdpartysupportmoneys);

}
