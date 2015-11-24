package com;

public class FriendshipNotification extends Notification{
	
	public FriendshipNotification(int notificationId, NotificationType type, String description, int targetId, boolean isDelivered){
		this.message = description + " has added you to his friends list.";
		this.type = type;
		this.date = "";
		this.time = "";
		this.isDelivered = isDelivered;
		this.targetId = targetId;
		this.notificationId = notificationId;
	}
}
