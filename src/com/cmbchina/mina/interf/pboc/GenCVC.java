package com.cmbchina.mina.interf.pboc;

import com.cmbchina.mina.client.HsmWork;

public class GenCVC implements HsmWork {
	public Object work(Object request) {
		System.out.println("GenCVC work");
		return null;
	}
	
	public static void register() {
		PbocWorkMngr.addWork("GenCVC", new GenCVC());
	}
}
