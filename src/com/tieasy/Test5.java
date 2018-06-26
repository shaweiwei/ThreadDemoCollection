package com.tieasy;

// ThreadLocal理解
public class Test5 {

	public static void main(String[] args) {

		ThreadLocal<String> threshold = new ThreadLocal<>();
		
		threshold.set("13");
		System.out.println("C1:"+threshold.get());
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("A1:"+threshold.get());
				threshold.set("15");
				System.out.println("A2:"+threshold.get());
			}
		}).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("B1:"+threshold.get());
				threshold.set("16");
				System.out.println("B2:"+threshold.get());
			}
		}).start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("C2:"+threshold.get());
	}
}
