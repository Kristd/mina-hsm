package com.hsm.mina.abstracts;


public interface IoWork {
	
	public String request(Object request);
	
	public Object response(Object response);
}
