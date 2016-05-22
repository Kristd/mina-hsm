package com.cmbchina.mina.interf.pboc;

import java.util.HashMap;

import com.cmbchina.mina.client.HsmWork;
import com.cmbchina.mina.client.HsmWorkMngr;

public class PbocWorkMngr extends HsmWorkMngr {
	private static PbocWorkMngr m_instant = new PbocWorkMngr();
	private static HashMap<String, HsmWork> m_workMap = new HashMap<String, HsmWork>();
	
	static{
		GenCVC.register();
		VerifyCVC.register();
	}
	
	public HsmWorkMngr getInstant() {
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
