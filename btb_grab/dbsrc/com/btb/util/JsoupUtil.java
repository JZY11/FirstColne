package com.btb.util;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class JsoupUtil {
	
	public static String getJson(String url) {
		Connection connect = Jsoup.connect(url).timeout(30000);
		connect.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
		connect.ignoreContentType(true);
		//connect.validateTLSCertificates(false);
		String text=null;
		try {
			text = connect.get().body().text();
			return text;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (text==null) {
				System.out.println("链接出错: "+url);
			}else {
				System.out.println("采集成功: "+url);
			}
		}
		return null;
	}
	
}
