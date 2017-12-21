package com.btb.util;

import java.util.Date;
import java.util.UUID;

/**
 * 产生新的id
 * @author Administrator
 *
 */
public class IdManage {
	public static String getUUid() {
		return UUID.randomUUID().toString();
	}
	private static String loastTime="";
	private static int times=0;
	public static String getTimeUUid() {
		String thisTime=DateUtil.dateFormat(new Date(), "yyyyMMddHHmmssSSS");
		String newid="";
		if (thisTime.equals(loastTime)) {
			newid=thisTime+times;
			times++;
		}else{
			times=0;
			newid=thisTime+times;
			loastTime=thisTime;
			times++;
		}
		return newid;
	}
}
