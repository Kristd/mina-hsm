package com.cmbchina.mina.server;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class ServiceListener implements IoServiceListener {	
	@Override
	public void serviceActivated(IoService service) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serviceIdle(IoService service, IdleStatus idleStatus) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("ServiceListener serviceIdle");
	}

	@Override
	public void serviceDeactivated(IoService service) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("ServiceListener sessionCreated");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionDestroyed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("ServiceListener sessionDestroyed");
	}
}
