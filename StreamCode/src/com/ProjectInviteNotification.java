package com;

public class ProjectInviteNotification extends Notification{

	private static final long serialVersionUID = 1L;

	public ProjectInviteNotification(String description){
		this.message = "You are a member of the " + description + " project";
	}
}
