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
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		System.out.println(calendar.getWeekYear() +"-"+ calendar.getWeeksInWeekYear()+"week");
		System.out.println(DateUtil.dateFormat(new Date(1514419260000L), "yyyy-u"));
		//System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse(DateUtil.getDateNoTime()).getTime()/1000);
	}
	public static void main1(String[] args) throws IOException, ClassNotFoundException {
		
	}
}
