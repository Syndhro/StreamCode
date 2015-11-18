package com;

public class ActivityCompletedNotification extends Notification{

	public ActivityCompletedNotification(String description){
		this.message = "The activity (" + description + ") is completed";
	}
}
