package com.hsm.mina.client;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hsm.mina.abstracts.IoPolicy;
import com.hsm.mina.conf.PoolJSONConf;
import com.hsm.mina.policy.RandomPolicy;
import com.hsm.mina.server.ResourceMngr;
import com.hsm.mina.utils.GlobalVars;
import com.hsm.mina.utils.JSONUtil;


public class HsmClientPool {
	protected Map<String, List<HsmClient>> m_appGrp;
	private PoolJSONConf m_hsmConf;
	private String m_appname;
	private RandomPolicy m_policy;
	private Object m_mutex = new Object();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HsmClientPool.class);

	protected HsmClientPool(String appname) {
		m_hsmConf = new PoolJSONConf();
		m_appGrp = new ConcurrentHashMap<String, List<HsmClient>>();
		m_appname = appname;
		//m_policy = new RandomPolicy();
	}
	
	public boolean init() throws Exception {
		m_hsmConf.loadObject(JSONUtil.parserJSONArray(ResourceMngr.getServiceConfigData(GlobalVars.POOL_CFG)));

		for(int n = 0; n < m_hsmConf.getAppSize(); n++) {
			String appname = m_hsmConf.apps(n).getAppName();
			
			if(m_appname.equals(appname)) {
				List<HsmClient> listHsms = new CopyOnWriteArrayList<HsmClient>();
				int nhsmCount = m_hsmConf.apps(n).getHsmSize();
				
				for(int m = 1; m <= nhsmCount; m++) {
					String ip = m_hsmConf.apps(n).HsmClinets(m).ip();
					int port = m_hsmConf.apps(n).HsmClinets(m).port();
					int max = m_hsmConf.apps(n).HsmClinets(m).maxConn();
					int min = m_hsmConf.apps(n).HsmClinets(m).minConn();
					int timeout = m_hsmConf.apps(n).HsmClinets(m).timeout();
					
					HsmClient client = new HsmClient(appname, ip, port, timeout, max);
					listHsms.add(client);
				}
				
				m_appGrp.put(appname, listHsms);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean start() throws Exception {
		return start(m_appname);
	}
	
	public boolean start(String appname) throws Exception {
		Iterator<Entry<String, List<HsmClient>>> it = m_appGrp.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String, List<HsmClient>> entry = it.next();
			String key = (String) entry.getKey();
			
			if(key.equals(appname)) {
				for(HsmClient client : m_appGrp.get(key)) {
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
		Iterator<Entry<String, List<HsmClient>>> it = m_appGrp.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String, List<HsmClient>> entry = it.next();
			String key = (String) entry.getKey();
			
			if(key.equals(appname)) {
				return m_appGrp.get(key).size();
			}
		}
		
		return -1;
	}
	
	public HsmClient getHSM(String appname, int n) throws Exception {		
		int i = 0;
		List<HsmClient> list = m_appGrp.get(m_appname);
		if(list.size() < n-1) {
			throw new Exception("HSM out of range");
		}
		
		Iterator<HsmClient> it = list.iterator();
		while(it.hasNext()) {
			if(i == n) {
				return it.next();
			}
			
			i++;
		}
		
		return null;
	}
	
	public HsmClient getHSM() throws Exception {
		if(m_appGrp.get(m_appname).size() > 1) {
			List<HsmClient> map = m_appGrp.get(m_appname);
			return (HsmClient) m_policy.route();
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
		for(HsmClient client : m_appGrp.get(appname)) {
			client.stop();
		}
	}
}
