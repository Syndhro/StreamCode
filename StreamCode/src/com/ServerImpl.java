package com;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.rmi.server.*;

public class ServerImpl extends UnicastRemoteObject implements Subject, ServerInterface {

	private static ServerImpl uniqueInstance;
	private DBManager dbManager;
	private NotificationSimpleFactory notificationFactory;
	private ArrayList<Project> registeredProjects;
	private ArrayList<Activity> registeredActivities;
	private ArrayList<User> registeredUsers;
	private ArrayList<Observer> loggedUsers;
	private ArrayList<Notification> registeredNotifications;
	private ArrayList<ClientInterface> clients;
	
	//CONSTRUCTORS---------------------------------------------------------------------

	private ServerImpl() throws RemoteException{
		this.dbManager = DBManager.getInstance();
		this.notificationFactory = new NotificationSimpleFactory();
		this.loggedUsers = new ArrayList<Observer>();
		this.registeredProjects = new ArrayList<Project>();
		this.registeredActivities = new ArrayList<Activity>();
		this.registeredUsers = new ArrayList<User>();
		this.clients = new ArrayList<ClientInterface>();
	}
	
	public static ServerImpl getInstance() {
		if (uniqueInstance == null){
			try{
			uniqueInstance = new ServerImpl();
			}catch(RemoteException e){
				e.printStackTrace();
			}
		}
		return uniqueInstance;
	}
	
	//DB RETRIEVING OPERATIONS--------------------------------------------------------------
	
	public void retrieveAllProjects(){
		this.registeredProjects = dbManager.getAllProjects();
	}
	
	public void retrieveAllUsers(){
		this.registeredUsers = dbManager.getAllUsers();
	}
	
	public void retrieveAllActivities(){
		this.registeredActivities = dbManager.getAllActivities();
	}
	
	public void retrieveAllNotifications(){
		this.registeredNotifications = dbManager.getAllNotifications();
	}
	
	public void linkProjectsToCollaborators(){		
		ArrayList<Integer> projectIds = new ArrayList<Integer>();
		for (int i = 0; i < registeredUsers.size(); i++){
			projectIds = dbManager.getCollabProjectsIdByUserId(registeredUsers.get(i).getUserId());
			for(int j = 0; j < projectIds.size(); j++){
				Project project = getProjectById(projectIds.get(j));
				registeredUsers.get(i).addCollaborationProject(project);
				project.addCollaborator(registeredUsers.get(i));
			}
		}	
	}
	
	public void linkProjectsToAdmins(){		
		ArrayList<Integer> projectIds = new ArrayList<Integer>();
		for (int i = 0; i < registeredUsers.size(); i++){
			projectIds = dbManager.getManagedProjectsIdByUserId(registeredUsers.get(i).getUserId());
			for(int j = 0; j < projectIds.size(); j++){
				Project project = getProjectById(projectIds.get(j));
				registeredUsers.get(i).addManagedProject(project);
				project.setAdmin(registeredUsers.get(i));
			}
		}	
	}
	
	public void linkActivitiesToUsers(){	
		ArrayList<Integer> activityIds = new ArrayList<Integer>();
		for (int i = 0; i < registeredUsers.size(); i++){
			activityIds = dbManager.getActivitiesIdByUserId(registeredUsers.get(i).getUserId());
			for(int j = 0; j < activityIds.size(); j++){
				Activity activity = getActivityById(activityIds.get(j));	
				registeredUsers.get(i).addUserActivities(activity);
				activity.addAgent(registeredUsers.get(i));
			}
		}	
	}
	
	public void linkFriends(){
		ArrayList<Integer> friendsIds = new ArrayList<Integer>();
		for(int i = 0; i < registeredUsers.size(); i++){
			friendsIds = dbManager.getFriendsIdByUserId(registeredUsers.get(i).getUserId());
			User user = null;
			for(int j = 0; j < friendsIds.size(); j++){
				try{
					user = getUserById(friendsIds.get(j));
				}catch(RemoteException e){
					e.printStackTrace();
				}
				registeredUsers.get(i).addFriend(user);
				}
		}
	};
	
	//OBSERVER PATTERN------------------------------------------------------------------------
	

	public void registerClient(ClientInterface client) throws RemoteException{
		clients.add(client);
		//client.getNotification("Registrato");
	}

