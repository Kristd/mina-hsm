package com.cmbchina.mina.client;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HsmSocketHandler extends IoHandlerAdapter {

	private final static Logger logger = LoggerFactory.getLogger(HsmSocketHandler.class);
	
	private  String values;
	private static int count=0;
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 

	public HsmSocketHandler()
	{
		//this.values=values;
	}
	
	public HsmSocketHandler(String values)
	{
		this.values=values;
	}

	@Override
	
	public void sessionOpened(IoSession session) throws Exception 
	{
		// TODO Auto-generated method stub	
		System.out.println("session opened");
/*		session.write(values);//output.toString());
		System.out.println(df.format(new Date()) + " send ["+values.toString()+"] ok");
				
		Thread.sleep(1000);
		char input[] = {0x43,0x43,0x43,0x43,0x4E,0x43};
		String send=new String(input,0,6);
		session.write(send);
		System.out.println(df.format(new Date()) + " send ["+send.toString()+"] ok");
*/
		
		//test pressure
/*		char input[] = {0x42,0x42,0x42,0x42,0x4B,0x57,0x30,0x39,0x58,0x31,0x41,0x31,0x41,0x32,
				0x32,0x44,0x41,0x42,0x36,0x45,0x32,0x44,0x34,0x31,0x39,0x45,0x39,0x44,0x46,0x46,
				0x31,0x33,0x31,0x39,0x44,0x30,0x33,0x31,0x39,0x37,0x43,0x12,0x34,0x56,0x78,0x90,
				0x12,0x34,0x56,0x12,0x34,0x31,0x31,0x14,0x73,0x92,0x81,0x47,0x93,0x21,0x74,0x09,
				0x39,0x01,0x23,0x94,0x32,0x94,0x72,0x09,0x3B,0x7D,0xBD,0xD3,0x7F,0x77,0x03,0xA2,0xFA};
		String send=new String(input,0,79);	//79
		
		for(int i=0;i<100000;i++)
		{
			Thread.sleep(10);
			session.write(send);
*/	//		System.out.println(/*df.format(new Date()) + " "+*/i + " send ["+send.toString()+"] ok");
//		}
			
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// Empty handler
		String str=message.toString();
		
		logger.info("the message received is["+str+"]");
		
		System.out.println(df.format(new Date()) + " the psh returned is["+str+"]");
		
		//if(str.startsWith("BBBBND00"))	//match,psm ok
/*		if(str.startsWith("BBBBKX00"))	//match,psm ok
		{
//			logger.info("session "+session.getRemoteAddress().toString()+" heartbeat received ok");
			//System.out.println("session "+session.getRemoteAddress().toString()+" heartbeat received ok");
//			System.out.println("heartbeat received ok");
			System.out.print(".");
			//synchronized (count) {
				count++;
				System.out.println(count);
			
		}
		else
		{
			System.out.println("################################################################");
		}
			*/	
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
    	//System.out.print("message sent");
    }
}