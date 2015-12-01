package com;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ActivityPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	ProjectListFrame parentFrame;
	ActivityPanel thisPanel; 
	
	Project project;
	ArrayList<User> collaborators;
	ArrayList<Activity> activities;
	ArrayList<JButton> activitiesButtons;
	ArrayList<JButton> collaboratorsButtons;
	
	JPanel projectInfoValues;
	JPanel activitiesPanel;
	JPanel collaboratorsPanel;
	JPanel activitySettings;
	JButton refreshButton = new JButton("Refresh");
	JButton addActivityButton = new JButton("Add activity");
	JButton addCollaboratorsButton = new JButton("Invite collaborators");
	JButton removeProject =  new JButton("Delete Project");
	JButton modifyProjectInfo = new JButton("Modify Project Info");
	JButton startButton = new JButton("Start Poject");
	JButton notifyCollaboratorsButton = new JButton("Notify others :)");
	JLabel projectName;
	JLabel projectDescription;
	JLabel projectCategory;
	
	
	public ActivityPanel(int z, ProjectListFrame parentFrame, Client client) throws RemoteException {
		
	    thisPanel = this;
		this.parentFrame = parentFrame;
	    setBounds(100, 100, 500, 336);			

	   	//RETRIEVE OBJECTS NEEDED
		project = client.getManagedProject().get(z);
		collaborators = project.getCollaborators();
		activities = project.getActivities();   	
		
	    projectName = new JLabel(project.getTitle());
		projectDescription = new JLabel(project.getDescription());
		projectCategory = new JLabel(project.getCategory().toString());
		
		projectInfoValues = new JPanel();
		projectInfoValues.setLayout(new BoxLayout(projectInfoValues, BoxLayout.PAGE_AXIS));
		projectInfoValues.add(new JLabel("Name: "));
		projectInfoValues.add(projectName);
		projectInfoValues.add(new JLabel(" "),"span, grow");
		projectInfoValues.add(new JLabel("Description: "));
		projectInfoValues.add(projectDescription);
		projectInfoValues.add(new JLabel(" "),"span, grow");
		projectInfoValues.add(new JLabel("Category: ")); 
		projectInfoValues.add(projectCategory);	
		add(projectInfoValues);
		
	    activitiesPanel = new JPanel();
	    add(activitiesPanel);
	    activitiesPanel.setLayout(new BoxLayout(activitiesPanel, BoxLayout.PAGE_AXIS));
	   
		ArrayList<JButton> activitiesButtons = new ArrayList<JButton>();		
		for(int j = 0; j < activities.size(); j++){
			JButton activityButton = new JButton();
			activityButton.setText(activities.get(j).getName());
			if(activities.get(j).isCompleted()){
				activityButton.setBackground(Color.GREEN);
			}
			else if(activities.get(j).isActive()){
				activityButton.setBackground(Color.ORANGE);
			}
			else{
				activityButton.setBackground(Color.RED);
			}			
			activitiesPanel.add(activityButton);		
			activitiesButtons.add(activityButton);
			activityButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					int x = activitiesButtons.indexOf(e.getSource());
					ActivityModifierPanel activityModifier = new ActivityModifierPanel(z, x, thisPanel,parentFrame, client); 

				    int result = JOptionPane.showConfirmDialog(thisPanel, activityModifier, "Modify activity", JOptionPane.OK_CANCEL_OPTION);			    
				    if(result == JOptionPane.OK_OPTION){
				    	Window w = SwingUtilities.getWindowAncestor(thisPanel); 
						w.setVisible(false);												
						ActivityPanel newactivityPanel = null;
						try {
							newactivityPanel = new ActivityPanel(z, parentFrame, client);
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
						JOptionPane.showConfirmDialog(parentFrame, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);
				    }				
				}				
			});
		}
			
		collaboratorsPanel = new JPanel();
		collaboratorsPanel. setLayout(new BoxLayout(collaboratorsPanel, BoxLayout.PAGE_AXIS));
		add(collaboratorsPanel);
		collaboratorsButtons = new ArrayList<JButton>();		
		for(int j = 0; j < collaborators.size(); j++){
			JButton collaboratorButton = new JButton();
			collaboratorButton.setText(collaborators.get(j).getUsername());
			collaboratorButton.setBackground(Color.CYAN);
			collaboratorsPanel.add(collaboratorButton);		
			collaboratorsButtons.add(collaboratorButton);
			collaboratorButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event){
					int i =  collaboratorsButtons.indexOf(event.getSource());
					int result = JOptionPane.showConfirmDialog(parentFrame, "Are you sure do you want to remove the selected collaborator?");
					if(result == JOptionPane.YES_OPTION){			
						User collaborator = collaborators.get(i);
						try {
							client.removeCollaborator(project.getProjectId(), collaborator.getUserId());
							Window w = SwingUtilities.getWindowAncestor(thisPanel);	//codice per nascondere 
							w.setVisible(false);									//la vecchia finestra
							ActivityPanel newactivityPanel = new ActivityPanel(z, parentFrame, client);
							JOptionPane.showConfirmDialog(parentFrame, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);			
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}				
				}
			});
		}
		
		//ACTIVITY SETTINGS-----------------------------------------------------------------
		activitySettings = new JPanel();		
		activitySettings.add(addActivityButton);
		
		if(project.getState() == ProjectState.COMPLETED){
			addActivityButton.setEnabled(false);
		}
		activitySettings.add(addCollaboratorsButton);
		if(project.getState() == ProjectState.COMPLETED){
			addCollaboratorsButton.setEnabled(false);
		}
		activitySettings.add(removeProject);
		if(project.getState() == ProjectState.COMPLETED){
			modifyProjectInfo.setEnabled(false);
		}
		activitySettings.add(modifyProjectInfo);	
		activitySettings.add(startButton);
		activitySettings.add(notifyCollaboratorsButton);
		add(activitySettings);
		
		if(project.getState().equals(ProjectState.ACTIVE)){
			startButton.setEnabled(false);
			startButton.setText("Started");
			startButton.setBackground(Color.GREEN);
		}
		if(project.getState().equals(ProjectState.COMPLETED)){
			startButton.setEnabled(false);
			startButton.setBackground(Color.GREEN);
			startButton.setText("Completed");
		}
		
		addActivityButton.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {		
				JTextField activityName = new JTextField(15);
				JTextField activityDescription = new JTextField(40);
				JTextField activityPlace = new JTextField(15);
		
				JPanel activityValues = new JPanel();
				activityValues.setLayout(new BoxLayout(activityValues, BoxLayout.PAGE_AXIS));
				activityValues.add(new JLabel("Insert name: "));
				activityValues.add(activityName);
				activityValues.add(new JLabel(" "),"span, grow");
				activityValues.add(new JLabel("Insert description: "));
				activityValues.add(activityDescription);
				activityValues.add(new JLabel(" "),"span, grow");
				activityValues.add(new JLabel("Insert place: "));
				activityValues.add(activityPlace);
				activityValues.setVisible(true);

				int result = JOptionPane.showConfirmDialog(parentFrame, activityValues, "Please Enter Activity Values", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION){
					if(!activityName.getText().equals("")){
						try{
						
							Window w = SwingUtilities.getWindowAncestor(thisPanel);	
							w.setVisible(false);									
							client.addActivity(project.getProjectId(), activityName.getText(), activityDescription.getText(), activityPlace.getText(), "Ora_attuale");				
							ActivityPanel newactivityPanel = new ActivityPanel(z, parentFrame, client);
							JOptionPane.showConfirmDialog(parentFrame, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);			
						}catch(RemoteException e2) {
							e2.printStackTrace();
						}
					}
					else{
						JOptionPane.showMessageDialog(thisPanel, "The activity must have a name");
					}
				}
			}
		});		
		
		//ADD COLLABORATORS---------------------------------------------------------------------
		addCollaboratorsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				JPanel inviteFriends = new JPanel();						
				ArrayList<User> collaborators = new ArrayList<User>();
					collaborators = client.getUserFriends();											
					ArrayList<JCheckBox> checkboxes = new ArrayList<>();
					for(int i = 0; i < collaborators.size(); i++) {
						boolean exist = false;
						for(int j = 0; j < collaboratorsButtons.size(); j++){
							if(collaboratorsButtons.get(j).getText().equals(collaborators.get(i).getUsername())){
								exist = true;
								break;
							}
						}
						if(!exist){
							JCheckBox box = new JCheckBox(collaborators.get(i).getUsername());
							checkboxes.add(box);
							inviteFriends.add(box);
						}
					}				
					ArrayList<Integer> selected = new ArrayList<Integer>();
					int result2 = JOptionPane.showConfirmDialog(parentFrame, inviteFriends, "Invite your friends",JOptionPane.OK_CANCEL_OPTION); 
					if (result2 == JOptionPane.OK_OPTION){
						try {
							for(int i = 0; i < checkboxes.size(); i++){
								for(int j = 0; j < collaborators.size(); j++){
									if(checkboxes.get(i).isSelected()){
										if(collaborators.get(j).getUsername().equals(checkboxes.get(i).getText())){
											selected.add(collaborators.get(j).getUserId());
										}
									}
								}														
							}
							client.addCollaborators(project.getProjectId(), selected);
							Window w = SwingUtilities.getWindowAncestor(thisPanel);
							w.setVisible(false);															
							ActivityPanel newactivityPanel = new ActivityPanel(z, parentFrame, client);
							JOptionPane.showConfirmDialog(parentFrame, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);
						}catch(RemoteException e1) {
							e1.printStackTrace();
						}	
					}
			}
		});
		
		//REMOVE PROJECT-------------------------------------------------------------------------------------------------------------------------------
		removeProject.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					client.removeProject(project.getProjectId());
					parentFrame.dispose();
					
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//MODIFY INFO PROJECT--------------------------------------------------------------------------------------------------------------------------		
		modifyProjectInfo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField projectName = new JTextField(15);
				JTextField projectDescription = new JTextField(40);
				String[] categories = Category.getStringsArray();			
				@SuppressWarnings({ "rawtypes", "unchecked" })
				JComboBox projectCategory = new JComboBox(categories);
						
				JPanel projectValues = new JPanel();
				projectValues.setLayout(new BoxLayout(projectValues, BoxLayout.PAGE_AXIS));
				projectValues.add(new JLabel("Insert title: "));
				projectValues.add(projectName);
				projectValues.add(new JLabel(" "),"span, grow");
				projectValues.add(new JLabel("Insert description: "));
				projectValues.add(projectDescription);
				projectValues.add(new JLabel(" "),"span, grow");
				projectValues.add(new JLabel("Insert category: "));
				projectValues.add(projectCategory);
				projectValues.setVisible(true);
				
				int result = JOptionPane.showConfirmDialog(thisPanel, projectValues, "Modify Project Values", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION){	
						try {
							client.modifyProject(project.getProjectId(), projectName.getText(), projectDescription.getText(), Category.getCategory(projectCategory.getSelectedItem().toString()));									
							Window w = SwingUtilities.getWindowAncestor(thisPanel);
							w.setVisible(false);										
							ActivityPanel newactivityPanel = new ActivityPanel(z, parentFrame, client);
							JOptionPane.showConfirmDialog(thisPanel, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}				
				}				
			}			
		});
		
		//START PROJECT	
		startButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(activitiesButtons.isEmpty()){
					JOptionPane.showMessageDialog(parentFrame, "You have no activities in this project, so it can't be started.Please add at least one.");
				}
				else{
					if(project.getState().equals(ProjectState.INACTIVE)){
					try {
						client.startProject(project.getProjectId());
						JOptionPane.showMessageDialog(parentFrame, "Project started successfully");
						Window w = SwingUtilities.getWindowAncestor(thisPanel);	
						w.setVisible(false);									
						ActivityPanel newactivityPanel = new ActivityPanel(z, parentFrame, client);						
						JOptionPane.showConfirmDialog(thisPanel, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					}
				}			
			}	
		});
		
		//NOTIFICA BORADCAST
		notifyCollaboratorsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<User> projectCollabos = project.getCollaborators();
				JTextField broadcastMex = new JTextField("Hello guys, Welcome to my project.");		
				JPanel broadcastPanel = new JPanel();
				broadcastPanel.setLayout(new BoxLayout(broadcastPanel, BoxLayout.PAGE_AXIS));
				broadcastPanel.add(new JLabel("Insert the message to notify to others: "));
				broadcastPanel.add(broadcastMex);
				
				int result = JOptionPane.showConfirmDialog(thisPanel, broadcastPanel, "Notify others", JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION){
					ArrayList<Integer> ids = new ArrayList<Integer>();
					for(int i = 0; i < projectCollabos.size(); i++){
						ids.add(projectCollabos.get(i).getUserId());
					}
					client.sendBroadcast(broadcastMex.getText(), ids);
					JOptionPane.showMessageDialog(parentFrame, "Done!");
				}
			}
		});
			    
	    //REFRESH
	    refreshButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Window w = SwingUtilities.getWindowAncestor(thisPanel);	
				w.setVisible(false);									
				ActivityPanel newactivityPanel3 = null;
				try {
					newactivityPanel3 = new ActivityPanel(z, parentFrame, client);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showConfirmDialog(parentFrame, newactivityPanel3,"Activities", JOptionPane.CLOSED_OPTION);
			}  	
	    });
	   	add(refreshButton);	
	}	
}