	public void registerObserver(Observer o) {
		loggedUsers.add(o);
	}
	
	public void removeObserver(Observer o) {
		int i = loggedUsers.indexOf(o);
		if(i >= 0){
			loggedUsers.remove(i);
		}
	}
	
	public void notifyObservers() {
		for (int i=0; i < loggedUsers.size(); i++){
			loggedUsers.get(i).update();
		}
	}
	
	//LOGIN-LOGOUT----------------------------------------------------------------------------
	@Override
	public int check(String username, String password) throws RemoteException {
		int id = dbManager.getUserId(username, password); 

		if(id == -1){
			return -1;
		}
		if(id == -2){
			return -2;
		}
		return id;
	}

	@Override
	public int login(String username, String password) throws RemoteException {
		int userId = -1;
			for(int i = 0; i < registeredUsers.size(); i++){
				if(registeredUsers.get(i).getUsername().equals(username)){
					registerObserver(registeredUsers.get(i));//aggiungimi a utenti loggati
					userId = registeredUsers.get(i).getUserId();
				}
			}
		return userId;
	}

	@Override
	public void logout(int userId) throws RemoteException {
		User user = getUserById(userId);
		removeObserver(user); //rimuovo utente dalla lista degli utenti loggati	
	}
	
	//ADDERS--------------------------------------------------------------------------------------

	@Override
	public void registerUser(String username, String password) throws RemoteException {
		dbManager.addUser(username, password);
		int id = dbManager.getUserId(username, password);
		User user = new User(id, username);
		registeredUsers.add(user);
	}
	
	@Override
	public void addFriend(int userId1, int userId2) throws RemoteException {
		User user1 = getUserById(userId1);
		User user2 = getUserById(userId2);
		ClientInterface clientToBeNotified = getClientById(userId2);
		user1.addFriend(user2);
		dbManager.addFriendship(user1.getUserId(), user2.getUserId());
		Notification notification = createNotification("friendship", user1.getUsername(), userId2);
		clientToBeNotified.getNotification(notification);
		dbManager.addNotification(notification);
	}
	
	@Override
	public void addProject(String title, String description, Category category, int userId) throws RemoteException {
		User user = getUserById(userId);
		int projectId = dbManager.getLastProjectId();
		Project project = new Project(projectId+1, title, description, category, user);
		registeredProjects.add(project);
		dbManager.addProject(project);
		for(int i = 0; i < registeredUsers.size(); i++){
			if(user.getUsername().equals(registeredUsers.get(i).getUsername())){
				registeredUsers.get(i).addManagedProject(project);
			}
		}
	}
	
	@Override
	public void addActivity(int projectId, String name, String description, String place, String dateTime) throws RemoteException {
		Project project = getProjectById(projectId);
		int id = dbManager.getLastActivityId();
		Activity activity = new Activity(id+1, project, name, description, place, dateTime);
		project.addActivity(activity);
		registeredActivities.add(activity);
		dbManager.addActivity(activity);
	}
	
	public Notification createNotification(String type, String description, int targetId){
		int notificationId = dbManager.getLastNotificationId() + 1;
		Notification notification = notificationFactory.createNotification(notificationId, type, description, targetId);
		return notification;
	}
	
	@Override
	public void addCollaborators(int projectId, ArrayList<Integer> userIds) throws RemoteException {
		Project project = getProjectById(projectId);
		User user;
		ClientInterface clientToBeNotified;
		for(int i = 0; i < userIds.size(); i++){
			Notification notification = createNotification("project_invite", project.getDescription(), userIds.get(i));
			user = getUserById(userIds.get(i));
			clientToBeNotified = getClientById(userIds.get(i));
			project.addCollaborator(user);
			user.addCollaborationProject(project);
			dbManager.addProjectMembership(user, project);	
			dbManager.addNotification(notification);
			clientToBeNotified.getNotification(notification);
		}
	}
	@Override
	public void startProject(int projectId) throws RemoteException{
		Project project = getProjectById(projectId);	
		User user;
		ClientInterface clientToBeNotified;
		for (int i = 0; i < project.getCollaborators().size(); i++){
			user = project.getCollaborators().get(i);
			clientToBeNotified = getClientById(project.getCollaborators().get(i).getUserId());
			Notification notification = createNotification("project_started", project.getDescription(), user.getUserId());
			dbManager.addNotification(notification);
			clientToBeNotified.getNotification(notification);
		}
		project.startProject();
	}
	
