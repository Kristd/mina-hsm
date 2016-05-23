package org.apache.mina.core.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculatorServer {
    private static final int PORT = 8181; 

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculatorServer.class); 

    public static void main(String[] args) throws IOException {
        IoAcceptor acceptor = new NioSocketAcceptor(); 

        acceptor.getFilterChain().addLast("logger", new LoggingFilter()); 
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8")))); 

        acceptor.setHandler(new CalculatorHandler()); 
        acceptor.bind(new InetSocketAddress(PORT));

        LOGGER.info("service start:" + PORT); 
    }
}
