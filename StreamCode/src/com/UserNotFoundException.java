package com;

import java.io.Serializable;

public class UserNotFoundException extends Exception implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException(){}
	
	public void PrintError(){
		System.out.println("User not found!");
	}

}
