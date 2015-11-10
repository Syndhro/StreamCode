package com;

import java.sql.*;

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
	
	public int getUser(String username, String password) {
		
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

	public void getProject) {
	}

	public void getActivity() {
	}

	public void getNotification() {
	}
	
}
