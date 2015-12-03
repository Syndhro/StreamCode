package com;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CollabActivityPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	ProjectListFrame parentFrame;
	CollabActivityPanel thisPanel; 
	Project project;
	Activity activity;
	int myId;
	ArrayList<Activity> activities;
	ArrayList<User> collaborators;
	ArrayList<JButton> activitiesButtons;
	ArrayList<JButton> collaboratorsButtons;

	JPanel projectInfoValues;
	JPanel activitiesPanel;
	JPanel collaboratorsPanel;
	JLabel projectName;
	JLabel projectDescription;
	JLabel projectCategory;
	JLabel projectAdmin;
	
	public CollabActivityPanel(int k, ProjectListFrame parentFrame, Client client) throws RemoteException {
		
		thisPanel = this;
		this.parentFrame = parentFrame;	
	    setBounds(100, 100, 500, 336);	
	    
	    //RETRIEVE OBJECTS NEEDED
	    myId = client.getClientId();
		project = client.getCollaborationProject().get(k);		
		activities = project.getActivities();
		collaborators = project.getCollaborators();
		 
	    projectName = new JLabel(project.getTitle());
		projectDescription = new JLabel(project.getDescription());
		projectCategory = new JLabel(project.getCategory().toString());
		projectAdmin = new JLabel(project.getAdmin().getUsername());
		
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
		projectInfoValues.add(new JLabel(" "),"span, grow");
		projectInfoValues.add(new JLabel("Created by: ")); 
		projectInfoValues.add(projectAdmin);
		add(projectInfoValues);
		
	    activitiesPanel = new JPanel();
	    add(activitiesPanel);
	    activitiesPanel.setLayout(new BoxLayout(activitiesPanel, BoxLayout.PAGE_AXIS));    
		activitiesButtons = new ArrayList<JButton>();
		
		for(int j = 0; j < activities.size(); j++){
			JButton button = new JButton();
			button.setText(activities.get(j).getName());
			if(activities.get(j).isActive()){
				button.setBackground(Color.ORANGE);
			}
			else if(activities.get(j).isCompleted()){
				button.setBackground(Color.GREEN);
			}
			else{
				button.setBackground(Color.RED);
			}
			activitiesPanel.add(button);		
			activitiesButtons.add(button);
			
			button.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					
					int x = activitiesButtons.indexOf(e.getSource());
					activity = project.getActivities().get(x);
					JLabel activityName = new JLabel(activity.getName());
					JLabel activityDescription = new JLabel(activity.getDescription());
					JLabel activityPlace = new JLabel(activity.getPlace());
					JButton completeActivityButton = new JButton("Complete");
					JButton refuseActivity = new JButton("Refuse");
					JButton viewNotesButton = new JButton("View notes");
					
					JPanel activityInfoValues = new JPanel();
					activityInfoValues.setLayout(new BoxLayout(activityInfoValues, BoxLayout.PAGE_AXIS));
					activityInfoValues.add(new JLabel("Name: "));
					activityInfoValues.add(activityName);
					activityInfoValues.add(new JLabel(" "),"span, grow");
					activityInfoValues.add(new JLabel("Description: "));
					activityInfoValues.add(activityDescription);
					activityInfoValues.add(new JLabel(" "),"span, grow");
					activityInfoValues.add(new JLabel("Place: ")); 
					activityInfoValues.add(activityPlace);
					activityInfoValues.add(viewNotesButton);
					
					for(int i = 0; i < activity.getActivityCollaborators().size();i++){
						if (activity.getActivityCollaborators().get(i).getUserId() == myId){
							activityInfoValues.add(completeActivityButton);
							activityInfoValues.add(refuseActivity);
						}	
					}
				
					if(!activity.isActive() || activity.isCompleted()){
						completeActivityButton.setEnabled(false);
					}
					if(activity.isCompleted()){
						refuseActivity.setEnabled(false);
					}
					completeActivityButton.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								client.completeActivity(activity.getActivityId(), client.getClientId());
								completeActivityButton.setEnabled(false);
								completeActivityButton.setText("Completed");
								completeActivityButton.setBackground(Color.GREEN);
							} catch (RemoteException e1) {
								e1.printStackTrace();
							}	
						}
						
					});

					refuseActivity.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							int result = JOptionPane.showConfirmDialog(parentFrame, "Are you sure do you want to refuse the assignment?");
							if(result == JOptionPane.OK_OPTION){
								try {
									client.removeAgent(activity.getActivityId(), myId);
								} catch (RemoteException e1) {
									e1.printStackTrace();
								}
							}		
						}		
					});
					
					//NOTES
					viewNotesButton.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							NotesView notesView = new NotesView(k, x, client);
							JOptionPane.showConfirmDialog(thisPanel, notesView, "Notes", JOptionPane.CLOSED_OPTION);
						}
						
					});
								
					//ACTIVITY COLLABORATORS
					JPanel activityCollaboratorsPanel = new JPanel();
					activityCollaboratorsPanel. setLayout(new BoxLayout(activityCollaboratorsPanel, BoxLayout.PAGE_AXIS));
					ArrayList<User> activityCollaborators = new ArrayList<User>();
					ArrayList<JButton> activityCollaboratorsButtons = new ArrayList<JButton>();
					activityCollaborators = activity.getActivityCollaborators();
					for(int j = 0; j < activityCollaborators.size(); j++){
						JButton button = new JButton();
						button.setText(activityCollaborators.get(j).getUsername());
						button.setBackground(Color.CYAN);
						activityCollaboratorsPanel.add(button);		
						activityCollaboratorsButtons.add(button);
					}
					activityInfoValues.add(activityCollaboratorsPanel);
					int result = JOptionPane.showConfirmDialog(parentFrame, activityInfoValues, "Activity Info", JOptionPane.CLOSED_OPTION);
					if(result == JOptionPane.OK_OPTION){
						Window w = SwingUtilities.getWindowAncestor(thisPanel);	
						w.setVisible(false);									
						CollabActivityPanel newactivityPanel = null;
						try {
							newactivityPanel = new CollabActivityPanel(k, parentFrame, client);
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
						JOptionPane.showConfirmDialog(parentFrame, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);
					}
				}				
			});
		
		}

		//PROJECTS COLLABORATORS
		collaboratorsPanel = new JPanel();
		collaboratorsPanel.setLayout(new BoxLayout(collaboratorsPanel, BoxLayout.PAGE_AXIS));		
		add(collaboratorsPanel);
		collaboratorsButtons = new ArrayList<JButton>();		
		for(int j = 0; j < collaborators.size(); j++){
			JButton button = new JButton();
			button.setText(collaborators.get(j).getUsername());
			button.setBackground(Color.CYAN);
			collaboratorsPanel.add(button);		
			collaboratorsButtons.add(button);
		}
	}
}

