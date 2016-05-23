package com.cmbchina.mina.server;

import java.io.File;
import java.io.FileInputStream;
import com.cmbchina.mina.utils.MiscUtil;


public class ResourceMngr {
	private static String m_configPath;
	
	static {
		m_configPath = System.getProperty("app.etc");
		if(m_configPath == null) {
			m_configPath = System.getProperty("user.dir") + File.separator + "etc"	+ File.separator;
		}
		else {
			if(!m_configPath.endsWith(File.separator)) {
				m_configPath += m_configPath + File.separator;
			}
		}
	}
	
	public static String getServiceConfigPath() {
		return m_configPath;
	}
	
	public static String getServiceConfigData(String fileName) throws Exception {
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(new File(getServiceConfigPath() + fileName));
			return MiscUtil.readTextFileContent(fs);
		}
		finally {
			if(fs != null) {
				fs.close();
				fs = null;
			}
		}
	}
}
