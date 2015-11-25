package com;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server{
	
	private final static ServerImpl server = ServerImpl.getInstance();
	
  public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {

    server.retrieveAllUsers();
	server.retrieveAllProjects();
	server.retrieveAllActivities();
	server.retrieveAllNotifications();
	
	server.linkProjectsToCollaborators();
	server.linkProjectsToAdmins();
	server.linkActivitiesToUsers();
	server.linkFriends();
    Naming.rebind("server", server);
    System.out.println("Press enter to terminate");
    try {
      System.in.read();
    } catch (IOException e) {
      /* Don't care */
    }
    Naming.unbind("server");
    UnicastRemoteObject.unexportObject(server, false);
  }
}
