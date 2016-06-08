package com.hsm.mina.client;

import java.util.Observable;
import java.util.Observer;

public class mObserver implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof String) {
			System.out.println(arg.toString());
		}
		
		System.out.println("wake up by monitor");
	}

}
