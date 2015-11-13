package com;

import java.rmi.Remote;
import java.util.ArrayList;

public interface ServerInterface extends Remote {

	public abstract void registerUser(String username, String password);
	public abstract void unregisterUser(User user);
	public abstract User login(String username, String password);
	public abstract void logout(User user);
	public abstract void addProject(String title, String description, String category, User user);
	public abstract void removeProject(int projectId);
	public abstract void removeActivity(Project project, Activity activity);
	public abstract ArrayList<String> searchUser(String string);
	public abstract void addFriend(User user1, User user2);
	public abstract void removeFriend(User user, User user2);
	public abstract void addActivity(Project project, String name, String description, String place, String dateTime);
	
}
