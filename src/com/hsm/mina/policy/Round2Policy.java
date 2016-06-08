package com.hsm.mina.policy;

import java.util.ArrayList;
import java.util.List;

import com.hsm.mina.client.HsmSocket;
import com.hsm.mina.enums.SockStatus;

public class Round2Policy {
	private int m_lastItemID;
	private HsmSocket m_lastItem;
	private List<HsmSocket> m_itemList;
	private int m_bound;
	
	public Round2Policy(List<HsmSocket> list, int bound) {
		m_lastItem = new HsmSocket();
		m_lastItemID = 0;
		m_itemList = new ArrayList<HsmSocket>();
		m_bound = bound;
		m_itemList = list;
	}
	
	//CHECK we need to take out the object which is down status
	public synchronized HsmSocket route() {
		int id = m_lastItemID + 1;
		if(id == m_bound) {
			id = 0;
		}
		
		for(HsmSocket sock : m_itemList) {
			if(m_lastItemID == 0) {
				if(checkStatus(sock)) {
					m_lastItem = sock;
					m_lastItemID = m_itemList.indexOf(sock);
					return m_lastItem;
				}
			}
			else {
				if(id == m_lastItemID) {
					if(checkStatus(sock)) {
						m_lastItem = sock;
						m_lastItemID = m_itemList.indexOf(sock);
						return m_lastItem;
					}
					else {
						id = m_itemList.indexOf(sock) + 1;
					}
				}
			}
		}
		
		return null;
	}
	
	private boolean checkStatus(HsmSocket sock) {
		SockStatus stat = sock.getStatus();
		if(stat != SockStatus.BUSY && stat != SockStatus.DOWN) {
			return true;
		}
		
		return false;
	}
}
