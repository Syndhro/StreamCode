package com;

public class AgentAddedNotification extends Notification{
	
	public AgentAddedNotification(int notificationId, NotificationType type, String description, int targetId){
		this.message = "You've been designated to complete (" + description + ") activity";
		this.type = type;
		this.date = "";
		this.time = "";
		this.isDelivered = false;
		this.targetId = targetId;
		this.notificationId = notificationId;
	}
}
