package com.cmbchina.mina.server;

import java.io.IOException;

import com.cmbchina.mina.client.HsmClientPool;
import com.cmbchina.mina.utils.GlobalVars;
import com.cmbchina.mina.utils.JSONUtil;

public class ServiceProcess {
	private static void initialize() throws IOException {
		return;
	}
	
	private static void startHsmService() throws Exception {
		HsmClientPool.instance().init();
		HsmClientPool.instance().start();
	}
	
	private static void startListener() throws Exception {
		ServiceSocket.instance().init();
		ServiceSocket.instance().listen();
	}
	
	private static void startDBService() {
		;
	}
	
	public static void start() throws Exception {
		initialize();
		startHsmService();
		startDBService();
		startListener();
	}
	
	public static void stop() {
		HsmClientPool.instance().release();
		ServiceSocket.instance().close();
	}
	
	protected void finalize() {
		stop();
	}
}
