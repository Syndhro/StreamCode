package com;

public class ProjectStartedNotification extends Notification{
	
	private static final long serialVersionUID = 1L;

	public ProjectStartedNotification(int notificationId, NotificationType type, String name, int targetId, boolean isDelivered){
		this.message = "The project " + name.toUpperCase() + " is started";
		this.type = type;
		this.date = "";
		this.time = "";
		this.isDelivered = isDelivered;
		this.targetId = targetId;
		this.notificationId = notificationId;
	}

}
