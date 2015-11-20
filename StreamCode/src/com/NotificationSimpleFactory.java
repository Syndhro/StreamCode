package com;

public class NotificationSimpleFactory {

	public Notification createNotification(int notificationId, String type, String description, int targetId){
		Notification notification = null;
		if (type.equals("activity_started")){
			notification = new ActivityStartedNotification(notificationId, NotificationType.getNotificationType(type), description, targetId);
		}
		if (type.equals("activity_completed")){
			notification = new ActivityCompletedNotification(notificationId, NotificationType.getNotificationType(type), description, targetId);
		}
		if (type.equals("agent_added")){
			notification = new AgentAddedNotification(notificationId, NotificationType.getNotificationType(type), description, targetId);
		}
		if (type.equals("project_started")){
			notification = new ProjectStartedNotification(notificationId, NotificationType.getNotificationType(type), description, targetId);
		}
		if (type.equals("project_invite")){
			notification = new ProjectInviteNotification(notificationId, NotificationType.getNotificationType(type), description, targetId);
		}
		if (type.equals("friendship")){
			notification = new FriendshipNotification(notificationId, NotificationType.getNotificationType(type), description, targetId);
		}
		if (type.equals("broadcast")){
			notification = new BroadcastNotification(notificationId, NotificationType.getNotificationType(type), description, targetId);
		}
		return notification;
	}
}
