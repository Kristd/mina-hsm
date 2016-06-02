package com.cmbchina.mina.server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class InnerThread {
	private ConcurrentLinkedQueue<Integer> m_queue;
	private static Object m_lock = new Object();
	private checkQueue check; 
	boolean m_done = false;
	static boolean existing = false;

	public void init() {
		m_queue = new ConcurrentLinkedQueue<Integer>();
		check = new checkQueue();
		check.start();
	}
	
	private class checkQueue extends Thread {
		int bound = 5, index = 0;
		boolean m_run = true;
		
		public checkQueue() {
			super();
		}
		
		public void run() {
			while(m_run) {
				synchronized(m_lock) {
					if(m_queue.size() > 0) {
						Integer i = m_queue.poll();
						//m_queue.remove(0);
						System.out.println("get integer=" + i.intValue());
						
						if(existing) {
							while(m_queue.size() > 0) {
								Integer m = m_queue.poll();
								m_queue.remove(0);
								System.out.println("get integer=" + m.intValue());
							}
							
							m_done = true;
							existing = false;
						}
					}
				}
			}
		}
	}
	
	public void add(Integer i) {
		synchronized(m_lock) {
			m_queue.add(i);
			System.out.println("put integer=" + i.intValue());
		}
	}
	
	public void stop() {
		check.m_run = false;
	}
	
	public boolean isDone() {
		return m_done;
	}
	
	public static void main(String args[]) {
		int i = 1;
		InnerThread th = new InnerThread();
		th.init();
		
		while(true) {
			synchronized(m_lock) {
				th.add(new Integer(i));
				if(i == 100) {
					existing = true;
					break;
				}
				
				i++;
			}
		}
		
		while(true) {
			if(th.isDone()) {
				th.stop();
				System.out.println("main line stop");
				break;
			}
		}
	}
}
