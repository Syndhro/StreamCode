package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import server.Notification;

public interface ClientInterface extends Remote {
	public void getNotification(Notification notification) throws RemoteException;
	public int getClientId() throws RemoteException;
}
