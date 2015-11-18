package com;

public class ProjectStartedNotification extends Notification{
	public ProjectStartedNotification(String description){
		this.message = "The project (" + description + ") is started";
	}
}
