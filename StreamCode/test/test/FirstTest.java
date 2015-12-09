package test;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import server.DBManager;
import server.ServerImpl;

public class FirstTest {
	
	ServerImpl server;
	DBManager dbManager;
	
	@BeforeClass
	public static void connectDB(){
		dbManager = DBManager.getInstance();
		dbManager.connect();
	}
	
	@Before
	public void initialize(){
		server = ServerImpl.getInstance();
	}
	
	public void getLastUsernameFromDB(){
		Statement statement = null;
	    try {
			 String query = "SELECT u.username , u.password FROM user u WHERE u.userId = (SELECT MAX(userId) from user)";
		     statement = (PreparedStatement) dbManager.connect().createStatement();        
		     resultset = statement.executeQuery(query);
		     while
		    }catch(Exception e){
		    	e.printStackTrace();
		    }	
	}
	
	public void getLastUsernameFromDB(){
		Statement statement = null;
	    try {
			 String query = "SELECT u.username , u.password FROM user u WHERE u.userId = (SELECT MAX(userId) from user)";
		     statement = (PreparedStatement) dbManager.connect().createStatement();        
		     resultset = statement.executeQuery(query);
		     while
		    }catch(Exception e){
		    	e.printStackTrace();
		    }	
	}
	
	@Test
	public void registerTest() {
		String username = "anielrossi";
		String password = "rossi";
		
		server.registerUser(username, password);
		
		dbManager.addUser(username, password);
		
		
	}
}
