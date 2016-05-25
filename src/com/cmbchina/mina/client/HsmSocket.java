package com.cmbchina.mina.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.cmbchina.mina.abstracts.IoSocket;
import com.cmbchina.mina.conf.SocketJSONConf;
import com.cmbchina.mina.enums.Status;
import com.cmbchina.mina.filter.keepalive.HsmKeepAliveFilterFactory;
import com.cmbchina.mina.server.ResourceMngr;
import com.cmbchina.mina.utils.GlobalVars;
import com.cmbchina.mina.utils.JSONUtil;

public class HsmSocket extends IoSocket {
	private IoConnector m_connector;
	private IoSession m_session;
	private Status m_status;
	private int m_timeout = 0;	
	private int busyCountValue = 0;
	private int freeCountValue = 0;
	private int busyCount = 0;
	private int freeCount = 0;
	private Object m_lock = new Object();
	private ConcurrentLinkedQueue<?> m_queue;
	private int m_msgsize;
	private String m_ip;
	private int m_port;
	private ConnectFuture m_future;
	private int m_resptime;
	private IoHandlerAdapter m_handler;
	private boolean m_connected = false;
	private SocketJSONConf m_clientconf;
	
	private class HsmSockFutureListner implements IoFutureListener {
		@Override
		public void operationComplete(IoFuture future) {
			System.out.println("myIoFutureListner operationComplete");
			if(future.getSession().isConnected()) {
				System.out.println("connected");
				
	        	m_status = Status.FREE;
	        	m_msgsize = 0;
	        	
	        	synchronized(m_lock) {
	        		m_connected = true;
	        	}
			}
		}
	}
	
	public HsmSocket(String ip, int port, int timeout) {
		m_ip = ip;
		m_port = port;
		m_timeout = timeout;
		m_msgsize = 0;
		
		m_connector = new NioSocketConnector();
		m_clientconf = new SocketJSONConf();
	}
	
	public boolean init() throws Exception {
		m_clientconf.loadObject(JSONUtil.parserJSONArray(ResourceMngr.getServiceConfigData(GlobalVars.CLIENT_CFG)));
		m_connector.setConnectTimeoutMillis(m_clientconf.socketTimeout());
		
		m_connector.getSessionConfig().setReadBufferSize(m_clientconf.socketBufferSize());
		m_connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, m_clientconf.socketIdleTime());
		
		if(m_clientconf.logger()) {
			setupLoggerFilter();
		}
		
		if(m_clientconf.codeflter()) {
			setupCodecFilter();
		}
		
		if(m_clientconf.threadPool()) {
			setupThreadsFilter();
		}
		
		if(m_clientconf.keepalive()) {
			setupKeepaliveFilter();
		}
		
		m_handler = new HsmSocketHandler();
		m_connector.setHandler(m_handler);
		
		return true;
	}
	
	protected void setupLoggerFilter() {
		m_connector.getFilterChain().addLast("logger", new LoggingFilter());
	}
	
	protected void setupThreadsFilter() {
		m_connector.getFilterChain().addLast("threads", new ExecutorFilter(Executors.newCachedThreadPool()));
	}
	
	protected void setupCodecFilter() {
		PrefixedStringCodecFactory codfilter = new PrefixedStringCodecFactory(Charset.forName(m_clientconf.codeflterEncoding()));
		codfilter.setEncoderPrefixLength(m_clientconf.codeflterEncodePrefix());
		codfilter.setDecoderPrefixLength(m_clientconf.codeflterDecodePrefix());
		m_connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(codfilter));
	}
	
	protected void setupKeepaliveFilter() {
		KeepAliveFilter hsmKeepAliveFilterFactory = new KeepAliveFilter(new HsmKeepAliveFilterFactory(), IdleStatus.BOTH_IDLE);
		hsmKeepAliveFilterFactory.setRequestInterval(m_clientconf.keepaliveInterval());
		hsmKeepAliveFilterFactory.setRequestTimeout(m_clientconf.keepaliveTimeout());
		hsmKeepAliveFilterFactory.setForwardEvent(m_clientconf.keepaliveForward());
		m_connector.getFilterChain().addLast("keepalive", hsmKeepAliveFilterFactory);
	}
	
	protected boolean connect() {
		m_future = m_connector.connect(new InetSocketAddress(m_ip, m_port));  
		//m_future.awaitUninterruptibly();
		m_future.addListener(new HsmSockFutureListner());
		
		/*
        if(m_future.isConnected()) {
        	m_status = Status.FREE;
        	m_msgsize = 0;
        }
		*/
		
		while(true) {
			synchronized(m_lock) {
				if(m_connected) {
					System.out.println("connected to " + m_ip + ":" + m_port);
					return m_connected;
				}
			}
		}
		
		//return false;
	}
	
	protected int disconnect() {
		IoSession session = m_future.getSession();
		session.close(true);
		m_status = Status.FREE;
		m_msgsize = 0;
		 
		return 0;
	}
	
	protected Object send(String buffer)	{
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
         }

        System.out.println("recv succ"+message.toString());
		return message;
	}
	
	public int recv() {
		return 0;
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
