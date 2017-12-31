package com.btb.tasks.service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.btb.util.IdManage;

import static org.quartz.JobBuilder.newJob; 
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

public class JobManager {
	private static SchedulerFactory jobFactory = new StdSchedulerFactory(); 
	
	/**
	 * 添加任务
	 * @param jobId 任务id
	 * @param c 任务类
	 * @param seconds 多少秒执行一次
	 * 参数
	 * @throws SchedulerException 
	 */
	public static void addJob(BaseJob baseJob) {
		Scheduler scheduler = null;
		try {
			scheduler = jobFactory.getScheduler();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JobDetail job = newJob(baseJob.getC()).withIdentity(baseJob.getJobId()).build();
		JobDataMap jobDataMap = job.getJobDataMap();
		jobDataMap.putAll(baseJob.getParam()==null?new HashMap<String,Object>():baseJob.getParam());
	    // Trigger the job to run now, and then repeat every 40 seconds
		Trigger trigger = newTrigger()
				//group:market-10000		id:btcusdt
	      .withIdentity(baseJob.getGroupId(),baseJob.getJobId())
	      .startNow()//一旦加入scheduler，立即生效
          .withSchedule(simpleSchedule() //使用SimpleTrigger
          .withIntervalInSeconds(baseJob.getSeconds()) //每隔一秒执行一次
          .repeatForever()) //一直执行，奔腾到老不停歇
	      .build();
		// Tell quartz to schedule the job using our trigger
		try {
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void addJob(Class<? extends Job> class1,Integer seconds) {
		Scheduler scheduler = null;
		try {
			scheduler = jobFactory.getScheduler();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JobDetail job = newJob(class1).withIdentity(IdManage.getUUid()).build();
		Trigger trigger = newTrigger()
	      .withIdentity(IdManage.getUUid())
	      .startNow()//一旦加入scheduler，立即生效
          .withSchedule(simpleSchedule() //使用SimpleTrigger
          .withIntervalInSeconds(seconds) //每隔一秒执行一次
          .repeatForever()) //一直执行，奔腾到老不停歇
	      .build();
		try {
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void addJob(Class<? extends Job> class1) {
		Integer seconds=0;
		try {
			Field field= class1.getDeclaredField("ts");
			field.setAccessible(true);
			seconds = (Integer)field.get(null);
			System.out.println(seconds);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		Scheduler scheduler = null;
		try {
			scheduler = jobFactory.getScheduler();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JobDetail job = newJob(class1).withIdentity(IdManage.getUUid()).build();
		Trigger trigger = newTrigger()
	      .withIdentity(IdManage.getUUid())
	      .startNow()//一旦加入scheduler，立即生效
          .withSchedule(simpleSchedule() //使用SimpleTrigger
          .withIntervalInSeconds(seconds) //每隔一秒执行一次
          .repeatForever()) //一直执行，奔腾到老不停歇
	      .build();
		try {
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
