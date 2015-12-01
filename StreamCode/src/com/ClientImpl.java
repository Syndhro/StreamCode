package com;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements ClientInterface{

	private static final long serialVersionUID = 1L;
	String name; 
	protected ClientImpl() throws RemoteException {
		super();
		this.name = "Aniel";
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getNotification(Notification notification) throws RemoteException {
		System.out.println(notification.getMessage());		
	}
	
	public void printName(){
		System.out.println(name);
	}

	@Override
	public int getClientId() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
