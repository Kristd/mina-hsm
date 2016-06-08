package com.hsm.mina.client;


public class HsmMessage {
	String m_messageId;
	String m_message;
	long m_startMillisecond;
	long m_endMillisecond;
	
	public HsmMessage() {
		m_messageId = "";
		m_message = "";
		m_startMillisecond = 0;
		m_endMillisecond = 0;
	}
	
	void setMessageID(String id) {
		m_messageId = id;
	}
	
	String getMessageID() {
		return m_messageId;
	}
	
	String getMessage() {
		return m_message;
	}
	
	void setMessage(String message) {
		m_message = message;
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
