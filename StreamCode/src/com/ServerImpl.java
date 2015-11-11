package com;

import java.util.ArrayList;

public class ServerImpl implements Subject, ServerInterface {

	private static ServerImpl uniqueInstance;
	private DBManager dbManager;
	private ArrayList<Project> projects;
	private ArrayList<Observer> users;

	private ServerImpl() {
		this.dbManager = DBManager.getInstance();
		this.projects = new ArrayList<Project>();
		this.users = new ArrayList<Observer>();
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
		users.add(o);
	}
	/**
	 * @see Subject#removeObserver(Observer)
	 *
	 *
	 */
	public void removeObserver(Observer o) {
		int i = users.indexOf(o);
		if(i >= 0){
			users.remove(i);
		}

	}
	/**
	 * @see Subject#notifyObservers()
	 *
	 *
	 */
	public void notifyObservers() {
		for (int i=0; i < users.size(); i++){
			users.get(i).update();
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
	
		projects.add(project);
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
	public int login(String username, String password) {
		// TODO Auto-generated method stub
		return 0;
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
}
