package server;

import java.io.Serializable;

public class ActivityAttachment implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public int attachmentId;
	public String text;
	public User author;
	public Activity parentActivity;
	
	public ActivityAttachment(int id, String text, Activity parentActivity, User author){
		this.text = text;
		this.author = author;
		this.parentActivity = parentActivity;
		this.attachmentId = id;
	}
	
	public ActivityAttachment(int id, String text){
		this.attachmentId = id;
		this.text = text;
	}
	
	//GETTERS
	public int getAttachmentId() {
		return attachmentId;
	}

	public String getText() {
		return text;
	}

	public User getAuthor() {
		return author;
	}
	
	public Activity getParentActivity(){
		return parentActivity;
	}

	//SETTERS
	public void setId(int id) {
		this.attachmentId = id;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
	
	public void setParentActivity(Activity parentActivity) {
		this.parentActivity = parentActivity;
	}
	
}
