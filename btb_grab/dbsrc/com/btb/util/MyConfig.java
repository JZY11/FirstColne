package com.btb.util;

import java.io.IOException;
import java.util.Properties;

public class MyConfig {
	private static Properties properties = new Properties();
	static{
		try {
			properties.load(MyConfig.class.getResourceAsStream("/config.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static String get(String key) {
		return properties.get(key).toString();
	}
	public static void main(String[] args) {
		System.out.println(get("nginxPath"));
	}
}
