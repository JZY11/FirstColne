package com.btb.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.h2.jdbcx.JdbcConnectionPool;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.btb.entity.Market;


public class H2Util {
	/*private static DruidDataSource dataSource=new DruidDataSource();
	 
    static{  
    	dataSource.setDriverClassName("org.h2.Driver");
    	dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    	dataSource.setUsername("root");
    	dataSource.setPassword("root");
    	dataSource.setInitialSize(10);//初始化链接数量
    	dataSource.setMaxActive(1000);//最大并发链接数
    	dataSource.setDefaultAutoCommit(true);
    	dataSource.setValidationQuery("SELECT 'x'");
    	
    	//h2创建表
    	H2Util.initTable();
    }  
	private static Connection getconn() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private static void closeConn(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static boolean exec(String sql) {
		Connection connection = getconn();
		try {
			Statement statement = connection.createStatement();
			boolean execute = statement.execute(sql);
			closeConn(connection);
			return execute;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static int update(String sql) {
		Connection connection = getconn();
		try {
			Statement statement = connection.createStatement();
			int execute = statement.executeUpdate(sql);
			closeConn(connection);
			return execute;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static List<Map<String, Object>> select(String sql) {
		Connection connection = getconn();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			ResultSetMetaData md = resultSet.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等   
           int columnCount = md.getColumnCount(); //返回此 ResultSet 对象中的列数   
           List<Map<String, Object>> list = new ArrayList<>();   
           Map<String, Object> rowData = null;
           while (resultSet.next()) {  
	        	rowData = new HashMap<>();
	            for (int i = 1; i <= columnCount; i++) {   
	                rowData.put(md.getColumnName(i).toLowerCase(), resultSet.getObject(i));   
	            }   
	            list.add(rowData);   
           }
           return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void initTable() {
		//创建行情表
		exec("create table IF NOT EXISTS market("
				//基础数据
				+ "id varchar(255) PRIMARY KEY,"//平台id.交易对
				+ "platformid varchar(255),"
				+ "platformIcon CLOB(255),"
				+ "platformUrl CLOB(255),"
				+ "platformName varchar(255),"
				+ "moneytype varchar(255),"
				+ "moneytypeName varchar(255),"
				+ "moneytypeIcon CLOB(255),"
				+ "moneytypeUrl CLOB(255),"
				+ "buymoneytype varchar(255),"
				+ "moneypair varchar(255),"
				
				//行情数据实时采集
				+ "close DECIMAL(28,8),"
				+ "closermb DECIMAL(28,8),"
				+ "zhangfu DECIMAL(28,8),"
				+ "zhangfuMoneyrmb DECIMAL(28,8),"
				
				//深度图数据实时采集
				+ "buy DECIMAL(28,8),"
				+ "sell DECIMAL(28,8),"
				+ "buyrmb DECIMAL(28,8),"
				+ "sellrmb DECIMAL(28,8),"
				
				//提取k线图表,凌晨0点收盘价格
				+ "open DECIMAL(28,8),"
				+ "openrmb DECIMAL(28,8),"
				
				//数据定时计算流通市值
				+ "allMoneyCount DECIMAL(28,8),"
				+ "allMoneyrmb DECIMAL(28,8),"
				
				//k线图定时计算
				+ "low DECIMAL(28,8),"
				+ "lowrmb DECIMAL(28,8),"
				+ "high DECIMAL(28,8),"
				+ "highrmb DECIMAL(28,8),"
				+ "vol DECIMAL(28,8),"
				+ "volrmb DECIMAL(28,8),"
				+ "count DECIMAL(28,8),"
				+ "amount DECIMAL(28,8)"
				+ ")");
		//创建交易平台信息表
		exec("create table IF NOT EXISTS thirdpartyplatforminfo("
				+ "id varchar(255) PRIMARY KEY,"
				+ "name varchar(255),"
				+ "urlicon CLOB,"
				+ "url CLOB,"
				+ "description CLOB,"
				+ "apiurl CLOB"
				+ ")");
		//创建币种信息表
		exec("create table IF NOT EXISTS market("
				+ "bitbcode varchar(255) PRIMARY KEY,"
				+ "bitbname varchar(255),"
				+ "bitbcnname varchar(255),"
				+ "currentcount DECIMAL(28,8),"
				+ "allcount DECIMAL(28,8),"
				+ "iconurl CLOB,"
				+ "desc CLOB"
				+ ")");
	}
	public static Map<String, Method> columns = getColumns();
	private static Map<String, Method> getColumns() {
		Map<String, Method> columns=new HashMap<>();
		Field[] fields = Market.class.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			try {
				if (!fieldName.equals("_id")) {
					columns.put(fieldName, Market.class.getMethod("get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1)));
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return columns;
	}
	
	public static void insertOrUpdate(Market market) {
		Connection connection = getconn();
		List<Object> parms=new ArrayList<>();
		String sql="update market set ";
		for(String columName:columns.keySet()){
			Method method = columns.get(columName);
			try {
				Object object = method.invoke(market, null);
				if (object != null) {
					sql+=columName+"=?,";
					parms.add(object);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (sql.endsWith(",")) {
			sql=sql.substring(0, sql.length()-1);
		}
		sql+= " where id=?";
		//System.out.println(sql);
		parms.add(market.get_id());
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			for (int i=0;i<parms.size();i++) {
				prepareStatement.setObject(i+1, parms.get(i));
			}
			int updateCount = prepareStatement.executeUpdate();
			if (updateCount != 1) {//如果不存在,需要先添加
				sql="insert into market(id,";
				String insertColumnStr="";
				String zhanwei="?,";
				parms=new ArrayList<>();
				parms.add(market.get_id());
				for(String columName:columns.keySet()){
					Method method = columns.get(columName);
					try {
						Object object = method.invoke(market, null);
						if (object != null) {
							insertColumnStr+=columName+",";
							parms.add(object);
							zhanwei+="?,";
						}
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				sql+= insertColumnStr.substring(0,insertColumnStr.length()-1)+") values("+zhanwei.substring(0, zhanwei.length()-1)+")";
				System.out.println(sql);
				PreparedStatement prepareStatement2 = connection.prepareStatement(sql);
				for (int i=0;i<parms.size();i++) {
					prepareStatement2.setObject(i+1, parms.get(i));
				}
				prepareStatement2.executeUpdate();
			}
			closeConn(connection);
			//System.out.println(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		initTable();
		Market market=new Market(null, null);
		market.setPlatformid("test");
		market.setMoneypair("btcUsdt");
		market.setClose(new BigDecimal(10));
		insertOrUpdate(market);
		
		System.out.println(select("select * from market"));;
	}*/
}
