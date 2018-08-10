package com.tieasy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// arraylist 速度快，但是线程不安全
// copyconwritelist  适合读多，写少的情况，它的线程安全是基于写的时候拷贝，它的数据只能保证最终一致性，不能保证及时一致性。
// synchronizedList  速度适中，线程安全
// 初始容量以及增加规律
public class Test9 {
	
	
	public static void main(String[] args) {
		ArrayList<String> alist = new ArrayList<>();
//		CopyOnWriteArrayList<String> alist = new CopyOnWriteArrayList<>();
//		List<String> alist = Collections.synchronizedList(alist1);
		System.out.println(DateUtil.getTime()+" begin");
		ExecutorService executorService = Executors.newCachedThreadPool();
		CountDownLatch countDownLatch = new CountDownLatch(3);
		for (int i = 0; i < 3; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < 20000; j++) {
//						System.out.println(DateUtil.getTime()+" size:"+alist.size()+", threadname:"+Thread.currentThread().getName());
						alist.add(UUID.randomUUID().toString());
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
		System.out.println(DateUtil.getTime()+" size:"+alist.size());
//		alist = null;
		System.out.println(DateUtil.getTime()+" end");
	}
	
	
}

