package com.cmbchina.mina.conf;

import com.cmbchina.mina.json.JSONArray;
import com.cmbchina.mina.json.JSONObject;

public class PoolJSONConf extends JSONConf {
	private AppJSONArray m_apps;
	
	public PoolJSONConf() {
		m_apps = new AppJSONArray();
	}
	
	public class AppJSONArray {
		public JSONArray m_AppGrps;
		public HsmJSONObject m_hsms;
		
		public AppJSONArray() {
			m_hsms = new HsmJSONObject();
		}
		
		public String getAppName() {
			return m_AppGrps.getJSONObject(0).getString("appname");
		}
		
		public int getHsmSize() {
			return m_AppGrps.length()-1;
		}
		
		public HsmJSONObject HsmClinets(int n) {
			m_hsms.m_hsmClient = m_AppGrps.getJSONObject(n);
			return m_hsms;
		}
	}
	
	public class HsmJSONObject {
		public JSONObject m_hsmClient;
		
		public String ip() {
			return m_hsmClient.getString("ipaddr");
		}
		
		public int port() {
			return m_hsmClient.getInt("port");
		}
		
		public int maxConn() {
			return m_hsmClient.getInt("maxconn");
		}
		
		public int minConn() {
			return m_hsmClient.getInt("minconn");
		}
		
		public int timeout() {
			return m_hsmClient.getInt("timeout");
		}
	}
	
	public int getAppSize() throws Exception {
		return size();
	}
	
	public AppJSONArray apps(int n) {
		m_apps.m_AppGrps = m_array.getJSONArray(n);
		return m_apps;
	}
	
	public JSONObject HsmClients(int n) {
		return m_array.getJSONArray(n).getJSONObject(n);
	}
}
