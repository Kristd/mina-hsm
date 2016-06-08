package com.hsm.mina.server;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;

public class ServiceFutureListener implements IoFutureListener {

	@Override
	public void operationComplete(IoFuture future) {
		// TODO Auto-generated method stub
		System.out.println(future.getSession().getRemoteAddress());
	}
}
