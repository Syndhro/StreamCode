package com;

public class FriendshipNotification extends Notification{
	
	public FriendshipNotification(int notificationId, NotificationType type, String description, int targetId){
		this.message = description + " has added you to his friends list.";
		this.type = type;
		this.date = "";
		this.time = "";
		this.isDelivered = false;
		this.targetId = targetId;
		this.notificationId = notificationId;
	}
}
