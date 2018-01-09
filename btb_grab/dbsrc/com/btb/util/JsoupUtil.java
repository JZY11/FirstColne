package com.btb.util;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class JsoupUtil {
	
	public static String getJson(String url) {
		System.out.println("正在获取");
		Connection connect = Jsoup.connect(url).timeout(120000);//过期两分钟
		connect.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
		connect.header("Accept-Language", "zh-cn");
		connect.ignoreContentType(true);
		connect.followRedirects(true);
		SecurityUtil.initSecurity();
		//connect.validateTLSCertificates(false);
		String text=null;
		try {
			text = connect.execute().body();
			// = connect.get().body().text();
			return text;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (text==null) {
				text="error";
				System.out.println("链接出错: "+url);
			}
			System.out.println("获取完毕");
		}
		return text;
	}
	public static Document getElement(String url) {
		Connection connect = Jsoup.connect(url).timeout(30000);
		connect.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:57.0) Gecko/20100101 Firefox/57.0");
		connect.header("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		connect.ignoreContentType(true);
		connect.followRedirects(true);
		connect.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connect.header("Host", "coinmarketcap.com");
		connect.maxBodySize(Integer.MAX_VALUE);
		SecurityUtil.initSecurity();
		Document text=null;
		try {
			text = connect.get();
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
