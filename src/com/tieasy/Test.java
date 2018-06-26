package com.tieasy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Test {
	
	private static Vector<String> readyMembers = null;
	
	// qq飞车48人赛
	// 规则：48人参赛，6人一组，每组前3名为胜者，进入下一轮比赛
	public static void main(String[] args) {
		int allmember = 48;
		int teammember = 6;
		int r = 0;
		for (int n = 0; n < 4; n++) {
			r= n+1;
			int threadNum = 8/r;
			ExecutorService executorService = Executors.newCachedThreadPool();
			final CountDownLatch countDownLatch = new CountDownLatch(threadNum);
//			final Semaphore semaphore = new Semaphore(threadNum);
			RacingTask racingTask = null;
			int s = 0;
			System.out.println("========="+r+" round begin==========");
			Vector<String> temporaryMs = new Vector();
			for (int i = 0; i < threadNum; i++) {
				s=i+1;
				racingTask = new RacingTask(getMember(r),"T"+s,countDownLatch);
//				executorService.execute(racingTask);
				Future<Vector<String>> result = executorService.submit(racingTask);
				try {
					Vector<String> winners = result.get();
					if (winners != null) {
						temporaryMs.addAll(winners);
						System.out.println(getTime()+" size:"+winners.size());
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			System.out.println("========="+r+" round end==========");
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (readyMembers != null) {
				readyMembers.removeAllElements();
			}
			readyMembers.addAll(temporaryMs);
			executorService.shutdown();
		}
		System.out.println("The end of this 48 competition...");
	}
	
	public static Vector<String> getMember(int round){
		if (round == 1) {
			readyMembers = new Vector<>();
			for (int i = 0; i < 6; i++) {
				readyMembers.add(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10));
			}
			return readyMembers;
		}else{
			Vector<String> newms = null;
			if (readyMembers.size() > 0) {
				newms = new Vector<>(readyMembers.subList(0, 6));
				readyMembers.removeAll(newms);
			}
			return newms;
		}
	}
	
	public static String getTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
}

class RacingTask implements Callable<Vector<String>>{
//class RacingTask implements Runnable{
	private Vector<String> members; 
	private String tname;
	private CountDownLatch countDownLatch;
	
	public RacingTask(Vector<String> members, String tname,CountDownLatch countDownLatch){
		this.members = members;
		this.tname = tname;
		this.countDownLatch = countDownLatch;
	}
	
	@Override
	public Vector<String> call() throws Exception {
		Vector<String> winners = null;// put winner
		if (members != null) {
			System.out.println(getTime()+" "+tname+" begin...");
			try {
				Thread.sleep((new Random().nextInt(10)+1)*500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			StringBuffer sb = new StringBuffer("team:"+tname+" member,");
			for (String member : members) {
				sb.append(member).append("/");
			}
			String rinfo = sb.toString();
			rinfo = rinfo.substring(0,rinfo.length()-1);
			System.out.println(getTime()+" "+rinfo);
			
			winners = new Vector<>();
			winners.addAll(members.subList(0, 3));// For the sake of simplicity, take the first three members as winner
			
			System.out.println(getTime()+" "+tname+" end...");
		}
		countDownLatch.countDown();
		return winners;
	}

//	@Override
//	public void run() {
//		System.out.println(tname+" begin...");
//		try {
//			Thread.sleep((new Random().nextInt(10)+1)*500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		StringBuffer sb = new StringBuffer("team:"+tname+" member,");
//		for (String member : members) {
//			sb.append(member).append("/");
//		}
//		String rinfo = sb.toString();
//		rinfo = rinfo.substring(0,rinfo.length()-1);
//		System.out.println(rinfo);
//		
//		Vector<String> winners = new Vector<>();// put winner
//		winners.addAll(members.subList(0, 3));
//		
//		System.out.println(tname+" end...");
//		countDownLatch.countDown();
//	}
	
	public static String getTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
}
