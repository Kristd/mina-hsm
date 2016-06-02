package com.cmbchina.mina.client;

import java.util.ArrayList;
import java.util.Iterator;

public class Subject
{    
	//��������ע��Ĺ۲��߶�������á�
	private ArrayList observers = new ArrayList();

	//ע��۲���
	public void attach(Observer observer) {
		observers.add(observer);
	}

	//ɾ����ע��Ĺ۲���
	public void detach(Observer observer) {
		observers.remove(observer);
	}

	//֪ͨ���еĹ۲���
	public void notifyObservers() {
		Iterator it = observers.iterator();
		while (it.hasNext()) {
			((Observer) it.next()).update(this);
		}
	}
}

// ConcreteSubject.java �������⣨ConcreteSubject����ɫ
public class ConcreteSubject extends Subject {
    private String state;
	
	//״̬�����ı�ʱ֪ͨ���еĹ۲���
	public void change(String newState)	{
		this.state = newState;
		notifyObservers();      
	}

	public String getState() {
		return state;
	}
}

// Observer.java����۲��ߣ�Observer����ɫ
public interface Observer {
    void update(Subject subject);
}



// ConcreteObserver.java����۲��ߣ�ConcreteObserver����ɫ
public class ConcreteObserver implements Observer {
	public void update(Subject subject)	{
		//put your code here
	}
} 