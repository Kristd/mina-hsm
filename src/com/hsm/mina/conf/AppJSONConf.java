package com.hsm.mina.conf;

import com.hsm.mina.abstracts.IoJSONConf;

public class AppJSONConf extends IoJSONConf {
	public String getClassPath(int n) {
		return m_array.getString(n).split(":")[1];
	}
	
	public String getClassName(int n) {
		return m_array.getString(n).split(":")[0];
	}
}
