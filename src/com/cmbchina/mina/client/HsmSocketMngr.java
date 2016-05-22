package com.cmbchina.mina.client;

import java.util.concurrent.ConcurrentHashMap;

import com.cmbchina.mina.enums.Status;


public class HsmSocketMngr {
	private String m_ip;
	private int m_port;
	private int m_conn;
	private int m_maxconn;
	private int m_minconn;
	private int m_timeout;
	private Status m_status;
	
	private ConcurrentHashMap<String, HsmSocket> m_conHashMap;
	
	
	public HsmSocketMngr(String appname, String ip, int port, int timeout) {
		;
	}
	
	/**
	 * Init all the connections
	 * */
	public void start() {
		;
	}
	
	public int nfreeConnection() {
		return 0;
	}
	
	public HsmSocket getConnection() {
		return null;
	}
}


