package com.cmbchina.mina.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmbchina.mina.abstracts.IoSocket;
import com.cmbchina.mina.conf.SocketJSONConf;
import com.cmbchina.mina.enums.HsmStatus;
import com.cmbchina.mina.enums.SockStatus;
import com.cmbchina.mina.filter.keepalive.HsmKeepAliveFilterFactory;
import com.cmbchina.mina.proto.HsmResponse;
import com.cmbchina.mina.server.ResourceMngr;
import com.cmbchina.mina.utils.GlobalVars;
import com.cmbchina.mina.utils.JSONUtil;

public class HsmSocket extends Observable implements IoSocket {
	private IoConnector m_connector;
	private IoSession m_session;
	private SockStatus m_status;
	private int m_timeout = 0;	

	private Object m_worklock = new Object();
	private Object m_statlock = new Object();
	
	
	private String m_ip;
	private int m_port;
	private ConnectFuture m_future;
	private int m_resptime;
	private IoHandlerAdapter m_handler;
	private boolean m_connected = false;
	private SocketJSONConf m_clientconf;
	
	private ConcurrentLinkedQueue<Long> m_dealtimeQueue;
	private static final int DEALIME_MAX = 5;
	private static final int MAX_SESSION_ID = 65535;
	private StatusThread m_statThread;
	private ArrayList<Observer> m_observer; 
	
	private int m_sessionID;
	
	private ConcurrentHashMap<String, IoSession> m_sessionMap;
	private final Logger LOGGER = LoggerFactory.getLogger(HsmSocket.class);
	
	private class HsmSockFutureListner implements IoFutureListener {
		@Override
		public void operationComplete(IoFuture future) {
			System.out.println("HsmSockFutureListner operationComplete");
			if(future.getSession().isConnected()) {
	        	m_status = SockStatus.FREE;
	        	
	        	synchronized(m_worklock) {
	        		System.out.println("HsmSockFutureListner connected");
	        		m_connected = true;
	        	}
			}
		}
	}
	
	//according to message header 1111..
	private class HsmSocketHandler extends IoHandlerAdapter {
		@Override
		public void sessionOpened(IoSession session) throws Exception {
			System.out.println("session opened=" + session.getRemoteAddress().toString());
		}
		
		@Override
		public void messageReceived(IoSession session, Object message) throws Exception {
			System.out.println("message Received=" + message.toString());
			
			IoSession _session_ = m_sessionMap.get(message.toString().substring(0, 4));
			if(_session_ != null) {
				_session_.write(message.toString());
			}
	    }
		
		public void sessionClosed(IoSession session) throws Exception {
			System.out.println("session closed");
	    }

	    public void messageSent(IoSession session, Object message) throws Exception {
	    	System.out.println("message sent=" + message.toString());
	    }
	}
	
	private class StatusThread extends Thread implements Observer {
		boolean m_run = true;
		
		public StatusThread() {
			super();
			m_run = false;
		}
		
		public void run() {
			while(m_run) {
				synchronized(m_statlock) {
					if(m_dealtimeQueue.size() == DEALIME_MAX) {
						m_status = SockStatus.BUSY;
					}
				}
			}
		}
		
		public void begin() {
			m_run = true;
		}
		
		public void end() {
			m_run = false;
		}

		@Override
		public void update(Observable o, Object arg) {
			run();
		}
	}
	
	public HsmSocket(String ip, int port, int timeout) {
		m_ip = ip;
		m_port = port;
		m_timeout = timeout;
		m_sessionID = 0x0000;
		
		m_connector = new NioSocketConnector();
		m_clientconf = new SocketJSONConf();
		m_dealtimeQueue = new ConcurrentLinkedQueue<Long>();
		m_statThread = new StatusThread();
		m_sessionMap = new ConcurrentHashMap<String, IoSession>();
	}
	
	public boolean init() throws Exception {
		m_clientconf.loadObject(JSONUtil.parserJSONArray(ResourceMngr.getServiceConfigData(GlobalVars.CLIENT_CFG)));
		m_connector.setConnectTimeoutMillis(m_clientconf.socketTimeout());
		
		m_connector.getSessionConfig().setReadBufferSize(m_clientconf.socketBufferSize());
		m_connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, m_clientconf.socketIdleTime());
		m_connector.addListener(new HsmServiceListener());
		
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
		
		addObserver(m_statThread);
		
		return true;
	}
	
	public void setupLoggerFilter() {
		m_connector.getFilterChain().addLast("logger", new LoggingFilter());
	}
	
	public void setupThreadsFilter() {
		m_connector.getFilterChain().addLast("threads", new ExecutorFilter(Executors.newCachedThreadPool()));
	}
	
	public void setupCodecFilter() {
		PrefixedStringCodecFactory codfilter = new PrefixedStringCodecFactory(Charset.forName(m_clientconf.codeflterEncoding()));
		codfilter.setEncoderPrefixLength(m_clientconf.codeflterEncodePrefix());
		codfilter.setDecoderPrefixLength(m_clientconf.codeflterDecodePrefix());
		m_connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(codfilter));
	}
	
	public void setupKeepaliveFilter() {
		KeepAliveFilter hsmKeepAliveFilterFactory = new KeepAliveFilter(new HsmKeepAliveFilterFactory(), IdleStatus.BOTH_IDLE);
		hsmKeepAliveFilterFactory.setRequestInterval(m_clientconf.keepaliveInterval());
		hsmKeepAliveFilterFactory.setRequestTimeout(m_clientconf.keepaliveTimeout());
		hsmKeepAliveFilterFactory.setForwardEvent(m_clientconf.keepaliveForward());
		m_connector.getFilterChain().addLast("keepalive", hsmKeepAliveFilterFactory);
	}
	
	protected boolean connect() throws Exception {
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
			synchronized(m_worklock) {
				if(m_connected) {
					m_session = m_future.getSession();
					m_session.getConfig().setUseReadOperation(true);
					
					System.out.println("HsmSocket connected to " + m_ip + ":" + m_port);
					return m_connected;
				}
				else {
					//System.out.println("HsmSocket not connected");
				}
			}
		}
		
		//return false;
	}
	
	protected int disconnect() {
		IoSession session = m_future.getSession();
		session.close(true);
		m_status = SockStatus.FREE;
		 
		return 0;
	}
	
	protected int process(IoSession requestSession, String request) {
		String s = String.format("000%s", Integer.toHexString(m_sessionID).toUpperCase());
		String ss = s.substring(s.length()-4);
		
		if(m_sessionMap.containsKey(ss)) {
			m_sessionMap.replace(ss, requestSession);
		}
		else {
			m_sessionMap.put(ss, requestSession);
		}
		
		request = ss + request.substring(4, request.length());
		
		WriteFuture wfuture = m_session.write(request);
		wfuture.addListener(new HsmSockFutureListner());
		
		if(m_sessionID == MAX_SESSION_ID) {
			m_sessionID = 0x0000;
		}
		else {
			m_sessionID += 1;
		}
		
		return request.length();
	}
	
	public SockStatus getStatus() {
		synchronized(m_statlock) {
			return m_status;
		}
	}
	
	public void close() {
		if(m_connector != null) {
			m_connector.dispose();
		}
		
		disconnect();
		deleteObservers();
	}
	
	//CHECK may cause a bug 
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
}
