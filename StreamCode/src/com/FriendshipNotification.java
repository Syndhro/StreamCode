package com;

public class FriendshipNotification extends Notification{

	private static final long serialVersionUID = 1L;

	public FriendshipNotification(int notificationId, NotificationType type, String sender, int targetId, boolean isDelivered){
		this.message = sender + " has added you to his friends list.";
		this.type = type;
		this.date = "";
		this.time = "";
		this.isDelivered = isDelivered;
		this.targetId = targetId;
		this.notificationId = notificationId;
	}
}
