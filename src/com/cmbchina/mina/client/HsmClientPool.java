package com.cmbchina.mina.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmbchina.mina.conf.PoolJSONConf;
import com.cmbchina.mina.server.ResourceMngr;
import com.cmbchina.mina.utils.GlobalVars;
import com.cmbchina.mina.utils.JSONUtil;


public class HsmClientPool {
	private ConcurrentHashMap<String, Vector<HsmClient>> m_appGrp;
	private PoolJSONConf m_hsmConf;
	private int m_nHsm;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HsmClientPool.class);

	private HsmClientPool() {
		m_hsmConf = new PoolJSONConf();
		m_appGrp = new ConcurrentHashMap<String, Vector<HsmClient>>();
		m_nHsm = 0;
	}
	
	private static class InstanceHolder {
		static final HsmClientPool INSTANCE = new HsmClientPool();
	}
	
	public static HsmClientPool instance() {
		return InstanceHolder.INSTANCE;
	}
	
	public void init() throws Exception {
		boolean isloaded = m_hsmConf.loadObject(JSONUtil.parserJSONArray(ResourceMngr.getServiceConfigData(GlobalVars.HSMPOOL_CFG)));
		
		if(!isloaded) {
			throw new Exception("loading JSON configuration failed");
		}
		else {
			LOGGER.info("HsmClientPool loading configuration");
		}
		
		int nAppSize = m_hsmConf.getAppSize();

		for(int n = 0; n < nAppSize; n++) {
			String appname = m_hsmConf.apps(n).getAppName();
			Vector<HsmClient> vecHsms = new Vector<HsmClient>();
			int nhsmCount = m_hsmConf.apps(n).getHsmSize();
			
			LOGGER.info("loading Application = " + appname + " HSM size = " + nhsmCount);
			
			for(int m = 1; m <= nhsmCount; m++) {
				String ip = m_hsmConf.apps(n).HsmClinets(m).ip();
				int port = m_hsmConf.apps(n).HsmClinets(m).port();
				int max = m_hsmConf.apps(n).HsmClinets(m).maxConn();
				int min = m_hsmConf.apps(n).HsmClinets(m).minConn();
				int timeout = m_hsmConf.apps(n).HsmClinets(m).timeout();
				
				HsmClient client = new HsmClient(appname, ip, port, timeout, max);
				vecHsms.add(client);
			}
			
			LOGGER.info("HsmClientPool vev=" + vecHsms.toString());
			m_appGrp.put(appname, vecHsms);
		}
		
		LOGGER.info("HsmClientPool init");
	}
	
	public int start() throws Exception {
		Iterator<Entry<String, Vector<HsmClient>>> it = m_appGrp.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String, Vector<HsmClient>> entry = it.next();
			String key = (String) entry.getKey();
			
			for(int i = 0; i < m_appGrp.get(key).size(); i++) {
				LOGGER.info("m_AppGrp.get(key)=" + key);
				m_appGrp.get(key).get(i).start();
			}
		}
		
		LOGGER.info("HsmClientPool started");
		return 0;
	}
	
	public int size() throws Exception {
		int total = 0;
		Iterator<Entry<String, Vector<HsmClient>>> it = m_appGrp.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String, Vector<HsmClient>> entry = it.next();
			String key = (String) entry.getKey();
			total += m_appGrp.get(key).size();
		}
		
		m_nHsm = total;
		return m_nHsm;
	}
	
	public HsmClient getHSM(String appname, int n) throws Exception {
		if(!m_appGrp.containsKey(appname)) {
			throw new Exception("app name not exist");
		}
		
		Vector<HsmClient> vec = m_appGrp.get(appname);
		if(vec.size() < n-1) {
			throw new Exception("HSM out of range");
		}
		
		HsmClient hsm = vec.get(n);
		if(hsm != null) {
			return hsm;
		}
		else {
			throw new Exception("HSM is null");
		}
	}
	
	public HsmClient getHSM(String appname) throws Exception {
		if(!m_appGrp.containsKey(appname)) {
			throw new Exception("app name not exist");
		}
		
		Random rand = new Random();
		int n = rand.nextInt(m_appGrp.get(appname).size()-1);
		
		return getHSM(appname, n);
	}
/*	
	public int send(String appname, String request) throws Exception {
		HsmClient hsm = getHSM(appname);
		if(hsm == null) {
			throw new Exception("");
		}
		
		String jobname = request;
		String _request_ = request;
		
		return hsm.send(jobname, _request_);
	}
	
	public Object recv() {
		return null;
	}
*/	
}
