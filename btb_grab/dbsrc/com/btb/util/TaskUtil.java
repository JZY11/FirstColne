package com.btb.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSON;
import com.btb.dao.BitbinfoMapper;
import com.btb.dao.RateMapper;
import com.btb.dao.ThirdpartysupportmoneyMapper;
import com.btb.entity.Bitbinfo;
import com.btb.entity.Rate;

public class TaskUtil {
	public static void main(String[] args) throws Exception {
		//System.out.println(DateUtil.dateFormat(new Date(1513767600000L), "yyyy-MM-dd HH:mm:ss"));
		long currentTimeMillis = System.currentTimeMillis();
		TaskUtil.initBtcCount();
		long currentTimeMillis2 = System.currentTimeMillis();
		System.out.println((currentTimeMillis2-currentTimeMillis)/1000);
	}
	
	/**
	 * 初始化CacheData.moneyPairs,所有平台的交易对
	 */
	public static void initMoneypair() {
		ThirdpartysupportmoneyMapper thirdpartysupportmoneyMapper = SpringUtil.getBean(ThirdpartysupportmoneyMapper.class);
		List<Map<String, String>> findplatformidAll = thirdpartysupportmoneyMapper.findplatformidAll();
		for (Map<String, String> map : findplatformidAll) {
			String platformid = map.get("platformid");
			List<Map<String, String>> findmoneypairByplatformid = thirdpartysupportmoneyMapper.findmoneypairByplatformid(platformid);
			List<String> moneypairs = new ArrayList<>();
			for (Map<String, String> map2 : findmoneypairByplatformid) {
				String moneypair = map2.get("moneypair");
				moneypairs.add(moneypair);
			}
			CacheData.moneyPairs.put(platformid, moneypairs);
		}
	}
	
	
	/*
	 * 获取每一种币的流通数量,5分钟一次
	 */
	private static boolean isrunningInitBtcCount = false;
	public static void initBtcCount() {
		BitbinfoMapper bitbinfoMapper = SpringUtil.getBean(BitbinfoMapper.class);
		bitbinfoMapper.deleteByExample(null);
		Document document=null;
		document = Jsoup.parse(WebClientUtil.getHtml("https://coinmarketcap.com/all/views/all/"));
		Elements elements = document.select("#currencies-all tbody tr");
		for (Element element : elements) {
			Bitbinfo bitbInfo = new Bitbinfo();
			bitbInfo.setBitbcode(element.select("td.col-symbol").text());
			bitbInfo.setBitbname(element.select("td.currency-name").text());
			Elements elements2 = element.select("td.circulating-supply > a");
			if (elements2 != null && elements2.size()>0) {
				String currentCount = elements2.get(0).attr("data-supply").trim();
				System.out.println("=============="+currentCount);
				bitbInfo.setCurrentcount(new BigDecimal(currentCount.equals("None") || currentCount.equals("")?"0":currentCount));
			}else {
				bitbInfo.setCurrentcount(new BigDecimal(0));
			}
			
			try {
				bitbinfoMapper.updateByPrimaryKeySelective(bitbInfo);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 每天跑一次欧洲下午4点开始跑
	 */
	public static void initWaiHui() throws IOException {
		Connection connect = Jsoup.connect("https://api.fixer.io/latest?base=CNY");
		connect.ignoreContentType(true);
		Map<String,BigDecimal> map = ((Map<String,Map<String,BigDecimal>>)JSON.parseObject(connect.get().text(), Map.class)).get("rates");
		CacheData.rateMap=map;//缓存数据,用于计算
		//持久化到数据库
		RateMapper rateMapper = SpringUtil.getBean(RateMapper.class);
		
		for (String moneyCode : map.keySet()) {
			Rate rate = new Rate();
			rate.setMoneycode(moneyCode);
			rate.setRate(map.get(moneyCode));
			rate.setUpdatetime(DateUtil.dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
			rateMapper.updateByPrimaryKeySelective(rate);
		}
		
	}
	
	
	
	/*Connection connect = Jsoup.connect("http://www.webmasterhome.cn/huilv/huobidaima.asp");
	try {
		Document document = connect.get();
		Elements elements = document.select("#huobi > a");
		for (Element element : elements) {
			System.out.println(element.text()+element.attr("title").replace("汇率", ""));
			Rate rate = new Rate();
			rate.setMoneycode(element.text());
			rate.setMoneyname(element.attr("title").replace("汇率", ""));
			RateMapper rateMapper = SpringUtil.getBean(RateMapper.class);
			rateMapper.insertSelective(rate);
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	
	/**
	 
	 			try {
				if (kaishi && !bitbInfo.getBitbcode().equals("COSS")) {
					Connection connect = Jsoup.connect(element.select("td.currency-name a").get(0).absUrl("href"));
					Document document2 = connect.get();
					Elements elements2 = document2.getElementsContainingOwnText("Max Supply");
					if (elements2 != null && elements2.size()>0) {
						Element element3 = elements2.get(0);
						bitbInfo.setAllcount(new BigDecimal(element3.parent().nextElementSibling().text().split(" ")[0].replaceAll(",", "")));
					}
					bitbinfoMapper.insertSelective(bitbInfo);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

	 */
}
