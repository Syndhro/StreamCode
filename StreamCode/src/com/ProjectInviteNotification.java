package com;

public class ProjectInviteNotification extends Notification{
	public ProjectInviteNotification(String description){
		this.message = "You are a member of the " + description + " project";
	}
}
