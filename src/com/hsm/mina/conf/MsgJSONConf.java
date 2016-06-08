package com.hsm.mina.conf;

import com.hsm.mina.abstracts.IoJSONConf;
import com.hsm.mina.utils.GlobalVars;

public class MsgJSONConf extends IoJSONConf {
	public int getContentLength(String keyname) {
		int res = m_array.getJSONObject(0).getInt(keyname);
		
		if(res == -1) {
			return GlobalVars.MAX_FIELD_LENGTH;
		}
		else {
			return res;
		}
	}
}
