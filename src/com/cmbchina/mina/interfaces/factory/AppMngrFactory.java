package com.cmbchina.mina.interfaces.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cmbchina.mina.abstracts.IoWorkManager;
import com.cmbchina.mina.client.HsmClientPool;
import com.cmbchina.mina.conf.AppJSONConf;
import com.cmbchina.mina.server.ResourceMngr;
import com.cmbchina.mina.utils.GlobalVars;
import com.cmbchina.mina.utils.JSONUtil;


public class AppMngrFactory {
	private static final Map<String, IoWorkManager> m_hashmap = new ConcurrentHashMap<String, IoWorkManager>();
	private static AppJSONConf m_appconf;
	
	static {
		m_appconf = new AppJSONConf();
	}
	
	public static boolean init() throws Exception {
		m_appconf.loadObject(JSONUtil.parserJSONArray(ResourceMngr.getServiceConfigData(GlobalVars.APP_CFG)));
		
		for(int i = 0; i < m_appconf.size(); i++) {
			String appname = m_appconf.getClassName(i);
			String classPath = m_appconf.getClassPath(i);
			
			//HsmWorkManager manager = (HsmWorkManager) Class.forName(classPath).getMethod("instance", null).invoke(null, null);
			m_hashmap.put(appname, (IoWorkManager) Class.forName(classPath).getMethod("instance", null).invoke(null, null));
		}
		
		return true;
	}
	
	public static IoWorkManager loadWorkManager(String appname) {
		return m_hashmap.get(appname);
	}
}
