package com.cmbchina.mina.proto;

public class HsmResponse extends HsmRequest {
	protected String m_retcd;
	protected String m_response;
	
	public HsmResponse() {
		super();
		m_retcd = "";
		m_response = "";
	}
	
	public String getRetcd() {
		return m_retcd;
	}
	
	public String getResponse() {
		return m_response;
	}
}
