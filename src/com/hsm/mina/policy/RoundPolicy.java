package com.hsm.mina.policy;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.hsm.mina.abstracts.IoPolicy;
import com.hsm.mina.client.HsmClient;
import com.hsm.mina.enums.HsmStatus;

public class RoundPolicy implements IoPolicy {
	private int m_lastItemID;
	private HsmClient m_lastItem;
	private List<HsmClient> m_itemList;
	private int m_bound;
	
	public RoundPolicy(Map<String, HsmClient> map, int bound) {
		m_lastItem = new HsmClient();
		m_lastItemID = 0;
		m_itemList = new CopyOnWriteArrayList<HsmClient>();
		m_bound = bound;
	}
	
	//CHECK we need to take out the object which is down status
	public synchronized HsmClient route() {
		int id = m_lastItemID + 1;
		if(id == m_bound) {
			id = 0;
		}
		
		for(HsmClient entry : m_itemList) {
			if(m_lastItemID == 0) {
				if(checkStatus(entry)) {
					m_lastItem = entry;
					m_lastItemID = m_itemList.indexOf(entry);
					return m_lastItem;
				}
			}
			else {
				if(id == m_itemList.indexOf(entry)) {
					if(checkStatus(entry)) {
						m_lastItem = entry;
						m_lastItemID = m_itemList.indexOf(entry);
						return m_lastItem;
					}
					else {
						id =  m_itemList.indexOf(entry) + 1;
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
