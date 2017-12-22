package com.btb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class H2Util {
	private static Connection getconn() {
		//jdbc:h2:mem:test;DB_CLOSE_DELAY=-1
		try {
			Class.forName("org.h2.Driver");
			Connection connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "root");
			return connection;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean exec(String sql) {
		Connection connection = getconn();
		try {
			Statement statement = connection.createStatement();
			boolean execute = statement.execute(sql);
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
	
	public static void main(String[] args) {
		initTable();
		exec("insert into market values('xx','xx','xx',10.2,10.2,10.2,10.2,10.2,10.2,10.2,10.2,10.2,10.2)");
		
		System.out.println(select("select * from market"));;
	}
}
