package com;

import java.sql.*;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class DBManager {
//gui branch modified
	private String dbusername;

	private String dbpassword;
	
	public DBManager(){
		connect();
	}

	public Connection connect(){
		Connection connection = null;
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
		
		return connection;
	}

	public void addUser() {
	}

	public void addProject() {
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
	
	public static void main(String[] args){
		
		DBManager dbmanager = new DBManager();
		
		Statement statement = null;
	    ResultSet resultSet = null;
		
	    try {
		 String query = "SELECT * FROM project";
	     statement = (Statement) dbmanager.connect().createStatement();           
	     resultSet = statement.executeQuery(query);
	     while (resultSet.next())
				System.out.println(resultSet.getString(1));
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		
	}
	
}
