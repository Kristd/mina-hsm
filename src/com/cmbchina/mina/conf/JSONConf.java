package com.cmbchina.mina.conf;

import com.cmbchina.mina.json.JSONArray;
import com.cmbchina.mina.json.JSONObject;

public abstract class JSONConf {
	protected JSONArray m_array;
	protected JSONObject m_object;
	
	public boolean loadObject(JSONArray array) {
		if(array == null) {
			return false;
		}
		
		m_array = array;
		return true;
	}
	
	public int size() throws Exception {
		if(m_array != null) {
			return m_array.length();
		}
		else {
			throw new Exception("HSM application not found");
		}
	}
}
