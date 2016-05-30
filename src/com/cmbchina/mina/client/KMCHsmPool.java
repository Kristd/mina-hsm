package com.cmbchina.mina.client;

import java.util.Map;

public class KMCHsmPool extends HsmClientPool {
	private static String m_appname;
	private static Map<String, HsmClient> m_hashMap;
	
	private KMCHsmPool() {
		super();
		
		try {
			super.init();
		}
		catch(Exception ex) {
			;
		}
	}
	
	public static boolean init(String appname) throws Exception {
		m_appname = appname;
		m_hashMap = m_appGrp.get(m_appname);
		
		return true;
	}
	
	public static boolean start() throws Exception {
		for(HsmClient client : m_hashMap.values()) {
			client.start();
		}
		
		return true;
	}
	
	private static class InstanceHolder {
		static final HsmClientPool INSTANCE = new KMCHsmPool();
	}
	
	public static HsmClientPool instance() {
		return InstanceHolder.INSTANCE;
	}
}
