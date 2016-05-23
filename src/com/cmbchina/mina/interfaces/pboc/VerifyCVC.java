package com.cmbchina.mina.interfaces.pboc;

import com.cmbchina.mina.client.HsmWork;
import com.cmbchina.mina.interfaces.factory.PBOCWorkMngr;

public class VerifyCVC implements HsmWork {
	public Object work(Object request) {
		System.out.println("VerifyCVC work");
		return null;
	}
	
	public static void register() {
		PBOCWorkMngr.addWork("VerifyCVC", new VerifyCVC());
	}

	@Override
	public void register(String appname) {
		// TODO Auto-generated method stub
		//use factory mode
	}
}
