package com.cmbchina.mina.interfaces.factory;

import java.util.HashMap;

import com.cmbchina.mina.abstracts.HsmWork;
import com.cmbchina.mina.abstracts.HsmWorkManager;
import com.cmbchina.mina.interfaces.kmc.MakeMAC;
import com.cmbchina.mina.proto.HsmRequest;
import com.cmbchina.mina.proto.HsmResponse;


public class KMCWorkMngr implements HsmWorkManager {
	private static HashMap<String, HsmWork> m_workMap = new HashMap<String, HsmWork>();
	
	static{
		MakeMAC.register();
	}
	
	private static class InstanceHolder {
		static final HsmWorkManager m_instance = new KMCWorkMngr();
	}
	
	public static HsmWorkManager instance() {
		return InstanceHolder.m_instance;
	}
	
	public static void addWork(String jobName, HsmWork work) {
		if(!m_workMap.containsKey(jobName)) {
			m_workMap.put(jobName, work);
		}
	}

	@Override
	public String request(String jobName, Object request) {
		return m_workMap.get(jobName).request(request);
	}

	@Override
	public Object response(String jobName, String response) {
		return m_workMap.get(jobName).response(response);
	}
}
