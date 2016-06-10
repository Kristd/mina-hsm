package com.hsm.mina.interfaces.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hsm.mina.abstracts.IoWorkManager;
import com.hsm.mina.conf.AppJSONConf;
import com.hsm.mina.server.ResourceMngr;
import com.hsm.mina.utils.GlobalVars;
import com.hsm.mina.utils.JSONUtil;


public class AppMngrFactory {
	private static final Map<String, IoWorkManager> m_hashmap = new ConcurrentHashMap<String, IoWorkManager>();
	private static AppJSONConf m_appconf = new AppJSONConf();
	
	
	public static boolean init() throws Exception {
		m_appconf.loadObject(JSONUtil.parserJSONArray(ResourceMngr.getServiceConfigData(GlobalVars.MANAGER_CFG)));
		
		for(int i = 0; i < m_appconf.size(); i++) {
			Class<?> cls = null;
			Object obj = null;
			
			String appname = m_appconf.getClassName(i);
			String classPath = m_appconf.getClassPath(i);
			
			m_hashmap.put(appname, (IoWorkManager) Class.forName(classPath).getMethod("instance", null).invoke(null, null));
		}
		
		return true;
	}
	
	public static IoWorkManager loadWorkManager(String appname) {
		return m_hashmap.get(appname);
	}
}
