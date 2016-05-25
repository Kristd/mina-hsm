package com.cmbchina.mina.interfaces.factory;

import java.util.HashMap;

import com.cmbchina.mina.abstracts.HsmWork;
import com.cmbchina.mina.abstracts.HsmWorkManager;
import com.cmbchina.mina.interfaces.pboc.GenCVC;
import com.cmbchina.mina.proto.HsmRequest;
import com.cmbchina.mina.proto.HsmResponse;


public class PBOCWorkMngr implements HsmWorkManager {
	private static HashMap<String, HsmWork> m_workMap = new HashMap<String, HsmWork>();
	
	static{
		GenCVC.register();
	}
	
	private static class InstanceHolder {
		static final HsmWorkManager m_instance = new PBOCWorkMngr();
	}
	
	public static HsmWorkManager instance() {
		return InstanceHolder.m_instance;
	}
	
	@Override
	public HsmRequest request(String jobName, String request) {
		return m_workMap.get(jobName).request(request);
	}
	
	@Override
	public String response(String jobName, HsmResponse response) {
		return m_workMap.get(jobName).response(response);
	}
	
	public static void addWork(String jobName, HsmWork work) {
		if(!m_workMap.containsKey(jobName)) {
			m_workMap.put(jobName, work);
		}
	}
}
