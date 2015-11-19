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

import com.mysql.jdbc.authentication.MysqlClearPasswordPlugin;

public class Client{
	
	Registry registry;
	static ServerInterface server;
	private int myUserId;
	
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


	public void unregisterUser(int userId, String password) throws RemoteException {
		server.unregisterUser(userId, password);
	}


	public void login(String username, String password) throws RemoteException {
		myUserId = server.login(username, password);
	}
	
	public int check(String username, String password) throws RemoteException{
		return server.check(username, password);
	}

	public void logout(int userId) throws RemoteException {
		server.logout(userId);
	}


	public void addProject(String title, String description, Category category, int userId) throws RemoteException {
		server.addProject(title, description, category , userId);
	}


	public void removeProject(int projectId) throws RemoteException {
		server.removeProject(projectId);	
	}


	public void addActivity(int projectId, String name, String description, String place, String dateTime)
		throws RemoteException {
		server.addActivity(projectId, name, description, place, dateTime);
	}


	public void removeActivity(int projectId, int activityId) throws RemoteException {
		server.removeActivity(projectId, activityId);
	}


	public void addFriend(int userId1, int userId2) throws RemoteException {
		server.addFriend(userId1, userId2);
	}


	public void removeFriend(int userId1, int userId2) throws RemoteException {
		server.removeFriend(userId1, userId2);
	}


	public ArrayList<User> searchUser(String string) throws RemoteException {
		ArrayList<User> searched = new ArrayList<User>();
		searched = server.searchUser(string);
		return searched;
	}


	public void addCollaborators(int projectId, ArrayList<Integer> userIds) throws RemoteException {
		server.addCollaborators(projectId, userIds);
	}


	public void removeCollaborator(int projectId, int userId) throws RemoteException {
		server.removeCollaborator(projectId, userId);
	}


	public void addAgent(int activityId, int userId) throws RemoteException {
		server.addAgent(activityId, userId);
	}


	public void removeAgent(int activityId, int userId) throws RemoteException {
		server.removeAgent(activityId, userId);
	}

	public void startProject(int projectId) throws RemoteException {
		server.startProject(projectId);
	}

	public void completeActivity(int activityId) throws RemoteException {
		server.completeActivity(activityId);
	}
	
	public int getMyUserId(){
		return myUserId;
	}
	
	public ArrayList<User> getUserFriends(){
		ArrayList<User> userFriends = new ArrayList<User>();
		try{
			userFriends = server.getUserFriend(myUserId);
		}catch(RemoteException e){
			e.printStackTrace();
		}
		return userFriends;
	}
	
	public void stampa() throws RemoteException {
		server.stampa();
	}
	
	public ArrayList<Project> getManagedProject(){
		ArrayList<Project> managedProject = new ArrayList<Project>();
		try{
			
			managedProject = server.getManagedProject(myUserId);
		}catch(RemoteException e){
			e.printStackTrace();
		}
		return managedProject;
	}
	
	public ArrayList<Notification> getNotifications(){
		ArrayList<Notification> myNotifications = new ArrayList<Notification>();
		try{
			myNotifications = server.getNotificationsById(myUserId);
		}catch(RemoteException e){
			e.printStackTrace();
		}
		return myNotifications;
	}

	
	
	public static void main(String[] args) throws RemoteException, NotBoundException{
		ClientImpl clientImpl = new ClientImpl();
		clientImpl.printName();
		Registry registry = LocateRegistry.getRegistry("localhost");
  		server = (ServerInterface) registry.lookup("server");
  		
  		server.registerClient(clientImpl);
  		
  		
		/*
		Client client = new Client();
		try{
			client.startup();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			int verifier = client.check("andreavaghi", "vaghi");
			if (verifier == -1)
				System.out.println("Wrong Username");
			else if (verifier == -2)
				System.out.println("Wrong Password");
			else
				client.login("andreavaghi", "vaghi");
			
			ArrayList<Notification> notif = client.getNotifications();
			
			for(int i = 0; i < notif.size(); i++){
				System.out.println(notif.get(i).getMessage());
			}
			
			client.stampa();
			
			client.addProject("Comple Spe", "A casa di spe", Category.getCategory("sport"), client.getMyUserId());
			
			client.addActivity(4, "Fare la spesa", "Andare al supermercato a fare la spesa", "Esselunga", "Venerd�");
			client.addActivity(4, "Preparare DJ Set", "Syndrome prepara le bombe", "Casa sua", "Sabato");
			
			client.addFriend(client.getMyUserId(), 17);
			client.addFriend(client.getMyUserId(), 18);
			
			ArrayList<Integer> userIds = new ArrayList<Integer>();
			userIds.add(17);
			userIds.add(18);
			
			client.addCollaborators(4, userIds);
			client.addAgent(1, 18);
			client.addAgent(1, 17);
			
			User user = server.getUserById(16);
			
			ArrayList<Project> managed = client.getManagedProject();
			
			for(int i = 0; i < managed.size(); i++){
				System.out.println(managed.get(i).getTitle());
			}
			
			client.stampa();
			
		}catch(RemoteException e){
			e.printStackTrace();
		}
		*/
	}
	
	
}
