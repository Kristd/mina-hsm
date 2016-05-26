package com.cmbchina.mina.interfaces.factory;

import java.util.concurrent.ConcurrentHashMap;

import com.cmbchina.mina.abstracts.HsmWorkManager;


public class AppMngrFactory {
	private static final ConcurrentHashMap<String, HsmWorkManager> m_hashmap = new ConcurrentHashMap<String, HsmWorkManager>();
	
	static {
		//reflect;
	}
	
	public static HsmWorkManager loadWorkManager(String appName) {
		if(appName.equals("PBOC")) {
			return PBOCWorkMngr.instance();
		}
		else if(appName.equals("KMC")) {
			return KMCWorkMngr.instance();
		}
		else {
			return null;
		}
	}
}
