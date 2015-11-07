package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client {
  public static void main(String[] args) throws NotBoundException, IOException {
  		System.setSecurityManager(new RMISecurityManager());

  		//locate registry and get an instance of remote server
  		Registry registry = LocateRegistry.getRegistry("localhost");
  		ServerImpl server = (ServerImpl) registry.lookup("server");
  		//input da tastiera
 		String username = "";
 		String password = "";
 		
 		int userId = server.check(username, password); //se non è presente viene sollevata una eccezione all'interno di SQL
 		User client = new User(userId);
 		
  		//add me to the server list
  		server.registerObserver(client);
  		
  	}
}
