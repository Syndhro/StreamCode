package com;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
	
public interface ServerInterface extends Remote {
	
	public abstract void registerUser(String username, String password) throws RemoteException;
	public abstract void unregisterUser(User user, String password) throws RemoteException;
	public abstract User login(String username, String password) throws RemoteException;
	public abstract void logout(User user) throws RemoteException;
	public abstract void addProject(String title, String description, String category, User user) throws RemoteException;
	public abstract void removeProject(Project projectId) throws RemoteException;
	public abstract void addActivity(Project project, String name, String description, String place, String dateTime) throws RemoteException;
	public abstract void removeActivity(Project project, Activity activity) throws RemoteException;
	public abstract void addFriend(User user1, User user2) throws RemoteException;
	public abstract void removeFriend(User user, User user2) throws RemoteException;
	public abstract ArrayList<User> searchUser(String string) throws RemoteException;
	public abstract void addCollaborators(Project project, ArrayList<User> users) throws RemoteException;
	public abstract void removeCollaborator(Project project, User user) throws RemoteException;
	public abstract void addAgent(Activity activity, User user) throws RemoteException;
	public abstract void removeAgent(Activity activity, User user) throws RemoteException;
	public abstract void stampa()throws RemoteException;
	public abstract void startProject(Project project) throws RemoteException;
	public abstract void completeActivity(Activity a) throws RemoteException;
}
