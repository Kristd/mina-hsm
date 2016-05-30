package com.cmbchina.mina.client;

public class PBOCHsmPool extends HsmClientPool {
	private String m_appname;
	
	private PBOCHsmPool() {
		super();
	}
	
	public boolean init(String appname) throws Exception {
		super.init();
		m_appname = appname;
		
		return true;
	}
	
	private static class InstanceHolder {
		static final HsmClientPool INSTANCE = new PBOCHsmPool();
	}
	
	public static HsmClientPool instance() {
		return InstanceHolder.INSTANCE;
	}
}
