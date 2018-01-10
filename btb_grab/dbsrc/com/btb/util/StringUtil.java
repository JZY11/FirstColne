package com.btb.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		if (open==null || close==null) {
			return null;
		}
		return close.subtract(open).divide(open,8,RoundingMode.HALF_UP).multiply(new BigDecimal(100));
	}
	/**
	 * 美元转人民币
	 * @param usdMoney
	 * @return
	 */
	public static BigDecimal ToRmb(BigDecimal usdMoney,String platformid,String buymoneytype) {
		if (usdMoney==null) {
			return null;
		}
		if (platformid == null || buymoneytype == null) {
			try {
				throw new Exception("平台id和交易币种类型,为空了");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BigDecimal bigDecimal = TaskUtil.buyMonetyTypeRate.get(platformid+"."+buymoneytype);
		if (bigDecimal != null) {
			BigDecimal fabiToRmb = bigDecimal.multiply(usdMoney);
			return fabiToRmb;
		}else {
			if (buymoneytype.equalsIgnoreCase("usdt")) {
				buymoneytype="USD";
			}
			return fabiToRmb(usdMoney, buymoneytype);
		}
	}
	
	public static BigDecimal fabiToRmb(BigDecimal usdMoney,String moneyType) {
		if (moneyType==null) {
			moneyType="USD";
		}
		if (usdMoney==null) {
			return null;
		}
		BigDecimal rabe = TaskUtil.rateMap.get(moneyType.toUpperCase());
		return usdMoney.divide(rabe, 8, RoundingMode.HALF_UP);
	}
	public static BigDecimal fabiToRmb(BigDecimal usdMoney) {
		String moneyType="USD";
		if (usdMoney==null) {
			return null;
		}
		return usdMoney.divide(TaskUtil.rateMap.get(moneyType), 8, RoundingMode.HALF_UP);
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
				File[] listFiles = file2.listFiles();
				for (File file3 : listFiles) {
					if (file3.getName().startsWith("WebSocketUtils") && !file3.getName().contains("$")) {
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
		}
		return websocketUtils;
	}
	
	public static long get0time() {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(DateUtil.getDateNoTime()).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 
	 * @param moneypair
	 * @return 0: buymoneyType,1:moneytype
	 */
	public static String[] getHuobiBuyMoneytype(String moneypair) {
		String[] jiaoyidui=new String[2];
		if (moneypair.endsWith("usdt")) {
			jiaoyidui[0]="usdt";
			jiaoyidui[1]=moneypair.substring(0, moneypair.length()-4);
		}else if (moneypair.endsWith("btc")) {
			jiaoyidui[0]="btc";
			jiaoyidui[1]=moneypair.substring(0, moneypair.length()-3);
		}else if (moneypair.endsWith("eth")) {
			jiaoyidui[0]="eth";
			jiaoyidui[1]=moneypair.substring(0, moneypair.length()-3);
		}else {
			//获取所有buymoneytype,和法币,获取交易对
			Set<String> keySet = TaskUtil.buyMonetyTypeRate.keySet();
			Set<String> keySet2 = TaskUtil.rateMap.keySet();//获取所有法币
			keySet.addAll(keySet2);
			for (String string : keySet) {
				string = string.toLowerCase();
				if (moneypair.endsWith(string)) {
					jiaoyidui[0]=string;
					jiaoyidui[1]=moneypair.substring(0, moneypair.length()-string.length()).toLowerCase();
				}
			}
			if (jiaoyidui[0]==null) {
				try {
					throw new Exception("没有分析出来交易对:"+moneypair);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return jiaoyidui;
	}
	
	public static BigDecimal toBigDecimal(Object object) {
		if (object == null) {
			return new BigDecimal("0");
		}else {
			String string = object.toString().replaceAll("[^0-9a-zA-Z-+.]","");
			if (StringUtil.hashText(string)) {
				if (string.split("\\.").length>2) {//点的数量也不能大于1个
					string = string.replaceFirst("\\.", "#").replaceAll("\\.", "").replace("#", ".");
				}
				return new BigDecimal(string).setScale(8, RoundingMode.HALF_UP);
			}else {
				return new BigDecimal("0");
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println(toBigDecimal("-sk-1djf7-de-987.1.2.37iuisdf"));
	}
}
