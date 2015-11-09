package com;

public class WrongPasswordException extends Exception{

	private static final long serialVersionUID = 1L;
	
	      public WrongPasswordException(){}
	      
	      public void printError(){
	    	  System.out.println("Wrong Password");
	      }

}
