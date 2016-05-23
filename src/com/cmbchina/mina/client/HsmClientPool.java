package com.cmbchina.mina.client;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import com.cmbchina.mina.conf.PoolJSONConf;


public class HsmClientPool {
	private int m_totalHsm = 0;
	private static HsmClientPool m_instant = new HsmClientPool();
	private ConcurrentHashMap<String, Vector<HsmClient>> m_AppGrp = new ConcurrentHashMap<String, Vector<HsmClient>>();

	private HsmClientPool() {
		return;
	}
	
	public static HsmClientPool instance() {
		return m_instant;
	}
	
	public void init( ) {
		PoolJSONConf hsmConf = new PoolJSONConf();
		int nAppSize = hsmConf.getAppSize();
		String appName;
		
		for(int i = 0; i < nAppSize; i++) {
			appName = hsmConf.Apps(i).getAppName();
			Vector<HsmClient> vecHsms = new Vector<HsmClient>();
			
			m_AppGrp.put(appName, vecHsms);
			int nhsmCount = hsmConf.Apps(i).getHsmSize();
			
			for(int j = 0; j < nhsmCount; j++) {
				String addr = hsmConf.Apps(i).HsmClinets(j).Address();
				int port = hsmConf.Apps(i).HsmClinets(j).Port();
				int max = hsmConf.Apps(i).HsmClinets(j).MaxConn();
				int min = hsmConf.Apps(i).HsmClinets(j).MinConn();
				
				HsmClient client = new HsmClient(appName, addr, port, max, min);
				vecHsms.add(client);
			}
		}
	}
	
	public int start() {
		Iterator<Entry<String, Vector<HsmClient>>> it = m_AppGrp.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String, Vector<HsmClient>> entry = it.next();
			String key = (String) entry.getKey();
			
			for(int i = 0; i < m_AppGrp.get(key).size(); i++) {
				m_AppGrp.get(key).get(i).start();
			}
		}
		
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
	
	public Object send(String appname, Object request) {
		return null;
	}
	
	public Object recv(String appname, Object response) {
		return null;
	}
}
