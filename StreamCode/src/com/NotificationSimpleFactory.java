package com;

public class NotificationSimpleFactory {

	public Notification createNotification(String type, String description){
		Notification notification = null;
		if (type.equals("activity_started")){
			notification = new ActivityStartedNotification(description);
		}
		if (type.equals("activity_completed")){
			notification = new ActivityCompletedNotification(description);
		}
		if (type.equals("project_started")){
			notification = new ProjectStartedNotification(description);
		}
		if (type.equals("project_invite")){
			notification = new ProjectInviteNotification(description);
		}
		if (type.equals("friendship")){
			notification = new FriendshipNotification(description);
		}
		if (type.equals("broadcast")){
			notification = new BroadcastNotification(description);
		}
		return notification;
	}
}
