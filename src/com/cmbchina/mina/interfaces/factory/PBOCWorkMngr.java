package com.cmbchina.mina.interfaces.factory;

import java.util.HashMap;

import com.cmbchina.mina.client.HsmWork;
import com.cmbchina.mina.interfaces.pboc.GenCVC;
import com.cmbchina.mina.interfaces.pboc.VerifyCVC;

public class PBOCWorkMngr extends HsmWorkManager {
	private static PBOCWorkMngr m_instant = new PBOCWorkMngr();
	private static HashMap<String, HsmWork> m_workMap = new HashMap<String, HsmWork>();
	
	static{
		GenCVC.register();
		VerifyCVC.register();
	}
	
	public HsmWorkManager instance() {
		return m_instant;
	}
	
	public Object doWork(String jobName, Object request) {
		HsmWork job = m_workMap.get(jobName);
		return job.work(request);
	}
	
	public static void addWork(String jobName, HsmWork work) {
		m_workMap.put(jobName, work);
	}
}
