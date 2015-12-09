package client;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.*;

import server.Activity;
import server.Category;
import server.Notification;
import server.Project;
import server.ProjectState;
import server.User;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProjectListFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	ProjectListFrame thisFrame;
	Project project;
	Activity activity;
	ArrayList<Project> managedProjects;
	ArrayList<Project> collaborationProjects;
	ArrayList<Notification> notifications;
	ArrayList<Activity> activities;
	
	Container mainPanel;
	JButton refresh;
	JButton friendsButton;
	JButton addFriendButton;
	JButton addProjectButton;
	JButton unregisterButton;
	JPanel managedProjectPanel;
	JPanel collaborationProjectPanel;
	JPanel notificationsPanel;
	JPanel activitiesPanel;
	JPanel projectSettings;
	ArrayList<JButton> managedProjectButtons;
	ArrayList<JButton> collaborationProjectButtons;
	JTextArea notificationsArea;
	JTextArea myActivitiesArea;
	JLabel notificationsLabel;
	JLabel activityiesLabel;
	
	public ProjectListFrame(Client client) throws RemoteException {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 700, 400);
		mainPanel = getContentPane();						
		mainPanel.setLayout(new FlowLayout());	
			
		//INITIALIZATIONS
		project = null;
		activity = null;
		thisFrame = this;
		refresh = new JButton("Refresh");
		friendsButton = new JButton("See your friends :)");
		addFriendButton = new JButton("Add Friend");
		addProjectButton = new JButton("Add project");
		unregisterButton = new JButton("Unregister");
		
		//RETRIEVE NEEDED OBJECTS
		managedProjects = client.getManagedProject();
		collaborationProjects = client.getCollaborationProject();
		notifications = client.getOfflineNotifications();	
		activities = client.getMyActivities();

		managedProjectPanel = new JPanel();
		managedProjectPanel.setLayout(new BoxLayout(managedProjectPanel, BoxLayout.PAGE_AXIS));
		managedProjectPanel.add(new JLabel("My Projects"));
		managedProjectButtons = new ArrayList<JButton>();
		mainPanel.add(managedProjectPanel);
		
		collaborationProjectPanel = new JPanel();
		collaborationProjectPanel.setLayout(new BoxLayout(collaborationProjectPanel, BoxLayout.PAGE_AXIS));
		collaborationProjectPanel.add(new JLabel("Other's Projects"));
		collaborationProjectButtons = new ArrayList<JButton>();
		mainPanel.add(collaborationProjectPanel);
			
		notificationsPanel = new JPanel();
		notificationsPanel.setLayout(new BoxLayout(notificationsPanel, BoxLayout.PAGE_AXIS));
		notificationsArea = new JTextArea(8, 12);
		for(int i = 0; i < notifications.size(); i++){
			notificationsArea.append(notifications.get(i).getMessage() +"\n");		
		}
		notificationsLabel = new JLabel("Notifications");
		notificationsPanel.add(notificationsLabel);
		notificationsPanel.add(notificationsArea);
		JScrollPane scrollNotifications = new JScrollPane (notificationsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		mainPanel.add(scrollNotifications);
		
		activitiesPanel = new JPanel();
		activitiesPanel.setLayout(new BoxLayout(activitiesPanel, BoxLayout.PAGE_AXIS));
		myActivitiesArea = new JTextArea(10, 14);
		activityiesLabel = new JLabel("My Activities");
		activitiesPanel.add(activityiesLabel);
		activitiesPanel.add(myActivitiesArea);
		JScrollPane scrollActivities = new JScrollPane (activitiesPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		mainPanel.add(scrollActivities);
		
		//MANAGED PROJECTS---------------------------------------------------------------------------------------------
		for(int i = 0; i < managedProjects.size(); i++){
			JButton managedProjectbutton = new JButton();
			managedProjectbutton.setText(managedProjects.get(i).getTitle());
			if(managedProjects.get(i).getState().equals(ProjectState.INACTIVE)){
				managedProjectbutton.setBackground(Color.RED);
			}
			else if(managedProjects.get(i).getState().equals(ProjectState.COMPLETED)){
				managedProjectbutton.setBackground(Color.GREEN);
			}
			else{
				managedProjectbutton.setBackground(Color.YELLOW);
			}
			managedProjectPanel.add(managedProjectbutton);
			managedProjectButtons.add(managedProjectbutton);
			
			managedProjectbutton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event) {
					int i = managedProjectButtons.indexOf(event.getSource());
					project = client.getManagedProject().get(i);
					//OPEN ACTIVITY PANEL------------------------------------------------------------------
						ActivityPanel activityPanel = null;
						try {
							activityPanel = new ActivityPanel(i, thisFrame, client);
							int result = JOptionPane.showConfirmDialog(thisFrame, activityPanel,"Activities", JOptionPane.CLOSED_OPTION);
							if(result == JOptionPane.CLOSED_OPTION){
								setVisible(false);
								ProjectListFrame projectListFrame = new ProjectListFrame(client);
								projectListFrame.setLocationRelativeTo(null);
	 							projectListFrame.setVisible(true);	
							}
						} catch (RemoteException e) {
							e.printStackTrace();
						}	
				}
			});				
		}
		
		//COLLABORATION PROJECTS-------------------------------------------------------------------------------------
		for(int j = 0; j < collaborationProjects.size(); j++){
			JButton collaborationProjectButton = new JButton();
			collaborationProjectButton.setText(collaborationProjects.get(j).getTitle());
			collaborationProjectButton.setBackground(Color.CYAN);
			collaborationProjectPanel.add(collaborationProjectButton);
			collaborationProjectButtons.add(collaborationProjectButton);
			collaborationProjectButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ev) {
					int k = collaborationProjectButtons.indexOf(ev.getSource());
					project = client.getCollaborationProject().get(k);
					//OPEN ACTIVITY PANEL------------------------------------------------------------------
					CollabActivityPanel collActivityPanel = null;
						try {
							collActivityPanel = new CollabActivityPanel(k, thisFrame, client);
							int result = JOptionPane.showConfirmDialog(thisFrame, collActivityPanel,"Activities", JOptionPane.CLOSED_OPTION);
							if(result == JOptionPane.CLOSED_OPTION){
								setVisible(false);
								ProjectListFrame projectListFrame = new ProjectListFrame(client);
								projectListFrame.setLocationRelativeTo(null);
	 							projectListFrame.setVisible(true);	
							}
						} catch (RemoteException e) {
							e.printStackTrace();
						}
				}
			});				
		}
		
		//GENERAL ACTIVITIES SUMMARY
		for(int j = 0; j < activities.size(); j++){
			myActivitiesArea.append("Name Activity: " + activities.get(j).getName() + "\n");
			myActivitiesArea.append("Name Project: " + activities.get(j).getParentProject().getTitle() + "\n");			
			if(activities.get(j).isActive()){			
				myActivitiesArea.append("Active: yes" + "\n"); 
			}
			else{
				myActivitiesArea.append("Active: no" + "\n"); 			
			}
			if(activities.get(j).isCompleted()){			
				myActivitiesArea.append("Completed: yes" + "\n"); 
			}
			else{
				myActivitiesArea.append("Completed: no" + "\n"); 			
			}
			myActivitiesArea.append("\n");
		}
		
		//PROJECT SETTINGS--------------------------------------------------------------------------------
		projectSettings = new JPanel();
		mainPanel.add(projectSettings);
		
		//ADD FRIEND PROCESS
		addFriendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FriendsPanel friendsPanel = new FriendsPanel(client);
				JOptionPane.showConfirmDialog(mainPanel, friendsPanel, "Friends", JOptionPane.OK_CANCEL_OPTION);
			}
		});	
		projectSettings.add(addFriendButton);
		
		//ADD PROJECT PROCESS	
		projectSettings.add(addProjectButton);
		addProjectButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				JTextField projectTitle = new JTextField(15);
				JTextField projectDescription = new JTextField(40);
				String[] categories = Category.getStringsArray();			
				@SuppressWarnings({ "rawtypes", "unchecked" })
				JComboBox projectCategory = new JComboBox(categories);
				
				JPanel projectValues = new JPanel();
				projectValues.setLayout(new BoxLayout(projectValues, BoxLayout.PAGE_AXIS));
				projectValues.add(new JLabel("Insert title: "));
				projectValues.add(projectTitle);
				projectValues.add(new JLabel(" "),"span, grow");
				projectValues.add(new JLabel("Insert description: "));
				projectValues.add(projectDescription);
				projectValues.add(new JLabel(" "),"span, grow");
				projectValues.add(new JLabel("Insert category: ")); 
				projectValues.add(projectCategory);
				projectValues.setVisible(true);


				int result = JOptionPane.showConfirmDialog(thisFrame, projectValues, "Please Enter Project Values", JOptionPane.OK_CANCEL_OPTION);			
				if (result == JOptionPane.OK_OPTION){
					if(!projectTitle.getText().equals("")){	
						ProjectListFrame projectListFrame = null;
							try {
								String projectTitleString = projectTitle.getText();
								String projectDescriptionString = projectDescription.getText();
								String projectCategoryString = projectCategory.getSelectedItem().toString();
								Category projectCategoryCategory =  Category.getCategory(projectCategoryString);
								int clientId = client.getClientId();
								client.addProject(projectTitleString, projectDescriptionString, projectCategoryCategory, clientId);
								dispose();
								projectListFrame = new ProjectListFrame(client);			
							} catch (RemoteException e1) {			
								e1.printStackTrace();
							}
							projectListFrame.setLocationRelativeTo(null);
 							projectListFrame.setVisible(true);						
					}
					else{
						JOptionPane.showMessageDialog(thisFrame, "A project must have a name");
					}
				}
			}
		});
		
		//REFRESH BUTTON
		refresh.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				thisFrame.dispose();
				ProjectListFrame newFrame = null;
				try {
					newFrame = new ProjectListFrame(client);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				newFrame.setLocationRelativeTo(null);
				newFrame.setVisible(true);
			}  	
		});
		add(refresh);

		
		//LISTA DEGLI AMICI----------------------------------------------------------------------------------------------------------------------------	
		friendsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel friendsPanel = new JPanel();
				friendsPanel.setLayout(new BoxLayout(friendsPanel, BoxLayout.PAGE_AXIS));
				ArrayList<User> friends = new ArrayList<User>();
				friends = client.getUserFriends();
				for(int i = 0; i < friends.size(); i++){
					JLabel friendLabel = new JLabel(friends.get(i).getUsername());			
					friendsPanel.add(friendLabel);
					friendsPanel.add(new JLabel(" "),"span, grow");
				}
				JOptionPane.showConfirmDialog(mainPanel,friendsPanel, "Friends List", JOptionPane.CLOSED_OPTION);
			}	
		});
		
		//ON CLOSING
		thisFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(thisFrame, 
		            "Are you sure to close this window?You will be logged out automatically", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		            try {
						client.logout();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
		        	System.exit(0);
		        }
		    }
		});		
		mainPanel.add(friendsButton);
		mainPanel.add(unregisterButton);
		
		//UNREGISTER PROCESS
		unregisterButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(thisFrame, 
					"Are you sure do you want to unregister from service?", "Really?The account will be deactivated", 
			        JOptionPane.YES_NO_OPTION,
			        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
					try {
						client.unregisterUser();
					} catch (RemoteException ev) {
						ev.printStackTrace();
					}
					System.exit(0);
		        }
			}			
		});
	}
}
