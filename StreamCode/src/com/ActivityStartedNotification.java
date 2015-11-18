package com;

public class ActivityStartedNotification extends Notification{
	
	public ActivityStartedNotification(String description){
		this.message = "The activity (" + description + ") is completed";
	}
}
