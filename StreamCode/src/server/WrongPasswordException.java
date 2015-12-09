package server;

import java.io.Serializable;

public class WrongPasswordException extends Exception implements Serializable{

	private static final long serialVersionUID = 1L;
	
	      public WrongPasswordException(){}
	      
	      public void printError(){
	    	  System.out.println("Wrong Password");
	      }

}
