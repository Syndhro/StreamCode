package com;

import java.util.ArrayList;

public class Activity {

	private int activityId;
	private Project parentProject;
	private String name;
	private String description;
	private ArrayList<User> activityCollaborators; //corretto che siano oggetti in modo da chiamare il metodo update di ognuno quando necessario
	private boolean isCompleted;
	private boolean isActive;
	
	//constructors

	public Activity(Project parentProject){
		this.parentProject = parentProject;
		this.activityCollaborators = new ArrayList<User>();
		this.isCompleted = false; //uncompleted
		this.isActive = false; //unactive	
	}
	
	public Activity(Project parentProject, String name, String description){
		this.parentProject = parentProject;
		this.name = name;
		this.description = description;
		this.activityCollaborators = new ArrayList<User>();
		this.isCompleted = false; //uncompleted
		this.isActive = false; //unactive	
	}
	
	//change status
	
	public void complete() {		//when activity is completed by all users
		this.isCompleted = true;
	}
	
	public void active() {			//when activity can be complete
		this.isActive = true;
	}
	
	//add & remove

	public void addCollaborator(User user) {
		activityCollaborators.add(user);
	}
	
	public void removeCollaborator(User user) {
		activityCollaborators.remove(user);
	}
	
	//getter

	public int getActivityId() {
		return activityId;
	}

	public Project getParentProject() {
		return parentProject;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<User> getActivityCollaborators() {
		return activityCollaborators;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public boolean isActive() {
		return isActive;
	}
	
	//setter

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setActivityCollaborators(ArrayList<User> activityCollaborators) {
		this.activityCollaborators = activityCollaborators;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
