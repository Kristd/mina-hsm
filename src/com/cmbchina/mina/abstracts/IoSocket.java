package com.cmbchina.mina.abstracts;


public abstract class IoSocket {
	public boolean init() throws Exception {
		return false;
	}
	
	protected void setupLoggerFilter() {
		;
	}
	
	protected void setupThreadsFilter() {
		;
	}
	
	protected void setupCodecFilter() {
		;
	}
	
	protected void setupKeepaliveFilter() {
		;
	}
	
	public void close() {
		;
	}
}
