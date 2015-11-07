package com;

public interface Subject {

	public abstract void registerObserver(Observer o);
	public abstract void removeObserver(Observer o);
	public abstract void notifyObservers();

}
