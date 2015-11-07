package com;

import java.util.ArrayList;

public class Activity {

	private static int activityIdCounter;
	private int id;
	private String name;
	private ArrayList<User> user; //corretto che siano oggetti in modo da chiamare il metodo update di ognuno quando necessario
	private boolean isCompleted;
	private boolean isActive;

	public Activity(){
		this.isCompleted = false; //uncompleted
		this.isActive = false; //unactive
		activityIdCounter++;
		this.id = Activity.activityIdCounter; //id generato progressivamente	
	}
	public void complete() {
		this.isCompleted = true;
	}
	
	public void active() {
		this.isActive = true;
	}

	public void modifyActivity() {
		//modify
	}

	public void addCollaborator(User u) {
		user.add(u);
	}
}
