package com.tieasy;

// 线程组相关
public class Test4 {

	public static void main(String[] args) {

		ThreadGroup group1 = new ThreadGroup("group1");
//		ThreadGroup group2 = new ThreadGroup("group2");
		
		System.out.println("group1 activeCount"+group1.activeCount());
		
		Thread t1 = new Thread(group1, new Runnable() {
			@Override
			public void run() {
				System.out.println("A");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					System.out.println("what can i saut");
					e.printStackTrace();
					return;
				}
				System.out.println("AA");
			}
		}, "t1");
		t1.start();
		System.out.println("group1 activeCount"+group1.activeCount());
		
		Thread[] newlist = new Thread[group1.activeCount()];
		int c = group1.enumerate(newlist);
		System.out.println("c:"+c);
		System.out.println("newlist size:"+newlist.length);
		System.out.println("1MaxPriority:"+group1.getMaxPriority());
		System.out.println("parent threadgroup:"+group1.getParent().getName());
		group1.setDaemon(true);
		System.out.println("isDaemon:"+group1.isDaemon());
		System.out.println("isDestroyed:"+group1.isDestroyed());
		group1.interrupt();
		
		
//		Thread t2 = new Thread(group2, new Runnable() {
//			@Override
//			public void run() {
//				System.out.println("B");
//			}
//		}, "t1");
//		t2.start();
//		System.out.println("group1 activeCount"+group1.activeCount());
//		System.out.println("2MaxPriority:"+group2.getMaxPriority());
	}
}
