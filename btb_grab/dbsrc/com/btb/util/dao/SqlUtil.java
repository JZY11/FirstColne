package com.btb.util.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import com.btb.util.DateUtil;
import com.btb.util.StringUtil;

public class SqlUtil {
	/**
	 * 组装分页语句
	 * @param sql
	 * @param page
	 * @param maxrow
	 * @return
	 */
	public static String makeFenye(String sql,Integer page,Integer maxrow) {
		if (page==null || maxrow==null || page<1 || maxrow <1) {
			page=1;
			maxrow=30;
		}
		sql=sql+" limit "+(page-1)*maxrow+","+maxrow;
		return sql;
	}
	/**
	 * 分页查询结果总数量
	 * @param sql
	 * @return
	 */
	public static String makeFenyeCount(String sql){
		sql="select count(*) from ("+sql+") t_alldatarows";
		return sql;
	}

	/**
	 * 返回Entity时，通过反射将值更改为 实体类字段需要的类型，以便正确封装实例类对象
	 * @param t
	 * @param object
	 * @return
	 */
	public static <T> T caseType(Class<T> t,Object object) {
		if (object==null) {
			return null;
		}else {
			if (t.isAssignableFrom(String.class)) {
				return t.cast(String.valueOf(object)) ;
			}else if(Integer.class.getName().equals(t.getName()) && StringUtil.hashText(object)){
				return t.cast(Integer.valueOf(String.valueOf(object)));
			}else if(Double.class.getName().equals(t.getName()) && StringUtil.hashText(object)){
				return t.cast(Double.parseDouble(String.valueOf(object)));
			}else if(Float.class.getName().equals(t.getName()) && StringUtil.hashText(object)){
				return t.cast(Float.parseFloat(String.valueOf(object)));
			}else if(Long.class.getName().equals(t.getName()) && StringUtil.hashText(object)){
				return t.cast(Long.parseLong(String.valueOf(object)));
			}else if(Date.class.getName().equals(t.getName()) && StringUtil.hashText(object)){
				return t.cast(DateUtil.dateFormatForSql(String.valueOf(object)));
			}else if(Timestamp.class.getName().equals(t.getName()) && StringUtil.hashText(object)){
				return t.cast(DateUtil.timestampFormatForSql(String.valueOf(object)));
			}else if (BigDecimal.class.getName().equals(t.getName()) && StringUtil.hashText(object)) {
				return t.cast(StringUtil.toBigDecimal(object));
			}else{
				return null;
			}
		}
	}
	
}
