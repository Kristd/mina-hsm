package com.hsm.mina.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hsm.mina.conf.PoolJSONConf;
import com.hsm.mina.server.ResourceMngr;
import com.hsm.mina.utils.GlobalVars;
import com.hsm.mina.utils.JSONUtil;

public class HsmPoolFactory {
	private static PoolJSONConf m_hsmConf = new PoolJSONConf();
	private static final Map<String, HsmClientPool> m_hashmap = new ConcurrentHashMap<String, HsmClientPool>();
	
	
	public static boolean init() throws Exception {
		m_hsmConf.loadObject(JSONUtil.parserJSONArray(ResourceMngr.getServiceConfigData(GlobalVars.POOL_CFG)));
		
		for(int i = 0; i < m_hsmConf.getAppSize(); i++) {
			String appname = m_hsmConf.apps(i).getAppName();
			
			HsmClientPool pool = new HsmClientPool(appname);
			if(pool.init()) {
				m_hashmap.put(appname, pool);
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
		}
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
	
	public static void stop() throws Exception {
		for(HsmClientPool pool : m_hashmap.values()) {
			pool.stop();
		}
	}
}
