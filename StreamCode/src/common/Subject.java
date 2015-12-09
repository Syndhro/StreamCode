package common;

import java.rmi.RemoteException;

import server.Notification;

public interface Subject {
	
	public abstract void registerClient(ClientInterface o) throws RemoteException;
	public abstract void unregisterClient(ClientInterface o) throws RemoteException;
	public abstract void notifyUser(ClientInterface o, Notification n) throws RemoteException;//PSEUDO
	
}
