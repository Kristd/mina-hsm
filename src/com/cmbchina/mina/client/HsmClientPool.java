package com.cmbchina.mina.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
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
	private ConcurrentHashMap<String, Vector<HsmClient>> m_AppGrp;
	private PoolJSONConf m_hsmConf;
	private int m_totalHsm;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HsmClientPool.class);

	private HsmClientPool() {
		m_hsmConf = new PoolJSONConf();
		m_AppGrp = new ConcurrentHashMap<String, Vector<HsmClient>>();
		m_totalHsm = 0;
	}
	
	public static class InstanceHolder {
		static final HsmClientPool INSTANCE = new HsmClientPool();
	}
	
	public static HsmClientPool instance() {
		return InstanceHolder.INSTANCE;
	}
	
	public void init( ) throws Exception {
		boolean isloaded = m_hsmConf.loadObject(JSONUtil.parserJSONArray(ResourceMngr.getServiceConfigData(GlobalVars.HSMPOOL_CFG)));
		
		if(!isloaded) {
			throw new Exception("loading JSON configuration failed");
		}
		else {
			LOGGER.info("HsmClientPool loading configuration");
		}
		
		int nAppSize = m_hsmConf.getAppSize();

		for(int i = 0; i < nAppSize; i++) {
			String appname = m_hsmConf.apps(i).getAppName();
			Vector<HsmClient> vecHsms = new Vector<HsmClient>();
			int nhsmCount = m_hsmConf.apps(i).getHsmSize();
			
			LOGGER.info("loading Application = " + appname + " HSM size = " + nhsmCount);
			
			for(int j = 1; j <= nhsmCount; j++) {
				String ip = m_hsmConf.apps(i).HsmClinets(j).ip();
				int port = m_hsmConf.apps(i).HsmClinets(j).port();
				int max = m_hsmConf.apps(i).HsmClinets(j).maxConn();
				int min = m_hsmConf.apps(i).HsmClinets(j).minConn();
				int timeout = m_hsmConf.apps(i).HsmClinets(j).timeout();
				
				HsmClient client = new HsmClient(appname, ip, port, timeout, max);
				vecHsms.add(client);
			}
			LOGGER.info("HsmClientPool vev=" + vecHsms.toString());
			m_AppGrp.put(appname, vecHsms);
		}
		
		LOGGER.info("HsmClientPool init");
	}
	
	public int start() throws Exception {
		Iterator<Entry<String, Vector<HsmClient>>> it = m_AppGrp.entrySet().iterator();
		
		while(it.hasNext()) {
			LOGGER.info("it.hasNext()");
			Entry<String, Vector<HsmClient>> entry = it.next();
			String key = (String) entry.getKey();
			
			for(int i = 0; i < m_AppGrp.get(key).size(); i++) {
				LOGGER.info("m_AppGrp.get(key)=" + key);
				m_AppGrp.get(key).get(i).start();
			}
		}
		
		LOGGER.info("HsmClientPool started");
		return 0;
	}
	
	public int size() {
		int total = 0;
		Iterator<Entry<String, Vector<HsmClient>>> it = m_AppGrp.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String, Vector<HsmClient>> entry = it.next();
			String key = (String) entry.getKey();
			total += m_AppGrp.get(key).size();
		}
		
		m_totalHsm = total;
		return m_totalHsm;
	}
	
	/*
	protected HsmClient getHSM(String name)	{
		Iterator<HsmClient> it = hsmPool.iterator();
		HsmClient hsm, hsm_bak=null;
		while(it.hasNext())
		{
			hsm = (HsmClient)it.next();
			if(hsm.getHsmStatus() == HsmClient.HSM_FREE)
			{
				hsm_bak=hsm;
				if(hsm.nameMatch(name))	//same app
				{
					return hsm;
				}				
			}				
		}
		if(hsm_bak != null)
			return hsm_bak;
		else
		{
			System.out.println("No free hsm.");
			throw new NullPointerException();
		}
	}
	
	protected HsmClient getHSM() {
		Iterator<HSM> it = hsmPool.iterator();
		HSM hsm;
		while(it.hasNext())
		{
			hsm = (HSM)it.next();
			if(hsm.getHsmStatus() == HSM.HSM_FREE)
			{
				return hsm;
			}				
		}
		
		System.out.println("No free hsm.");
		throw new NullPointerException();
	}
	*/
	
	public Object send(String buffer, String appname) {
		return null;
	}
	
	public Object recv() {
		return null;
	}
}
