package com.hsm.mina.policy;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLEngineResult.Status;

import com.hsm.mina.abstracts.IoPolicy;
import com.hsm.mina.client.HsmClient;
import com.hsm.mina.enums.HsmStatus;

public class RandomPolicy implements IoPolicy {
	private long m_lastSeed;
	private long m_seed;
	private String m_lastItemID;
	private HsmClient m_lastItem;
	private Random m_random;
	private int m_bound;
	private int m_lastRes;
	private List<HsmClient> m_itemList;
	
	public RandomPolicy(List<HsmClient> list, int bound) {
		m_lastSeed = 0;
		m_seed = 0;
		m_lastRes = 0;
		m_bound = bound;
		
		m_lastItemID = "0";
		m_lastItem = new HsmClient();
		m_itemList = list;
	}

	public synchronized HsmClient route() {
		HsmClient client = null;
		m_seed = m_lastSeed;
		
		while(true) {
			while(m_lastSeed == m_seed) {
				m_seed = System.nanoTime();
			}
			m_lastSeed = m_seed;
			
			m_random = new Random(m_seed);
			m_lastRes = m_random.nextInt(m_bound);
			client = (HsmClient) m_itemList.get(m_lastRes);
			
			if(checkStatus(client)) {
				m_lastItem = client;
				m_lastItemID = String.valueOf(m_lastRes);
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
