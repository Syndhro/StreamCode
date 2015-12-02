package com;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable{

	private static final long serialVersionUID = 1L;
	private int projectId;
	private String title;
	private String description;
	private Category category;
	private User admin;
	private ArrayList<Activity> projectActivities;
	private ArrayList<User> projectCollaborators;
	private ProjectState state;
	
	//CONSTRUCTOR
	public Project(int projectId, String title, String description, Category cat, User admin, ProjectState state){
		this.projectId = projectId;
		this.projectCollaborators = new ArrayList<User>();
		this.projectActivities = new ArrayList<Activity>();
		this.state = state;
		this.title = title;
		this.description = description;
		this.category = cat;
		this.admin = admin;
	}
	
	public void addCollaborator(User user) {
		this.projectCollaborators.add(user);
	}
	
	public void addActivity(Activity activity) {
		this.projectActivities.add(activity);
	}
	
	public void removeCollaborator(User user) {
		this.projectCollaborators.remove(user);
	}
	
	public void removeActivity(Activity activity) {
		this.projectActivities.remove(activity);
	}

	//START/END
	public void startProject() {
		this.state = ProjectState.ACTIVE;
	}
	
	public void endProject() {
		this.state = ProjectState.COMPLETED;
	}
	
	//SETTERS
	public void setAdmin(User user) {
		this.admin = user;
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
	
	public void setState(ProjectState state){
		this.state = state;
	}
	
	//GETTERS
	public String getTitle() {
		return this.title;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public Category getCategory() {
		return this.category;
	}
	
	public int getProjectId() {
		return this.projectId;
	}
	
	public User getAdmin() {
		return this.admin;
	}
	
	public ProjectState getState() {
		return this.state;
	}
	
	public ArrayList<Activity> getActivities() {
		return this.projectActivities;
	}
	
	public ArrayList<User> getCollaborators() {
		return this.projectCollaborators;
	}
}
