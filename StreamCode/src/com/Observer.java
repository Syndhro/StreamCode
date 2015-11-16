package com;

public interface Observer {
	public abstract void update(Notification notification);
	public abstract void update();
}
