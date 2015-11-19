package com;

public class BroadcastNotification extends Notification {
	
	public BroadcastNotification(int notificationId, NotificationType type, String description, int targetId){
		this.message = description;
		this.type = type;
		this.date = "";
		this.time = "";
		this.isDelivered = false;
		this.targetId = targetId;
		this.notificationId = notificationId;
	}
}
