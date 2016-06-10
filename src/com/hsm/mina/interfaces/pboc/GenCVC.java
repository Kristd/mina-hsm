package com.hsm.mina.interfaces.pboc;

import com.hsm.mina.abstracts.IoWork;
import com.hsm.mina.interfaces.factory.PBOCWorkManager;

public class GenCVC implements IoWork {	
	public static void register() {
		PBOCWorkManager.addWork("GenCVC", new GenCVC());
	}

	@Override
	public String request(Object request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object response(Object response) {
		// TODO Auto-generated method stub
		return null;
	}
}
