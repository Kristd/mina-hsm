package com.cmbchina.mina.proto;

public class HsmRequest {
	protected String m_header;
	protected String m_command;
	protected String m_data;
	protected String m_request;
	
	public HsmRequest() {
		m_header = "";
		m_command = "";
		m_data = "";
		m_request = "";
	}
	
	public String getHeader() {
		return m_header;
	}
	
	public String getCommand() {
		return m_command;
	}
	
	public String getData() {
		return m_data;
	}
	
	public String getRequest() {
		return m_request;
	}
}
