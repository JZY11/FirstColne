

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.btb.entity.AllPlatformKline;
import com.btb.entity.Bitbinfo;
import com.btb.entity.PlatformInfo;
import com.btb.entity.PlatformSupportmoney;
import com.btb.tasks.threads.TestThread;
import com.btb.util.DateUtil;
import com.btb.util.IdManage;
import com.btb.util.JsoupUtil;
import com.btb.util.SpringUtil;
import com.btb.util.StringUtil;
	//币种大全
	//https://www.cryptocompare.com/api/data/coinlist/
	//单个币种最新价格
	//https://min-api.cryptocompare.com/data/price?e=CCCAGG&fsym=ETH&tsyms=BTC,USD,EUR,CNY
	//多中币种转换价格
	//https://min-api.cryptocompare.com/data/pricemulti?e=CCCAGG&fsyms=ETH,DASH&tsyms=BTC,USD,EUR
	//多种币种转换价格,带显示的,以及一些统计数据
	//https://min-api.cryptocompare.com/data/pricemultifull?e=CCCAGG&fsyms=ETH,DASH&tsyms=BTC,USD,EUR
	//交易对,全网统计和各个平台的详细数据
	//https://www.cryptocompare.com/api/data/coinsnapshot/?fsym=ETH&tsym=BTC
	//通过币种id 获取币种的详细信息
	//https://www.cryptocompare.com/api/data/coinsnapshotfullbyid/?id=1182
	//1分钟k线图
	//https://min-api.cryptocompare.com/data/histominute?fsym=BTC&tsym=USD&limit=10&aggregate=1&e=Poloniex
	//最新价格
	//
	//平台和交易对
	//
import com.btb.util.dao.BaseDaoSql;
import com.btb.util.thread.ThreadPoolManager;

public class Test1 {
	/*@org.junit.Test
	public void main() {
		BitbinfoMapper bitbinfoMapper = SpringUtil.getBean(BitbinfoMapper.class);
		String string = JsoupUtil.getJson("https://www.cryptocompare.com/api/data/coinlist/");
		Map map = JSON.parseObject(string, Map.class);
		Map<String, Map<String,String>> map2 = (Map<String, Map<String,String>>)map.get("Data");
		for (String bitbcode : map2.keySet()) {
			Map<String, String> map3 = map2.get(bitbcode);
			System.out.println(bitbcode+":"+JSON.toJSONString(map2.get(bitbcode)));
			Bitbinfo bitbinfo = new Bitbinfo();
			bitbinfo.setCcid(map3.get("Id"));
			String totalCoinSupply = map3.get("TotalCoinSupply");
			if (StringUtil.hashText(totalCoinSupply) && !"N/A".equals(totalCoinSupply)) {
				totalCoinSupply=totalCoinSupply.replaceAll(",", "").replaceAll(" ", "").trim();
				if (totalCoinSupply.split("\\.").length>2) {
					totalCoinSupply=totalCoinSupply.substring(0, totalCoinSupply.indexOf("."));
				}
				System.out.println(totalCoinSupply);
				bitbinfo.setAllcount(new BigDecimal(totalCoinSupply));
			}
			bitbinfo.setBitbcode(map3.get("Symbol"));
			bitbinfo.setBitbfullname(map3.get("FullName"));
			bitbinfo.setBitbname(map3.get("CoinName"));
			bitbinfo.setBitbcnname(map3.get("CoinName")+"币");
			bitbinfo.setIconurl("https://www.cryptocompare.com"+map3.get("ImageUrl"));
			bitbinfo.setSort(Integer.valueOf(map3.get("SortOrder")));
			int count = bitbinfoMapper.updateByPrimaryKeySelective(bitbinfo);
			if (count==0) {
				bitbinfoMapper.insertSelective(bitbinfo);
			}
		}
		//System.out.println(JSON.toJSONString());
	}*/
	
