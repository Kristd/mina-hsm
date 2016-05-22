package com.cmbchina.mina.interf.pboc;

import com.cmbchina.mina.client.HsmWork;

public class VerifyCVC implements HsmWork {
	public Object work(Object request) {
		System.out.println("VerifyCVC work");
		return null;
	}
	
	public static void register() {
		PbocWorkMngr.addWork("VerifyCVC", new VerifyCVC());
	}
}
