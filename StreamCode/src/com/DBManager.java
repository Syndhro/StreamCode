package com;

import java.sql.*;
import java.util.ArrayList;

import javax.print.attribute.standard.RequestingUserName;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class DBManager {
	
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

	//connect to database
	
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
	
	//add to database

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

	public void addActivity(Activity activity) {
		
		PreparedStatement statement = null;
		
		try {
			 String query = "INSERT INTO activity (activityId, projectId, name, description, place, dateTime, isActive, isComplete) VALUES (?,?,?,?,?,?,?,?)";
		     statement = (PreparedStatement) connection.prepareStatement(query);         
		     statement.setInt(1, activity.getActivityId());
		     statement.setInt(2, activity.getParentProject().getProjectId());
		     statement.setString(3, activity.getName());
		     statement.setString(4, activity.getDescription());
		     statement.setString(5, activity.getPlace());
		     statement.setString(6, activity.getDateTime());
		     statement.setBoolean(7, activity.isActive());
		     statement.setBoolean(8, activity.isCompleted());
		     statement.executeUpdate();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		
	}

	public void addNotification(Notification notification) {
		
		
		
	}

	public void removeUser(String username, String password) {
		
	}

	public void removeProject(Project project) {
	}

	public void removeActivity(Activity activity) {
	}

	public void removeNotification(Notification notification ) {
	}
	
	public int getLastProjectId(){
		int lastProjectId = 0;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try{
			String query = "SELECT MAX projectId FROM project";
			statement = (Statement) connection.createStatement();
			resultSet = statement.executeQuery(query);
			
			resultSet.next();
			
			lastProjectId = resultSet.getInt(1);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return lastProjectId;
	}
	
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
				allProjects.add(new Project(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), Category.getCategory(resultSet.getString(4)), admin));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		for(int i = 0; i < allProjects.size(); i++){
			System.out.println(allProjects.get(i).toString());
		}
		
		return allProjects;
	}
	
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
		
		for(int i = 0; i < allUsers.size(); i++){
			System.out.println(allUsers.get(i).toString());
		}
		
		return allUsers;
	}

	public ArrayList<Integer> getProjectsIdByUserId(int userId) {
		
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
	
public ArrayList<Integer> getActivityIdByUserId(int userId) {
		
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
	
	public ArrayList<Activity> getAllActivity() {
		
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
				String dateTime = resultSet.getString(6);
				boolean completed = resultSet.getBoolean(7);
				boolean active = resultSet.getBoolean(8);
				Activity newActivity = new Activity(activityId, name, descr, place, dateTime, completed, active);
				for(int i = 0; i < ServerImpl.getInstance().getRegisteredProjects().size(); i++){
					int id = ServerImpl.getInstance().getRegisteredProjects().get(i).getProjectId();
					if(id == projectId){
						parentProject = ServerImpl.getInstance().getRegisteredProjects().get(i);
						parentProject.addActivity(newActivity);
						newActivity.setParentProject(parentProject);
					}
				}
				allActivity.add(newActivity);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		for(int i = 0; i < allActivity.size(); i++){
			System.out.println(allActivity.get(i).toString());
		}
		
		return allActivity;
	}
	
	public void getNotification() {
	}

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
	
	public static void main(String[] args){
		
		DBManager db = DBManager.getInstance();
		
		//db.getAllProjects();
		//db.getAllUsers();
		
	}
	
}
