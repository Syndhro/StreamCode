package com;

public class ActivityCompletedNotification extends Notification{

	public ActivityCompletedNotification(int notificationId, NotificationType type, String description, int targetId, boolean isDelivered){
		this.message = "The activity (" + description + ") is completed";
		this.type = type;
		this.date = "";
		this.time = "";
		this.isDelivered = isDelivered;
		this.targetId = targetId;
		this.notificationId = notificationId;
	}
}
