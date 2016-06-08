package com.hsm.mina.abstracts;

import com.hsm.mina.json.JSONArray;
import com.hsm.mina.json.JSONObject;

public abstract class IoJSONConf {
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
			throw new Exception("Objects not found");
		}
	}
}
