package com.hsm.mina.client;

import org.apache.mina.core.session.IoSession;

public class HsmSession {
	String m_sessionID;
	IoSession m_session;
	long m_startMillisecond;
	long m_endMillisecond;
	String m_jobname;
	
	public HsmSession() {
		m_sessionID = "";
		m_session = null;
		m_jobname = "";
		m_startMillisecond = 0;
		m_endMillisecond = 0;
	}
	
	void setJobName(String jobname) {
		m_jobname = jobname;
	}
	
	String getJobName() {
		return m_jobname;
	}
	
	
	void setSessionID(String id) {
		m_sessionID = id;
	}
	
	String getSessionID() {
		return m_sessionID;
	}
	
	IoSession getSession() {
		return m_session;
	}
	
	void setSession(IoSession session) {
		m_session = session;
	}
	
	void setStartTimeMS(long millisecond) {
		m_startMillisecond = millisecond;
	}
	
	void setEndTimeMS(long millisecond) {
		m_endMillisecond = millisecond;
	}
	
	long responseConsume() {
		return m_endMillisecond - m_startMillisecond;
	}
}
