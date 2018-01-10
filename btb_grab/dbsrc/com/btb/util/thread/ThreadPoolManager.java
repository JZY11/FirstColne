package com.btb.util.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
	public static ExecutorService threadExecutor;
	
	static{
		threadExecutor=new ThreadPoolExecutor(
				10,//最小线程数
				10000,//最大线程数
				120,//线程多长时间不用就失效
				TimeUnit.SECONDS,//失效时间按分钟计算
				new LinkedBlockingQueue<Runnable>(10000),//超出5000个线程后,指定等待执行的任务数量
				new ThreadPoolExecutor.DiscardPolicy());//用来构造线程池里的worker线程
	}
			/*相关文档: http://blog.csdn.net/a837199685/article/details/50619311
			 * workQueue任务队列）：用于保存等待执行的任务的阻塞队列。可以选择以下几个阻塞队列。
					ArrayBlockingQueue：是一个基于数组结构的有界阻塞队列，此队列按 FIFO（先进先出）原则对元素进行排序。
					LinkedBlockingQueue：一个基于链表结构的阻塞队列，此队列按FIFO （先进先出） 排序元素，吞吐量通常要高于ArrayBlockingQueue。静态工厂方法Executors.newFixedThreadPool()使用了这个队列
					SynchronousQueue：一个不存储元素的阻塞队列。每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态，吞吐量通常要高于LinkedBlockingQueue，静态工厂方法Executors.newCachedThreadPool使用了这个队列。
					PriorityBlockingQueue：一个具有优先级的无限阻塞队列。
			 */
	/**
	 *  此方法执行一个没有结果的方法
	 *  请注意:应为是没有执行结果的线程,主线程是不会等待 子线程执行完毕的.
	 * @param runnable
	 */
	public static void workNoResult(Runnable runnable) {
		threadExecutor.execute(runnable);
	}

	
	/**
	 * 这个方法是执行多任务 ,且每个任务都是有返回结果的
	 * 请注意1:每个任务的返回结果会放到同一个list当中,取出list中的值转换成本来返回的类型即可
	 * 请注意2:多个任务的返回结果在list中是分先后顺序的,跟放进去的list任务顺序是一一对照的,因此不会导致任务结果集错乱.
	 * 请注意3:应为主线程会等待子线程完成,在执行后面的任务,因此一定要把全部任务分配到批量任务里面.
	 * @param callableObjectList
	 * @return
	 */
	public static List<Object> workListResut(Works works) {
		List<Object> objects=new ArrayList<Object>();
		try {
			List<Future<Object>> futureObjectList= threadExecutor.invokeAll(works);
			for (Future<Object> future : futureObjectList) {
				objects.add(future.get());
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(Thread.currentThread().getClass()+"{执行中出现故障}");
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(Thread.currentThread().getClass()+"{转换类型失败}");
		}
		return objects;
	}
	
	
	//关闭worker
	public static void closeWorker() {
		threadExecutor.shutdown();
	}
}
