package com;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class DBManager implements Serializable{

	private static final long serialVersionUID = 1L;
	private static DBManager uniqueInstance;
	private Connection connection;
	private Credential credentials;
	
	//constructor
	private DBManager(){
		credentials = new Credential();
		connect();
	}
	
	public static DBManager getInstance() {
		if (uniqueInstance == null){
			uniqueInstance = new DBManager();
		}
		return uniqueInstance;
	}

	//connection to database
	public void connect(){
		this.connection = null;
		try {
		     Class.forName("com.mysql.jdbc.Driver");		     
		     connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/streamdata", credentials.getDbUsername(), credentials.getDbPassword());			      	
	    }
	    catch(ClassNotFoundException e) {
	      e.printStackTrace();
	    }
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	}
	
	//add user credentials to database in the registration process(only one time)
	public void addUser(String username, String password) {
		PreparedStatement statement = null;
	    try {
			 String query = "INSERT INTO user (username, password) VALUES (?,?)";
		     statement = (PreparedStatement) connection.prepareStatement(query);         
		     statement.setString(1, username);
		     statement.setString(2, password);
		     statement.executeUpdate();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }	
	}
	
	//add project to database after the creation of his object with the id previously queried to db(last index in the table) 
	public void addProject(Project project) {	
		PreparedStatement statement = null;
	    try {
			 String query = "INSERT INTO project (projectId, title, description, category, adminId, state) VALUES (?,?,?,?,?,?)";
		     statement = (PreparedStatement) connection.prepareStatement(query);         
		     statement.setInt(1, project.getProjectId());
		     statement.setString(2, project.getTitle());
		     statement.setString(3, project.getDescription());
		     statement.setString(4, project.getCategory().toString().toLowerCase());
		     statement.setInt(5, project.getAdmin().getUserId());
		     statement.setString(6, project.getState().toString().toLowerCase());
		     statement.executeUpdate();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
	}

	//add project to database after the creation of his object with the id previously queried to db(last index in the table) 
	public void addActivity(Activity activity) {
		PreparedStatement statement = null;
		try {
			 String query = "INSERT INTO activity (activityId, projectId, name, description, place, day, month, year, isActive, isCompleted) VALUES (?,?,?,?,?,?,?,?,?,?)";
		     statement = (PreparedStatement) connection.prepareStatement(query);         
		     statement.setInt(1, activity.getActivityId());
		     statement.setInt(2, activity.getParentProject().getProjectId());
		     statement.setString(3, activity.getName());
		     statement.setString(4, activity.getDescription());
		     statement.setString(5, activity.getPlace());
		     statement.setInt(6, activity.getDate().getDay());
		     statement.setInt(7, activity.getDate().getMonth());
		     statement.setInt(8, activity.getDate().getYear());
		     statement.setBoolean(9, activity.isActive());
		     statement.setBoolean(10, activity.isCompleted());
		     statement.executeUpdate();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
	}
	
	public void addAttachment(ActivityAttachment activityAttachment) {
		PreparedStatement statement = null;
		try {
			 String query = "INSERT INTO activity_attachment (attachmentId, text, authorId, activityId) VALUES (?,?,?,?)";
		     statement = (PreparedStatement) connection.prepareStatement(query);         
		     statement.setInt(1, activityAttachment.getAttachmentId());
		     statement.setString(2, activityAttachment.getText());
		     statement.setInt(3, activityAttachment.getAuthor().getUserId());
		     statement.setInt(4, activityAttachment.getParentActivity().getActivityId());
		     statement.executeUpdate();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
	}
	
	//add friendship to db with the two ids
	public void addFriendship(int userId1, int userId2) {	
		PreparedStatement statement = null;
	    try {
			 String query = "INSERT INTO friendship (userId1, userId2) VALUES (?,?)";
		     statement = (PreparedStatement) connection.prepareStatement(query);         
		     statement.setInt(1, userId1);
		     statement.setInt(2, userId2);
		     statement.executeUpdate();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
	}
	
	//add project membership
	public void addProjectMembership(User user, Project project) {	
		PreparedStatement statement = null;
	    try {
			 String query = "INSERT INTO project_membership (userId, projectId) VALUES (?,?)";
		     statement = (PreparedStatement) connection.prepareStatement(query);         
		     statement.setInt(1, user.getUserId());
		     statement.setInt(2, project.getProjectId());
		     statement.executeUpdate();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
	}
	
	//add activity membership
	public void addActivityMembership(User user, Activity activity) {	
		PreparedStatement statement = null;
	    try {
			 String query = "INSERT INTO activity_membership (userId, activityId) VALUES (?,?)";
		     statement = (PreparedStatement) connection.prepareStatement(query);         
		     statement.setInt(1, user.getUserId());
		     statement.setInt(2, activity.getActivityId());
		     statement.executeUpdate();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
	}
	
	//
	public void addNotification(Notification notification) {
		PreparedStatement statement = null;
		try {
			 String query = "INSERT INTO notification (notificationId, message, date, time, targetId, isDelivered, type) VALUES (?,?,?,?,?,?,?)";
		     statement = (PreparedStatement) connection.prepareStatement(query);   
		     statement.setInt(1, notification.getNotificationId());
		     statement.setString(2, notification.getMessage());
		     statement.setString(3, notification.getDate().toString());
		     statement.setString(4, notification.getTime().toString());
		     statement.setInt(5, notification.getTargetId()); 
		     statement.setBoolean(6, notification.isDelivered());
		     statement.setString(7, notification.getType().toString().toLowerCase());
		     statement.executeUpdate();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
	}

	//remove user from database into unregistration process(only one time)
	public void removeUser(User user) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try{
			String query = "DELETE FROM user WHERE userId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, user.getUserId());
			statement.executeUpdate();
			
			query = "SELECT * FROM project WHERE adminId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, user.getUserId());
			resultSet = statement.executeQuery();
			while(resultSet.next()){
				removeAllByProjectId(resultSet.getInt(1));
			}
			
			query = "DELETE FROM project WHERE adminId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, user.getUserId());
			statement.executeUpdate();
			
			query = "DELETE FROM project_membership WHERE userId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, user.getUserId());
			statement.executeUpdate();
			
			query = "DELETE FROM activity_membership WHERE userId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, user.getUserId());
			statement.executeUpdate();
			
			query = "DELETE FROM friendship WHERE userId1 = ? OR userId2 = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, user.getUserId());
			statement.setInt(2, user.getUserId());
			statement.executeUpdate();
			
			query = "DELETE FROM notification WHERE targetId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, user.getUserId());
			statement.executeUpdate();
			
			query = "DELETE FROM activity_attachment WHERE authorId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, user.getUserId());
			statement.executeUpdate();
			
		}catch(SQLException e){ 
			e.printStackTrace();
		}
	}
	
	public void removeAllByProjectId(int projectId){
		
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try{
			
			String query = "SELECT * FROM activity WHERE projectId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, projectId);
			resultSet = statement.executeQuery();
			while(resultSet.next()){
				removeAllByActivityId(resultSet.getInt(1));
			}
			
			query = "DELETE FROM project_membership WHERE projectId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, projectId);
			statement.executeUpdate();
			
			query = "DELETE FROM activity WHERE projectId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, projectId);
			statement.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void removeAllByActivityId(int activityId){
		PreparedStatement statement = null;
		try{		
			String query = "DELETE FROM activity_membership WHERE activityId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, activityId);
			statement.executeUpdate();
			
			query = "DELETE FROM activity_attachment WHERE activityId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, activityId);
			statement.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	//remove project from database passing the object from where I call the function
	public void removeProject(Project project) {
		PreparedStatement statement = null;
		try{
			String query = "DELETE FROM project WHERE projectId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, project.getProjectId());
			statement.executeUpdate();
			
			String query2 = "DELETE FROM activity WHERE projectId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query2);
			statement.setInt(1, project.getProjectId());
			statement.executeUpdate();
			
			String query3 = "DELETE FROM project_membership WHERE projectId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query3);
			statement.setInt(1, project.getProjectId());
			statement.executeUpdate();
			
			ArrayList<Activity> projectActivities = project.getActivities();
			
			for(int i = 0; i < projectActivities.size(); i++){
				int activityId = projectActivities.get(i).getActivityId();
				String query4 = "DELETE FROM activity_membership WHERE activityId = ?";
				statement = (PreparedStatement) connection.prepareStatement(query4);
				statement.setInt(1, activityId);
				statement.executeUpdate();
			}

		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	//remove activity from database passing the object from where I call the function
	public void removeActivity(Activity activity) {
		PreparedStatement statement = null;
		try{
			String query = "DELETE FROM activity WHERE activityId=?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, activity.getActivityId());
			statement.executeUpdate();
			
			String query2 = "DELETE FROM activity_membership WHERE activityId=?";
			statement = (PreparedStatement) connection.prepareStatement(query2);
			statement.setInt(1, activity.getActivityId());
			statement.executeUpdate();

		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	//remove friendship from database
	public void removeFriendship(int user1, int user2) {
		PreparedStatement statement = null;		
		try{
			String query = "DELETE FROM friendship WHERE userId1=? userId2=?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, user1);
			statement.setInt(2, user2);
			statement.executeUpdate();	
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	//remove project membership
	public void removeProjectMembership(int userId, int projectId) {
		PreparedStatement statement = null;		
		try{
			String query = "DELETE pm.* , am.* FROM project_membership pm INNER JOIN activity_membership am ON am.userId = pm.userId WHERE pm.userId=? AND projectId=?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, userId);
			statement.setInt(2, projectId);
			statement.executeUpdate();
			
			String query2 = "DELETE FROM project_membership WHERE userId=? AND projectId=?";
			statement = (PreparedStatement) connection.prepareStatement(query2);
			statement.setInt(1, userId);
			statement.setInt(2, projectId);
			statement.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	//remove activity membership
	public void removeActivityMembership(int userId, int activityId) {
		PreparedStatement statement = null;		
		try{
			String query = "DELETE FROM activity_membership WHERE userId=? AND activityId=?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, userId);
			statement.setInt(2, activityId);
			statement.executeUpdate();	
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	//remove notification from database when read
	public void removeNotification(int notificationId) {
		PreparedStatement statement = null;		
		try{
			String query = "DELETE FROM notification WHERE notificationId=?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, notificationId);
			statement.executeUpdate();	
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	//remove attachment
	public void removeAttachment(int activityAttachmentId) {
		PreparedStatement statement = null;
		try {
			 String query = "DELETE FROM activity_attachment WHERE attachmentId=?";
		     statement = (PreparedStatement) connection.prepareStatement(query);         
		     statement.setInt(1, activityAttachmentId);
		     statement.executeUpdate();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
	}
	
	//getting the following id in the project table for the newcoming project
	public int getLastProjectId(){
		int lastProjectId = 0;
		Statement statement = null;
		ResultSet resultSet = null;		
		try{
			String query = "SELECT MAX(projectId) FROM project";
			statement = (Statement) connection.createStatement();
			resultSet = statement.executeQuery(query);		
			if(resultSet.next())		
				lastProjectId = resultSet.getInt(1);
			else 
				lastProjectId = 0;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return lastProjectId;
	}
	
	//getting the following id in the activity table for the newcoming activity
	public int getLastActivityId(){
		int lastActivityId = 0;
		Statement statement = null;
		ResultSet resultSet = null;
		try{
			String query = "SELECT MAX(activityId) FROM activity";
			statement = (Statement) connection.createStatement();
			resultSet = statement.executeQuery(query);
			if(resultSet.next())		
				lastActivityId = resultSet.getInt(1);
			else 
				lastActivityId = 0;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return lastActivityId;
	}
	
	public int getLastNotificationId(){
		int lastNotificationId = 0;
		Statement statement = null;
		ResultSet resultSet = null;
		try{
			String query = "SELECT MAX(notificationId) FROM notification";
			statement = (Statement) connection.createStatement();
			resultSet = statement.executeQuery(query);
			if(resultSet.next())		
				lastNotificationId = resultSet.getInt(1);
			else 
				lastNotificationId = 0;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return lastNotificationId;
	}
	
	public int getLastAttachmentId(){
		int lastAttachmentId = 0;
		Statement statement = null;
		ResultSet resultSet = null;
		try{
			String query = "SELECT MAX(attachmentId) FROM activity_attachment";
			statement = (Statement) connection.createStatement();
			resultSet = statement.executeQuery(query);
			if(resultSet.next())		
				lastAttachmentId = resultSet.getInt(1);
			else 
				lastAttachmentId = 0;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return lastAttachmentId;
	}
	
	//get the client id from credentials (only one time) 
	public int getUserId(String username, String password) {	
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int returnedId = 0;
		try{
			String query = "SELECT * FROM user WHERE username = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setString(1, username);
			resultSet = statement.executeQuery();		
			if(resultSet.next()){
				if(resultSet.getString(3).equals(password)){
					returnedId = resultSet.getInt(1);
				}
				else{
					throw new WrongPasswordException();
				}
			}
			else{
				throw new UserNotFoundException();
			}		
		}catch(SQLException e){
			e.printStackTrace();
		}catch(UserNotFoundException u){
			return -1;
		}catch(WrongPasswordException w){
			return -2;
		}		
		return returnedId;
	}
	
	//download and create from database the list of all extisting users on the server when booting
	public ArrayList<User> getAllUsers(){
		Statement statement;
		ResultSet resultSet;
		ArrayList<User> allUsers = new ArrayList<User>();
		try{
			String query = "SELECT * FROM user";
			statement = (Statement) connection.createStatement();
			resultSet = statement.executeQuery(query);
			
			while (resultSet.next()){
				allUsers.add(new User(resultSet.getInt(1), resultSet.getString(2)));
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return allUsers;
	}

	//download and create from database the list of all extisting projects on the server when booting
	public ArrayList<Project> getAllProjects(){	
		Statement statement;
		ResultSet resultSet;
		ArrayList<Project> allProjects = new ArrayList<Project>();
		try{
			String query = "SELECT * FROM project";
			statement = (Statement) connection.createStatement();
			resultSet = statement.executeQuery(query);		
			while (resultSet.next()){
				int adminId = resultSet.getInt(5);
				User admin = null;
				for(int i = 0; i < ServerImpl.getInstance().getRegisteredUsers().size(); i++){
					User user = ServerImpl.getInstance().getRegisteredUsers().get(i);
					int id = user.getUserId();
					if(id == adminId){
						admin = ServerImpl.getInstance().getRegisteredUsers().get(i);
					}
				}
				allProjects.add(new Project(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), Category.getCategory(resultSet.getString(4)), admin, ProjectState.getState(resultSet.getString(6))));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}		
		return allProjects;
	}
	
	//download and create from database the list of all extisting activities on the server when booting
	public ArrayList<Activity> getAllActivities() {	
		Statement statement;
		ResultSet resultSet;
		ArrayList<Activity> allActivity = new ArrayList<Activity>();
		try{
			String query = "SELECT * FROM activity";
			statement = (Statement) connection.createStatement();
			resultSet = statement.executeQuery(query);
			
			while (resultSet.next()){
				Project parentProject = null;
				int activityId = resultSet.getInt(1);
				int projectId = resultSet.getInt(2);
				String name = resultSet.getString(3);
				String descr = resultSet.getString(4);
				String place = resultSet.getString(5);
				int day = resultSet.getInt(6);
				int month = resultSet.getInt(7);
				int year = resultSet.getInt(8);
				boolean completed = resultSet.getBoolean(9);
				boolean active = resultSet.getBoolean(10);
				Activity newActivity = new Activity(activityId, name, descr, place, day, month, year, active, completed);
				try {
					parentProject = ServerImpl.getInstance().getProjectById(projectId);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				parentProject.addActivity(newActivity);
				newActivity.setParentProject(parentProject);
				allActivity.add(newActivity);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}			
		return allActivity;
	}
	
	public ArrayList<ActivityAttachment> getAllAttachments() throws RemoteException {	
		Statement statement;
		ResultSet resultSet;
		ArrayList<ActivityAttachment> allAttachments = new ArrayList<ActivityAttachment>();
		try{
			String query = "SELECT * FROM activity_attachment";
			statement = (Statement) connection.createStatement();
			resultSet = statement.executeQuery(query);
			
			while (resultSet.next()){
				Activity parentActivity = null;
				User author = null;
				int attachmentId = resultSet.getInt(1);
				String text = resultSet.getString(2);
				int authorId = resultSet.getInt(3);
				int activityId = resultSet.getInt(4);
				ActivityAttachment newAttachment = new ActivityAttachment(attachmentId, text);
				parentActivity = ServerImpl.getInstance().getActivityById(activityId);
				author = ServerImpl.getInstance().getUserById(authorId);
				newAttachment.setParentActivity(parentActivity);
				newAttachment.setAuthor(author);
				parentActivity.addAttachment(newAttachment);
				allAttachments.add(newAttachment);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}			
		return allAttachments;
	}
	
	public ArrayList<Notification> getAllNotifications() {	
		Statement statement;
		ResultSet resultSet;
		NotificationSimpleFactory notificationFactory = new NotificationSimpleFactory();
		ArrayList<Notification> allNotifications = new ArrayList<Notification>();
		try{
			String query = "SELECT * FROM notification";
			statement = (Statement) connection.createStatement();
			resultSet = statement.executeQuery(query);
			
			while (resultSet.next()){
				int notificationId = resultSet.getInt(1);
				String message = resultSet.getString(2);
				//String date = resultSet.getString(3);
				//String time = resultSet.getString(4);
				int targetId = resultSet.getInt(5);
				boolean isDelivered = resultSet.getBoolean(6);
				String type = resultSet.getString(7);
				Notification newNotification = notificationFactory.createNotification(notificationId, type, "", targetId, isDelivered);
				newNotification.setMessage(message);
				allNotifications.add(newNotification);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}				
		return allNotifications;
	}
	
	
	
	//retrieve the ids of the projects of the user with the passed id
	public ArrayList<Integer> getCollabProjectsIdByUserId(int userId) {		
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<Integer> projectIds = new ArrayList<Integer>();
		try{
			String query = "SELECT projectId FROM project_membership WHERE userId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, userId);
			resultSet = statement.executeQuery();
			while(resultSet.next()){
				projectIds.add(resultSet.getInt(1));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}	
		return projectIds;
	}
	
	public ArrayList<Integer> getManagedProjectsIdByUserId(int userId) {		
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<Integer> projectIds = new ArrayList<Integer>();
		try{
			String query = "SELECT projectId FROM project WHERE adminId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, userId);
			resultSet = statement.executeQuery();
			while(resultSet.next()){
				projectIds.add(resultSet.getInt(1));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}	
		return projectIds;
	}
	
	//retrieve the ids of the activities of the user with the passed id
	public ArrayList<Integer> getActivitiesIdByUserId(int userId) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<Integer> activityIds = new ArrayList<Integer>();
		try{
			String query = "SELECT activityId FROM activity_membership WHERE userId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, userId);
			resultSet = statement.executeQuery();
			while(resultSet.next()){
				activityIds.add(resultSet.getInt(1));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}	
		return activityIds;
	}
	
	//retrieve the ids of the friends of the user with the passed id
	public ArrayList<Integer> getFriendsIdByUserId(int userId) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<Integer> friendsIds = new ArrayList<Integer>();
		try{
			String query = "SELECT userId2 FROM friendship WHERE userId1 = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, userId);
			resultSet = statement.executeQuery();
			while(resultSet.next()){
				friendsIds.add(resultSet.getInt(1));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}	
		return friendsIds;
	}
	
	public ArrayList<Integer> getNotificationByUserId(int userId) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<Integer> notificationIds = new ArrayList<Integer>();
		try{
			String query = "SELECT notificationId FROM notification WHERE targetId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, userId);
			resultSet = statement.executeQuery();
			while(resultSet.next()){
				notificationIds.add(resultSet.getInt(1));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}	
		return notificationIds;
	}
	
	public void modifyProject(Project project){
		PreparedStatement statement = null;
		String query = "UPDATE project SET title = ?, description = ?, category = ? WHERE projectId = ?";
		try{
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setString(1, project.getTitle());
			statement.setString(2, project.getDescription());
			statement.setString(3, project.getCategory().toString().toLowerCase());
			statement.setInt(4, project.getProjectId());
			statement.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void modifyActivity(Activity activity){
		PreparedStatement statement = null;
		String query = "UPDATE activity SET name = ?, description = ?, place = ?, day = ?, month = ?, year = ? WHERE activityId = ?";
		try{
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setString(1, activity.getName());
			statement.setString(2, activity.getDescription());
			statement.setString(3, activity.getPlace());
			statement.setInt(4, activity.getDate().getDay());
			statement.setInt(5, activity.getDate().getMonth());
			statement.setInt(6, activity.getDate().getYear());
			statement.setInt(7, activity.getActivityId());
			statement.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void modifyNotifications(ArrayList<Notification> deliveredNotifications){
		PreparedStatement statement = null;
		String query = "UPDATE notification SET isDelivered = 1 WHERE notificationId = ?";
		try{
			statement = (PreparedStatement) connection.prepareStatement(query);
		}catch(SQLException e){
			e.printStackTrace();
		}
		for(int i = 0; i < deliveredNotifications.size(); i++){
			try{
				statement.setInt(1, deliveredNotifications.get(i).getNotificationId());
				statement.executeUpdate();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public void startProject(Project project){
		PreparedStatement statement = null;
		String query = "UPDATE project SET state = 'active' WHERE projectId = ?";
		try{
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, project.getProjectId());
			statement.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void completeProject(Project project){
		PreparedStatement statement = null;
		String query = "UPDATE project SET state = 'completed' WHERE projectId = ?";
		try{
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, project.getProjectId());
			statement.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void activeActivity(Activity activity){
		PreparedStatement statement = null;
		String query = "UPDATE activity SET isActive = 1 WHERE activityId = ?";
		try{
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, activity.getActivityId());
			statement.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void disactiveActivity(Activity activity){
		PreparedStatement statement = null;
		String query = "UPDATE activity SET isActive = 0 WHERE activityId = ?";
		try{
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, activity.getActivityId());
			statement.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void completeActivity(Activity activity){
		PreparedStatement statement = null;
		String query = "UPDATE activity SET isCompleted = 1 WHERE activityId = ?";
		try{
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, activity.getActivityId());
			statement.executeUpdate();
			disactiveActivity(activity);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
