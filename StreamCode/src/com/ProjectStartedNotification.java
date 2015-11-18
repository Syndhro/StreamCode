package com;

public class ProjectStartedNotification extends Notification{
	
	private static final long serialVersionUID = 1L;

	public ProjectStartedNotification(String description){
		this.message = "The project (" + description + ") is started";
	}
}
