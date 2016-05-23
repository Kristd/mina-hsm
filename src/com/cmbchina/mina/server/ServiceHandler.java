package com.cmbchina.mina.server;


import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmbchina.mina.client.HsmClientPool;


public class ServiceHandler extends IoHandlerAdapter {
	private final static Logger LOGGER = LoggerFactory.getLogger(ServiceHandler.class);

	@Override
	public void messageReceived(IoSession session, Object message)throws Exception {
		
	}
	
	@Override
    public void sessionCreated(IoSession session) throws Exception {
        // Empty handler
    }

	@Override
    public void sessionOpened(IoSession session) throws Exception {
        // Empty handler
    }

	@Override
    public void sessionClosed(IoSession session) throws Exception {
        // Empty handler
    }

	@Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        // Empty handler
    }

	@Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        // Empty handler
    }

	@Override
    public void messageSent(IoSession session, Object message) throws Exception {
        // Empty handler
    } 
}

