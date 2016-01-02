package server;

public class Credential {
	final String dbUsername;
	final String dbPassword;
	
	public Credential(){
		dbUsername = "root";
		dbPassword = "informatica";
	}
	
	public String getDbUsername(){
		return dbUsername;		
	}
	
	public String getDbPassword(){
		return dbPassword;	
	}
}