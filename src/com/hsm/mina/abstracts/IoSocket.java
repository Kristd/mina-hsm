package com.hsm.mina.abstracts;


public interface IoSocket {
	public boolean init() throws Exception;
	
	void setupLoggerFilter();
	
	void setupThreadsFilter();
	
	void setupCodecFilter();
	
	void setupKeepaliveFilter();
	
	public void close();
}
