package com;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
	
public interface ServerInterface extends Remote {
	
	public abstract void registerUser(String username, String password) throws RemoteException;
	public abstract void unregisterUser(int userId, String password, ClientInterface Client) throws RemoteException;
	public abstract int login(String username, String password, ClientInterface client) throws RemoteException;
	public abstract void logout(ClientInterface client) throws RemoteException;
	public abstract void addProject(String title, String description, Category category, int userId) throws RemoteException;
	public abstract void modifyProject(int projectId, String title, String description, Category category) throws RemoteException;
	public abstract void removeProject(int projectId) throws RemoteException;
	public abstract void addActivity(int projectId, String name, String description, String place, String dateTime) throws RemoteException;
	public abstract void modifyActivity(int activityId, String name, String description, String place, String dateTime) throws RemoteException;
	public abstract void removeActivity(int projectId, int activityId, int userId) throws RemoteException;
	public abstract void addFriend(int userId1, int userId2) throws RemoteException;
	public abstract void removeFriend(int userId1, int userId2) throws RemoteException;
	public abstract ArrayList<User> searchUser(String string) throws RemoteException;
	public abstract void addCollaborators(int projectId, ArrayList<Integer> userIds) throws RemoteException;
	public abstract void removeCollaborator(int projectId, int userId) throws RemoteException;
	public abstract void addAgent(int activityId, int userId) throws RemoteException;
	public abstract void removeAgent(int activityId, int userId) throws RemoteException;
	public abstract void stampa()throws RemoteException;
	public abstract void startProject(int projectId) throws RemoteException;
	public abstract void completeActivity(int activityId, int userId) throws RemoteException;
	public abstract int check(String username, String password) throws RemoteException;
	public abstract User getUserById(int userId)throws RemoteException;
	public abstract ArrayList<User> getUserFriend(int userId) throws RemoteException;
	public abstract ArrayList<Project> getManagedProject(int userId) throws RemoteException;
	public abstract ArrayList<Notification> getNotificationsById(int userId) throws RemoteException;
	public abstract ArrayList<Notification> getOfflineNotificationsById(int userId) throws RemoteException;
	public abstract void registerClient(ClientInterface client) throws RemoteException;
	public abstract Project getProjectById(int projectId) throws RemoteException;
	public abstract ArrayList<Project> getCollaborationProjects(int myUserId) throws RemoteException;
	public abstract void sendBroadcast(String description, ArrayList<Integer> ids)throws RemoteException;
	public abstract ArrayList<Activity> getMyActivities(int myUserId)throws RemoteException;
	public abstract void notifyAdmin(String message, int adminId) throws RemoteException;
}
