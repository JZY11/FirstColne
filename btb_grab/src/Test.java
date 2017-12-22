import java.util.Date;

import com.btb.util.DateUtil;

public class Test {
	public static void main(String[] args) {
		System.out.println(DateUtil.dateFormat(new Date(1513852080000L), "yyyy-MM-dd HH:mm:ss"));
	}
}
