package com;

import java.rmi.Remote;

public interface ServerInterface extends Remote {

	public abstract int check(String username, String password);
	public abstract void createProject(String title, String description, Category category, int adminID);

}
