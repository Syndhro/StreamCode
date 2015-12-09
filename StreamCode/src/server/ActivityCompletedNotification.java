package server;

public class ActivityCompletedNotification extends Notification{

	private static final long serialVersionUID = 1L;

	public ActivityCompletedNotification(int notificationId, NotificationType type, String name, int targetId, boolean isDelivered){
		this.message = "The activity " + name.toUpperCase() + " is completed";
		this.type = type;
		this.date = "";
		this.time = "";
		this.isDelivered = isDelivered;
		this.targetId = targetId;
		this.notificationId = notificationId;
	}
}
