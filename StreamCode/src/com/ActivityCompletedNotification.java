package com;

public class ActivityCompletedNotification extends Notification{

	public ActivityCompletedNotification(int notificationId, NotificationType type, String description, int targetId){
		this.message = "The activity (" + description + ") is completed";
		this.type = type;
		this.date = "";
		this.time = "";
		this.isDelivered = false;
		this.targetId = targetId;
		this.notificationId = notificationId;
	}
}
