package com.hnn.learnJava.concurrency;

public class DeadLockDemo {

	public static void main(String[] args) {
		final DeadLockMethods lockMethods = new DeadLockMethods();
		Runnable r1 = new Runnable() {
			
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + " - Call method1 then method2");
				lockMethods.method1();
				lockMethods.method2();
			}
		};
		Runnable r2 = new Runnable() {
			
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + " - Call method2 then method1");
				lockMethods.method2();
				lockMethods.method1();
			}
		};
		
		Thread t1 = new Thread(r1, "T1");
		Thread t2 = new Thread(r1, "T2");
		Thread t3 = new Thread(r2, "T3");
		Thread t4 = new Thread(r2, "T4");
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		try {
			t1.join();
			t2.join();
			t3.join();
			t4.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("End OK");
	}

}

class DeadLockMethods {
	public void method1() {
		synchronized (String.class) {
			System.out.println("method1 - acquired lock on String.class object");
			synchronized (Integer.class) {
				System.out.println("method1 - acquired lock on Integer.class object");
			}
		}
	}
	
	public void method2() { 
		// this causes dead lock when called at the same time with method1
//		synchronized (Integer.class) {
//			System.out.println("method2 - acquired lock on Integer.class object");
//			synchronized (String.class) {
//				System.out.println("method2 - acquired lock on String.class object");
//			}
//		}
		
		// to correct the problem of Dead Lock
		synchronized (String.class) {
			System.out.println("method2 - acquired lock on Integer.class object");
			synchronized (Integer.class) {
				System.out.println("method2 - acquired lock on String.class object");
			}
		}
	}
}