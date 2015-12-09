package server;

import java.io.Serializable;

public class Credential implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String dbUsername;
	final String dbPassword;
	
	public Credential(){
		dbUsername = "root";
		dbPassword = "93Matrix";
	}
	
	public String getDbUsername(){
		return dbUsername;		
	}
	
	public String getDbPassword(){
		return dbPassword;	
	}
}
