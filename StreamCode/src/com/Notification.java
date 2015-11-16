package com;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public abstract class Notification implements Serializable{

	private int notificationId;
	protected String message;
	private Time time;
	private Date date;
	private int sourceID;
	private int targetID;
	private boolean isDelivered;
	
	public int getNotificationId() {
		return notificationId;
	}
	public String getMessage() {
		return message;
	}
	public Time getTime() {
		return time;
	}
	public Date getDate() {
		return date;
	}
	public int getSourceId() {
		return sourceID;
	}
	public int getTargetId() {
		return targetID;
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
	public void setTime(Time time) {
		this.time = time;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setSourceID(int sourceID) {
		this.sourceID = sourceID;
	}
	public void setTargetID(int targetID) {
		this.targetID = targetID;
	}
	public void setDelivered(boolean isDelivered) {
		this.isDelivered = isDelivered;
	}

}
