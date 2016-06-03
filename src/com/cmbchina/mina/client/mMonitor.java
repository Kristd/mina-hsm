package com.cmbchina.mina.client;

import java.util.Observable;

public class mMonitor extends Observable {
	public static void main(String args[]) throws InterruptedException {
		mMonitor n = new mMonitor();
		mObserver m = new mObserver();
		n.addObserver(m);
		n.setChanged();
		n.notifyObservers("hello observer");
		n.clearChanged();
		System.out.println("end");
		
		Thread.sleep(10000);
	}
}
