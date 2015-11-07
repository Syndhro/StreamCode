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

  		User cli = new User();

  		Registry registry = LocateRegistry.getRegistry("localhost");
  		ServerImpl server = (ServerImpl) registry.lookup("server");

      server.registerObserver(cli);
//
  	}
}
