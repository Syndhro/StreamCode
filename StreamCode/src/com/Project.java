package com;

import java.util.ArrayList;

public class Project {

	private int projectId;
	private String title;
	private String description;
	private Category category;
	private User admin;
	private ArrayList<Activity> projectActivities;
	private ArrayList<User> projectUsers;
	private ProjectState state;
	
	//constructors
	
	public Project(User admin){
		this.projectUsers = new ArrayList<User>();
		this.projectActivities = new ArrayList<Activity>();
		this.projectUsers.add(admin);							//add admin to project collaborators
		this.state = ProjectState.INACTIVE;
		this.admin = admin;
	}
	
	public Project(int projectId, String title, String description, Category cat, int adminId){
		this.projectId = projectId;
		this.projectUsers = new ArrayList<User>();
		this.projectActivities = new ArrayList<Activity>();
		this.state = ProjectState.INACTIVE;
		this.title = title;
		this.description = description;
		this.category = cat;
		for(int i = 0; i < projectUsers.size(); i++){
			if(projectUsers.get(i).getUserId() == adminId)
				this.admin = projectUsers.get(i);
		}
	}
	
	//add attribute

	public void addCollaborator(User user) {
		this.projectUsers.add(user);
	}

	public void addActivity(Activity activity) {
		this.projectActivities.add(activity);
	}
	
	public void removeCollaborator(User user) {
		this.projectUsers.remove(user);
	}

	public void removeActivity(Activity activity) {
		this.projectActivities.remove(activity);
	}
	
	//start/end

	public void startProject() {
		this.state = ProjectState.ACTIVE;
	}

	public void endProject() {
		this.state = ProjectState.COMPLETED;
	}
	
	//setters

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	//getters
	
	public String getTitle() {
		return this.title;
	}

	public String getDescription() {
		return this.description;
	}

	public Category getCategory() {
		return this.category;
	}
	
	public int getId() {
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
	
	public ArrayList<User> getUsers() {
		return this.projectUsers;
	}

	@Override
	public String toString() {
		return "Project [projectId=" + projectId + ", \ntitle=" + title + ", \ndescription=" + description + ", \ncategory="
				+ category + ", \nadminId=" + admin + ", \nprojectActivities=" + projectActivities + ", \nprojectUsers="
				+ projectUsers + ", \nstate=" + state + "]\n\n";
	}
	
	
	
}
