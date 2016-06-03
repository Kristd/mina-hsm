package com.cmbchina.mina.interfaces.factory;

import java.util.HashMap;

import com.cmbchina.mina.abstracts.IoWork;
import com.cmbchina.mina.abstracts.IoWorkManager;
import com.cmbchina.mina.interfaces.pboc.GenCVC;
import com.cmbchina.mina.proto.HsmRequest;
import com.cmbchina.mina.proto.HsmResponse;


public class PBOCWorkMngr implements IoWorkManager {
	private static HashMap<String, IoWork> m_workMap = new HashMap<String, IoWork>();
	
	static{
		GenCVC.register();
	}
	
	private static class InstanceHolder {
		static final IoWorkManager m_instance = new PBOCWorkMngr();
	}
	
	public static IoWorkManager instance() {
		return InstanceHolder.m_instance;
	}
	
	public static void addWork(String jobName, IoWork work) {
		if(!m_workMap.containsKey(jobName)) {
			m_workMap.put(jobName, work);
		}
	}
	
	@Override
	public String request(String jobName, Object request) {
		return m_workMap.get(jobName).request(request);
	}
	
	@Override
	public Object response(String jobName, Object response) {
		return m_workMap.get(jobName).response(response);
	}
}
