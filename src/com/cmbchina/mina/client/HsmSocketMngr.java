package com.cmbchina.mina.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HsmSocketMngr {
	private String m_ip;
	private int m_port;
	private int m_nConn;
	private int m_maxconn;
	private int m_minConn;
	private int m_timeout;
	private String m_appname;
	private ArrayList<HsmSocket> m_connections;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HsmSocketMngr.class);
	
	
	public HsmSocketMngr(String appname, String ip, int port, int timeout, int maxconn) {
		m_appname = appname;
		m_ip = ip;
		m_port = port;
		m_timeout = timeout;
		m_maxconn = maxconn;
		m_connections = new ArrayList<HsmSocket>();
	}
	
	public boolean init(HsmClient host) throws Exception {
		for(int i = 0; i < m_maxconn; i++) {
			HsmSocket sock = new HsmSocket(host, m_ip, m_port, m_timeout);
			sock.init();
			m_connections.add(sock);
		}
			
		m_nConn = m_maxconn;
		return true;
	}
	
	public boolean start(HsmClient host) throws Exception {
		if(init(host)) {
			try {
				for(int i = 0; i < m_connections.size(); i++){
					HsmSocket sock = m_connections.get(i);
					if(sock != null) {
						sock.connect();
					}
				}

				return true;
			}
			catch(Exception ex) {
				stop();	
			}
		}
		
		return false;
	}
	
	public int freeConnection() {
		return 0;
	}
	
	public HsmSocket getConnection() {
		return m_connections.get(0);
	}
	
	public ArrayList<HsmSocket> getConnections() {
		return m_connections;
	}
	
	public HsmSocket route() {
		return null;
	}
	
	public void stop() {
		for(HsmSocket sock : m_connections){
			sock.disconnect();
		}
		
		m_nConn = 0;
	}
}


