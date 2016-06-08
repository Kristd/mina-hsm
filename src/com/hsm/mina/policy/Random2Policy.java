package com.hsm.mina.policy;

import java.util.List;
import java.util.Random;

import com.hsm.mina.client.HsmSocket;
import com.hsm.mina.enums.SockStatus;

public class Random2Policy {
	private long m_lastSeed;
	private long m_seed;
	private int m_lastItemID;
	private HsmSocket m_lastItem;
	private Random m_random;
	private int m_bound;
	private int m_lastRes;
	private List<HsmSocket> m_itemList;
	
	public Random2Policy(List<HsmSocket> list, int bound) {
		m_lastSeed = 0;
		m_seed = 0;
		m_lastRes = 0;
		m_bound = bound;
		m_lastItemID = 0;
		m_lastItem = new HsmSocket();
		m_itemList = list;
	}

	public synchronized HsmSocket route() {
		HsmSocket sock = null;
		m_seed = m_lastSeed;
		
		while(true) {
			while(m_lastSeed == m_seed) {
				m_seed = System.nanoTime();
			}
			m_lastSeed = m_seed;
			
			m_random = new Random(m_seed);
			m_lastRes = m_random.nextInt(m_bound);
			sock = (HsmSocket) m_itemList.get(m_lastRes);
			
			if(checkStatus(sock)) {
				m_lastItem = sock;
				m_lastItemID = m_lastRes;
				break;
			}
		}
		
		return sock;
	}
	
	private boolean checkStatus(HsmSocket sock) {
		SockStatus stat = sock.getStatus();
		if(stat != SockStatus.BUSY && stat != SockStatus.DOWN) {
			return true;
		}
		
		return false;
	}
}
