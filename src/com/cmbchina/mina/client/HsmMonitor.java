package com.cmbchina.mina.client;

public class HsmMonitor {
	private String appName;
	private float averageHandleTime=-1;
	private float Last1MinHandlerTime=-1;
	private float Last1HourHandlerTime=-1;
	private float Last1DayHandlerTime=-1;
	
	private long num;
	
	public void calculate(String name)
	{
		appName = name;
	}
	
	public void calculateTime(long costTime)
	{
		//同步保护
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
