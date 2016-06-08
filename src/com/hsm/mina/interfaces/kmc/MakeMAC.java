package com.hsm.mina.interfaces.kmc;

import com.hsm.mina.abstracts.IoWork;
import com.hsm.mina.interfaces.factory.KMCWorkMngr;

public class MakeMAC implements IoWork {
	public static void register() {
		KMCWorkMngr.addWork("MakeMAC", new MakeMAC());
	}

	@Override
	public String request(Object request) {
		return "NCMUMA1580551B4E5427BC" + request;
	}

	@Override
	public Object response(Object response) { 
		return response.toString().substring(8, response.toString().length()-1);
	}

}
