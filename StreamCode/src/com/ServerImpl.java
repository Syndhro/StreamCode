package com;

import java.util.ArrayList;

public class ServerImpl implements Subject, ServerInterface {

	private static ServerImpl uniqueInstance;
	private DBManager dbManager;
	private ArrayList<Project> registeredProjects;
	private ArrayList<Activity> registeredActivities;
	private ArrayList<User> registeredUsers;
	private ArrayList<Observer> loggedUsers;

	private ServerImpl() {
		this.dbManager = DBManager.getInstance();
		this.loggedUsers = new ArrayList<Observer>();
		this.registeredProjects = new ArrayList<Project>();
		this.registeredActivities = new ArrayList<Activity>();
		this.registeredUsers = new ArrayList<User>();
	}
	
	public void retrieveAllProjects(){
		this.registeredProjects = dbManager.getAllProjects();
	}
	
	public void retrieveAllUsers(){
		this.registeredUsers = dbManager.getAllUsers();
	}
	
	public void retrieveAllActivity(){
		this.registeredActivities = dbManager.getAllActivities();
	}
	
	public void linkProjectToUser(){
		
		ArrayList<Integer> projectIds = new ArrayList<Integer>();
		for (int i = 0; i < registeredUsers.size(); i++){
			projectIds = dbManager.getProjectsIdByUserId(registeredUsers.get(i).getUserId());
			for(int j = 0; j < projectIds.size(); j++){
				for(int k = 0; k < registeredProjects.size(); k++){
					if(registeredProjects.get(k).getProjectId() == projectIds.get(j)){
						registeredUsers.get(i).addCollaborationProject(registeredProjects.get(k));
						registeredProjects.get(k).addCollaborator(registeredUsers.get(i));
					}
				}
			}
		}	
	}
	
	public void linkActivityToUser(){
		
		ArrayList<Integer> activityIds = new ArrayList<Integer>();
		for (int i = 0; i < registeredUsers.size(); i++){
			activityIds = dbManager.getActivitiesIdByUserId(registeredUsers.get(i).getUserId());
			for(int j = 0; j < activityIds.size(); j++){
				for(int k = 0; k < registeredActivities.size(); k++){
					if(registeredActivities.get(k).getActivityId() == activityIds.get(j)){
						registeredUsers.get(i).addUserActivities(registeredActivities.get(k));
						registeredActivities.get(k).addCollaborator(registeredUsers.get(i));
					}
				}
			}
		}	
	}

	public static ServerImpl getInstance() {
		if (uniqueInstance == null){
			uniqueInstance = new ServerImpl();
		}
		return uniqueInstance;
	}
	/**
	 * @see Subject#registerObserver(Observer)
	 *
	 *
	 */
	public void registerObserver(Observer o) {
		loggedUsers.add(o);
	}
	/**
	 * @see Subject#removeObserver(Observer)
	 *
	 *
	 */
	public void removeObserver(Observer o) {
		int i = loggedUsers.indexOf(o);
		if(i >= 0){
			loggedUsers.remove(i);
		}
	}
	/**
	 * @see Subject#notifyObservers()
	 *
	 *
	 */
	public void notifyObservers() {
		for (int i=0; i < loggedUsers.size(); i++){
			loggedUsers.get(i).update();
		}
	}
	/**
	 * @see Server#check(java.lang.String, java.lang.String)
	 */

	@Override
	public void registerUser(String username, String password) {
		dbManager.addUser(username, password);
		int id = dbManager.getUserId(username, password);
		User user = new User(id, username);
		registeredUsers.add(user);
	}

	@Override
	public void unregisterUser(User user) {
		registeredUsers.remove(user);	
	}

	@Override
	public User login(String username, String password) {
		dbManager.getUserId(username, password); //se va male solleva eccezione SQL
		for(int i = 0; i < registeredUsers.size(); i++){
			if(registeredUsers.get(i).getUsername().equals(username)){
				registerObserver(registeredUsers.get(i));//aggiungimi a utenti loggati
				return registeredUsers.get(i);
			}
		}
		System.out.println("Utente non registrato");
		return null;
	}

	@Override
	public void logout(User user) {
		removeObserver(user); //rimuovo utente dalla lista degli utenti loggati	
	}

	@Override
	public void removeProject(int projectId) {
		Project project = getProjectById(projectId);
		registeredProjects.remove(project);
	}

