package com.hnn.learnJava.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * https://javarevisited.blogspot.com/2014/10/how-to-use-locks-in-multi-threaded-java-program-example.html 
 * 13/01/2020
 */
public class LockDemo {

	public static void main(String[] args) {

		final Counter myCounter = new Counter(new ReentrantLock()); 
		
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
//				for (int i = 0; i < 20; i++) {
					System.out.printf("Count at thread %s is %d %n", Thread.currentThread().getName(), myCounter.getCount());
//				}
			}
		};
		
		Thread t1 = new Thread(r, "T1");
		Thread t2 = new Thread(r, "T2");
		Thread t3 = new Thread(r, "T3");
		
		t1.start();
		t2.start();
		t3.start();
	}
}

class Counter {
	private Lock lock; 
	private int count;
	
	public Counter(Lock lock) {
		this.lock = lock;
	}

	public final int getCount() {
		lock.lock();
		try {
			count++;
			return count;
		} finally {
			lock.unlock();
		}
//		try {
//			Thread.sleep(50);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		return count;
	} 
	
}
