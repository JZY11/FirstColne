package com.btb.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.java_websocket.client.WebSocketClient;

public class StringUtil {
	public static boolean hashText(Object text) {
		if (text != null && !text.toString().trim().equals("")) {
			return true;
		}
		return false;
	}
	public static String valueOf(Object object) {
		if (object==null) {
			return "";
		}else {
			return object.toString();
		}
	}
	
	public static String decode(String code) {
		if (code==null || code.equals("")) {
			return code;
		}
		try {
			return URLDecoder.decode(code,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return code;
	}
	public static boolean isNumeric(String str){
		   for(int i=str.length();--i>=0;){
		      int chr=str.charAt(i);
		      if(chr<48 || chr>57)
		         return false;
		   }
		   return true;
	}
	
	/**
	 * 计算增长百分比
	 * @param open 开始值
	 * @param close 结束值
	 * @return
	 */
	public static BigDecimal getbaifenbi(BigDecimal open,BigDecimal close) {
		return close.subtract(open).divide(open,8,RoundingMode.HALF_UP).multiply(new BigDecimal(100));
	}
	/**
	 * 美元转人民币
	 * @param usdMoney
	 * @return
	 */
	public static BigDecimal UsdToRmb(BigDecimal usdMoney) {
		return usdMoney.divide(CacheData.rateMap.get("USD"), 2, RoundingMode.HALF_UP);
	}
	
	public static List<Class<WebSocketClient>> getAllWebSocketUtils() {
		List<Class<WebSocketClient>> websocketUtils = new ArrayList<>();
		Enumeration<URL> resources = null;
		try {
			resources = Thread.currentThread().getContextClassLoader().getResources("com/btb");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (resources.hasMoreElements()) {
			URL url = (URL) resources.nextElement();
			File file = new File(url.getPath());
			File[] files = file.listFiles();
			for (File file2 : files) {
				 File file3 = new File(file2.getPath()+"\\WebSocketUtils.class");
				 if (file3.exists()) {
					String path = file3.getPath();
					Class<WebSocketClient> forName;
					try {
						forName = (Class<WebSocketClient>) Class.forName(path.substring(path.indexOf("com\\btb\\"), path.lastIndexOf(".class")).replaceAll("\\\\", "\\."));
						websocketUtils.add(forName);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			}
		}
		return websocketUtils;
	}
}
