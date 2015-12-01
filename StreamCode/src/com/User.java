package com;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Observer, Serializable {

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

	public void update(Notification notification) {
		System.out.println(notification.getMessage());
	}

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

	public void setUsername(String username) {
		this.username = username;
	}

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

	@Override
	public String toString() {
		return "User [userId=" + userId + ", \nusername=" + username + ", \nuserProjects=" + collaborationProjects
				+ ", \nuserActivities=" + userActivities + ", \nuserFriend=" + userFriends + "]\n\n";
	}
	
	
	public void addFriend(User user) {
		userFriends.add(user);	
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
