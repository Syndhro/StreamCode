package com;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public abstract class Notification implements Serializable{

	private int notificationId;
	private String message;
	private Time time;
	private Date date;
	private int sourceID;
	private int targetID;
	private boolean delivered;

}