	@Override 
	public void completeActivity(int activityId) throws RemoteException{
		Activity activity = getActivityById(activityId);	
		User user;
		ClientInterface clientToBeNotified;
		for (int i = 0; i < activity.getActivityCollaborators().size(); i++){
			user = activity.getActivityCollaborators().get(i);
			clientToBeNotified = getClientById(activity.getActivityCollaborators().get(i).getUserId());
			Notification notification = createNotification("activity_completed", activity.getDescription(), user.getUserId());
			clientToBeNotified.getNotification(notification);
			dbManager.addNotification(notification);
		}
		activity.complete();
		Project currentProject = activity.getParentProject();
		Activity nextActivity = null;
		for(int i = 0; i < currentProject.getActivities().size(); i++){
			if(currentProject.getActivities().get(i).equals(activity)){
				nextActivity = currentProject.getActivities().get(i+1);
				break;
			}
		}
		for(int i=0; i < nextActivity.getActivityCollaborators().size(); i++){
			User userNextActivity = nextActivity.getActivityCollaborators().get(i);
			ClientInterface clientNextActivity = getClientById(nextActivity.getActivityCollaborators().get(i).getUserId());
			Notification nextNotification = createNotification("activity_started", activity.getDescription(), userNextActivity.getUserId());
			clientNextActivity.getNotification((nextNotification));
			dbManager.addNotification(nextNotification);
		}
		nextActivity.setActive(true);	
	}
	
	@Override
	public void addAgent(int activityId, int userId) throws RemoteException {
		Activity activity = getActivityById(activityId);
		User user = getUserById(userId);
		ClientInterface clientToBeNotified = getClientById(userId);
		Notification nextNotification = createNotification("agent_added", activity.getDescription(), userId);
		activity.addAgent(user);
		user.addUserActivities(activity);
		dbManager.addActivityMembership(user, activity);
	}
	
	//REMOVERS-----------------------------------------------------------------------------------

	@Override
	public void unregisterUser(int userId, String password) throws RemoteException {
		logout(userId);
		User user = getUserById(userId);
		dbManager.removeUser(user.getUsername(), password);
		registeredUsers.remove(user);
	}
	
	@Override
	public void removeFriend(int userId1, int userId2) throws RemoteException {
		User user1 = getUserById(userId1);
		User user2 = getUserById(userId2);
		user1.getUserFriends().remove(user2);
		dbManager.removeFriendship(user1.getUserId(), user2.getUserId());
	}
	
	@Override
	public void removeProject(int projectId) throws RemoteException {
		Project project = getProjectById(projectId);
		project.getAdmin().getManagedProject().remove(project);
		for(int i = 0; i < project.getCollaborators().size(); i++){
			project.getCollaborators().get(i).getCollaborationProject().remove(project);
		}
		dbManager.removeProject(project);
		registeredProjects.remove(project);
	}
	
	@Override
	public void removeActivity(int projectId, int activityId) throws RemoteException {
		Project project = getProjectById(projectId);
		Activity activity = getActivityById(activityId);
		for(int i = 0; i < activity.getActivityCollaborators().size(); i++){
			activity.getActivityCollaborators().get(i).getUserActivities().remove(activity);
		}
		project.getActivities().remove(activity);
		dbManager.removeActivity(activity);
		registeredActivities.remove(activity);
	}
	
	@Override
	public void removeCollaborator(int projectId, int userId) throws RemoteException {
		Project project = getProjectById(projectId);
		User user = getUserById(userId);
		project.getCollaborators().remove(user);
		user.getCollaborationProject().remove(project);
		dbManager.removeProjectMembership(user.getUserId(), project.getProjectId());
	}

	@Override
	public void removeAgent(int activityId, int userId) throws RemoteException {
		Activity activity = getActivityById(activityId);
		User user = getUserById(userId);
		activity.getActivityCollaborators().remove(user);
		user.getUserActivities().remove(activity);
		dbManager.removeActivityMembership(user.getUserId(), activity.getActivityId());
		
	}
	
	//GETTERS------------------------------------------------------------------------------------
	
