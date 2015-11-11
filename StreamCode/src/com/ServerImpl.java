package com;

import java.util.ArrayList;

public class ServerImpl implements Subject, ServerInterface {

	private static ServerImpl uniqueInstance;
	private DBManager dbManager;
	private ArrayList<Project> projects;
	private ArrayList<User> registeredUsers;
	private ArrayList<Observer> loggedUsers;

	private ServerImpl() {
		this.dbManager = DBManager.getInstance();
		this.loggedUsers = new ArrayList<Observer>();
		retrieveAllUsers();
		retrieveAllProjects();
		linkProjectToUser();
		
	}
	
	public void retrieveAllProjects(){
		this.projects = dbManager.getAllProjects();
	}
	
	public void retrieveAllUsers(){
		this.registeredUsers = dbManager.getAllUsers();
	}
	
	public void linkProjectToUser(){
		
		ArrayList<Integer> projectIds = new ArrayList<Integer>();
		for (int i = 0; i < registeredUsers.size(); i++){
			projectIds = dbManager.getProjectsIdByUserId(registeredUsers.get(i).getUserId());
			for(int j = 0; j < projectIds.size(); j++){
				for(int k = 0; k < projects.size(); k++){
					if(projects.get(k).getId() == projectIds.get(j)){
						registeredUsers.get(i).addUserProjects(projects.get(k));
						projects.get(k).addCollaborator(registeredUsers.get(i));
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
	public int check(String username, String password) {
		//return true if the user and the password are present in the DB and they are correct
		int returnedId;
		
		returnedId = dbManager.getUserId(username, password);
		
		return returnedId;
	}

	@Override
	public void registerUser(String username, String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterUser(User user) {
		// TODO Auto-generated method stub
		
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
	public void removeProject(Project project) {
		projects.remove(project);
	}

	@Override
	public void addActivity(Project project, Activity activity) {
		for(int i = 0; i < projects.size(); i++){
			if(projects.get(i).equals(project)){
				Project myProject = projects.get(i);
				myProject.addActivity(activity);
			}
		}	
	}

	@Override
	public void removeActivity(Project project, Activity activity) {
		for(int i = 0; i < projects.size(); i++){
			if(projects.get(i).equals(project)){
				Project myProject = projects.get(i);
				myProject.getActivities().remove(activity);
			}
		}		
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
		dbManager.addFriendship(user1.getUsername(), user2.getUsername());
	}

	@Override
	public void removeFriend(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addProject(String title, String description, Category category, User user) {
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<User> getRegisteredUsers() {
		return registeredUsers;
	}

	@Override
	public String toString() {
		return "ServerImpl [projects=" + projects.toString() + ", registeredUser=" + registeredUsers.toString() + "]";
	}

	public static void main(String[] args){
		
		ServerImpl server = new ServerImpl();
		
		server.toString();
	}
	
}
