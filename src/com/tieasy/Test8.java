package com.tieasy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


// excel 12month  youche/dianche  5jiekou  jindu
public class Test8 {
	
	static AtomicInteger progress = new AtomicInteger(0);// put down progress
	
	public static void main(String[] args) {
		getDownProgress();
		excelDetail();
	}
	
	public static void excelDetail() {
		int month = 12;
		ExcelTask et = new ExcelTask();
		
		// excel detail thread pool
		ExecutorService sheetExecutorService = Executors.newCachedThreadPool();
		CountDownLatch countDownLatch = new CountDownLatch(month);// sheet count down
		List<Integer> sheetcountv = new ArrayList<>();
		for (int i = 1; i <= month; i++) {
			final int n = i;
			System.out.println(DateUtil.getTime()+" "+i+" month sheet begin...");
			Future<Integer> sheetFuture = sheetExecutorService.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					return et.sheet(n);
				}
			});
			Integer sv = null;
			try {
				sv = sheetFuture.get();
				sheetcountv.add(sv);
				System.out.println(DateUtil.getTime()+" "+sv);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				progress.addAndGet(6);
				System.out.println(DateUtil.getTime()+" "+i+" month sheet end...");
				countDownLatch.countDown();
			}
			System.out.println("-------------------------------------------------------");
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sheetExecutorService.shutdown();
		
		progress.compareAndSet(12*6, 100);// if (progress == 12month * 6) set progress = 100
		System.out.println(DateUtil.getTime()+" excel reday ");
		for (int i = 0; i < sheetcountv.size(); i++) {
			int m = i+1;
			System.out.println(DateUtil.getTime()+" "+m+" month/"+sheetcountv.get(i));
		}
	}
	
	public static void getDownProgress(){
		System.out.println("getDownProgress begin...");
		ScheduledExecutorService progressExecutorService = Executors.newScheduledThreadPool(1);
		ScheduledFuture<?> future = progressExecutorService.scheduleAtFixedRate(new Runnable() {// It begins in 0.5 seconds, and executes once every second.
			@Override
			public void run() {
				System.out.println("excel download progress:"+progress.get()+"%");
				if (progress.get() == 100) {
					System.out.println("excel download progress is 100%");
					progressExecutorService.shutdown();
				}
			}
		}, (long) 0.5, 1, TimeUnit.SECONDS);
		System.out.println("getDownProgress end...");
	}
}

class ExcelTask{
	private ReentrantLock lock = new ReentrantLock();
//	private Condition condition = lock.newCondition();
	
	private SheetTask sheetTask = new SheetTask();
	final int jiekouNum = 5;
	private CountDownLatch countDownLatch = new CountDownLatch(jiekouNum);// jiekou count down
	public Integer sheet(int month){
		List<Integer> vlist = new ArrayList<>();// put jiekou result
		ExecutorService jiekouExecutorService = Executors.newCachedThreadPool();
		for (int i = 1; i <= jiekouNum; i++) {
			System.out.println(DateUtil.getTime()+" "+month+" month sheet "+i+" jiekou begin...");
			Future<Integer> jiekouFuture = jiekouExecutorService.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					return sheetTask.jiekou("http://127.0.0.1:8080/v1/statistics/"+""+UUID.randomUUID().toString().replaceAll("-", "").substring(0,10));
				}
			});
			try {
				Integer v = jiekouFuture.get();
				System.out.println(DateUtil.getTime()+" "+month+" month sheet "+i+" jiekou return v:"+v);
				vlist.add(v);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				System.out.println(DateUtil.getTime()+" "+month+" month sheet "+i+" jiekou end...");
				countDownLatch.countDown();
			}
			System.out.println("------------------");
		}
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int vc = 0;
		for (Integer v : vlist) {
			vc+=v;
		}
		StringBuffer sb = new StringBuffer(month+" month result count:"+vc);
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.println(DateUtil.getTime()+" "+sb.toString());
		jiekouExecutorService.shutdown();
		jiekouExecutorService = null;
		return vc;
	}
	
}

class SheetTask{
	public int jiekou(String url){
		System.out.println(DateUtil.getTime()+" invoking url "+url+" begin...");
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			if (new Random().nextInt(100) == 99) {
//				throw new IllegalStateException();// Simulated small probability anomaly
//			}
		}
		System.out.println(DateUtil.getTime()+" invoking url "+url+" end...");
		return new Random().nextInt(100);
	}
}
