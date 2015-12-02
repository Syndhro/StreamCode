package com;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Server{
	
	private static ServerImpl server;
	
	public Server(){
		server = ServerImpl.getInstance();
	};
	
	public void startup() throws RemoteException, MalformedURLException, NotBoundException {
		server.restoreFromDatabase();
	    Naming.rebind("server", server);
	}
	
	public void addInterfaceObserver(ServerInterfaceObserver serverInterface){
		server.addInterfaceObserver(serverInterface);
	}
	
	public ServerImpl getServerImpl(){
		return server;
	}
	
}
