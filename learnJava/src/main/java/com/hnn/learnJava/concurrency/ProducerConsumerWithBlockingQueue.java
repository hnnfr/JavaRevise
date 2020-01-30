package com.hnn.learnJava.concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * https://javarevisited.blogspot.com/2012/02/producer-consumer-design-pattern-with.html
 * 28/01/2020
 * https://javarevisited.blogspot.com/2011/10/how-to-stop-thread-java-example.html
 * 28/01/2020
 * Question: How to tell Consumer to stop when there is no more to consume (Producer is stopped) ?
 * => Answer: Using BlockingQueue.poll(timeout, unit)
 * or do it manually by double-check flag exitNow and wait a moment between 
 */
public class ProducerConsumerWithBlockingQueue {
	
	public static void main(String[] args) {
		BlockingQueue<Integer> sharedQueue = new LinkedBlockingDeque<Integer>(); 
		
		Consumer consumer = new Consumer(sharedQueue);
		Thread producerThread = new Thread(new Producer(sharedQueue), "Producer Thread");
		Thread consumerThread = new Thread(consumer, "Consumer Thread");
		
		producerThread.start();
		consumerThread.start();
		
		try {
			producerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Producer thread finished. Stop the Consumer thread.");
		consumer.setExitNow(true);
	}
}

class Producer implements Runnable {

	private final BlockingQueue<Integer> sharedQueue; 
	
	public Producer(BlockingQueue<Integer> sharedQueue) {
		this.sharedQueue = sharedQueue;
	}

	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			try {
				System.out.println("Produced " + i);
				sharedQueue.put(i);
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}

class Consumer implements Runnable {

	private final BlockingQueue<Integer> sharedQueue;
	
	private volatile boolean exitNow = false;
	
	public Consumer(BlockingQueue<Integer> sharedQueue) {
		this.sharedQueue = sharedQueue;
	}

	public void setExitNow(boolean exitNow) {
		this.exitNow = exitNow;
		System.out.println("exitNow = " + this.exitNow);
	}

	@Override
	public void run() {

		while (!exitNow) {
			try {
				System.out.println("exitNow = " + exitNow);
				// use this to stop Consumer
				System.out.println("Consumed: " + sharedQueue.poll(10, TimeUnit.MILLISECONDS));
				
				// or use this to stop Consumer
//				Thread.sleep(20);
//				if (!exitNow) {
//					System.out.println("Consumed: " + sharedQueue.take());
//				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}