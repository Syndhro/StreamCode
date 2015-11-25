package com;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProjectListFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	ProjectListFrame thisFrame = this;
	Project project = null;
	
	public ProjectListFrame(Client client) throws RemoteException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		Container mainPanel = getContentPane();						
		mainPanel.setLayout(new FlowLayout());
		
		//recupero gli oggetti che mi servono
		ArrayList<Project> managedProjects = new ArrayList<Project>();
		ArrayList<Project> collaborationProjects = new ArrayList<Project>();
		ArrayList<Notification> notifications = new ArrayList<Notification>();	
		
		managedProjects = client.getManagedProject();
		collaborationProjects = client.getCollaborationProject();
		notifications = client.getOfflineNotifications();
		
		JPanel managedProjectPanel = new JPanel();
		managedProjectPanel.setLayout(new BoxLayout(managedProjectPanel, BoxLayout.PAGE_AXIS));
		managedProjectPanel.add(new JLabel("My Projects"));
		
		JPanel collaborationProjectPanel = new JPanel();
		collaborationProjectPanel.setLayout(new BoxLayout(collaborationProjectPanel, BoxLayout.PAGE_AXIS));
		collaborationProjectPanel.add(new JLabel("Other's Projects"));
	
		//LISTA DEGLI AMICI----------------------------------------------------------------------------------------------------------------------------
		JButton friendsButton = new JButton("See your friends :)");	
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
		
		ArrayList<JButton> managedProjectButtons = new ArrayList<JButton>();
		ArrayList<JButton> collaborationProjectButtons = new ArrayList<JButton>();
		JTextArea notificationsArea = new JTextArea(8, 12);
		JScrollPane scroll = new JScrollPane (notificationsArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
		mainPanel.add(scroll);
		mainPanel.add(managedProjectPanel);
		mainPanel.add(collaborationProjectPanel);
		mainPanel.add(friendsButton);
		
		for(int i = 0; i < notifications.size(); i++){
			notificationsArea.append(notifications.get(i).message +"\n");		
		}

		
		
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
						JOptionPane activityPane = new JOptionPane();
						ActivityPanel activityPanel = null;
						try {
							activityPanel = new ActivityPanel(i, thisFrame, client);
							int result = activityPane.showConfirmDialog(thisFrame, activityPanel,"Activities", JOptionPane.CLOSED_OPTION);
							if(result == activityPane.CLOSED_OPTION){
								setVisible(false);
								ProjectListFrame projectListFrame = null;
								projectListFrame = new ProjectListFrame(client);
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
						JOptionPane collActivityPane = new JOptionPane();
						CollabActivityPanel collActivityPanel = null;
						try {
							collActivityPanel = new CollabActivityPanel(k, thisFrame, client);
							int result = collActivityPane.showConfirmDialog(thisFrame, collActivityPanel,"Activities", JOptionPane.CLOSED_OPTION);
							if(result == collActivityPane.CLOSED_OPTION){
								setVisible(false);
								ProjectListFrame projectListFrame = null;
								projectListFrame = new ProjectListFrame(client);
								projectListFrame.setLocationRelativeTo(null);
	 							projectListFrame.setVisible(true);	
							}
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			});				
		}
		
		//PROJECT SETTINGS--------------------------------------------------------------------------------
		JPanel projectSettings = new JPanel();
		
		JButton btnAddFriend = new JButton("Add Friend");
		btnAddFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FriendsPanel friendsPanel = new FriendsPanel(client);
				int result4 = JOptionPane.showConfirmDialog(mainPanel, friendsPanel, "Friends", JOptionPane.OK_CANCEL_OPTION);
			}
		});
		projectSettings.add(btnAddFriend);
		JButton addProjectButton = new JButton("Add project");
		projectSettings.add(addProjectButton);
		getContentPane().add(projectSettings);
		addProjectButton.addActionListener(new ActionListener(){

			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				
				JTextField projectTitle = new JTextField(15);
				JTextField projectDescription = new JTextField(40);
				String[] categories = Category.getStringsArray();			
				JComboBox projectCategory = new JComboBox(categories);
				
				
				JPanel projectValues = new JPanel();
				projectValues.setLayout(new BoxLayout(projectValues, BoxLayout.PAGE_AXIS));
				projectValues.add(new JLabel("Insert title: "));
				projectValues.add(projectTitle);
				projectValues.add(new JLabel(" "),"span, grow");
				projectValues.add(new JLabel("Insert description: "));
				projectValues.add(projectDescription);
				projectValues.add(new JLabel(" "),"span, grow");
				projectValues.add(new JLabel("Insert category: ")); //fare combobox
				projectValues.add(projectCategory);
				projectValues.setVisible(true);
				//
				JOptionPane creatingProject = new JOptionPane();
				int result = creatingProject.showConfirmDialog(thisFrame, projectValues, "Please Enter Project Values", JOptionPane.OK_CANCEL_OPTION);
				
				if (result == creatingProject.OK_OPTION){
					if(!projectTitle.getText().equals("")){				
							try {
								client.addProject(projectTitle.getText(), projectDescription.getText(), Category.getCategory(projectCategory.getSelectedItem().toString()), client.getClientId());
							} catch (RemoteException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							dispose();
							ProjectListFrame projectListFrame = null;
							try {
								projectListFrame = new ProjectListFrame(client);
							} catch (RemoteException e1) {
								// TODO Auto-generated catch block
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
	}
}
