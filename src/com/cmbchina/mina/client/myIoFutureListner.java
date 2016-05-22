package com.cmbchina.mina.client;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;


public class myIoFutureListner implements IoFutureListener {

	@Override
	public void operationComplete(IoFuture future) {
		// TODO Auto-generated method stub
		System.out.println("myIoFutureListner operationComplete");
	}
}
