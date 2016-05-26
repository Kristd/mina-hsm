package com.cmbchina.mina.server;


import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmbchina.mina.client.HsmClient;
import com.cmbchina.mina.client.HsmClientPool;


public class ServiceHandler extends IoHandlerAdapter {
	private final static Logger LOGGER = LoggerFactory.getLogger(ServiceHandler.class);
	
	@Override
    public void sessionCreated(IoSession session) throws Exception {
		System.out.println("ServiceHandler sessionCreated");
    }

	@Override
    public void sessionOpened(IoSession session) throws Exception {
		System.out.println("ServiceHandler sessionOpened");
    }

	@Override
    public void sessionClosed(IoSession session) throws Exception {
		System.out.println("ServiceHandler sessionClosed");
    }

	@Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		System.out.println("ServiceHandler sessionIdle");
    }

	@Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		throw new Exception("Exception caught in ServiceHandler session=" 
							+ session.toString() + " - " + cause.getMessage());
    }

	@Override
    public void messageSent(IoSession session, Object message) throws Exception {
		System.out.println("ServiceHandler messageSent=" + message.toString());
    }
	
	@Override
	public void messageReceived(IoSession session, Object message)throws Exception {
		System.out.println("ServiceHandler messageReceived=" + message.toString());
		
		String _message_ = message.toString();
		
		String appname = _message_.substring(0, 4).trim();
		System.out.println("appname=" + appname);
		
		String jobname = _message_.substring(4, 14).trim();
		System.out.println("jobname=" + jobname);
		
		String request = _message_.substring(14, message.toString().length()).trim();
		System.out.println("request=" + request);
		
		String response = (String) HsmClientPool.instance().getHSM(appname).process(jobname, request);
	
		WriteFuture future = session.write(response);
		future.addListener(new ServiceFutureListener());
	}
}

