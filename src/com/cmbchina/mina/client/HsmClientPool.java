package com.cmbchina.mina.client;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmbchina.mina.conf.PoolJSONConf;
import com.cmbchina.mina.server.ResourceMngr;
import com.cmbchina.mina.utils.GlobalVars;
import com.cmbchina.mina.utils.JSONUtil;


public abstract class HsmClientPool {
	protected static Map<String, Map<String, HsmClient>> m_appGrp;
	private PoolJSONConf m_hsmConf;
	private int m_nHsm;
	private String m_lastHsm;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HsmClientPool.class);

	protected HsmClientPool() {
		m_hsmConf = new PoolJSONConf();
		m_appGrp = new ConcurrentHashMap<String, Map<String, HsmClient>>();
		m_nHsm = 0;
		m_lastHsm = "0";
	}
	
	public boolean init() throws Exception {
		boolean isloaded = m_hsmConf.loadObject(JSONUtil.parserJSONArray(ResourceMngr.getServiceConfigData(GlobalVars.HSMPOOL_CFG)));
		
		if(!isloaded) {
			throw new Exception("loading JSON configuration failed");
		}
		else {
			LOGGER.info("HsmClientPool loading configuration");
		}

		for(int n = 0; n < m_hsmConf.getAppSize(); n++) {
			String appname = m_hsmConf.apps(n).getAppName();
			
			Map<String, HsmClient> mapHsms = new ConcurrentHashMap<String, HsmClient>();
			int nhsmCount = m_hsmConf.apps(n).getHsmSize();
			
			for(int m = 1; m <= nhsmCount; m++) {
				String ip = m_hsmConf.apps(n).HsmClinets(m).ip();
				int port = m_hsmConf.apps(n).HsmClinets(m).port();
				int max = m_hsmConf.apps(n).HsmClinets(m).maxConn();
				int min = m_hsmConf.apps(n).HsmClinets(m).minConn();
				int timeout = m_hsmConf.apps(n).HsmClinets(m).timeout();
				
				HsmClient client = new HsmClient(appname, ip, port, timeout, max);
				
				System.out.println("[" + m +"]class id=" + m_lastHsm);
				
				mapHsms.put(m_lastHsm, client);
				m_lastHsm = String.valueOf(Integer.parseInt(m_lastHsm) + 1);
			}
			
			m_appGrp.put(appname, mapHsms);
		}
		
		LOGGER.info("HsmClientPool init");
		return true;
	}
	
	public boolean start() throws Exception {
		Iterator<Entry<String, Map<String, HsmClient>>> it = m_appGrp.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String, Map<String, HsmClient>> entry = it.next();
			String key = (String) entry.getKey();
			
			for(int i = 0; i < m_appGrp.get(key).size(); i++) {
				LOGGER.info("m_appGrp.get(key)=" + key);
				//m_appGrp.get(key).values().get(i).start();
				for(HsmClient client : m_appGrp.get(key).values()) {
					client.start();
				}
			}
		}
		
		LOGGER.info("HsmClientPool started");
		return true;
	}
	
	public int size() throws Exception {
		int total = 0;
		Iterator<Entry<String, Map<String, HsmClient>>> it = m_appGrp.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String, Map<String, HsmClient>> entry = it.next();
			String key = (String) entry.getKey();
			total += m_appGrp.get(key).size();
		}
		
		m_nHsm = total;
		return m_nHsm;
	}
	
	public HsmClient getHSM(String appname, int n) throws Exception {
		if(m_appGrp.containsKey(appname) || 
			m_appGrp.containsKey(appname.toLowerCase()) || 
			m_appGrp.containsKey(appname.toUpperCase())) {
			
			int i = 0;
			HsmClient client = null;
		
			Map<String, HsmClient> map = m_appGrp.get(appname);
			if(map.size() < n-1) {
				throw new Exception("HSM out of range");
			}
			
			Iterator<Entry<String, HsmClient>> it = map.entrySet().iterator();
			while(it.hasNext()) {
				if(i == n) {
					client = map.get(it.next().getKey());
					break;
				}
				else {
					i++;
				}
			}
			
			if(client != null) {
				return client;
			}
			else {
				throw new Exception("HSM is null");
			}
		}
		else {
			throw new Exception("key not in tables");
		}
	}
	
	public HsmClient getHSM(String appname) throws Exception {
		System.out.println("[HsmClientPool]appname=" + appname);
		if(m_appGrp.containsKey(appname) || 
			m_appGrp.containsKey(appname.toLowerCase()) || 
			m_appGrp.containsKey(appname.toUpperCase())) {
		
			Random rand = new Random();
			if(m_appGrp.get(appname).size() > 1) {
				return getHSM(appname, rand.nextInt(m_appGrp.get(appname).size()-1));
			}
			else {
				return getHSM(appname, 0);
			}
		}
		else {
			throw new Exception("key not in tables");
		}
	}
	
	public void stop() {
		for(Map<String, HsmClient> app : m_appGrp.values()) {
			for(HsmClient client : app.values()) {
				if(client != null) {
					client.stop();
				}
			}
		}
	}
}