	/*@org.junit.Test
	public void testName() throws Exception {
		BitbinfoMapper bitbinfoMapper = SpringUtil.getBean(BitbinfoMapper.class);
		List<Bitbinfo> list = bitbinfoMapper.getElementErrorCode();
		for (Bitbinfo bitbinfo : list) {
			String id = bitbinfo.getCcid();
			String json = JsoupUtil.getJson("https://www.cryptocompare.com/api/data/coinsnapshotfullbyid/?id="+id);
			if (json.equals("error")) {
				bitbinfo.setCheckerror("error");
				bitbinfoMapper.updateByPrimaryKeySelective(bitbinfo);
			}else {
				bitbinfo.setCheckerror("yes");
				Map map = JSON.parseObject(json, Map.class);
				Map map2 = (Map) ((Map)map.get("Data")).get("General");
				bitbinfo.setDesc(StringUtil.valueOf(map2.get("Description")));
				bitbinfo.setAffiliateUrl(StringUtil.valueOf(map2.get("AffiliateUrl")));
				bitbinfo.setCcoverviewurl("https://www.cryptocompare.com"+map2.get("Url"));
				String totalCoinsMined = StringUtil.valueOf(map2.get("TotalCoinsMined"));
				if (StringUtil.hashText(totalCoinsMined) && !"N/A".equals(totalCoinsMined)) {
					totalCoinsMined=totalCoinsMined.replaceAll(",", "").replaceAll(" ", "").trim();
					if (totalCoinsMined.split("\\.").length>2) {
						totalCoinsMined=totalCoinsMined.substring(0, totalCoinsMined.indexOf("."));
					}
					BigDecimal setScale = new BigDecimal(totalCoinsMined).setScale(2, BigDecimal.ROUND_HALF_UP);
					System.out.println(setScale.toPlainString());
					bitbinfo.setCurrentcount(setScale);
				}
				bitbinfo.setDesc(StringUtil.valueOf(map2.get("Description")));
				bitbinfo.setFeatures(StringUtil.valueOf(map2.get("Features")));
				bitbinfo.setTechnology(StringUtil.valueOf(map2.get("Technology")));
				bitbinfoMapper.updateByPrimaryKeySelective(bitbinfo);
			}
		}
		
	}*/
	/*@org.junit.Test
	public void testName2() throws Exception {
		BitbinfoMapper bitbinfoMapper = SpringUtil.getBean(BitbinfoMapper.class);
		ThirdpartysupportmoneyMapper thirdpartysupportmoneyMapper = SpringUtil.getBean(ThirdpartysupportmoneyMapper.class);
		List<Bitbinfo> list = bitbinfoMapper.getElementErrorCode();
		for (Bitbinfo bitbinfo : list) {
			String id = bitbinfo.getCcid();
			String json = JsoupUtil.getJson("https://www.cryptocompare.com/api/data/coinsnapshotfullbyid/?id="+id);
			if (json.equals("error")) {
				bitbinfo.setCheckerror("error");
				bitbinfoMapper.updateByPrimaryKeySelective(bitbinfo);
			}else {
				bitbinfo.setCheckerror("yes");
				Map map = JSON.parseObject(json, Map.class);
				List<Object> list2 = (List<Object>) ((Map)map.get("Data")).get("Subs");
				System.out.println(list2);
				for (Object sub : list2) {
					Thirdpartysupportmoney thirdpartysupportmoney= new Thirdpartysupportmoney();
					String[] split = sub.toString().split("~");
					thirdpartysupportmoney.setPlatformid(split[1]);
					thirdpartysupportmoney.setMoneytype(split[2]);
					thirdpartysupportmoney.setBuymoneytype(split[3]);
					thirdpartysupportmoney.setMoneypair(split[2]+"_"+split[3]);
					List<Thirdpartysupportmoney> select = thirdpartysupportmoneyMapper.select(thirdpartysupportmoney);
					if (select == null || select.isEmpty()) {
						thirdpartysupportmoneyMapper.insertSelective(thirdpartysupportmoney);
					}
				}
				bitbinfoMapper.updateByPrimaryKeySelective(bitbinfo);
			}
		}
		
	}*/
	
/*	@org.junit.Test
	public void createTable() throws Exception {
		SqlSessionFactory sessionFactory = SpringUtil.getBean(SqlSessionFactory.class);
		SqlSession session = sessionFactory.openSession();
		List<String> list = session.selectList("selectPlateformid");
		for (String plateformid : list) {
			session.update("createtable_markethistory_1min",plateformid);
		}
		session.close();
	}*/

