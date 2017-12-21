package com.btb.util;

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
}
