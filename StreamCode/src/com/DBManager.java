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
		     statement.setInt(1, project.getId());
		     statement.setString(2, project.getTitle());
		     statement.setString(3, project.getDescription());
		     statement.setString(4, project.getCategory().toString().toLowerCase());
		     statement.setInt(5, project.getAdminInt());
		     statement.setString(6, project.getState().toString().toLowerCase());
		     statement.executeUpdate();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
	}

	public void addActivity(Activity activity) {
	}

	public void addNotification() {
	}

	public void removeUser(String username, String password) {
		
	}

	public void removeProject(Project project) {
	}

	public void removeActivity(Activity activity) {
	}

	public void removeNotification(Notification notification ) {
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
	
	public ArrayList<Project> getAllProject(){
		
		Statement statement;
		ResultSet resultSet;
		ArrayList<Project> allProjects = new ArrayList<Project>();
		try{
			String query = "SELECT * FROM project";
			statement = (Statement) connection.createStatement();
			resultSet = statement.executeQuery(query);
			
			while (resultSet.next()){
				allProjects.add(new Project(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), Category.getCategory(resultSet.getString(4)), resultSet.getInt(5)));
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		for(int i = 0; i < allProjects.size(); i++){
			System.out.println(allProjects.get(i).toString());
		}
		
		return allProjects;
	}
	
	public ArrayList<User> getAllUser(){
		
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

	/* public ArrayList<Project> getProjectByUser(User user) {
		
		PreparedStatement statement;
		ResultSet resultSet;
		ArrayList<Project> projectsByUser = new ArrayList<Project>();
		try{
			String query = "SELECT * FROM projectmembership JOIN project ON membership.projectId = project.projectId WHERE userId = ?";
			statement = (PreparedStatement) connection.prepareStatement(query);
			statement.setInt(1, user.getUserId());
			resultSet = statement.executeQuery();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		try{
			while(resultSet.next()){
				Project projectToBeFilled = new Project(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), Category.getCategory(resultSet.getString(4)), user);
				ResultSet projectUsers;
				PreparedStatement projectUsersStatement;
				String projectUsersQuery = "SELECT * FROM projectmembership WHERE projectId = ?";
				projectUsersStatement = (PreparedStatement) connection.prepareStatement(projectUsersQuery);
				statement.setInt(1, projectToBeFilled.getId());
				projectUsers = statement.executeQuery();
				while(projectUsers.next()){
				}
				
				PreparedStatement innerStatement;
				String innerQuery = "SELECT * FROM activity WHERE projectId = ?";
				innerStatement = (PreparedStatement) connection.prepareStatement(innerQuery);
				statement.setString(1, resultSet.getString(2));
				innerResultSet = innerStatement.executeQuery();
				
				while(innerResultSet.next()){
					Activity activityToBeFilled = new Activity();
				}
				
				projectsByUser.add(projectToBeFilled);
				
			}
		}
		
	} */
	
	public void getActivity() {
	}
	
	public void getNotification() {
	}
	
	public static void main(String[] args){
		
		DBManager db = DBManager.getInstance();
		
		db.getAllProject();
		db.getAllUser();
		
	}
	
}
