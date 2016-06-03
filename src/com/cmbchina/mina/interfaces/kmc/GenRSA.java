package com.cmbchina.mina.interfaces.kmc;

import com.cmbchina.mina.abstracts.IoWork;
import com.cmbchina.mina.interfaces.factory.KMCWorkMngr;

public class GenRSA implements IoWork {
	public static void register() {
		KMCWorkMngr.addWork("GenRSA", new GenRSA());
	}
	
	@Override
	public String request(Object request) {
		return "NCMUEI0204801";
	}

	@Override
	public Object response(Object response) {
		return response.toString().substring(8, response.toString().length()-1);
	}

}
