package com.tieasy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


// HashMap Hashtable ConcurrentHashMap
public class Test10 {
	
	
	public static void main(String[] args) {
//		HashMap<String, String> map = new HashMap<>();
//		Hashtable<String, String> map = new Hashtable<>();
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
		System.out.println(DateUtil.getTime()+" begin");
		ExecutorService executorService = Executors.newCachedThreadPool();
		CountDownLatch countDownLatch = new CountDownLatch(3);
		for (int i = 0; i < 3; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < 20000; j++) {
//						System.out.println(DateUtil.getTime()+" threadname:"+Thread.currentThread().getName()+",idx:"+j);
						map.put(""+j, UUID.randomUUID().toString());
					}
					countDownLatch.countDown();
				}
			});
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executorService.shutdown();
		System.out.println(DateUtil.getTime()+" size:"+map.size());
//		alist = null;
		System.out.println(DateUtil.getTime()+" end");
		
//		for (String key : map.keySet()) {
//			System.out.println(DateUtil.getTime()+" key:"+key);
//		}
	}
	
	
}

