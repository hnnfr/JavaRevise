package com.hnn.learnJava.concurrency;

/**
 * Wrong example: the synchronized block releases lock after getting an exception, but not hold on lock.  
 * https://javarevisited.blogspot.com/2011/04/synchronization-in-java-synchronized.html
 * 29/11/2020
 */
public class HoldingLockWhenException {
	private boolean isLocked = false;

	private class Foo implements Runnable {
		public void run() {
			synchronized (HoldingLockWhenException.this) {
				System.out.println(Thread.currentThread().getName() + " acquired the Lock on outer this");
				if (!isLocked)
					throw new IllegalThreadStateException();

				isLocked = true;
				HoldingLockWhenException.this.notify();
			}
		}
	}

	private class Bar implements Runnable {
		public void run() {
			synchronized (HoldingLockWhenException.this) {
				System.out.println(Thread.currentThread().getName() + " acquired the Lock on outer this");
				while (!isLocked) {
					try {
						HoldingLockWhenException.this.wait(500);
						System.out.println(Thread.currentThread().getName() + " is waiting...");
					} catch (InterruptedException interx) {
					}
				}
				System.out.println(Thread.currentThread().getName() + " process further");
			}
		}
	}

	public static void main(String[] argv) {
		HoldingLockWhenException outer = new HoldingLockWhenException();
		Thread run1 = new Thread(outer.new Foo(), "Foo thread");
		Thread run2 = new Thread(outer.new Bar(), "Bar thread");
		run1.start();
		run2.start();
	}
}
