package com;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Observer, Serializable {

	private int userId;
	private String username;
	private ArrayList<Project> collaborationProjects;
	private ArrayList<Project> managedProjects;
	private ArrayList<Activity> userActivities;
	private ArrayList<User> userFriends;
	

	public User(int id, String username){
		this.userId = id;
		this.username = username;
		collaborationProjects = new ArrayList<Project>();
		managedProjects = new ArrayList<Project>();
		userActivities = new ArrayList<Activity>();
		userFriends = new ArrayList<User>();
	}
	
	//chain of responsability

	public void next() {
		//chain of responsability
	}

	@Override
	//observer
	public void update(Notification notification) {
		
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

	@Override
	public String toString() {
		return "User [userId=" + userId + ", \nusername=" + username + ", \nuserProjects=" + collaborationProjects
				+ ", \nuserActivities=" + userActivities + ", \nuserFriend=" + userFriends + "]\n\n";
	}
	
	
	public void addFriend(User user) {
		userFriends.add(user);	
	}

}