	//获取所有平台
	@Test
	public void testName21() throws Exception {
		
		Document document = JsoupUtil.getElement("https://coinmarketcap.com/zh/exchanges/volume/24-hour/all/");
		System.out.println(document.html());
		//
		Elements elements = document.select("table.table-condensed > tbody > tr[id]");
		for (Element element : elements) {
			PlatformInfo platformInfo = new PlatformInfo();
			//Element nextElementSibling = element.nextElementSibling();
			//System.out.println(element.text().split("\\.")[0].trim()+element.text().split("\\.")[1].trim());
			//System.out.println(element.select("td > h3 > a").attr("abs:href"));
			platformInfo.setName(element.text().split("\\.")[1].trim());
			platformInfo.setId(platformInfo.getName().replaceAll(" ", "_").replaceAll("-", "_"));
			platformInfo.setVolrank(Integer.valueOf(element.text().split("\\.")[0].trim()));
			platformInfo.setCoinmarketcapurl(element.select("td > h3 > a").attr("abs:href"));
			while (true) {
				element = element.nextElementSibling();
				if (element.text().contains("总计")) {
					String text_rmb = element.child(1).text();
					if (StringUtil.hashText(text_rmb)) {
						platformInfo.setRmb_vol(StringUtil.toBigDecimal(text_rmb.replace("$","")));
					}
					String data_usd = element.child(1).attr("data-usd");
					String data_btc = element.child(1).attr("data-btc");
					platformInfo.setBtc_vol(StringUtil.toBigDecimal(data_btc));
					platformInfo.setUsd_vol(StringUtil.toBigDecimal(data_usd));
					break;
				}
				if (StringUtil.hashText(element.attr("id"))) {//如果id不等于空,跳出
					break;
				}
			}
			BaseDaoSql.save(platformInfo);
		}
	}
	
	//获取某个平台下的交易对
	@Test
	public void testName3() throws Exception {
		List<Map<String, Object>> list = BaseDaoSql.findListMaps("select * from platforminfo");
		for (Map<String, Object> map : list) {
			String platformid = map.get("id").toString();
			String coinmarketcapurl = map.get("coinmarketcapurl").toString();
			Document document = JsoupUtil.getElement(coinmarketcapurl);
			if (document == null) {//失败记录一下
				BaseDaoSql.update("update platform set description = 'error' where id = '"+platformid+"'");
				continue;
			}
			System.out.println(document.select("span.glyphicon.glyphicon-link.text-gray").get(0).nextElementSibling().attr("abs:href"));
			Elements elements = document.select("table.table.no-border.table-condensed > tbody > tr");
			for (int i=1;i<elements.size();i++) {
				Element element = elements.get(i);
				PlatformSupportmoney platformSupportmoney = new PlatformSupportmoney();
				platformSupportmoney.setPlatformid(platformid);
				String moneypair = element.child(2).select("a").get(0).text();
				platformSupportmoney.setBuymoneytype(moneypair.split("/")[1]);
				platformSupportmoney.setMoneytype(moneypair.split("/")[0]);
				platformSupportmoney.setMoneypair(platformSupportmoney.getMoneytype()+"_"+platformSupportmoney.getBuymoneytype());
				platformSupportmoney.setMoneypairurl(element.child(2).select("a").get(0).absUrl("href"));
				platformSupportmoney.setVol24(StringUtil.toBigDecimal(element.child(3).text()));
				platformSupportmoney.setPricenow(StringUtil.toBigDecimal(element.child(4).text()));
				platformSupportmoney.setOrderBaifubi(StringUtil.toBigDecimal(element.child(5).text()));
				platformSupportmoney.setUpdatetime(element.child(6).text());
				int updateCount = BaseDaoSql.update(platformSupportmoney);
				if (updateCount==0) {
					BaseDaoSql.save(platformSupportmoney);
				}
				Bitbinfo bitbinfo = new Bitbinfo();
				bitbinfo.setBitbcode(platformSupportmoney.getMoneytype());
				bitbinfo.setMoneyurl(element.child(1).select("a").get(0).absUrl("href"));
				BaseDaoSql.update(bitbinfo);
			}
		}
	}
	
