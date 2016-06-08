package com.hsm.mina.client;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestHandler extends IoHandlerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestHandler.class);
	
	public TestHandler() {
		System.out.println("TestHandler Init");
	}
	
    public void sessionCreated(IoSession session) throws Exception {
    	System.out.println("TestHandler sessionCreated");
    }
    
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("TestHandler sessionOpened");
        
        /*
        WriteFuture writeFuture = session.write("1111WB");
        if(writeFuture.isWritten()) {
        	System.out.println("TestHandler isWritten");
        }
        else {
        	System.out.println("TestHandler isn't Written");
        }
        
        if(writeFuture.isDone()) {
        	System.out.println("TestHandler isDone");
        }
        else {
        	System.out.println("TestHandler isn't Done");
        }
        */
    }

    public void sessionClosed(IoSession session) throws Exception {
    	System.out.println("TestHandler sessionClosed");
    }

    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
    	System.out.println("TestHandler sessionIdle");
    	System.out.println("IDLE:" + session.getIdleCount(status));
    }
    
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		LOGGER.warn(cause.getMessage(), cause); 
	}
	
    public void messageReceived(IoSession session, Object message) throws Exception {
    	System.out.println("TestHandler messageReceived=" + message.toString());
    }

    public void messageSent(IoSession session, Object message) throws Exception {
    	System.out.println("TestHandler messageSent=" + message.toString());
    }
    
    public void inputClosed(IoSession session) throws Exception {
    	System.out.println("TestHandler inputClosed");
        session.close(true);
    }
}
