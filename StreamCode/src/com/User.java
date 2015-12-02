package com;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private int userId;
	private String username;
	private ArrayList<Project> collaborationProjects;
	private ArrayList<Project> managedProjects;
	private ArrayList<Activity> userActivities;
	private ArrayList<User> userFriends;
	private ArrayList<Notification> userNotifications;
	

	public User(int id, String username){
		this.userId = id;
		this.username = username;
		collaborationProjects = new ArrayList<Project>();
		managedProjects = new ArrayList<Project>();
		userActivities = new ArrayList<Activity>();
		userFriends = new ArrayList<User>();
	}

	//ADDERS

	public void addCollaborationProject(Project project) {
		this.collaborationProjects.add(project);
	}
	
	public void addManagedProject(Project project) {
		this.managedProjects.add(project);
	}

	public void addUserActivities(Activity activity) {
		this.userActivities.add(activity);
	}
	
	public void addUserNotifications(Notification notification){
		this.userNotifications.add(notification);
	}

	public void addFriend(User user) {
		userFriends.add(user);	
	}
	
	//GETTERS
	public int getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public ArrayList<Project> getCollaborationProject() {
		return collaborationProjects;
	}
	
	public ArrayList<Project> getManagedProject() {
		return managedProjects;
	}

	public ArrayList<Activity> getUserActivities() {
		return userActivities;
	}
	
	public ArrayList<User> getUserFriends() {
		return userFriends;
	}
	
	public ArrayList<Notification> getUserNotification(){
		return userNotifications;
	}
}
