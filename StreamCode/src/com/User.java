package com;

import java.util.ArrayList;

public class User implements Observer {

	private int userId;
	private String username;
	private ArrayList<Project> collaborationProjects;
	private ArrayList<Project> managedProject;
	private ArrayList<Activity> userActivities;
	private ArrayList<User> userFriend;
	

	public User(int id, String username){
		this.userId = id;
		this.username = username;
		collaborationProjects = new ArrayList<Project>();
		managedProject = new ArrayList<Project>();
		userActivities = new ArrayList<Activity>();
		userFriend = new ArrayList<User>();
	}
	
	//chain of responsability

	public void next() {
		//chain of responsability
	}

	@Override
	//observer
	public void update() {
		// TODO Auto-generated method s
	}

	public int getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public ArrayList<Project> getUserProjects() {
		return collaborationProjects;
	}

	public ArrayList<Activity> getUserActivities() {
		return userActivities;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void addCollaborationProject(Project project) {
		this.collaborationProjects.add(project);
	}
	
	public void addManagedProject(Project project) {
		this.managedProject.add(project);
	}

	public void addUserActivities(Activity activity) {
		this.userActivities.add(activity);
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", \nusername=" + username + ", \nuserProjects=" + collaborationProjects
				+ ", \nuserActivities=" + userActivities + ", \nuserFriend=" + userFriend + "]\n\n";
	}
	
	
	public void addFriend(User user) {
		userFriend.add(user);	
	}

}
