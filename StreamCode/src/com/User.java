package com;

public class User implements Observer {

	private static int userId = 0;
	private String username;

	public User(){
		User.userId++;
		this.userId = User.userId;
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
