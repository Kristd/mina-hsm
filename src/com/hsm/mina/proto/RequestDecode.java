package com.hsm.mina.proto;

import com.hsm.mina.conf.MsgJSONConf;

public class RequestDecode {
	private MsgJSONConf m_msgconf;
	private int m_break;
	
	public RequestDecode() {
		m_msgconf = new MsgJSONConf();
	}
	
	public String getStringField(String message, String keyname) {
		int length = m_msgconf.getContentLength(keyname);
		return message.substring(m_break, length);
	}
	
	public String getIntField(String message, String keyname) {
		int length = m_msgconf.getContentLength(keyname);
		return message.substring(m_break, length);
	}
}
