package com.cmbchina.mina.interfaces.kmc;

import com.cmbchina.mina.abstracts.HsmWork;
import com.cmbchina.mina.interfaces.factory.KMCWorkMngr;

public class MakeMAC implements HsmWork {

	@Override
	public Object work(Object request) {
		System.out.println("MakeMAC work");
		return null;
	}

	public static void register() {
		KMCWorkMngr.addWork("MakeMAC", new MakeMAC());
	}

}
