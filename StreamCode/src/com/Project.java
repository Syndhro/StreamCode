package com;

import java.util.ArrayList;

public class Project {

	private static String projectID;
	private String title;
	private String description;
	private Category category;
	private int adminId;
	private ArrayList<Activity> activities;
	private ArrayList<User> projectUsers;
	private ProjectState state;
	
	public Project(int adminId){
		this.projectUsers = new ArrayList<User>();
		this.activities = new ArrayList<Activity>();
		this.state = ProjectState.INACTIVE;
		this.adminId = adminId;
	}

	public void addCollaborator(User u) {
		this.projectUsers.add(u);
	}

	public void addActivity(Activity a) {
		this.activities.add(a);
	}

	public void startProject() {
		this.state = ProjectState.ACTIVE;
	}

	public void endProject() {
		this.state = ProjectState.COMPLETED;
	}

	public void inviteFriends() {
	}

	public void modifyProject() {
		//modify
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
