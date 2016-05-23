package com.cmbchina.mina.client;

import java.util.concurrent.ConcurrentLinkedQueue;


public class HsmSocketMngr {
	private String m_ip;
	private int m_port;
	private int m_nConn;
	private int m_maxconn;
	private int m_minConn;
	private int m_timeout;
	private String m_appname;
	private ConcurrentLinkedQueue<HsmSocket> m_connectionQueue;
	
	
	public HsmSocketMngr(String appname, String ip, int port, int timeout) {
		m_appname = appname;
		m_ip = ip;
		m_port = port;
		m_timeout = timeout;
	}
	
	public boolean init() {
		try {
			for(int i = 0; i < m_minConn; i++) {
				HsmSocket sock = new HsmSocket(m_ip, m_port, m_timeout);
				sock.init();
				m_connectionQueue.add(sock);
			}
			
			m_nConn = m_minConn;
			return true;
		}
		catch(Exception ex) {
			return false;
		}
	}
	
	public boolean start() {
		if(init()) {
			try {
				for(HsmSocket sock : m_connectionQueue){
					sock.connect();
				}
				
				return true;
			}
			finally {
				dispose();
			}
		}
		
		return false;
	}
	
	public int nfreeConnection() {
		return 0;
	}
	
	public HsmSocket getConnection() {
		return null;
	}
	
	public void dispose() {
		;
	}
}


