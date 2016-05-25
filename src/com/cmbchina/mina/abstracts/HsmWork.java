package com.cmbchina.mina.abstracts;

import com.cmbchina.mina.proto.HsmRequest;
import com.cmbchina.mina.proto.HsmResponse;


public interface HsmWork {
	public HsmRequest request(Object request);
	public String response(HsmResponse response);
}
