package com.btb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.h2.jdbcx.JdbcConnectionPool;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.btb.entity.Market;


public class H2Util {
	private static DruidDataSource dataSource=new DruidDataSource();
	 
    static{  
    	dataSource.setDriverClassName("org.h2.Driver");
    	dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    	dataSource.setUsername("root");
    	dataSource.setPassword("root");
    	dataSource.setInitialSize(10);//初始化链接数量
    	dataSource.setMaxActive(1000);//最大并发链接数
    	dataSource.setDefaultAutoCommit(true);
    	dataSource.setValidationQuery("SELECT 'x'");
    }  
	static{
		initTable();
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
				+ "id varchar(255) PRIMARY KEY,"
				+ "platformid varchar(255),"
				+ "moneypair varchar(255),"
				+ "open DECIMAL(28,8),"
				+ "openrmb DECIMAL(28,8),"
				+ "close DECIMAL(28,8),"
				+ "closermb DECIMAL(28,8),"
				+ "low DECIMAL(28,8),"
				+ "lowrmb DECIMAL(28,8),"
				+ "high DECIMAL(28,8),"
				+ "highrmb DECIMAL(28,8),"
				+ "vol DECIMAL(28,8),"
				+ "volrmb DECIMAL(28,8)"
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
	
	
	public static void insertOrUpdate(Market market) {
		Connection connection = getconn();
		String sql="update market set open=?,openrmb=?,close=?,closermb=?,low=?,lowrmb=?,high=?,highrmb=?,vol=?,volrmb=? where id=?";
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			int parameterIndex = 0;
			prepareStatement.setBigDecimal(++parameterIndex, market.getOpen());
			prepareStatement.setBigDecimal(++parameterIndex, market.getOpenrmb());
			prepareStatement.setBigDecimal(++parameterIndex, market.getClose());
			prepareStatement.setBigDecimal(++parameterIndex, market.getClosermb());
			prepareStatement.setBigDecimal(++parameterIndex, market.getLow());
			prepareStatement.setBigDecimal(++parameterIndex, market.getLowrmb());
			prepareStatement.setBigDecimal(++parameterIndex, market.getHigh());
			prepareStatement.setBigDecimal(++parameterIndex, market.getHighrmb());
			prepareStatement.setBigDecimal(++parameterIndex, market.getVol());
			prepareStatement.setBigDecimal(++parameterIndex, market.getVolrmb());
			prepareStatement.setString(++parameterIndex,market.getPlatformid()+"-"+market.getMoneypair());
			int updateCount = prepareStatement.executeUpdate();
			if (updateCount != 1) {//如果不存在,需要先添加
				sql="insert into market values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement prepareStatement2 = connection.prepareStatement(sql);
				int parameterIndex2 = 0;
				prepareStatement2.setString(++parameterIndex2,market.getPlatformid()+"-"+market.getMoneypair());
				prepareStatement2.setString(++parameterIndex2,market.getPlatformid());
				prepareStatement2.setString(++parameterIndex2,market.getMoneypair());
				prepareStatement2.setBigDecimal(++parameterIndex2, market.getOpen());
				prepareStatement2.setBigDecimal(++parameterIndex2, market.getOpenrmb());
				prepareStatement2.setBigDecimal(++parameterIndex2, market.getClose());
				prepareStatement2.setBigDecimal(++parameterIndex2, market.getClosermb());
				prepareStatement2.setBigDecimal(++parameterIndex2, market.getLow());
				prepareStatement2.setBigDecimal(++parameterIndex2, market.getLowrmb());
				prepareStatement2.setBigDecimal(++parameterIndex2, market.getHigh());
				prepareStatement2.setBigDecimal(++parameterIndex2, market.getHighrmb());
				prepareStatement2.setBigDecimal(++parameterIndex2, market.getVol());
				prepareStatement2.setBigDecimal(++parameterIndex2, market.getVolrmb());
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
		exec("insert into market values('xx','xx','xx',10.2,10.2,10.2,10.2,10.2,10.2,10.2,10.2,10.2,10.2)");
		
		System.out.println(select("select * from market"));;
	}
}
