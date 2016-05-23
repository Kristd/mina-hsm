package com.cmbchina.mina.conf;

public class SocketJSONConf extends JSONConf {	
	public String getSocketAddr() {
		try {
			return m_object.getString("ip");
		}
		catch(Exception ex) {
			return "";
		}
	}
	
	public int getSocketPort() {
		try {
			return Integer.parseInt(m_object.getString("port"));
		}
		catch(Exception ex) {
			return 0;
		}
	}
	
	public int getSocketMaxConn() {
		try {
			return Integer.parseInt(m_object.getString("maxconn"));
		}
		catch(Exception ex) {
			return 0;
		}
	}
}
