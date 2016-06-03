package com.cmbchina.mina.abstracts;


public interface IoWorkManager {
	
	public String request(String jobname, Object request);
	
	public Object response(String jobname, Object response);
}
