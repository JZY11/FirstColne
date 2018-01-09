import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.java_websocket.client.WebSocketClient;

import com.btb.util.DateUtil;
import com.btb.util.StringUtil;

public class Test {
	public static void main(String[] args) throws Exception {
		/*Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		System.out.println(calendar.getWeekYear() +"-"+ calendar.getWeeksInWeekYear()+"week");
		System.out.println(DateUtil.dateFormat(new Date(1514419260000L), "yyyy-u"));*/
		//System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse(DateUtil.getDateNoTime()).getTime()/1000);
		System.out.println(DateUtil.dateFormat(new Date(1515253555000l), "yyyy-MM-dd HH:mm:ss"));
	}
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
	public static void main1(String[] args) throws IOException, ClassNotFoundException {
		
	}
}
