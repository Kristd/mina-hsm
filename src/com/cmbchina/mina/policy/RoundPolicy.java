package com.cmbchina.mina.policy;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.cmbchina.mina.abstracts.IoPolicy;
import com.cmbchina.mina.client.HsmClient;
import com.cmbchina.mina.enums.HsmStatus;

public class RoundPolicy implements IoPolicy {
	private String m_lastItemID;
	private HsmClient m_lastItem;
	private Map<String, HsmClient> m_itemMap;
	private int m_bound;
	
	public RoundPolicy(Map<String, HsmClient> map, int bound) {
		m_lastItem = new HsmClient();
		m_lastItemID = "";
		m_itemMap = new ConcurrentHashMap<String, HsmClient>();
		m_bound = bound;
	}
	
	//CHECK we need to take out the object which is down status
	public synchronized HsmClient route() {
		int id = Integer.parseInt(m_lastItemID) + 1;
		if(id == m_bound) {
			id = 0;
		}
		
		for(Entry<String, HsmClient> entry : m_itemMap.entrySet()) {
			if(m_lastItemID.equals("")) {
				if(checkStatus(entry.getValue())) {
					m_lastItem = entry.getValue();
					m_lastItemID = entry.getKey();
					return m_lastItem;
				}
			}
			else {
				if(id == Integer.parseInt(entry.getKey())) {
					if(checkStatus(entry.getValue())) {
						m_lastItem = entry.getValue();
						m_lastItemID = entry.getKey();
						return m_lastItem;
					}
					else {
						id = Integer.parseInt(entry.getKey()) + 1;
					}
				}
			}
		}
		
		return null;
	}
	
	private boolean checkStatus(HsmClient client) {
		HsmStatus stat = client.getStatus();
		if(stat != HsmStatus.BUSY && stat != HsmStatus.DOWN) {
			return true;
		}
		
		return false;
	}
}
