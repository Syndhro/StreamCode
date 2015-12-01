package com;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Server{
	
	private final static ServerImpl server = ServerImpl.getInstance();
	
	public void startup() throws RemoteException, MalformedURLException, NotBoundException {

		server.retrieveAllUsers();
		server.retrieveAllProjects();
		server.retrieveAllActivities();
		server.retrieveAllNotifications();
		
		server.linkProjectsToCollaborators();
		server.linkProjectsToAdmins();
		server.linkActivitiesToUsers();
		server.linkFriends();
	    Naming.rebind("server", server);
	}
	
	public void addInterfaceObserver(ServerInterfaceObserver serverInterface){
		server.addInterfaceObserver(serverInterface);
	}
	
	public ServerImpl getServerImpl(){
		return server;
	}
	
}
