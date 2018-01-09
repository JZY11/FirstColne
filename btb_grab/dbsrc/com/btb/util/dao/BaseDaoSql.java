package com.btb.util.dao;


import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.RowSet;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.btb.util.SpringUtil;
import com.btb.util.StringUtil;

public class BaseDaoSql {
	
	private static JdbcTemplate jdbcTemplate;
	private static SqlSessionFactory sessionFactory ;
	
	/**
	 * mybatis方法
	 * @param sqlId sql标签的id
	 * @param object 传入的参数
	 * @return
	 */
	public static <T> List<T> selectList(String sqlId,Object object) {
		initsessionfacotry();
		SqlSession session = sessionFactory.openSession();
		List<T> list = session.selectList(sqlId, object);
		session.close();
		return list;
	}
	/**
	 * mybatis方法
	 * @param sqlId sql标签的id
	 * @return
	 */
	public static <T> List<T> selectList(String sqlId) {
		initsessionfacotry();
		SqlSession session = sessionFactory.openSession();
		List<T> list = session.selectList(sqlId);
		session.close();
		return list;
	}
	/**
	 * mybatis方法
	 * @param sqlId sql标签的id
	 * @param object 传入的参数
	 * @return
	 */
	public static <T> T selectOne(String sqlId,Object object) {
		initsessionfacotry();
		SqlSession session = sessionFactory.openSession();
		T t = session.selectOne(sqlId, object);
		session.close();
		return t;
	}
	/**
	 * mybatis方法
	 * @param sqlId sql标签的id
	 * @return
	 */
	public static <T> T selectOne(String sqlId) {
		initsessionfacotry();
		SqlSession session = sessionFactory.openSession();
		T t = session.selectOne(sqlId);
		session.close();
		return t;
	}
	/**
	 * mybatis方法,增删改,建表都用这个
	 * @param sqlId sql标签的id
	 * @param t 传入的参数
	 * @return
	 */
	public static <T> int excByMybatis(String sqlId,T t) {
		initsessionfacotry();
		SqlSession session = sessionFactory.openSession();
		int updateCount = session.update(sqlId, t);
		session.close();
		return updateCount;
	}
	/**
	 * mybatis方法,增删改,建表都用这个
	 * @param sqlId sql标签的id
	 * @return
	 */
	public static int excByMybatis(String sqlId) {
		initsessionfacotry();
		SqlSession session = sessionFactory.openSession();
		int updateCount = session.update(sqlId);
		session.close();
		return updateCount;
	}
	private static void initsessionfacotry() {
		if (sessionFactory == null) {
			sessionFactory=SpringUtil.getBean(SqlSessionFactory.class);
		}
	}
	private static void initjdbcTemplate() {
		if (jdbcTemplate == null) {
			jdbcTemplate=SpringUtil.getBean(JdbcTemplate.class);
		}
	}
	
	
	/**
	 * 是用springTemplate执行sql语句
	 * 放一个实体类就可以
	 * @param sql
	 */
	public static <T> int update(T entity) {
		initjdbcTemplate();
		Map<String, Object> entityConvert=EntityManager.updateSql(entity);
		Object[] args= (Object[])entityConvert.get("args");
		String sql=(String)entityConvert.get("sql");
		System.out.println(sql+JSON.toJSONString(args));
		int	result=jdbcTemplate.update(sql,args);
		/*try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return result;
	}
	/**
	 * 是用springTemplate执行sql语句
	 * 增删改查都用这个方法
	 * @param sql
	 */
	public static int excBySql(String sql) {
		initjdbcTemplate();
		System.out.println(sql);
		int result = jdbcTemplate.update(sql);
		/*try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return result;
	}
	/**
	 * 持久化对象
	 * @param entity
	 */
	public static <T> int save(T entity) {
		initjdbcTemplate();
		Map<String, Object> entityConvert=EntityManager.insertsql(entity);
		Object[] args= (Object[])entityConvert.get("args");
		String sql=(String)entityConvert.get("sql");
		System.out.println(sql+"  "+JSON.toJSONString(args));
		int result = jdbcTemplate.update(sql,args);
		/*try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return result;
	}
	/**
	 * 通过实体类的多个字段删除,如果通过id删除,只需要传入id字段即可
	 * @param entity
	 * @return
	 */
	public static <T> int delete(T entity) {
		initjdbcTemplate();
		Map<String, Object> entityConvert=EntityManager.deltesql(entity);
		Object[] args= (Object[])entityConvert.get("args");
		String sql=(String)entityConvert.get("sql");
		System.out.println(sql+"  "+JSON.toJSONString(args));
		int result = jdbcTemplate.update(sql,args);
		/*try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return result;
	}
	
	/**
	 * 通过sql语句查询listMap
	 * @param sql
	 * @return
	 */
	public static List<Map<String, Object>> findListMaps(String sql) {
		initjdbcTemplate();
		System.out.println(sql);
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
		/*try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return queryForList;
		
	}
	public static Map<String, Object> findMap(String sql) {
		initjdbcTemplate();
		Map<String, Object> map=null;
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
		/*try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if (!list.isEmpty()) {
			map=list.get(0);
		}
		return map;
	}
	
	public static <T> T findOne(String sql,Class<T> c) {
		initjdbcTemplate();
		Map<String, Object> map=null;
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
		/*try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if (!list.isEmpty()) {
			map=list.get(0);
		}
		T t=EntityManager.getModel(map, c);
		return t;
	}
	
	
	public static <T> T findModelById(Object id,Class<T> c) {
		Object[] parameters=new Object[]{id};
		String tableName = EntityManager.tableMap.get(c);
		String idAttrName = EntityManager.idMap.get(c);
		String idcolumName=EntityManager.columnMap.get(c).get(idAttrName);
		String sql="select * from "+tableName +" where "+idcolumName+"=?";
		T model = findModel(sql, parameters, c);
		return model;
	}
	public static <T> T findModelById(T t) {
		try {
			Class<T> c=(Class<T>) t.getClass();
			String tableName = EntityManager.tableMap.get(c);
			String idAttrName = EntityManager.idMap.get(c);
			String idcolumName=EntityManager.columnMap.get(c).get(idAttrName);
			Field idField = c.getDeclaredField(idAttrName);
			idField.setAccessible(true);
			Object[] parameters = new Object[]{idField.get(t)};
			String sql="select * from "+tableName +" where "+idcolumName+"=?";
			T model = findModel(sql, parameters, c);
			return model;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static <T> List<T> findAll(Class<T> c) {
		try {
			String tableName = EntityManager.tableMap.get(c);
			String sql="select * from "+tableName;
			List<T> models = findListModel(sql, null, c);
			return models;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static <T> List<T> findList(String sql,Class<T> c) {
		try {
			List<T> models = findListModel(sql, null, c);
			return models;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private static <T> T findModel(String sql,Object[] paramters,Class<T> c) {
		initjdbcTemplate();
		T t=null;
		Map<String, Object> map=null;
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql,paramters);
		/*try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if (!list.isEmpty()) {
			map=list.get(0);
		}
		t=EntityManager.getModel(map, c);
		return t;
	}
	private static <T> List<T> findListModel(String sql,Object[] paramters,Class<T> c) {
		initjdbcTemplate();
		List<T> tList= new ArrayList<>();
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql,paramters);
		/*try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if (!list.isEmpty()) {
			for (Map<String, Object> map : list) {
				T t=EntityManager.getModel(map, c);
				tList.add(t);
			}
		}
		return tList;
	}
	public static String findSingleResult(String sql) {
		initjdbcTemplate();
		String result = null;
		List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql);
		/*try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if (list1 != null && list1.size()!=0) {
			Map<String, Object> value = list1.get(0);
			Collection<Object> values = value.values();
			if (values != null && values.size() != 0) {
				result = StringUtil.valueOf(values.toArray()[0]);
			}
		}
		return result;
	}
}
