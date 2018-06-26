package com.tieasy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// object的wait notify 实现 交替打印 A和B
public class Test2 {

	public static void main(String[] args) {
		
		
		TaskTool taskTool = new TaskTool();
		
//		ExecutorService executorService = Executors.newFixedThreadPool(2);
//		executorService.execute(new TaskRuna(lock,flag));
//		executorService.execute(new TaskRunb(lock,flag));
//		executorService.shutdown();
		
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					taskTool.printA();
				}
			}).start();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					taskTool.printB();
				}
			}).start();
		}
		
		
	}
}

class TaskTool {
	
	private String flag = "A";
	
	public synchronized void printA(){
		while (!flag.equals("A")) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("A");
		flag = "B";
		notifyAll();
	}
	
	public synchronized void printB(){
		while (!flag.equals("B")) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("B");
		flag = "A";
		notifyAll();
	}
	
}

