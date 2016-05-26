package com.cmbchina.mina.client;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HsmSocketHandler extends IoHandlerAdapter {
	private final static Logger LOGGER = LoggerFactory.getLogger(HsmSocketHandler.class);
	
	private String values;
	private static int count=0;
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 

	public HsmSocketHandler() {

	}
	
	public HsmSocketHandler(String values) {
		this.values=values;
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("session opened");
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// Empty handler
		String str=message.toString();
		
		LOGGER.info("the message received is["+str+"]");
		
		System.out.println(df.format(new Date()) + " the psh returned is["+str+"]");
    }
	
	public void sessionClosed(IoSession session) throws Exception {
        // Empty handler
		System.out.println("session closed");
    }

    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        // Empty handler
    	System.out.println("session idle");
    }


    public void messageSent(IoSession session, Object message) throws Exception {
        // Empty handler
    	System.out.println("message sent");
    }
}