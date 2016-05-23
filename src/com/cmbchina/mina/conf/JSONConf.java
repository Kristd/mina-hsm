package com.cmbchina.mina.conf;

import com.cmbchina.mina.json.JSONArray;
import com.cmbchina.mina.json.JSONObject;

public abstract class JSONConf {
	protected JSONArray m_array = new JSONArray();
	protected JSONObject m_object = new JSONObject();
	
	public boolean loadObject(JSONArray array) {
		if(array == null) {
			return false;
		}
		
		m_array = array;
		return true;
	}
	
	public int size() {
		if(m_array != null) {
			return m_array.length();
		}
		else {
			return 0;
		}
	}
}
