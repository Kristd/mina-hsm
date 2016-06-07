package com.cmbchina.mina.client;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmbchina.mina.abstracts.IoWorkManager;
import com.cmbchina.mina.enums.HsmStatus;
import com.cmbchina.mina.enums.SockStatus;
import com.cmbchina.mina.interfaces.factory.AppMngrFactory;
import com.cmbchina.mina.proto.HsmRequest;
import com.cmbchina.mina.proto.HsmResponse;
import com.cmbchina.mina.utils.GlobalVars;

public class HsmClient {
	private String m_ip;
	private int m_port;
	private int m_conn;
	private int m_maxconn;
	private int m_minconn;
	private int m_timeout;
	private HsmStatus m_status;
	private HsmSocketMngr m_socketManager;
	private String m_appname;
	private int m_weight;
	private final Object m_lock = new Object();
	private final Object m_statlock = new Object();
	private IoWorkManager m_workManager;
	private ConcurrentHashMap<String, HsmSession> m_sessionMap;
	private int m_sessionID;
	private boolean m_stop;
	private int m_nbusyConn;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HsmClient.class);
	
	
	private class StatThread extends Thread {
		public void run() {
			while(!m_stop) {
				for(HsmSocket sock : m_socketManager.getConnections()) {
					if(sock.getStatus() == SockStatus.BUSY) {
						m_nbusyConn++;
						if(m_nbusyConn == m_maxconn) {
							synchronized(m_statlock) {
								m_status = HsmStatus.BUSY;
							}
						}
						else {
							synchronized(m_statlock) {
								m_status = HsmStatus.FREE;
							}
						}
					}
				}
			}
		}
		
		public void begin() {
			m_stop = false;
		}
		
		public void end() {
			m_stop = true;
		}
	}
	
	public HsmClient() {
		return;
	}
	
	public HsmClient(String appname, String ip, int port, int timeout, int maxconn) {
		m_appname = appname;
		m_ip = ip;
		m_port = port;
		m_timeout = timeout;
		m_conn = maxconn;
		m_weight = 0;
		m_sessionID = 0x0000;
		m_status = HsmStatus.FREE;
		m_stop = false;
		m_socketManager = new HsmSocketMngr(m_appname, m_ip, m_port, m_timeout, m_conn);
		m_workManager = AppMngrFactory.loadWorkManager(m_appname);
	}
	
	public void start() throws Exception {
		m_socketManager.start(this);
	}
	
	public int request(IoSession requestSession, String jobname, String request) {
		//assume that we can get the connection randomly
		HsmSocket sock = m_socketManager.getConnection();
		
		
		
		String strtempId = String.format("000%s", Integer.toHexString(m_sessionID).toUpperCase());
		String strId = strtempId.substring(strtempId.length()-4);
		
		HsmSession tempSession = new HsmSession();
		tempSession.setSession(requestSession);
		tempSession.setSessionID(strId);
		tempSession.setJobName(jobname);
		
		if(m_sessionMap.containsKey(strId)) {
			m_sessionMap.replace(strId, tempSession);
		}
		else {
			m_sessionMap.put(strId, tempSession);
		}
		
		request = strId + request.substring(4, request.length());
		
		if(m_sessionID == GlobalVars.MAX_SESSION_ID) {
			m_sessionID = 0x0000;
		}
		else {
			m_sessionID += 1;
		}
		
		
		
		
		//process the request
		String _request_ = m_workManager.request(jobname, request);
		
		System.out.println("sending request=" + _request_);
		
		//send the reuqest to hsms
		return sock.process(_request_);
	}
	
	//need a hook to response
	public int response(Object response) {
		HsmSession session = m_sessionMap.get(response.toString().substring(0, 4));
		
			
		
		
		String _response_ = (String) m_workManager.response(session.getJobName(), response);
		session.getSession().write(_response_);

		
		
		System.out.println("sending response=" + _response_);
		
		return _response_.length();
	}
	
	//start a thread to monitor status
	public HsmStatus refresh() {
		return m_status;
	}
	
	public void setHsmStatus(HsmStatus stat) {
		m_status = stat;
	}
	
	public synchronized HsmStatus getStatus() {
			refresh();
			return m_status;
	}

	public String getAppName(String name) {
		return m_appname;
	}
	
	public synchronized void stop() {
		m_stop = true;
		m_socketManager.stop();
	}
}
