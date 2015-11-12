package com;

import java.util.ArrayList;

public class User implements Observer {

	private int userId;
	private String username;
	private ArrayList<Project> userProjects;
	private ArrayList<Activity> userActivities;
	private ArrayList<User> userFriend;
	

	public User(int id, String username){
		this.userId = id;
		this.username = username;
		userProjects = new ArrayList<Project>();
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
		return userProjects;
	}

	public ArrayList<Activity> getUserActivities() {
		return userActivities;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void addUserProjects(Project project) {
		this.userProjects.add(project);
	}

	public void addUserActivities(Activity activity) {
		this.userActivities.add(activity);
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", \nusername=" + username + ", \nuserProjects=" + userProjects
				+ ", \nuserActivities=" + userActivities + ", \nuserFriend=" + userFriend + "]\n\n";
	}
	
	

	public void addFriend(User user2) {
		// TODO Auto-generated method stub
		
	}
}
