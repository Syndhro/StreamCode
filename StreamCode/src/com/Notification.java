package com;

import java.io.Serializable;

public abstract class Notification implements Serializable{

	private static final long serialVersionUID = 1L;
	protected int notificationId;
	protected String message;
	protected String time;
	protected String date;
	protected int targetId;
	protected boolean isDelivered;
	protected NotificationType type;
	
	public int getNotificationId() {
		return notificationId;
	}
	public String getMessage() {
		return message;
	}
	public String getTime() {
		return time;
	}
	public String getDate() {
		return date;
	}
	
	public int getTargetId() {
		return targetId;
	}
	
	public NotificationType getType() {
		return type;
	}
	
	public boolean isDelivered() {
		return isDelivered;
	}
	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public void setTargetID(int targetId) {
		this.targetId = targetId;
	}
	public void setDelivered(boolean isDelivered) {
		this.isDelivered = isDelivered;
	}
	
	public void setType(NotificationType type){
		this.type = type;
	}

}
