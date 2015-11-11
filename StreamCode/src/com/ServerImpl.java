package com;

import java.util.ArrayList;

public class ServerImpl implements Subject, ServerInterface {

	private static ServerImpl uniqueInstance;
	private DBManager dbManager;
	private ArrayList<Project> projects;
	private ArrayList<User> registeredUser;
	private ArrayList<Observer> loggedUsers;

	private ServerImpl() {
		this.dbManager = DBManager.getInstance();
		//this.projects = new ArrayList<Project>();
		this.loggedUsers = new ArrayList<Observer>();
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
	/**
	 * @see Server#createProject(java.lang.String, java.lang.String, Category, int)
	 */
	public void createProject(String title, String description, Category category, User user) {
		Project project = new Project(user);
		project.setTitle(title);
		project.setDescription(description);
		project.setCategory(category);
		
		//projects.add(project);
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
		int userId = dbManager.getUserId(username, password);
		if (userId == -1){
			
		}
		
		
		
		return null;
	}

	@Override
	public void logout(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeProject(Project project) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addActivity(Project project, Activity activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeActivity(Project project, Activity activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<String> searchUser(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addFriend(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFriend(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addProject(String title, String description, Category category, User user) {
		// TODO Auto-generated method stub
		
	}
}
