package com;

import java.io.Serializable;

public enum NotificationType implements Serializable{
	
	ACTIVITY_STARTED,
	ACTIVITY_COMPLETED,
	PROJECT_STARTED,
	PROJECT_INVITE,
	FRIENDSHIP,
	BROADCAST,
	AGENT_ADDED
	;
	
	public static NotificationType getNotificationType(String string){
		
		NotificationType notType = null;
		
		if (string.equals(null))
			return null;
		
		switch (string){
		case "agent_added":
			notType = AGENT_ADDED;
			break;
		case "activity_started":
			notType = ACTIVITY_STARTED;
			break;
		case "activity_completed":
			notType = ACTIVITY_COMPLETED;
			break;
		case "project_started":
			notType = PROJECT_STARTED;
			break;
		case "project_invite":
			notType = PROJECT_INVITE;
			break;
		case "friendship":
			notType = FRIENDSHIP;
			break;
		case "broadcast":
			notType = BROADCAST;
			break;
		default:
			notType = null;
		}
		
		return notType;
	}
	
	

}
