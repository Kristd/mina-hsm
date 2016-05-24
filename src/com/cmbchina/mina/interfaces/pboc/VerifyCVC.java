package com.cmbchina.mina.interfaces.pboc;

import com.cmbchina.mina.abstracts.HsmWork;
import com.cmbchina.mina.interfaces.factory.PBOCWorkMngr;

public class VerifyCVC implements HsmWork {
	
	@Override
	public Object work(Object request) {
		System.out.println("VerifyCVC work");
		return null;
	}
	
	public static void register() {
		PBOCWorkMngr.addWork("VerifyCVC", new VerifyCVC());
	}
}
