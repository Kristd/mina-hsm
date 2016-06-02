package com.cmbchina.mina.policy;

import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLEngineResult.Status;

import com.cmbchina.mina.abstracts.IoPolicy;
import com.cmbchina.mina.client.HsmClient;
import com.cmbchina.mina.enums.HsmStatus;

public class RandomPolicy implements IoPolicy {
	private long m_lastSeed;
	private long m_seed;
	
	private int m_lastRes;
	
	private Random m_random;
	private Object m_lock = new Object();
	
	public RandomPolicy() {
		m_lastSeed = 0;
		m_seed = 0;
		m_lastRes = 0;
	}

	public Object route(Map<?, ?> map, int bound) {
		HsmClient client = null;
		
		synchronized(m_lock) {
			while(true) {
				while(m_lastSeed == m_seed) {
					m_seed = System.nanoTime();
				}
				m_lastSeed = m_seed;
				
				m_random = new Random(m_seed);
				m_lastRes = m_random.nextInt(bound);
				client = (HsmClient) map.get(String.valueOf(m_lastRes));
				
				if(!client.getStatus().equals(HsmStatus.BUSY)) {
					break;
				}
			}
			
			return client;
		}
	}
}
