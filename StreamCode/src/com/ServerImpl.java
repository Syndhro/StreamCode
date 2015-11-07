package com;

import java.util.ArrayList;

public class ServerImpl implements Subject, ServerInterface {

	private static ServerImpl uniqueInstance;
	private DBManager dbManager;
	private ArrayList<Project> projects;
	private ArrayList<Observer> users;

	private void ServerImpl() {
		this.dbManager = new DBManager();
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
	public Boolean check(String username, String password) {
		//return true if the user and the password are present in the DB and they are correct
		return null;
	}
	/**
	 * @see Server#createProject(java.lang.String, java.lang.String, Category, int)
	 */
	public void createProject(String title, String description, Category category, int adminId) {
		Project project = new Project(adminId);
		project.setTitle(title);
		project.setDescription(description);
		project.setCategory(category);
	
		projects.add(project);
	}
}
