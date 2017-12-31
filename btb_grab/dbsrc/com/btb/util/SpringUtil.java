package com.btb.util;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtil {
	public static ApplicationContext context;
	static{
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}
	
	public static void testinitSpring() {
		Map<String, BaseHttp> beanHttpMap = SpringUtil.context.getBeansOfType(BaseHttp.class);
		for (BaseHttp baseHttp : beanHttpMap.values()) {
			TaskUtil.httpBeans.put(baseHttp.getPlatformId(), baseHttp);
		}
	}
	
}
