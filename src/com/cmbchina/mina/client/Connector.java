package com.cmbchina.mina.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.cmbchina.mina.filter.keepalive.HsmKeepAliveFilterFactory;


public class Connector {
	private String m_ip;
	private int m_port;
	
	public static void main(String[] args) {
		IoSession session;
		ConnectFuture connectFuture;
		final IoConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(5000);
		
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		
		connector.getFilterChain().addLast("exec", new ExecutorFilter());
		
		PrefixedStringCodecFactory pre_filter = new PrefixedStringCodecFactory(Charset.forName("ISO-8859-1"));
		pre_filter.setEncoderPrefixLength(2);
		pre_filter.setDecoderPrefixLength(2);
		connector.getFilterChain().addLast("codec",	new ProtocolCodecFilter(pre_filter));
		
		KeepAliveFilter HsmKeepAliveFilter = new KeepAliveFilter(new HsmKeepAliveFilterFactory(), IdleStatus.BOTH_IDLE);
		HsmKeepAliveFilter.setRequestInterval(3);
		HsmKeepAliveFilter.setRequestTimeout(5);
		HsmKeepAliveFilter.setForwardEvent(true);
		connector.getFilterChain().addLast("keepAlive", HsmKeepAliveFilter);
		
		connector.setHandler(new TestHandler());
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 5);
		
		connectFuture = connector.connect(new InetSocketAddress("99.12.38.74", 82));
		//connectFuture.awaitUninterruptibly(5000);
		//connectFuture.addListener(new _HsmFutureListner());
		/*
		if(connectFuture.isConnected()) {
			System.out.println("connect succ");
			session = connectFuture.getSession();
		}
		else {
			System.out.println("connect failed");
		}
		
		
		while(true) {
			if(session == null || !session.isConnected()) {
				
				connector.dispose();
				if(connector.isDisposed()) {
					System.out.println("connector isDisposed");
				}
				
				
				connectFuture = connector.connect(new InetSocketAddress("99.12.38.74", 81));
				connectFuture.awaitUninterruptibly();
				if(connectFuture.isConnected()) {
					session = connectFuture.getSession();
				}
			}
		}
		*/
	}
}
