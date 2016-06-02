package com.cmbchina.mina.client;

import java.util.ArrayList;
import java.util.Iterator;

public class Subject
{    
	//保存所有注册的观察者对像的引用。
	private ArrayList observers = new ArrayList();

	//注册观察者
	public void attach(Observer observer) {
		observers.add(observer);
	}

	//删除已注册的观察者
	public void detach(Observer observer) {
		observers.remove(observer);
	}

	//通知所有的观察者
	public void notifyObservers() {
		Iterator it = observers.iterator();
		while (it.hasNext()) {
			((Observer) it.next()).update(this);
		}
	}
}

// ConcreteSubject.java 具体主题（ConcreteSubject）角色
public class ConcreteSubject extends Subject {
    private String state;
	
	//状态发生改变时通知所有的观察者
	public void change(String newState)	{
		this.state = newState;
		notifyObservers();      
	}

	public String getState() {
		return state;
	}
}

// Observer.java抽象观察者（Observer）角色
public interface Observer {
    void update(Subject subject);
}



// ConcreteObserver.java具体观察者（ConcreteObserver）角色
public class ConcreteObserver implements Observer {
	public void update(Subject subject)	{
		//put your code here
	}
} 