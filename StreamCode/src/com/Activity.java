package com;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.crypto.Data;

public class Activity implements Serializable{

	private int activityId;
	private Project parentProject;
	private String name;
	private String description;
	private String place;
	private String dateTime;
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
	
	public Activity(int activityId, Project parentProject, String name, String description, String place, String dateTime){
		this.activityId = activityId;
		this.parentProject = parentProject;
		this.name = name;
		this.description = description;
		this.place = place;
		this.dateTime = dateTime;
		this.activityCollaborators = new ArrayList<User>();
		this.isCompleted = false; //uncompleted
		this.isActive = false; //unactive	
	}
	
	public Activity(int activityId, String name, String description, String place, String dateTime, boolean isCompleted, boolean isActive){
		this.activityId = activityId;
		this.name = name;
		this.description = description;
		this.place = place;
		this.dateTime = dateTime;
		this.activityCollaborators = new ArrayList<User>();
		this.isCompleted = isCompleted;
		this.isActive = isActive;	
	}
	
	//add & remove

	public void addAgent(User user) {
		activityCollaborators.add(user);
	}
	
	public void removeAgent(User user) {
		activityCollaborators.remove(user);
	}
	
	//GETTERS

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

	public String getPlace() {
		return place;
	}

	public String getDateTime() {
		return dateTime;
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
	
	//SETTERS

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public void setParentProject(Project parentProject) {
		this.parentProject = parentProject;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
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

