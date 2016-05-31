package com.cmbchina.mina.policy;

import java.util.Map;
import java.util.Random;

import com.cmbchina.mina.abstracts.IoPolicy;

public class RoundPolicy extends IoPolicy {
	private long m_lastSeed;
	private long m_seed;
	private Random m_random;
	private long m_lastRes;
	
	public RoundPolicy() {
		m_lastSeed = m_seed = System.nanoTime();
		m_random = new Random(m_seed);
	}
	
	public Object doReq(Map<Object, Object> map, int max) {
		long res = m_random.nextInt(max);
		return map;
	}
}