	//获取币的排名,和币当前流通量,id,
	@Test
	public void test23() throws Exception {
		for (Integer i =1;i<=3;i++) {
			String json = JsoupUtil.getJson("https://api.coinmarketcap.com/v1/ticker/?convert=CNY&start="+(i-1)*500+"&limit=500");
			List<Map> list = JSON.parseArray(json, Map.class);
			for (Map map : list) {
				Bitbinfo bitbinfo = new Bitbinfo();
				bitbinfo.setCcid(map.get("id").toString());
				bitbinfo.setRank(Integer.valueOf(String.valueOf(map.get("rank"))));
				bitbinfo.setBitbcode(map.get("symbol").toString());
				bitbinfo.setCurrentcount(StringUtil.toBigDecimal(map.get("total_supply")));
				bitbinfo.setAllcount(StringUtil.toBigDecimal(map.get("max_supply")));
				bitbinfo.setPercent_change_1h(StringUtil.toBigDecimal(map.get("percent_change_1h")));
				bitbinfo.setPercent_change_24h(StringUtil.toBigDecimal(map.get("percent_change_24h")));
				bitbinfo.setPercent_change_7d(StringUtil.toBigDecimal(map.get("percent_change_7d")));
				bitbinfo.setLast_updated(Long.valueOf(StringUtil.valueOf(map.get("last_updated"))));
				bitbinfo.setPriceRmb(StringUtil.toBigDecimal(map.get("price_cny")));
				bitbinfo.setVol24Rmb(StringUtil.toBigDecimal(map.get("24h_volume_cny")));
				bitbinfo.setMarketCapRmb(StringUtil.toBigDecimal(map.get("market_cap_cny")));
				int updateCount = BaseDaoSql.update(bitbinfo);
				if (updateCount == 0) {
					BaseDaoSql.save(bitbinfo);
				}
			}
			
		}
		
	}
	
	//https://graphs.coinmarketcap.com/currencies/bitcoin/1367174841000/1489400827013/
	@Test
	public void allplatformCaiji(){
		List<Map<String, Object>> list = BaseDaoSql.findListMaps("SELECT ccid,bitbcode from bitbinfo where percent_change_1h is not null");
		for (Map<String, Object> map : list) {
			AllPlatformKline aPlatformKline = new AllPlatformKline();
			aPlatformKline.setBitbcode(map.get("bitbcode").toString());
			aPlatformKline.setBitbname(map.get("ccid").toString());
			while (true) {
				String result = BaseDaoSql.findSingleResult("select min(timeid) from allplatformkline where bitbname = '"+aPlatformKline.getBitbname()+"'");
				long end = new Date().getTime();
				long start = DateUtil.dateMath(new Date(), DateUtil.Date, -1).getTime();
				if (StringUtil.hashText(result)) {//如果不为空
					end = Long.valueOf(result)-1;
					start=DateUtil.dateMath(new Date(end), DateUtil.Date, -1).getTime();
				}
				String url = "https://graphs.coinmarketcap.com/currencies/"+aPlatformKline.getBitbname()+"/"+start+"/"+end+"/";
				System.out.println(url);
				try {
					String string = JsoupUtil.getJson(url);
					Map map2 = JSON.parseObject(string, Map.class);
					List<List<Object>> object = (List<List<Object>>)map2.get("market_cap_by_available_supply");
					List<List<Object>> object2 = (List<List<Object>>)map2.get("price_btc");
					List<List<Object>> object3 = (List<List<Object>>)map2.get("price_usd");
					List<List<Object>> object4 = (List<List<Object>>)map2.get("volume_usd");
					if (!string.equals("error") && object.isEmpty()) {//如果没有数据了
						break;
					}
					for (int i=0;i<object.size();i++) {
						aPlatformKline.setTimeid(Long.valueOf(StringUtil.valueOf(object.get(i).get(0))));
						aPlatformKline.setCapSuppy(StringUtil.toBigDecimal(object.get(i).get(1)));
						aPlatformKline.setPrice_btc(StringUtil.toBigDecimal(object2.get(i).get(1)));
						aPlatformKline.setPrice_usd(StringUtil.toBigDecimal(object3.get(i).get(1)));
						aPlatformKline.setVol_usd(StringUtil.toBigDecimal(object4.get(i).get(1)));
						BaseDaoSql.save(aPlatformKline);
					}
				} catch (Exception e) {
				}
				
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println();
		System.out.println(DateUtil.dateMath(new Date(), DateUtil.Date, -1).getTime());
		System.out.println(DateUtil.dateFormat(new Date(1367261101*1000l), "yyyy-MM-dd HH:mm:ss"));
		System.out.println(DateUtil.dateFormat(new Date(1515383961*1000l), "yyyy-MM-dd HH:mm:ss"));
		System.out.println(DateUtil.dateFormat(new Date(1515399261*1000l), "yyyy-MM-dd HH:mm:ss"));
		
	}
	
	
}
