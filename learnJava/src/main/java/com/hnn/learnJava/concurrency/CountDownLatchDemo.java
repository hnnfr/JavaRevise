package com.hnn.learnJava.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * https://javarevisited.blogspot.com/2012/07/countdownlatch-example-in-java.html 
 * 15/01/2020
 */
public class CountDownLatchDemo {
	
	public static void main(String[] args) {
		CountDownLatch latch = new CountDownLatch(3);
		int  startingTime = 1300;
		Thread cacheService = new Thread(new Service("CacheService", startingTime, latch));
		Thread alertService = new Thread(new Service("AlertService", startingTime, latch));
		Thread validationService = new Thread(new Service("ValidationService", startingTime, latch));
		
		cacheService.start();
		alertService.start();
		validationService.start();
		
		// application should not start processing any thread until all service is up and ready to do there job.
		// Countdown latch is idle choice here, main thread will start with count 3 
		// and wait until count reaches zero. Each thread once up and read will do
		// a count down. This will ensure that main thread is not started processing
		// until all services is up.
		
		try {
//			latch.await();
			latch.await(5, TimeUnit.SECONDS);
			System.out.println("All services are up, Application is starting now...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class Service implements Runnable {

	private String name; 
	private int timeToStart; 
	private CountDownLatch latch;
	
	
	public Service(String name, int timeToStart, CountDownLatch latch) {
		this.name = name;
		this.timeToStart = timeToStart;
		this.latch = latch;
	}


	@Override
	public void run() {
		try {
			Thread.sleep(timeToStart);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(name + " is Up!");
		latch.countDown();
	}
}
