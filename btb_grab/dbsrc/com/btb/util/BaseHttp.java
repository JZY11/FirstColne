package com.btb.util;

import java.io.IOException;
import java.util.List;

import com.btb.entity.Market;
import com.btb.entity.Markethistory;
import com.btb.entity.PlatformSupportmoney;

public abstract class BaseHttp {
	public abstract String getPlatformId();
	public abstract void geThirdpartysupportmoneys(List<PlatformSupportmoney> thirdpartysupportmoneys);
	public abstract void getKLineData(Markethistory marketHistory,Long size,Long dbCurrentTime);
}
