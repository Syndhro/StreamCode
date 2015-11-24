package com;

public class ProjectStartedNotification extends Notification{
	
	private static final long serialVersionUID = 1L;

	public ProjectStartedNotification(int notificationId, NotificationType type, String description, int targetId, boolean isDelivered){
		this.message = "The project (" + description + ") is started";
		this.type = type;
		this.date = "";
		this.time = "";
		this.isDelivered = isDelivered;
		this.targetId = targetId;
		this.notificationId = notificationId;
	}

}
