package com.cmbchina.mina.server;

import java.io.IOException;

import com.cmbchina.mina.client.HsmClientPool;
import com.cmbchina.mina.client.HsmPoolFactory;
import com.cmbchina.mina.utils.GlobalVars;
import com.cmbchina.mina.utils.JSONUtil;

public class ServiceProcess {
	private static void initialize() throws IOException {
		;
	}
	
	private static void startHsmService() throws Exception {
		HsmPoolFactory.init();
		HsmPoolFactory.start();
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
		HsmPoolFactory.stop();
		ServiceSocket.instance().close();
	}
	
	protected void finalize() {
		stop();
	}
}
