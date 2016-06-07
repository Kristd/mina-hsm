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
	private int m_lastItemID;
	private int m_lastItem;
	private Random m_random;
	private int m_bound;
	private int m_lastRes;
	
	public RandomPolicy(Map<String, HsmClient> map, int bound) {
		m_lastSeed = 0;
		m_seed = 0;
		m_lastRes = 0;
	}

	public synchronized HsmClient route() {
		HsmClient client = null;
		
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
	
	private boolean checkStatus(HsmClient client) {
		HsmStatus stat = client.getStatus();
		if(stat != HsmStatus.BUSY && stat != HsmStatus.DOWN) {
			return true;
		}
		
		return false;
	}
}
