package com.hsm.mina.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hsm.mina.abstracts.IoSocket;
import com.hsm.mina.client.HsmClientPool;
import com.hsm.mina.client.HsmPoolFactory;
import com.hsm.mina.conf.SocketJSONConf;
import com.hsm.mina.filter.keepalive.HsmKeepAliveFilterFactory;
import com.hsm.mina.utils.GlobalVars;
import com.hsm.mina.utils.JSONUtil;


public class ServiceSocket implements IoSocket {
	private static IoAcceptor m_acceptor;
	private static IoHandlerAdapter m_handler;
	
	private static String m_ip;
	private static int m_port;
	private static int m_nConnection;
	
	SocketJSONConf m_serviceconf;
	
	private static Object m_lock = new Object();
	
	private static class InstanceHolder {
		static final ServiceSocket INSTANCE = new ServiceSocket();
	}
	
	private ServiceSocket() {
		m_serviceconf = new SocketJSONConf();
		m_acceptor = new NioSocketAcceptor();
	}
	
	public static ServiceSocket instance() {
		return InstanceHolder.INSTANCE;
	}
	
	private class ServiceHandler extends IoHandlerAdapter {
		private final Logger LOGGER = LoggerFactory.getLogger(ServiceHandler.class);

		@Override
	    public void sessionOpened(IoSession session) throws Exception {
			System.out.println("ServiceHandler sessionOpened");
	    }

		@Override
	    public void sessionClosed(IoSession session) throws Exception {
			System.out.println("ServiceHandler sessionClosed");
	    }

		@Override
	    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
			throw new Exception("Exception caught in ServiceHandler session=" + session.toString() + " - " + cause.getMessage());
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
			
			//pass the request session
			HsmPoolFactory.loadPoolManager(appname).getHSM().request(session, jobname, request);
		}
	}
	
	public boolean init() throws Exception {
		m_serviceconf.loadObject(JSONUtil.parserJSONArray(ResourceMngr.getServiceConfigData(GlobalVars.SERVICE_CFG)));
		
		m_ip = m_serviceconf.socketAddr();
		m_port = m_serviceconf.socketPort();
		m_nConnection = m_serviceconf.socketMaxConn();
		
		m_acceptor.getSessionConfig().setReadBufferSize(m_serviceconf.socketBufferSize());
		m_acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, m_serviceconf.socketIdleTime());
		
		if(m_serviceconf.logger()) {
			setupLoggerFilter();
		}
		
		if(m_serviceconf.threadPool()) {
			setupThreadsFilter();
		}
		
		if(m_serviceconf.codeflter()) {
			setupCodecFilter();
		}
		
		if(m_serviceconf.keepalive()) {
			setupKeepaliveFilter();
		}
		
		m_handler = new ServiceHandler();
		m_acceptor.setHandler(m_handler);
		
		return true;
	}
	
	public void setupLoggerFilter() {
		m_acceptor.getFilterChain().addLast("logger", new LoggingFilter());
	}
	
	public void setupThreadsFilter() {
		m_acceptor.getFilterChain().addLast("threads", new ExecutorFilter(Executors.newCachedThreadPool()));
	}
	
	public void setupCodecFilter() {
		PrefixedStringCodecFactory codfilter = new PrefixedStringCodecFactory(Charset.forName(m_serviceconf.codeflterEncoding()));
		codfilter.setEncoderPrefixLength(m_serviceconf.codeflterEncodePrefix());
		codfilter.setDecoderPrefixLength(m_serviceconf.codeflterDecodePrefix());
		m_acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(codfilter));
	}
	
	public void setupKeepaliveFilter() {
		KeepAliveFilter servKeepAliveFilterFactory = new KeepAliveFilter(new HsmKeepAliveFilterFactory(), IdleStatus.BOTH_IDLE);
		servKeepAliveFilterFactory.setRequestInterval(m_serviceconf.keepaliveInterval());
		servKeepAliveFilterFactory.setRequestTimeout(m_serviceconf.keepaliveTimeout());
		servKeepAliveFilterFactory.setForwardEvent(m_serviceconf.keepaliveForward());
		m_acceptor.getFilterChain().addLast("keepalive", servKeepAliveFilterFactory);
	}
	
	public void listen() throws IOException {
		m_acceptor.addListener(new ServiceServiceListener());
		m_acceptor.bind(new InetSocketAddress(m_port));
		
		System.out.println("service started on port[" + m_port + "]");
	}
	
	public void close() {
		if(m_acceptor != null) {
			m_acceptor.dispose();
		}
	}
	
	//CHECK may cause a bug 
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
}
