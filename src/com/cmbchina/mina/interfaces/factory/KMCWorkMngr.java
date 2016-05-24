package com.cmbchina.mina.interfaces.factory;

import java.util.HashMap;

import com.cmbchina.mina.abstracts.HsmWork;
import com.cmbchina.mina.abstracts.HsmWorkManager;
import com.cmbchina.mina.interfaces.kmc.MakeMAC;


public class KMCWorkMngr extends HsmWorkManager {
	private static HashMap<String, HsmWork> m_workMap = new HashMap<String, HsmWork>();
	
	static{
		MakeMAC.register();
	}
	
	private static class InstanceHolder {
		static final HsmWorkManager m_instance = new PBOCWorkMngr();
	}
	
	public static HsmWorkManager instance() {
		return InstanceHolder.m_instance;
	}
	
	public Object doWork(String jobName, Object request) {
		HsmWork job = m_workMap.get(jobName);
		return job.work(request);
	}
	
	public static void addWork(String jobName, HsmWork work) {
		if(!m_workMap.containsKey(jobName)) {
			m_workMap.put(jobName, work);
		}
	}
}
