package com.cmbchina.mina.server;

import com.cmbchina.mina.client.HsmPoolFactory;
import com.cmbchina.mina.interfaces.factory.AppMngrFactory;

public class ServiceProcess {
	private static void initialize() throws Exception {
		AppMngrFactory.init();
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
	
	public static void stop() throws Exception {
		HsmPoolFactory.stop();
		ServiceSocket.instance().close();
	}
	
	protected void finalize() throws Exception {
		stop();
	}
}
