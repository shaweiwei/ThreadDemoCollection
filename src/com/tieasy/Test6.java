package com.tieasy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// condition 实现 轮流打印A、B
public class Test6 {
	public static void main(String[] args) {
		TaskLock tl = new TaskLock();
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
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
		}
		executorService.shutdown();
	}
}

class TaskLock{
	private ReentrantLock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	private String flag = "A";
	public void printA(){
		try {
//			if(lock.tryLock()){ // 会出现java.lang.IllegalMonitorStateException
				lock.lock();
				while (!flag.equals("A")) {
					condition.await();
				}
				System.out.println("A");
				flag = "B";
				condition.signal();
//			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			// TODO: handle finally clause
			lock.unlock();
		}
	}
	public void printB(){
		try {
//			if(lock.tryLock()){
			lock.lock();
				while (!flag.equals("B")) {
					condition.await();
				}
				System.out.println("B");
				flag = "A";
				condition.signal();
//			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			// TODO: handle finally clause
			lock.unlock();
		}
	}
}
