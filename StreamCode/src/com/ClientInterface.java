package com;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
	public void getNotification(String string) throws RemoteException;
}
