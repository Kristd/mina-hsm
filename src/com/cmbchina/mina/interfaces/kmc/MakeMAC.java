package com.cmbchina.mina.interfaces.kmc;

import com.cmbchina.mina.abstracts.HsmWork;
import com.cmbchina.mina.interfaces.factory.KMCWorkMngr;
import com.cmbchina.mina.proto.HsmRequest;
import com.cmbchina.mina.proto.HsmResponse;

public class MakeMAC implements HsmWork {
	public static void register() {
		KMCWorkMngr.addWork("MakeMAC", new MakeMAC());
	}

	@Override
	public String request(Object request) {
		return "NCMUMA1580551B4E5427BC" + request;
	}

	@Override
	public Object response(String response) { 
		return response.substring(8, response.length()-1);
	}

}
