package com;

import java.sql.*;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class DBManager {
//db stuff
	
	private Connection connection;
	
	private String dbusername;

	private String dbpassword;
	
	public DBManager(){
		connect();
	}

	public void connect(){
		this.connection = null;
		try {
		     Class.forName("com.mysql.jdbc.Driver");
		     
		     connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/streamdata", "root", "93Matrix");			      	
	    }
	    catch(ClassNotFoundException e) {
	      e.printStackTrace();
	    }
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	}

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

	public void addProject(Project p) {
		
PreparedStatement statement = null;
	    
	    try {
			 String query = "INSERT INTO project (projectId, title, description, category, adminId, state) VALUES (?,?,?,?,?,?)";
		     statement = (PreparedStatement) connection.prepareStatement(query);         
		     statement.setInt(1, p.getId());
		     statement.setString(2, p.getTitle());
		     statement.setString(3, p.getDescription());
		     statement.setString(4, p.getCategory().toString().toLowerCase());
		     statement.setInt(5, p.getAdminInt());
		     statement.setString(6, p.getState().toString().toLowerCase());
		     statement.executeUpdate();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
	}

	public void addActivity() {
	}

	public void addNotification() {
	}

	public void removeUser() {
	}

	public void removeProject() {
	}

	public void removeActivity() {
	}

	public void removeNotification() {
	}
	
	public void getUser() {
	}

	public void getProject() {
	}

	public void getActivity() {
	}

	public void getNotification() {
	}
	
	public static void main(String[] args){
		
		DBManager dbManager = new DBManager();
		
		dbManager.addUser("aniel93", "bucio");
		
	}
	
}
