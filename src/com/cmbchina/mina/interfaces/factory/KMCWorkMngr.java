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
		static final HsmWorkManager m_instance = new PBOCWorkMngr();
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
	public HsmRequest request(String jobname, String request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String response(String jobname, HsmResponse request) {
		// TODO Auto-generated method stub
		return null;
	}
}
