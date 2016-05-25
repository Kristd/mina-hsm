package com.cmbchina.mina.conf;

import com.cmbchina.mina.utils.GlobalVars;

public class SocketJSONConf extends JSONConf {
	private static final int SOCKET_INDEX = 0;
	private static final int ENCODING_INDEX = 1;
	private static final int THREAD_INDEX = 2;
	private static final int KEEPALIVE_INDEX = 3;
	
	public boolean logger() {
		try {
			return true;
		}
		catch(Exception ex) {
			return false;
		}
	}
	
	public String socketAddr() {
		try {
			return m_array.getJSONObject(SOCKET_INDEX).getString("ip");
		}
		catch(Exception ex) {
			return GlobalVars.DEFAULT_IPADDR;
		}
	}
	
	public int socketPort() {
		try {
			return m_array.getJSONObject(SOCKET_INDEX).getInt("port");
		}
		catch(Exception ex) {
			return GlobalVars.DEFAULT_PORT;
		}
	}
	
	public int socketMaxConn() {
		try {
			return m_array.getJSONObject(SOCKET_INDEX).getInt("maxconn");
		}
		catch(Exception ex) {
			return GlobalVars.DEFAULT_MAX_CONNECT;
		}
	}
	
	public int socketIdleTime() {
		try {
			return m_array.getJSONObject(SOCKET_INDEX).getInt("idletime");
		}
		catch(Exception ex) {
			return GlobalVars.DEFAULT_IDLE_TIME;
		}
	}
	
	public int socketBufferSize() {
		try {
			return m_array.getJSONObject(SOCKET_INDEX).getInt("buffersize");
		}
		catch(Exception ex) {
			return GlobalVars.MAX_BUFF_SIZE;
		}
	}
	
	public int socketTimeout() {
		try {
			return m_array.getJSONObject(SOCKET_INDEX).getInt("timeout");
		}
		catch(Exception ex) {
			return GlobalVars.TIMEOUT;
		}
	}
	
	public boolean codeflter() {
		try {
			return m_array.getJSONObject(ENCODING_INDEX).getBoolean("codefilter");
		}
		catch(Exception ex) {
			return GlobalVars.DEFAULT_BOOLEAN;
		}
	}
	
	public String codeflterEncoding() {
		try {
			return m_array.getJSONObject(ENCODING_INDEX).getString("encoding");
		}
		catch(Exception ex) {
			return "";
		}
	}
	
	public int codeflterEncodePrefix() {
		try {
			return m_array.getJSONObject(ENCODING_INDEX).getInt("encodeprefix");
		}
		catch(Exception ex) {
			return 0;
		}
	}
	
	public int codeflterDecodePrefix() {
		try {
			return m_array.getJSONObject(ENCODING_INDEX).getInt("decodeprefix");
		}
		catch(Exception ex) {
			return 0;
		}
	}
	
	public boolean threadPool() {
		try {
			return m_array.getJSONObject(THREAD_INDEX).getBoolean("threadpool");
		}
		catch(Exception ex) {
			return GlobalVars.DEFAULT_BOOLEAN;
		}
	}
	
	public String threadPoolType() {
		try {
			return m_array.getJSONObject(THREAD_INDEX).getString("pooltype");
		}
		catch(Exception ex) {
			return "";
		}
	}
	
	public boolean keepalive() {
		try {
			return m_array.getJSONObject(KEEPALIVE_INDEX).getBoolean("keepalive");
		}
		catch(Exception ex) {
			return GlobalVars.DEFAULT_BOOLEAN;
		}
	}
	
	public int keepaliveTimeout() {
		try {
			return m_array.getJSONObject(KEEPALIVE_INDEX).getInt("timeout");
		}
		catch(Exception ex) {
			return GlobalVars.DEFAULT_TIMEOUT_SECOND;
		}
	}
	
	public int keepaliveInterval() {
		try {
			return m_array.getJSONObject(KEEPALIVE_INDEX).getInt("interval");
		}
		catch(Exception ex) {
			return GlobalVars.DEFAULT_INTERVAL_SECOND;
		}
	}
	
	public boolean keepaliveForward() {
		try {
			return m_array.getJSONObject(KEEPALIVE_INDEX).getBoolean("forward");
		}
		catch(Exception ex) {
			return GlobalVars.DEFAULT_BOOLEAN;
		}
	}
}
