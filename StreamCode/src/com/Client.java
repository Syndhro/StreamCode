package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Client{
	
	Registry registry;
	ServerInterface server;
	User myUser;
	
	public Client(){};
	
	public void startup() throws NotBoundException, IOException {
  		System.setSecurityManager(new RMISecurityManager());

  		//locate registry and get an instance of remote server
  		registry = LocateRegistry.getRegistry("localhost");
  		server = (ServerInterface) registry.lookup("server");
  		
  	}


	public void registerUser(String username, String password) throws RemoteException {
		server.registerUser(username, password);
	}


	public void unregisterUser(User user, String password) throws RemoteException {
		server.unregisterUser(user, password);
	}


	public void login(String username, String password) throws RemoteException {
		myUser = server.login(username, password);
	}
	
	public int check(String username, String password) throws RemoteException{
		return server.check(username, password);
	}

	public void logout(User user) throws RemoteException {
		server.logout(user);
		this.myUser = null;
	}


	public void addProject(String title, String description, String category, User user) throws RemoteException {
		server.addProject(title, description, category, user);
	}


	public void removeProject(Project projectId) throws RemoteException {
		server.removeProject(projectId);	
	}


	public void addActivity(Project project, String name, String description, String place, String dateTime)
		throws RemoteException {
		server.addActivity(project, name, description, place, dateTime);
	}


	public void removeActivity(Project project, Activity activity) throws RemoteException {
		server.removeActivity(project, activity);
	}


	public void addFriend(User user1, User user2) throws RemoteException {
		server.addFriend(user1, user2);
	}


	public void removeFriend(User user, User user2) throws RemoteException {
		server.removeFriend(user, user2);
	}


	public ArrayList<User> searchUser(String string) throws RemoteException {
		ArrayList<User> searched = new ArrayList<User>();
		searched = server.searchUser(string);
		return searched;
	}


	public void addCollaborators(Project project, ArrayList<User> users) throws RemoteException {
		server.addCollaborators(project, users);
	}


	public void removeCollaborator(Project project, User user) throws RemoteException {
		server.removeCollaborator(project, user);
	}


	public void addAgent(Activity activity, User user) throws RemoteException {
		server.addAgent(activity, user);
	}


	public void removeAgent(Activity activity, User user) throws RemoteException {
		server.removeAgent(activity, user);
	}

	public void startProject(Project project) throws RemoteException {
		server.startProject(project);
	}

	public void completeActivity(Activity activity) throws RemoteException {
		server.completeActivity(activity);
	}
	
	public User getMyUser(){
		return myUser;
	}
	
	public ArrayList<Project> getMyProjects(){
		ArrayList<Project> projects = new ArrayList<Project>();
		projects = myUser.getManagedProject();
		return projects;		
	}
	
}
