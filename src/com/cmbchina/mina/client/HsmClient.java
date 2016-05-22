package com.cmbchina.mina.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.cmbchina.mina.enums.Status;
import com.cmbchina.mina.interf.pboc.PbocWorkMngr;

public class HsmClient {
	private String m_ip;
	private int m_port;
	private int m_conn;
	private int m_maxconn;
	private int m_minconn;
	private int m_timeout;
	private Status m_status;
	private HsmSocketMngr m_hsmSockMngr;
	private String m_appName;
	private int m_weight;
	private final Object m_lock = new Object();
	private PbocWorkMngr m_pbocWkMngr;
	private HashMap<String, HsmWorkMngr> m_mngr;
	
	
	protected HsmClient(String appname, String ip, int port, int timeout, int conn_num) {
		m_appName = appname;
		m_ip = ip;
		m_port = port;
		m_timeout = timeout;
		m_conn = conn_num;
		m_weight = 0;
		m_status = Status.ERROR;
		m_hsmSockMngr = new HsmSocketMngr(m_appName, m_ip, m_port, m_timeout);
		m_mngr = new HashMap<String, HsmWorkMngr>();
	}
	
	public void start()	{
		m_hsmSockMngr.start();		
		m_status = setHsmStatus(m_hsmSockMngr.nfreeConnection());
		
		m_mngr.put("PBOC", m_pbocWkMngr);
	}

	/**
	 * 
	 * */
	public Object process(String job, Object request) {
		PbocWorkMngr mng = (PbocWorkMngr) m_mngr.get("PBOC");
		return mng.doWork(job, request);
	}
	
	public Object doWork(String job, Object request) {
		Object obj = process(job, request);
		//need a asyn control here
		m_hsmSockMngr.getClass();
		return null;
	}
	
	public void send(String buffer) {
		Object obj = null;
		Iterator<HsmSocket> it = connPool.iterator();
		while(it.hasNext())
		{
			HsmSocket conn = (HsmSocket)it.next();
			if(conn.getConnStatus() == HsmSocket.HSM_CONN_FREE)
			{
				obj = conn.SendAndRecv(buffer);
				getHsmStatus();
			}
		}
	}
	
	public Object recv() {
		return null;
	}
	
	public int refresh() {
		int free_num=0;
		int busy_num=0;
		float per = 0;
	
		Iterator<HsmSocket> it = connPool.iterator();
		while(it.hasNext())	{
			HsmSocket conn = (HsmSocket)it.next();
			if(conn.getConnStatus() == HsmSocket.HSM_CONN_FREE)
				free_num++;
			else if(conn.getConnStatus() == HsmSocket.HSM_CONN_BUSY)
				busy_num++;
			else if(conn.getConnStatus() == HsmSocket.HSM_CONN_ERR)
			{
				hsm_status = HSM_DOWN;
				return hsm_status;
			}
			
		}
		per = free_num/conn_num;
		if(per>0.5)
			hsm_status = HSM_FREE;
		else
			hsm_status = HSM_BUSY;
		
		return hsm_status;
	}
	
	public Status setHsmStatus(int nfree) {
		return Status.FREE;
	}
	
	public String getStatus() {
		synchronized(m_lock) {
			refresh();
			return m_status.toString();
		}
	}

	public String getAppName(String name) {
		return m_appName;
	}
}
