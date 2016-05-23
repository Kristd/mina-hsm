package com.cmbchina.mina.conf;

import com.cmbchina.mina.json.JSONArray;
import com.cmbchina.mina.json.JSONObject;

public class PoolJSONConf extends JSONConf {
	private AppJSONArray m_apps;
	
	public class AppJSONArray {
		public JSONArray m_AppGrps;
		public HsmJSONObject m_hsms;
		
		public String getAppName() {
			return m_AppGrps.getString(0);
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
		
		public String Address() {
			return m_hsmClient.getString("ipaddr");
		}
		
		public int Port() {
			return m_hsmClient.getInt("port");
		}
		
		public int MaxConn() {
			return m_hsmClient.getInt("maxconn");
		}
		
		public int MinConn() {
			return m_hsmClient.getInt("minconn");
		}
	}
	
	public int getAppSize() {
		return size();
	}
	
	public AppJSONArray Apps(int n) {
		m_apps.m_AppGrps = m_array.getJSONArray(n);
		return m_apps;
	}
	
	public JSONObject HsmClients(int n) {
		return m_array.getJSONArray(n).getJSONObject(n);
	}
}
