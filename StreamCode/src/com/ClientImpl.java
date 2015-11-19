package com;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements ClientInterface{
	String name; 
	protected ClientImpl() throws RemoteException {
		super();
		this.name = "Aniel";
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getNotification(String string) throws RemoteException {
		System.out.println(string);		
	}
	
	public void printName(){
		System.out.println(name);
	}
	
}
