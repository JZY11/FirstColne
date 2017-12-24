package com.btb.util.thread;

import java.util.List;

public class ThreadTest {
		public static void main(String[] args) {
			noRerultTest();
			haveRerultTest();
		}
		private static void noRerultTest() {
			ThreadPoolManager.workNoResult(new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(6000);
						System.out.println("没有返回值的多线程测试");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		private static void haveRerultTest() {
			//测试多new Work()可以写成内部类或者单个类
			Works works=new Works();
			works.add(new Work() {
				@Override
				public String call() throws Exception {
					// TODO Auto-generated method stub
					Thread.sleep(5000);
					return "2000";
				}
			});
			works.add(new Work() {

				@Override
				public Integer call() throws Exception {
					// TODO Auto-generated method stub
					return 1;
				}
			});
			
			List<Object> objects = ThreadPoolManager.workListResut(works);
			System.out.println((String)objects.get(0));
			System.out.println((Integer)objects.get(1));
		}
}
