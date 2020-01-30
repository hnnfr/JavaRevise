package com.hnn.learnJava.concurrency;

/**
 * https://www.java67.com/2015/07/how-to-join-two-threads-in-java-example.html
 * 13/01/2020
 */
public class JoinDemo {

	private static class ParallelTask implements Runnable {

		private Thread predecessor; 
		
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName() + " started.");
			if (predecessor != null) {
				try {
					predecessor.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(Thread.currentThread().getName() + " finished.");
		}

		public void setPredecessor(Thread predecessor) {
			this.predecessor = predecessor;
		}
		
	}
	
	public static void main(String[] args) {

		ParallelTask task1 = new ParallelTask();
		ParallelTask task2 = new ParallelTask(); 
		ParallelTask task3 = new ParallelTask();
		
		final Thread t1 = new Thread(task1, "Thread 1");
		final Thread t2 = new Thread(task2, "Thread 2");
		final Thread t3 = new Thread(task3, "Thread 3");
		
		task2.setPredecessor(t1);
		task3.setPredecessor(t2);
		
		t1.start();
		t2.start();
		t3.start();
	}

}
