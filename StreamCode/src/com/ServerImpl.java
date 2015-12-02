package com;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.rmi.server.*;

public class ServerImpl extends UnicastRemoteObject implements Subject, ServerInterface {

	private static final long serialVersionUID = 1L;
	private static ServerImpl uniqueInstance;
	private DBManager dbManager;
	private NotificationSimpleFactory notificationFactory;
	private ArrayList<Project> registeredProjects;
	private ArrayList<Activity> registeredActivities;
	private ArrayList<User> registeredUsers;
	private ArrayList<Notification> registeredNotifications;
	private ArrayList<ClientInterface> onlineClients;
	private ServerInterfaceObserver serverInterface;
	private ArrayList<ActivityAttachment> registeredAttachments;
	
	//CONSTRUCTORS---------------------------------------------------------------------
	private ServerImpl() throws RemoteException{
		this.dbManager = DBManager.getInstance();
		this.notificationFactory = new NotificationSimpleFactory();
		this.registeredProjects = new ArrayList<Project>();
		this.registeredActivities = new ArrayList<Activity>();
		this.registeredUsers = new ArrayList<User>();
		this.registeredAttachments = new ArrayList<ActivityAttachment>();
		this.onlineClients = new ArrayList<ClientInterface>();
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
	
	public void retrieveAllAttachment() throws RemoteException{
		this.registeredAttachments = dbManager.getAllAttachments();
	}
	
	public void linkProjectsToCollaborators() throws RemoteException{		
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
	
	public void linkProjectsToAdmins() throws RemoteException{		
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
		onlineClients.add(client);
	}
	
	public void unregisterClient(ClientInterface client) throws RemoteException{
		onlineClients.remove(client);
		System.out.println("User with id=" + client.getClientId() + " logged out");
	}

	@Override
	public void notifyUser(ClientInterface o, Notification n) throws RemoteException {
		o.getNotification(n);
	}
	
	public void addInterfaceObserver(ServerInterfaceObserver serverInterface){
		this.serverInterface = serverInterface;
	}
	
	private void updateInterface(){
		serverInterface.update();
	}
	
	//LOGIN-LOGOUT----------------------------------------------------------------------------
	@Override
	public int check(String username, String password) throws RemoteException {
		int id = dbManager.getUserId(username, password); 

		if(id == -1){
			return -1;
		}
		else if(id == -2){
			return -2;
		}
		else{
			for(int i = 0; i < onlineClients.size(); i++){
				if (id == onlineClients.get(i).getClientId())
					return -3;
			}
		}
		return id;
	}

	@Override
	public int login(String username, String password, ClientInterface client) throws RemoteException {
		int userId = -1; //initialization (not 0 because exists)
			for(int i = 0; i < registeredUsers.size(); i++){
				if(registeredUsers.get(i).getUsername().equals(username)){
					userId = registeredUsers.get(i).getUserId();
					registerClient(client); //add the client to online ones
					updateInterface();
				}
			}
		return userId;
	}

	@Override
	public void logout(ClientInterface client) throws RemoteException {
		unregisterClient(client); //rimuovo utente dalla lista degli utenti loggati
		updateInterface();
	}
	
	public boolean isClientOnline(int clientId) throws RemoteException{
		
		boolean isOnline = false;
		
		for (int i = 0; i < onlineClients.size(); i++){
			if(clientId == onlineClients.get(i).getClientId()){
				isOnline = true;
				return isOnline;
			}
		}
		return isOnline;
	}
	
	//ADDERS--------------------------------------------------------------------------------------
	@Override
	public void registerUser(String username, String password) throws RemoteException {
		dbManager.addUser(username, password);
		int id = dbManager.getUserId(username, password);
		User user = new User(id, username);
		registeredUsers.add(user);
		updateInterface();
	}
	
	@Override
	public void addFriend(int userId1, int userId2) throws RemoteException {
		User user1 = getUserById(userId1);
		User user2 = getUserById(userId2);
		user1.addFriend(user2);
		dbManager.addFriendship(user1.getUserId(), user2.getUserId());
		Notification notification = createNotification("friendship", user1.getUsername(), userId2);
		registeredNotifications.add(notification);
		if (isClientOnline(userId2)){
			ClientInterface clientToBeNotified = getClientById(userId2);
			notifyUser(clientToBeNotified, notification);
			notification.setDelivered(true);
		}
		dbManager.addNotification(notification);
	}
	
	
	
	@Override
	public void addProject(String title, String description, Category category, int userId) throws RemoteException {
		User user = getUserById(userId);
		int lastProjectId = dbManager.getLastProjectId();
		Project project = new Project(lastProjectId+1, title, description, category, user, ProjectState.INACTIVE);//I need the state because when I retrieve from database 																										  
		registeredProjects.add(project);																	  //not all projects are inactive
		dbManager.addProject(project);
		user.addManagedProject(project);
		updateInterface();
	}
	
	@Override
	public void addAttachment(String text, int userId, int activityId) throws RemoteException {
		User user = getUserById(userId);
		Activity activity = getActivityById(activityId);
		int lastAttachmentId = dbManager.getLastAttachmentId();
		ActivityAttachment attachment = new ActivityAttachment(lastAttachmentId + 1, text, activity, user);	
		activity.addAttachment(attachment);
		registeredAttachments.add(attachment);																	  //not all projects are inactive
		dbManager.addAttachment(attachment);
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
		Notification notification = notificationFactory.createNotification(notificationId, type, description, targetId, false);
		return notification;
	}
	
	@Override
	public void addCollaborators(int projectId, ArrayList<Integer> userIds) throws RemoteException {
		Project project = getProjectById(projectId);
		User user;
		for(int i = 0; i < userIds.size(); i++){
			int idTarget = userIds.get(i).intValue();
			Notification notification = createNotification("project_invite", project.getTitle(), idTarget);
			registeredNotifications.add(notification);
			user = getUserById(idTarget);
			if (isClientOnline(idTarget)){
				ClientInterface clientToBeNotified = getClientById(idTarget);
				notifyUser(clientToBeNotified, notification);
				notification.setDelivered(true);
			}
			project.addCollaborator(user);
			user.addCollaborationProject(project);
			dbManager.addProjectMembership(user, project);	
			dbManager.addNotification(notification);
		}
	}
	@Override
	public void startProject(int projectId) throws RemoteException{
		Project project = getProjectById(projectId);
		if (project.getActivities().size() > 0){
			User user;
			ClientInterface clientToBeNotified;
			for (int i = 0; i < project.getCollaborators().size(); i++){
				user = project.getCollaborators().get(i);
				int targetId = user.getUserId();
				Notification notification = createNotification("project_started", project.getTitle(), targetId);
				registeredNotifications.add(notification);
				if(isClientOnline(targetId)){
					clientToBeNotified = getClientById(targetId);
					notifyUser(clientToBeNotified, notification);
					notification.setDelivered(true);
				}
				dbManager.addNotification(notification);
			}
			project.startProject();
			dbManager.startProject(project);
			project.getActivities().get(0).setActive(true);
			dbManager.activeActivity(project.getActivities().get(0));
			updateInterface();
		}
	}
	
	@Override 
	public void completeActivity(int activityId, int userId) throws RemoteException{
		Activity activity = getActivityById(activityId);	
		User user;
		activity.setCompleted(true);
		activity.setActive(false);
		dbManager.completeActivity(activity);
		
		for (int i = 0; i < activity.getActivityCollaborators().size(); i++){
			user = activity.getActivityCollaborators().get(i);
			if(user.getUserId()!= userId){
				int targetId = user.getUserId();
				Notification notification = createNotification("activity_completed", activity.getName(), user.getUserId());
				registeredNotifications.add(notification);
				if(isClientOnline(targetId)){
					ClientInterface clientToBeNotified = null;
					clientToBeNotified = getClientById(targetId);
					notifyUser(clientToBeNotified, notification);
					notification.setDelivered(true);
				}
			}
		}
		
		Project currentProject = activity.getParentProject();
		Activity nextActivity = null;
		boolean isLast = true;
		for(int i = 0; i < currentProject.getActivities().size()-1; i++){
			if(currentProject.getActivities().get(i).equals(activity)){
				nextActivity = currentProject.getActivities().get(i+1);
				isLast = false;
				break;
			}
		}		
		if (!isLast){
			for(int i=0; i < nextActivity.getActivityCollaborators().size(); i++){
				User userNextActivity = nextActivity.getActivityCollaborators().get(i);
				int nextTargetId = userNextActivity.getUserId();
				if(nextTargetId != userId){
					Notification nextNotification = createNotification("activity_started", activity.getName(), nextTargetId);
					registeredNotifications.add(nextNotification);
					if(isClientOnline(nextTargetId)){
						ClientInterface clientNextActivity = getClientById(nextTargetId);
						notifyUser(clientNextActivity, nextNotification);
						nextNotification.setDelivered(true);
					}
					dbManager.addNotification(nextNotification);
				}
			}
			nextActivity.setActive(true);
			dbManager.activeActivity(nextActivity);
		}
		else{
			currentProject.setState(ProjectState.COMPLETED);
			dbManager.completeProject(currentProject);
			updateInterface();
		}
		
	}
	
	@Override
	public void addAgent(int activityId, int userId) throws RemoteException {
		Activity activity = getActivityById(activityId);
		User user = getUserById(userId);
		int targetId = user.getUserId();
		Notification notification = createNotification("agent_added", activity.getName(), userId);
		registeredNotifications.add(notification);
		if(isClientOnline(targetId)){
			ClientInterface clientToBeNotified = getClientById(targetId);
			notifyUser(clientToBeNotified, notification);
			notification.setDelivered(true);
		}
		activity.addAgent(user);
		user.addUserActivities(activity);
		dbManager.addActivityMembership(user, activity);
		dbManager.addNotification(notification);
	}
	
	@Override
	public void sendBroadcast(String description, ArrayList<Integer> ids){
		for(int i = 0; i < ids.size(); i++){	
			Notification notification = createNotification("broadcast", description, ids.get(i));
			registeredNotifications.add(notification);
			try {
				if(isClientOnline(ids.get(i))){
					ClientInterface clientToBeNotified = getClientById(ids.get(i));				
					notifyUser(clientToBeNotified, notification);	
					notification.setDelivered(true);			
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}			
			dbManager.addNotification(notification);			
		}
	}
	
	@Override
	public void notifyAdmin(String message, int adminId){
		Notification notification = createNotification("broadcast", message, adminId);
		registeredNotifications.add(notification);
		try {
			if(isClientOnline(adminId)){
				ClientInterface clientToBeNotified = getClientById(adminId);				
				notifyUser(clientToBeNotified, notification);
				notification.setDelivered(true);			
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}			
		dbManager.addNotification(notification);			
	}
	
	//REMOVERS-----------------------------------------------------------------------------------

	@Override
	public void unregisterUser(int userId, String password, ClientInterface client) throws RemoteException {
		logout(client);
		User user = getUserById(userId);
		dbManager.removeUser(user.getUsername(), password);
		registeredUsers.remove(user);
		updateInterface();
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
		updateInterface();
	}
	
	@Override
	public void removeActivity(int projectId, int activityId, int userId) throws RemoteException {
		Project project = getProjectById(projectId);
		Activity activity = getActivityById(activityId);
		boolean isLast = true;
		Activity nextActivity = null;
		
		for(int i = 0; i < activity.getActivityCollaborators().size(); i++){
			activity.getActivityCollaborators().get(i).getUserActivities().remove(activity);
		}
		if (activity.isActive()){
			int activityPosition = project.getActivities().indexOf(activity);
			if(activityPosition != -1){
				if(activityPosition != (project.getActivities().size() - 1)){ //IF LAST ELEMENT OF THE LIST
					nextActivity = project.getActivities().get(activityPosition+1);
					nextActivity.setActive(true);
					dbManager.activeActivity(nextActivity);
					isLast = false;
				}
			}
			if (!isLast){
				for(int i=0; i < nextActivity.getActivityCollaborators().size(); i++){
					User userNextActivity = nextActivity.getActivityCollaborators().get(i);
					int nextTargetId = userNextActivity.getUserId();
					if(nextTargetId != userId){
						Notification nextNotification = createNotification("activity_started", activity.getDescription(), nextTargetId);
						registeredNotifications.add(nextNotification);
						if(isClientOnline(nextTargetId)){
							ClientInterface clientNextActivity = getClientById(nextTargetId);
							notifyUser(clientNextActivity, nextNotification);
							nextNotification.setDelivered(true);
						}
						dbManager.addNotification(nextNotification);
					}
				}
			}
			else{
				project.setState(ProjectState.COMPLETED);
				dbManager.completeProject(project);
			}		
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
		for (int i = 0; i < project.getActivities().size(); i++){
			Activity activity = project.getActivities().get(i);
			activity.removeAgent(user);
			user.getUserActivities().remove(activity);
		}
	}

	@Override
	public void removeAgent(int activityId, int userId) throws RemoteException {
		Activity activity = getActivityById(activityId);
		User user = getUserById(userId);
		activity.getActivityCollaborators().remove(user);
		user.getUserActivities().remove(activity);
		dbManager.removeActivityMembership(user.getUserId(), activity.getActivityId());	
	}
	
	@Override
	public void removeAttachment(int attachmentId, int activityId){
		Activity activity = getActivityById(activityId);
		ActivityAttachment attachment = getAttachmentById(attachmentId);
		activity.removeAttachment(attachment);
		registeredAttachments.remove(attachment);
		dbManager.removeAttachment(attachmentId);
	}
	
	//MODIFIERS------------------------------------------------------------------------------------
	@Override
	public void modifyProject(int projectId, String title, String description, Category category) throws RemoteException {
		Project project = getProjectById(projectId);
		project.setTitle(title);
		project.setDescription(description);
		project.setCategory(category);
	}

	@Override
	public void modifyActivity(int activityId, String name, String description, String place, String dateTime) throws RemoteException {
		Activity activity = getActivityById(activityId);
		activity.setName(name);
		activity.setDescription(description);
		activity.setPlace(place);
		activity.setDateTime(dateTime);
		
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
		return user;
	}
	
	public ClientInterface getClientById(int clientId)throws RemoteException{
		ClientInterface client = null;
		for(int i = 0; i < onlineClients.size(); i++){
			if(onlineClients.get(i).getClientId() == clientId){
				client = onlineClients.get(i);
				return client;
			}
		}
		return client;
	}
	
	public Project getProjectById(int projectId) throws RemoteException{
		Project project = null;
		for(int i = 0; i < registeredProjects.size(); i++){
			if(registeredProjects.get(i).getProjectId() == projectId){
				project = registeredProjects.get(i);
				return project;
			}
		}
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
		return activity;
	}
	
	public ActivityAttachment getAttachmentById(int attachmentId){
		ActivityAttachment attachment = null;
		for(int i = 0; i < registeredAttachments.size(); i++){
			if(registeredAttachments.get(i).getAttachmentId() == attachmentId){
				attachment = registeredAttachments.get(i);
				return attachment;
			}
		}
		return attachment;
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
	
	public ArrayList<Project> getCollaborationProjects(int userId) throws RemoteException{
		ArrayList<Project> collaborationProject = new ArrayList<Project>();
		try{
			collaborationProject = getUserById(userId).getCollaborationProject();
		}catch(RemoteException e){
			e.printStackTrace();
		}
		return collaborationProject;
	}
	
	@Override
	public ArrayList<Activity> getMyActivities(int userId) throws RemoteException{
		ArrayList<Activity> myActivities = new ArrayList<Activity>();
		myActivities = getUserById(userId).getUserActivities();
		return myActivities;
	}
	
	public ArrayList<Notification> getNotificationsById(int userId){
		ArrayList<Notification> notifications = new ArrayList<Notification>();
		for(int i = 0; i < registeredNotifications.size(); i++){
			if(registeredNotifications.get(i).getTargetId() == userId){
				notifications.add(registeredNotifications.get(i));
			}
		}
		return notifications;
	}
	
	public ArrayList<Notification> getOfflineNotificationsById(int userId){
		ArrayList<Notification> notDeliveredNotifications = new ArrayList<Notification>();
		for(int i = 0; i < registeredNotifications.size(); i++){
			if(registeredNotifications.get(i).getTargetId() == userId && registeredNotifications.get(i).isDelivered() == false){
				notDeliveredNotifications.add(registeredNotifications.get(i));
				registeredNotifications.get(i).setDelivered(true);
			}
		}
		dbManager.modifyNotifications(notDeliveredNotifications);
		return notDeliveredNotifications;
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
	
	public ArrayList<ClientInterface> getOnlineClients() {
		return onlineClients;
	}
	
	public ArrayList<Project> getActiveProjects(){
		ArrayList<Project> activeProjects = new ArrayList<Project>();
		for (int i = 0; i < registeredProjects.size(); i++){
			if(registeredProjects.get(i).getState() == ProjectState.ACTIVE){
				activeProjects.add(registeredProjects.get(i));
			}
		}
		return activeProjects;
	}
	
	public ArrayList<Project> getCompletedProjects(){
		ArrayList<Project> completedProjects = new ArrayList<Project>();
		for (int i = 0; i < registeredProjects.size(); i++){
			if(registeredProjects.get(i).getState() == ProjectState.COMPLETED){
				completedProjects.add(registeredProjects.get(i));
			}
		}
		return completedProjects;
	}
}
