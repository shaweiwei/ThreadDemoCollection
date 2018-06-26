package com.tieasy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// 死锁
public class Test7 {
	public static void main(String[] args) {
		TaskLock1 tl = new TaskLock1();
		ExecutorService executorService = Executors.newCachedThreadPool();
//		for (int i = 0; i < 10; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					tl.printA();
				}
			});
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					tl.printB();
				}
			});
//		}
		executorService.shutdown();
	}
}

class TaskLock1{
	private ReentrantLock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	public void printA(){
		try {
			System.out.println("A1");
			lock.lock();
			condition.await();
			System.out.println("A2");
			condition.signal();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			// TODO: handle finally clause
			lock.unlock();
		}
	}
	public void printB(){
		try {
			System.out.println("B1");
			lock.lock();
			condition.await();
			System.out.println("B2");
			condition.signal();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			// TODO: handle finally clause
			lock.unlock();
		}
	}
}
