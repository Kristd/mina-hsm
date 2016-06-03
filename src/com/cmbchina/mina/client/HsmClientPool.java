package com.cmbchina.mina.client;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmbchina.mina.abstracts.IoPolicy;
import com.cmbchina.mina.conf.PoolJSONConf;
import com.cmbchina.mina.policy.RandomPolicy;
import com.cmbchina.mina.server.ResourceMngr;
import com.cmbchina.mina.utils.GlobalVars;
import com.cmbchina.mina.utils.JSONUtil;


public class HsmClientPool {
	protected Map<String, Map<String, HsmClient>> m_appGrp;
	private PoolJSONConf m_hsmConf;
	private String m_lastHsm;
	private String m_appname;
	private long m_lastSeed;
	private RandomPolicy m_policy;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HsmClientPool.class);

	protected HsmClientPool(String appname) {
		m_hsmConf = new PoolJSONConf();
		m_appGrp = new ConcurrentHashMap<String, Map<String, HsmClient>>();
		m_lastHsm = "0";
		m_appname = appname;
		m_policy = new RandomPolicy();
	}
	
	public boolean init() throws Exception {
		boolean isloaded = m_hsmConf.loadObject(JSONUtil.parserJSONArray(
				ResourceMngr.getServiceConfigData(GlobalVars.HSMPOOL_CFG)));

		for(int n = 0; n < m_hsmConf.getAppSize(); n++) {
			String appname = m_hsmConf.apps(n).getAppName();
			
			if(m_appname.equals(appname)) {
				Map<String, HsmClient> mapHsms = new ConcurrentHashMap<String, HsmClient>();
				int nhsmCount = m_hsmConf.apps(n).getHsmSize();
				
				for(int m = 1; m <= nhsmCount; m++) {
					String ip = m_hsmConf.apps(n).HsmClinets(m).ip();
					int port = m_hsmConf.apps(n).HsmClinets(m).port();
					int max = m_hsmConf.apps(n).HsmClinets(m).maxConn();
					int min = m_hsmConf.apps(n).HsmClinets(m).minConn();
					int timeout = m_hsmConf.apps(n).HsmClinets(m).timeout();
					
					HsmClient client = new HsmClient(appname, ip, port, timeout, max);
					mapHsms.put(m_lastHsm, client);
					m_lastHsm = String.valueOf(Integer.parseInt(m_lastHsm) + 1);
				}
				
				m_appGrp.put(appname, mapHsms);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean start() throws Exception {
		return start(m_appname);
	}
	
	public boolean start(String appname) throws Exception {
		Iterator<Entry<String, Map<String, HsmClient>>> it = m_appGrp.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String, Map<String, HsmClient>> entry = it.next();
			String key = (String) entry.getKey();
			
			if(key.equals(appname)) {
				for(HsmClient client : m_appGrp.get(key).values()) {
					client.start();
				}
			}
		}
		
		return true;
	}
	
	public int size() throws Exception {
		return size(m_appname);
	}
	
	public int size(String appname) throws Exception {
		Iterator<Entry<String, Map<String, HsmClient>>> it = m_appGrp.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String, Map<String, HsmClient>> entry = it.next();
			String key = (String) entry.getKey();
			if(key.equals(appname)) {
				return m_appGrp.get(key).size();
			}
		}
		
		return -1;
	}
	
	public HsmClient getHSM(String appname, int n) throws Exception {		
		int i = 0;
		Map<String, HsmClient> map = m_appGrp.get(m_appname);
		if(map.size() < n-1) {
			throw new Exception("HSM out of range");
		}
		
		Iterator<Entry<String, HsmClient>> it = map.entrySet().iterator();
		while(it.hasNext()) {
			if(i == n) {
				return map.get(it.next().getKey());
			}
			
			i++;
		}
		
		return null;
	}
	
	public HsmClient getHSM() throws Exception {
		if(m_appGrp.get(m_appname).size() > 1) {
			Map<String, HsmClient> map = m_appGrp.get(m_appname);
			return (HsmClient) m_policy.route(map, map.size());
		}
		else {
			return getHSM(m_appname, 0);
		}
	}
	
	public HsmClient route() {
		return null;
	}
	
	public void stop() throws Exception {
		stop(m_appname);
	}
	
	public void stop(String appname) throws Exception {
		for(HsmClient client : m_appGrp.get(appname).values()) {
			client.stop();
		}
	}
}
