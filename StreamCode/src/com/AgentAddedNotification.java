package com;

public class AgentAddedNotification extends Notification{
	
	public AgentAddedNotification(int notificationId, NotificationType type, String description, int targetId, boolean isDelivered){
		this.message = "You've been designated to complete (" + description + ") activity";
		this.type = type;
		this.date = "";
		this.time = "";
		this.isDelivered = isDelivered;
		this.targetId = targetId;
		this.notificationId = notificationId;
	}
}
