package com;

import java.io.Serializable;
import java.util.ArrayList;

public class Activity implements Serializable{

	private static final long serialVersionUID = 1L;
	private int activityId;
	private Project parentProject;
	private String name;
	private String description;
	private String place;
	private String dateTime;
	private ArrayList<User> activityCollaborators; 
	private boolean isCompleted;
	private boolean isActive;
	private ArrayList<ActivityAttachment> attachments;
	
	//CONSTRUCTORS
	//WHEN CREATED 
	public Activity(int activityId, Project parentProject, String name, String description, String place, String dateTime){
		this.activityId = activityId;
		this.parentProject = parentProject;
		this.name = name;
		this.description = description;
		this.place = place;
		this.dateTime = dateTime;
		this.activityCollaborators = new ArrayList<User>();
		this.isCompleted = false; //UNCOMPLETED
		this.isActive = false; //UNACTIVE
		this.attachments = new ArrayList<ActivityAttachment>();
	}
	
	//WHEN RETRIEVED FROM DB(PREVIOUS ISCOMPLETED AND ISACTIVE NEEDED)
	public Activity(int activityId, String name, String description, String place, String dateTime, boolean isCompleted, boolean isActive){
		this.activityId = activityId;
		this.name = name;
		this.description = description;
		this.place = place;
		this.dateTime = dateTime;
		this.activityCollaborators = new ArrayList<User>();
		this.isCompleted = isCompleted;
		this.isActive = isActive;	
		this.attachments = new ArrayList<ActivityAttachment>();
	}
	
	//ADDERS & REMOVERS
	public void addAgent(User user) {
		activityCollaborators.add(user);
	}
	
	public void addAttachment(ActivityAttachment att){
		attachments.add(att);
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
	
	public ArrayList<ActivityAttachment> getAttachments() {
		return attachments;
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

