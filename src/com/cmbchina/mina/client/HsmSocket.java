package com.cmbchina.mina.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.LinkedList;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.cmbchina.mina.enums.Status;

public class HsmSocket {
	private IoConnector m_connector;
	private ConnectFuture m_confuture;
	private IoSession session;
	private Status m_status;
	private int thresholdTime = 0;	
	private int busyCountValue = 0;
	private int freeCountValue = 0;
	private int busyCount = 0;
	private int freeCount = 0;
	private static Object m_locker = new Object();
	private LinkedList<?> m_queue;
	
	public HsmSocket(int tTime,int bCount, int fCount) {
		thresholdTime = tTime;
		busyCountValue = bCount;
		freeCountValue = fCount;
	}
	
	protected int connect(String ip, int port, int timeout) {
		IoConnector connector = new NioSocketConnector();
	
		connector.setConnectTimeoutMillis(timeout);	//30000);
	
		InetSocketAddress sa = new InetSocketAddress(ip, port);//("99.12.115.81",8);	//("localhost",3008);

		PrefixedStringCodecFactory pre_filter = new PrefixedStringCodecFactory(Charset.forName("ISO-8859-1"));//US-ASCII  UTF-8
		pre_filter.setEncoderPrefixLength(2);
		pre_filter.setDecoderPrefixLength(2);
	
		connector.getFilterChain().addLast("codec",	new ProtocolCodecFilter(pre_filter));
		
		LoggingFilter loggingFilter = new LoggingFilter();
		connector.getFilterChain().addLast("logging", loggingFilter);
		
		connector.setHandler(new HsmSocketHandler());	
		
		future = connector.connect(sa);  
        future.awaitUninterruptibly();
        if(future.isConnected())	//succ
        {
        	synchronized(m_locker)
    		{
        		connStatus = HSM_CONN_FREE;
    		}
        }
	
		return 0;
		
	}
	
	protected int disconnect()
	{
		 IoSession session = future.getSession();
		 session.close(true);	//close immediately.
		 synchronized(locker)
		{
			 connStatus = HSM_CONN_ERR;
		}
		return 0;
	}
	
	protected Object SendAndRecv(String buffer)	{
		long startTime=0, endTime=0, costTime=-1;
         IoSession session = future.getSession();
         Object message;
         
         //System.out.println("IP-port:"+ip+":"+port+" is handling......");
         startTime = System.currentTimeMillis();
         
         WriteFuture w_future = session.write(buffer);
          
         // Wait until the message is completely written out to the O/S buffer.
         w_future.awaitUninterruptibly();//join();
         if( w_future.isWritten() )
         {
             // The message has been written successfully.
        	 System.out.println("send succ");
         }
         else
         {
             // The messsage couldn't be written out completely for some reason.
             // (e.g. Connection is closed)
        	 System.out.println("send err");
         }
         
         
         // useReadOperation must be enabled to use read operation.
         session.getConfig().setUseReadOperation(true);
         
         ReadFuture r_future = session.read();
         // Wait until a message is received.
         try {
			r_future.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
         try {
             message = r_future.getMessage();
         } catch (Exception e) {
             throw new RuntimeException();
         }
         finally{
        	 endTime = System.currentTimeMillis();
        	 costTime = endTime - startTime;
        	 calculate(costTime);
         }

        System.out.println("recv succ"+message.toString());
		return message;
	}
	
	protected void calculate(long cost) {
		synchronized(m_locker) {
			if(cost >= thresholdTime) {
				busyCount++;
				if(busyCount >= busyCountValue)
				{
					m_status = Status.BUSY;
					freeCount=0;
				}					
			}
			else {
				freeCount++;
				if(freeCount >= freeCountValue)	{
					m_status = Status.FREE;
					busyCount=0;
				}
			}			
		}
	}
	
	public int receive() {
		return 0;
	}
	
	public void send() {
		return;
	}
	
	public Status getStatus() {
		return m_status;
	}
}
