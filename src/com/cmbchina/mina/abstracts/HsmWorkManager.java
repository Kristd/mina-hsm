package com.cmbchina.mina.abstracts;

import com.cmbchina.mina.proto.HsmRequest;
import com.cmbchina.mina.proto.HsmResponse;


public interface HsmWorkManager {	
	public HsmRequest request(String jobname, String request);
	public String response(String jobname, HsmResponse request);
}
