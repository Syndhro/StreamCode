package com;

import java.io.IOException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Client extends UnicastRemoteObject implements ClientInterface{
	
	private static final long serialVersionUID = 1L;
	Registry registry;
	static ServerInterface server;
	private int myUserId;
	private String myUsername;
	
	public Client()throws RemoteException{};
	
	@SuppressWarnings("deprecation")
	public void startup() throws NotBoundException, IOException {
  		System.setSecurityManager(new RMISecurityManager());

  		//locate registry and get an instance of remote server
  		registry = LocateRegistry.getRegistry("localhost");
  		server = (ServerInterface) registry.lookup("server");
  		
  	}
	
	//OBSERVER PATTERN
	@Override
	public void getNotification(Notification notification) throws RemoteException {
		JOptionPane.showMessageDialog(null, notification.getMessage());
	}
	
	public void registerUser(String username, String password) throws RemoteException {
		server.registerUser(username, password);
	}
	
	
	public void unregisterUser(int userId, String password) throws RemoteException {
		server.unregisterUser(userId, password, this);
	}
	
	
	public void login(String username, String password) throws RemoteException {
		myUserId = server.login(username, password, (ClientInterface) this);
		myUsername = username;
	}
	
	public int check(String username, String password) throws RemoteException{
		return server.check(username, password);
	}
	
	public void logout() throws RemoteException {
		server.logout(this);
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
		server.removeActivity(projectId, activityId, this.myUserId);
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
	
	public void modifyProject(int projectId, String title, String description, Category category) throws RemoteException {
		server.modifyProject(projectId, title, description, category);
	}
	
	public void modifyActivity(int activityId, String name, String description, String place, String dateTime) throws RemoteException {
		server.modifyActivity(activityId, name, description, place, dateTime);
	}

	public void completeActivity(int activityId, int userId) throws RemoteException {
		server.completeActivity(activityId, userId);
	}
	
	public int getClientId() throws RemoteException{
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
	
	public String getMyUsername(){
		return myUsername;
	}
	
	public Project getProjectById(int projectId) throws RemoteException{
		Project project = null;
		project = server.getProjectById(projectId);
		return project;
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
	
	public ArrayList<Project> getCollaborationProject(){
		ArrayList<Project> collaborationProject = new ArrayList<Project>();
		try{
			
			collaborationProject = server.getCollaborationProjects(myUserId);
		}catch(RemoteException e){
			e.printStackTrace();
		}
		return collaborationProject;
	}
	
	public ArrayList<Activity> getMyActivities(){
		ArrayList<Activity> activities = new ArrayList<Activity>();
		try {
			activities = server.getMyActivities(myUserId);
		} catch (RemoteException e) {
			e.printStackTrace();
		} 
		return activities;
	}
	
	public ArrayList<Notification> getOfflineNotifications(){
		ArrayList<Notification> myOfflineNotifications = new ArrayList<Notification>();
		try{
			myOfflineNotifications = server.getOfflineNotificationsById(myUserId);
		}catch(RemoteException e){
			e.printStackTrace();
		}
		return myOfflineNotifications;
	}

	public void sendBroadcast(String message, ArrayList<Integer> ids){
		try {
			server.sendBroadcast(message, ids);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void notifyAdmin(String message, int adminId){
		try {
			server.notifyAdmin(message, adminId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
