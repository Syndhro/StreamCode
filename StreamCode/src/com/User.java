package com;

public class User implements Observer {

	private int userId;
	private String username;

	public User(int id, String username){
		this.userId = id;
		this.username = username;
	}
	
	public int getID() {
		return this.userId;
	}

	public void next() {
		//chain of responsability
	}

	@Override
	public void update() {
		// TODO Auto-generated method s
	}

}