	@Override
	public ArrayList<User> searchUser(String string) throws RemoteException {
		ArrayList<User> matchedUsers = new ArrayList<User>();
		for(int i = 0; i < registeredUsers.size(); i++){
			if(registeredUsers.get(i).getUsername().contains(string)){
				matchedUsers.add(registeredUsers.get(i));
			}
		}
		return matchedUsers;
	}
	
	public User getUserById(int userId)throws RemoteException{
		User user = null;
		for(int i = 0; i < registeredUsers.size(); i++){
			if(registeredUsers.get(i).getUserId() == userId){
				user = registeredUsers.get(i);
				return user;
			}
		}
		System.out.println("User not found");
		return user;
	}
	
	public ClientInterface getClientById(int clientId)throws RemoteException{
		ClientInterface client = null;
		for(int i = 0; i < clients.size(); i++){
			if(clients.get(i).getMyUserId() == clientId){
				client = clients.get(i);
				return client;
			}
		}
		System.out.println("User not found");
		return client;
	}

	public Project getProjectById(int projectId){
		Project project = null;
		for(int i = 0; i < registeredProjects.size(); i++){
			if(registeredProjects.get(i).getProjectId() == projectId){
				project = registeredProjects.get(i);
				return project;
			}
		}
		System.out.println("Project not found");
		return project;
	}
	
	@Override
	public ArrayList<User> getUserFriend(int userId) throws RemoteException {
		User user = getUserById(userId);
		ArrayList<User> userFriends = new ArrayList<User>();
		userFriends = user.getUserFriends();
		return userFriends;
	}
	
	public Activity getActivityById(int activityId){
		Activity activity = null;
		for(int i = 0; i < registeredActivities.size(); i++){
			if(registeredActivities.get(i).getActivityId() == activityId){
				activity = registeredActivities.get(i);
				return activity;
			}
		}
		System.out.println("Activity not found");
		return activity;
	}
	
	public ArrayList<Project> getManagedProject(int userId) throws RemoteException{
		ArrayList<Project> managedProject = new ArrayList<Project>();
		try{
			managedProject = getUserById(userId).getManagedProject();
		}catch(RemoteException e){
			e.printStackTrace();
		}
		return managedProject;
	}
	
	public ArrayList<Notification> getNotificationsById(int userId) throws RemoteException{
		ArrayList<Notification> notifications = new ArrayList<Notification>();
		for(int i = 0; i < registeredNotifications.size(); i++){
			if(registeredNotifications.get(i).getTargetId() == userId){
				notifications.add(registeredNotifications.get(i));
			}
		}
		return notifications;
	}
	
	public ArrayList<User> getRegisteredUsers() {
		return registeredUsers;
	}
	
	public ArrayList<Project> getRegisteredProjects() {
		return registeredProjects;
	}
	
	public ArrayList<Activity> getRegisteredActivities() {
		return registeredActivities;
	}
	
	//TEST PRINT-----------------------------------------------------------------------------------

	public void stampa()throws RemoteException{
		for(int i = 0; i < registeredProjects.size(); i++){
		System.out.println("Project=" + registeredProjects.get(i).getTitle());
		System.out.println("Users:"); 
			for(int j=0; j < registeredProjects.get(i).getCollaborators().size(); j++){
				System.out.println(registeredProjects.get(i).getCollaborators().get(j).getUsername());
			}
		System.out.println("Admin:");	
		System.out.println(registeredProjects.get(i).getAdmin().getUsername());
		}
		System.out.println();
		for(int i = 0; i < registeredUsers.size(); i++){
			System.out.println("Users:" + registeredUsers.get(i).getUsername());
			System.out.println("Projects:");
			for(int j=0; j < registeredUsers.get(i).getManagedProject().size(); j++){
				System.out.println(registeredUsers.get(i).getManagedProject().get(j).getTitle());
			}
		}
		System.out.println();
		for(int i = 0; i < registeredUsers.size(); i++){
			System.out.println("Users:" + registeredUsers.get(i).getUsername());
			System.out.println("Activities:");
			for(int j=0; j < registeredUsers.get(i).getUserActivities().size(); j++){
				System.out.println(registeredUsers.get(i).getUserActivities().get(j).getName());
			}
		}
		System.out.println("----------------------------------------------------------------");
	}
	
	//MAIN
	
}
