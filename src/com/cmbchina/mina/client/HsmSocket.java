package com.cmbchina.mina.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.concurrent.Executors;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.cmbchina.mina.abs.IoSocket;
import com.cmbchina.mina.enums.Status;
import com.cmbchina.mina.filter.keepalive.HsmKeepAliveFilterFactory;
import com.cmbchina.mina.utils.GlobalVars;

public class HsmSocket extends IoSocket {
	private IoConnector m_connector;
	private IoSession m_session;
	private Status m_status;
	private int m_timeout = 0;	
	private int busyCountValue = 0;
	private int freeCountValue = 0;
	private int busyCount = 0;
	private int freeCount = 0;
	private static Object m_locker = new Object();
	private LinkedList<?> m_queue;
	private int m_msgsize;
	private String m_ip;
	private int m_port;
	private ConnectFuture m_future;
	
	public HsmSocket(int timeout) {
		m_timeout = timeout;
		m_msgsize = 0;
	}
	
	public boolean init() {
		m_connector = new NioSocketConnector();
		m_connector.setConnectTimeoutMillis(3000);
		
		setupLoggerFilter();
		setupCodecFilter();
		setupThreadsFilter();
		setupKeepaliveFilter();
		
		m_connector.setHandler(new HsmSocketHandler());
		
		return true;
	}
	
	protected void setupLoggerFilter() {
		m_connector.getFilterChain().addLast("logger", new LoggingFilter());
	}
	
	protected void setupThreadsFilter() {
		m_connector.getFilterChain().addLast("threads", new ExecutorFilter(Executors.newCachedThreadPool()));
	}
	
	protected void setupCodecFilter() {
		PrefixedStringCodecFactory codfilter = new PrefixedStringCodecFactory(Charset.forName(GlobalVars.ENCODING));
		codfilter.setEncoderPrefixLength(GlobalVars.CODE_PREFIX);
		codfilter.setDecoderPrefixLength(GlobalVars.CODE_PREFIX);
		m_connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(codfilter));
	}
	
	protected void setupKeepaliveFilter() {
		KeepAliveFilter ServKeepAliveFilterFactory = new KeepAliveFilter(new HsmKeepAliveFilterFactory(), IdleStatus.BOTH_IDLE);
		ServKeepAliveFilterFactory.setRequestInterval(GlobalVars.ACCEPTOR_KA_INTERVAL);
		ServKeepAliveFilterFactory.setRequestTimeout(GlobalVars.ACCEPTOR_KA_TIMEOUT);
		ServKeepAliveFilterFactory.setForwardEvent(GlobalVars.ACCEPTOR_KA_FORWARD);
		m_connector.getFilterChain().addLast("keepalive", ServKeepAliveFilterFactory);
	}
	
	protected int connect() {
		m_future = m_connector.connect(new InetSocketAddress(m_ip, m_port));  
		m_future.awaitUninterruptibly();
        if(m_future.isConnected()) {
        	synchronized(m_locker) {
        		m_status = Status.FREE;
    		}
        }
	
		return 0;
	}
	
	protected int disconnect() {
		 IoSession session = m_future.getSession();
		 session.close(true);
		 synchronized(m_locker)	{
			 m_status = Status.FREE;
		}
		 
		return 0;
	}
	
	protected Object SendAndRecv(String buffer)	{
		long startTime=0, endTime=0, costTime=-1;
         IoSession session = m_future.getSession();
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
			if(cost >= 8) {
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
	
	public void close() {
		if(m_connector != null) {
			m_connector.dispose();
		}
	}
	
	//CHECK may cause a bug 
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
}
