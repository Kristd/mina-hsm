package com.cmbchina.mina.interfaces.factory;

import com.cmbchina.mina.abstracts.HsmWorkManager;


public class WorkMngrFactory {	
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
