package com;

public class AgentAddedNotification extends Notification{

	private static final long serialVersionUID = 1L;

	public AgentAddedNotification(int notificationId, NotificationType type, String name, int targetId, boolean isDelivered){
		this.message = "You've been designated to complete " + name.toUpperCase() + " activity";
		this.type = type;
		this.date = "";
		this.time = "";
		this.isDelivered = isDelivered;
		this.targetId = targetId;
		this.notificationId = notificationId;
	}
}
