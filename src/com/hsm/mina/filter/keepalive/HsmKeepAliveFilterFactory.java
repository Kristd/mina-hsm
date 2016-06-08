package com.hsm.mina.filter.keepalive;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

public class HsmKeepAliveFilterFactory implements KeepAliveMessageFactory {
	private String m_request;
	private String m_response;
	
	public void setKeepaliveMessage(String request) {
		m_request = request;
	}
	
	//if the message is a alive request, ignore it because hsm wont send request by default
	@Override
	public boolean isRequest(IoSession session, Object message) {
		// TODO Auto-generated method stub
		return false;
	}

	//if the message is a alive response, not null
	@Override
	public boolean isResponse(IoSession session, Object message) {
		// TODO Auto-generated method stub					
		System.out.println("[1]keepalive message received is[" + new String(message.toString().getBytes(), 0, 6) + "]");
		if(new String(message.toString().getBytes(), 0, 6).startsWith("1111WC")) {
			return true;
		}
		
		return false;
	}
	
	//return a alive request message, not null
	@Override
	public Object getRequest(IoSession session) {
		// TODO Auto-generated method stub
		String sndMsg = "1111WB";
		byte[] byteMsg = new byte[sndMsg.length()+2];
		byteMsg[0] = (byte)(sndMsg.length()/256);
		byteMsg[1] = (byte)(sndMsg.length()%256);
		
		for(int i = 0; i < sndMsg.length(); i++) {
			byteMsg[i+2] = sndMsg.getBytes()[i];
		}
		
		System.out.println("keepalive message send is[" + sndMsg + "]");
		
		return sndMsg;
	}
	
	//return a alive response message, ignore
	@Override
	public Object getResponse(IoSession session, Object request) {
		// TODO Auto-generated method stub
		return null;
	}

}
