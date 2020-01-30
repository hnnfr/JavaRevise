package com.hnn.learnJava.concurrency;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * https://javarevisited.blogspot.com/2012/07/cyclicbarrier-example-java-5-concurrency-tutorial.html
 * 28/01/2020
 */
public class CyclicBarrierDemo {

	private static class Task implements Runnable {

		private CyclicBarrier barrier; 
		
		public Task(CyclicBarrier cyclicBarrier) {
			this.barrier = cyclicBarrier;
		}

		@Override
		public void run() {
			try {
				System.out.println(Thread.currentThread().getName() + " is waiting on barrier");
				Thread.sleep(200);
				barrier.await();
				System.out.println(Thread.currentThread().getName() + " has crossed the barrier");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		
		int n = 30;
		
		CyclicBarrier cb = new CyclicBarrier(n, new Runnable() {
			
			@Override
			public void run() {
				System.out.println("All parties are arrived at barrier, lets play");
			}
		});
		
		for (int i = 0; i < n; i++) {
			new Thread(new Task(cb), "Thread " + i).start();
		}
		
//		Thread t1 = new Thread(new Task(cb), "Thread 1");
//		Thread t2 = new Thread(new Task(cb), "Thread 2");
//		Thread t3 = new Thread(new Task(cb), "Thread 3");
//		
//		t1.start();
//		t2.start();
//		t3.start();
	}
}
