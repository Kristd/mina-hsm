package com.cmbchina.mina.abstracts;


public interface HsmWorkManager {	
	public String request(String jobname, Object request);
	public Object response(String jobname, String response);
}
