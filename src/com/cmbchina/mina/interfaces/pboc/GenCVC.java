package com.cmbchina.mina.interfaces.pboc;

import com.cmbchina.mina.abstracts.HsmWork;
import com.cmbchina.mina.interfaces.factory.PBOCWorkMngr;

public class GenCVC implements HsmWork {
	
	@Override
	public Object work(Object request) {
		System.out.println("GenCVC work");
		return null;
	}
	
	public static void register() {
		PBOCWorkMngr.addWork("GenCVC", new GenCVC());
	}
}
