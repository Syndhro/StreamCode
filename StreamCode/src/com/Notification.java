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
	
	public String getMessage() {
		return message;
	}
	
	public boolean isDelivered() {
		return isDelivered;
	}
	public void setDelivered(boolean isDelivered) {
		this.isDelivered = isDelivered;
	}

}
