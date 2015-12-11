package test;


import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.sql.ResultSet;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import server.DBManager;
import server.ServerImpl;

public class RegisterTest {
	
	ServerImpl server;
	static DBManager dbManager;
	
	@BeforeClass
	public static void connectDB(){
		dbManager = DBManager.getInstance();
		dbManager.connect();
	}
	
	@Before
	public void initialize(){
		server = ServerImpl.getInstance();
	}
	
	@AfterClass
	public static void confirm(){
		System.out.println("Test completato");
	}
	
	public String getLastUsernameFromDB(){
		Statement statement = null;
		ResultSet resultset = null;
		String retrievedUsername = null;
	    try {
			 String query = "SELECT u.username FROM user u WHERE u.userId = (SELECT MAX(userId) from user)";
		     statement = (PreparedStatement) dbManager.connect().createStatement();        
		     resultset = statement.executeQuery(query);
		     resultset.next();
		     retrievedUsername = new String(resultset.getString(1));
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
	    return retrievedUsername;
	}
	
	public String getLastPasswordFromDB(){
		Statement statement = null;
		ResultSet resultset = null;
		String retrievedPassword = null;
	    try {
			 String query = "SELECT u.password FROM user u WHERE u.userId = (SELECT MAX(userId) from user)";
		     statement = (PreparedStatement) dbManager.connect().createStatement();        
		     resultset = statement.executeQuery(query);
		     resultset.next();
		     retrievedPassword = new String(resultset.getString(1));
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
	    return retrievedPassword;
	}

	@Test (expected = NullPointerException.class)
	public void registerTest() throws RemoteException {
		
		String username = "anielrossi";
		String password = "rossi";
		
		server.registerUser(username, password);
		
		dbManager.addUser(username, password);
		
		String retrievedUsername = getLastUsernameFromDB();
		String retrievedPassword = getLastPasswordFromDB();
		
		assertEquals(username, retrievedUsername);
		assertEquals(password, retrievedPassword);
		assertEquals(username, server.getRegisteredUsers().get(server.getRegisteredUsers().size() - 1).getUsername()); //username is univoque
	}
}
