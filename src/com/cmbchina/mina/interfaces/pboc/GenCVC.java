package com.cmbchina.mina.interfaces.pboc;

import com.cmbchina.mina.abstracts.HsmWork;
import com.cmbchina.mina.interfaces.factory.PBOCWorkMngr;
import com.cmbchina.mina.proto.HsmRequest;
import com.cmbchina.mina.proto.HsmResponse;

public class GenCVC implements HsmWork {	
	public static void register() {
		PBOCWorkMngr.addWork("GenCVC", new GenCVC());
	}

	@Override
	public String request(Object request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object response(String response) {
		// TODO Auto-generated method stub
		return null;
	}
}
