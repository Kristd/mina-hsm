package com.hsm.mina.interfaces.kmc;

import com.hsm.mina.abstracts.IoWork;
import com.hsm.mina.interfaces.factory.KMCWorkManager;

public class GenRSA implements IoWork {
	public static void register() {
		KMCWorkManager.addWork("GenRSA", new GenRSA());
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
