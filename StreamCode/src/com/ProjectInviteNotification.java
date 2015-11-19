package com;

public class ProjectInviteNotification extends Notification{

	private static final long serialVersionUID = 1L;

	public ProjectInviteNotification(int notificationId, NotificationType type, String description, int targetId){
		this.message = "You are a member of the " + description + " project";
		this.type = type;
		this.date = "";
		this.time = "";
		this.isDelivered = false;
		this.targetId = targetId;
		this.notificationId = notificationId;
	}

}
