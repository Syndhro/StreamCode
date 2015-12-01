package com;

public class BroadcastNotification extends Notification {

	private static final long serialVersionUID = 1L;

	public BroadcastNotification(int notificationId, NotificationType type, String description, int targetId, boolean isDelivered){
		this.message = description;
		this.type = type;
		this.date = "";
		this.time = "";
		this.isDelivered = isDelivered;
		this.targetId = targetId;
		this.notificationId = notificationId;
	}
}