	public Project getProjectById(int projectId){
		Project project = null;
		for(int i = 0; i < registeredProjects.size(); i++){
			if(registeredProjects.get(i).getProjectId() == projectId){
				project = registeredProjects.get(i);
				return project;
			}
		}
		System.out.println("Project not found");
		return project;
	}
	
	public Activity getActivityById(int activityId){
		Activity activity = null;
		for(int i = 0; i < registeredActivities.size(); i++){
			if(registeredActivities.get(i).getActivityId() == activityId){
				activity = registeredActivities.get(i);
				return activity;
			}
		}
		System.out.println("Activity not found");
		return activity;
	}
	
	public User getUserById(int userId){
		User user = null;
		for(int i = 0; i < registeredUsers.size(); i++){
			if(registeredUsers.get(i).getUserId() == userId){
				user = registeredUsers.get(i);
				return user;
			}
		}
		System.out.println("User not found");
		return user;
	}
	
	@Override
	public void addActivity(Project project, String name, String description, String place, String dateTime) {
		int id = dbManager.getLastActivityId();
		Activity activity = new Activity(id, project, name, description, place, dateTime);
		project.addActivity(activity);
		registeredActivities.add(activity);
		dbManager.addActivity(activity);
	}

	@Override
	public void removeActivity(Project project, Activity activity) {
		/*
		  	for(int i = 0; i < registeredProjects.size(); i++){
			if(registeredProjects.get(i).equals(project)){
				Project myProject = registeredProjects.get(i);
		*/
				project.getActivities().remove(activity);
	}	

	@Override
	public ArrayList<String> searchUser(String string) {
		ArrayList<String> matchedUsers = new ArrayList<String>();
		for(int i = 0; i < registeredUsers.size(); i++){
			if(registeredUsers.get(i).getUsername().contains(string)){
				matchedUsers.add(registeredUsers.get(i).getUsername());
			}
		}
		return matchedUsers;
	}


	@Override
	public void addFriend(User user1, User user2) {
		user1.addFriend(user2);
		dbManager.addFriendship(user1.getUserId(), user2.getUserId());
	}

	@Override
	public void removeFriend(User user1, User user2) {
		user1.getUserFriends().remove(user2);
		dbManager.removeFriend(user1.getUserId(), user2.getUserId());
	}

	@Override
	public void addProject(String title, String description, String category, User user) {
		int projectId = dbManager.getLastProjectId();
		Project project = new Project(projectId, title, description, Category.getCategory(category), user);
		registeredProjects.add(project);
		dbManager.addProject(project);
		user.addManagedProject(project);
	}
	
	public ArrayList<User> getRegisteredUsers() {
		return registeredUsers;
	}
	
	public ArrayList<Project> getRegisteredProjects() {
		return registeredProjects;
	}
	
	public ArrayList<Activity> getRegisteredActivity() {
		return registeredActivities;
	}

	public void stampa() {
		for(int i = 0; i < registeredProjects.size(); i++){
		System.out.println("Project=" + registeredProjects.get(i).getTitle());
		System.out.println("Users:");
			for(int j=0; j < registeredProjects.get(i).getUsers().size(); j++){
				System.out.println(registeredProjects.get(i).getUsers().get(j).getUsername());
			}
		}
		System.out.println();
		for(int i = 0; i < registeredUsers.size(); i++){
			System.out.println("Users:" + registeredUsers.get(i).getUsername());
			System.out.println("Projects:");
			for(int j=0; j < registeredUsers.get(i).getUserProjects().size(); j++){
				System.out.println(registeredUsers.get(i).getUserProjects().get(j).getTitle());
			}
		}
		System.out.println();
		for(int i = 0; i < registeredUsers.size(); i++){
			System.out.println("Users:" + registeredUsers.get(i).getUsername());
			System.out.println("Activities:");
			for(int j=0; j < registeredUsers.get(i).getUserActivities().size(); j++){
				System.out.println(registeredUsers.get(i).getUserActivities().get(j).getName());
			}
		}
	}
	
	
	
	public static void main(String[] args){
		
		ServerImpl server = ServerImpl.getInstance();
		server.retrieveAllUsers();
		server.retrieveAllProjects();
		server.retrieveAllActivity();
		
		server.linkProjectToUser();
		server.linkActivityToUser();
		
		server.stampa();
	}

	
}
