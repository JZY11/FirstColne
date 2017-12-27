package com.btb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {
	//天
	public static int Date=Calendar.DATE;
	//月
	public static int Month=Calendar.MONTH;
	//年
	public static int Year=Calendar.YEAR;
	//小时
	public static int Hour=Calendar.HOUR;
	//分钟
	public static int Minute=Calendar.MINUTE;
	//秒
	public static int Second=Calendar.SECOND;
	//周
	public static int Week=Calendar.WEEK_OF_YEAR;
	/**
	 * 日期格式化
	 * @param date  时间
	 * @param regx  例如"yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String dateFormat(Date date,String regx) {
		return new SimpleDateFormat(regx).format(date);
	}
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getDate() {
		return dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
	}
	public static String getDateNoTime() {
		return dateFormat(new Date(), "yyyy-MM-dd");
	}
	
	
	/**
	 * 
	 * @param date 字符串日期    如"2013-12-23"
	 * @param regx 字符串日期规则 如"yyyy-MM-dd"
	 * @return 返回一个 日期类型
	 */
	public static Date dateFormat(String date,String regx) {
		try {
			return new SimpleDateFormat(regx).parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static java.sql.Date dateFormatForSql(String date) {
		if (StringUtil.hashText(date)) {
			try {
				return new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	public static java.sql.Timestamp timestampFormatForSql(String date) {
		if (StringUtil.hashText(date)) {
			try {
				return new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd HH-mm:ss").parse(date).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 日期加减  分别可以对      年，月，日，周，时，分，秒  进行加减操作
	 * @param date  日期
	 * @param type  年：DateUtil.Year
	 * @param type	月：DateUtil.Month
	 * @param type	日：DateUtil.Date
	 * @param type	周：DateUtil.Week
	 * @param type	时：DateUtil.Hour
	 * @param type	分：DateUtil.Minute
	 * @param type	秒：DateUtil.Second
	 * @param count  整数相加，负数相减       如-1   or  1
	 * @return  返回计算后的 日期
	 */
	public static Date dateMath(Date date,int type,int count) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(type, count);
		return calendar.getTime();
	}
	
	/**
	 * 日期加减  分别可以对      年，月，日，周，时，分，秒  进行加减操作
	 * @param date  字符串日期
	 * @param regx  日期格式 和返回日期格式
	 * @param type  年：DateUtil.Year
	 * @param type	月：DateUtil.Month
	 * @param type	日：DateUtil.Date
	 * @param type	周：DateUtil.Week
	 * @param type	时：DateUtil.Hour
	 * @param type	分：DateUtil.Minute
	 * @param type	秒：DateUtil.Second
	 * @param count  整数相加，负数相减       如-1   or  1
	 * @return  返回计算后的 日期
	 */
	public static String dateMath(String date,int type,int count,String regx) {
		Date date2=null;
		try {
			date2 = new SimpleDateFormat(regx).parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date2);
		calendar.add(type, count);
		return new SimpleDateFormat(regx).format(calendar.getTime());
	}
	
	public static String changeTimeToString(Object longtime) {
		if (StringUtil.hashText(longtime)) {
			long time=Math.abs(Long.valueOf(String.valueOf(longtime)));
			long date=0;
			long hour=0;
			long minite=0;
			date=time/(1000*60*60*24);
			hour=(time - date*1000*60*60*24)/(1000*60*60);
			minite=(time - date*1000*60*60*24 - hour*1000*60*60)/(1000*60);
			String result="";
			if (date != 0) {
				result+=date+"天";
			}
			if (hour != 0) {
				result+=hour+"时";
			}
			if (minite != 0) {
				result+=minite+"分";
			}
			return result;
		}else {
			return null;
		}
	}

}
