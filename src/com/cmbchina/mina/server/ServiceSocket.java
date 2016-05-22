package com.cmbchina.mina.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.cmbchina.mina.abs.IoSocket;
import com.cmbchina.mina.conf.SocketJSONConf;
import com.cmbchina.mina.filter.keepalive.HsmKeepAliveFilterFactory;
import com.cmbchina.mina.utils.GlobalVars;
import com.cmbchina.mina.utils.JSONUtil;


public class ServiceSocket extends IoSocket {
	private static ServiceSocket m_instant = new ServiceSocket();
	private static IoAcceptor m_acceptor = null;
	private static IoHandlerAdapter m_handler = null;
	
	private static String m_ip;
	private static int m_port;
	private static int m_maxConn;
	
	private static Object m_lock = new Object();
	
	private ServiceSocket() {
		return;
	}
	
	public ServiceSocket instance() {
		if() {
			;
		}
	}
	
	public void init() throws Exception {
		SocketJSONConf sockConf = new SocketJSONConf();
		sockConf.loadObject(JSONUtil.parserJSONArray(ResourceMngr.getServiceConfigData(GlobalVars.SOCKET_CFG)));
		
		m_ip = sockConf.getSocketAddr();
		m_port = sockConf.getSocketPort();
		m_maxConn = sockConf.getSocketMaxConn();
		
		m_acceptor = new NioSocketAcceptor();
		m_acceptor.getSessionConfig().setReadBufferSize(GlobalVars.MAX_BUFF_SIZE);
		m_acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, GlobalVars.ACCEPTOR_IDLE_TIME);
		
		m_handler = new ServiceHandler();
		m_acceptor.setHandler(m_handler);
		
		setupLoggerFilter();
		setupThreadsFilter();
		setupCodecFilter();
		setupKeepaliveFilter();
	}
	
	private static void setupLoggerFilter() {
		m_acceptor.getFilterChain().addLast("logger", new LoggingFilter());
	}
	
	private static void setupThreadsFilter() {
		m_acceptor.getFilterChain().addLast("threads", new ExecutorFilter(Executors.newCachedThreadPool()));
	}
	
	private static void setupCodecFilter() {
		PrefixedStringCodecFactory codfilter = new PrefixedStringCodecFactory(Charset.forName(GlobalVars.ENCODING));
		codfilter.setEncoderPrefixLength(GlobalVars.CODE_PREFIX);
		codfilter.setDecoderPrefixLength(GlobalVars.CODE_PREFIX);
		m_acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(codfilter));
	}
	
	private static void setupKeepaliveFilter() {
		KeepAliveFilter ServKeepAliveFilterFactory = new KeepAliveFilter(new HsmKeepAliveFilterFactory(), IdleStatus.BOTH_IDLE);
		ServKeepAliveFilterFactory.setRequestInterval(GlobalVars.ACCEPTOR_KA_INTERVAL);
		ServKeepAliveFilterFactory.setRequestTimeout(GlobalVars.ACCEPTOR_KA_TIMEOUT);
		ServKeepAliveFilterFactory.setForwardEvent(GlobalVars.ACCEPTOR_KA_FORWARD);
		m_acceptor.getFilterChain().addLast("keepalive", ServKeepAliveFilterFactory);
	}
	
	public static void listen() throws IOException {
		m_acceptor.bind(new InetSocketAddress(m_port));
		System.out.println("[" + m_port + "]service started..");
	}
	
	public static void close() {
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
