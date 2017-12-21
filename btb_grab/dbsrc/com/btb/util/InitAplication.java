package com.btb.util;

import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.btb.tasks.RateJob;

public class InitAplication implements ApplicationListener<ContextRefreshedEvent> {
	
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		
		
	}
	
}
