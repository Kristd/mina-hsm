package com.cmbchina.mina.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cmbchina.mina.conf.AppJSONConf;
import com.cmbchina.mina.server.ResourceMngr;
import com.cmbchina.mina.utils.GlobalVars;
import com.cmbchina.mina.utils.JSONUtil;

public class HsmPoolFactory {
	static AppJSONConf m_appconf;
	private static final Map<String, HsmClientPool> m_hashmap = new ConcurrentHashMap<String, HsmClientPool>();
	
	private HsmPoolFactory() {
		m_appconf = new AppJSONConf();
	}
	
	public static boolean init() throws Exception {
		m_appconf.loadObject(JSONUtil.parserJSONArray(ResourceMngr.getServiceConfigData(GlobalVars.APP_CFG)));
		
		for(int i = 0; i < m_appconf.size(); i++) {
			String className = m_appconf.getClassName(i);
			String classPath = m_appconf.getClassPath(i);
			
			HsmClientPool pool = (HsmClientPool) Class.forName(classPath).getMethod("instance", null).invoke(null, null);
			if(pool.init()) {
				m_hashmap.put(className, pool);
			}
			else {
				return false;
			}
		}
		
		return true;
	}
	
	public static void start() throws Exception {
		for(HsmClientPool pool : m_hashmap.values()) {
			pool.start();
		};
	}
	
	public static HsmClientPool loadPoolManager(String appName) throws Exception {
		HsmClientPool pool = m_hashmap.get(appName);
		
		if(pool != null) {
			return pool;
		}
		else {
			return null;
		}
	}
	
	public static void stop() {
		for(HsmClientPool pool : m_hashmap.values()) {
			pool.stop();
		}
	}
}
