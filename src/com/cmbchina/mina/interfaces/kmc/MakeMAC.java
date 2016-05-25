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
	public HsmRequest request(Object request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String response(HsmResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
