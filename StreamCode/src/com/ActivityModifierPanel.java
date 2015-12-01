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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ActivityModifierPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	ActivityModifierPanel thisPanel;
	ActivityPanel parentPanel;
	Activity activity;
	Project project;
	ArrayList<User> collaborators;
	
	JPanel collaboratorsPanel;
	ArrayList<JButton> collaboratorsButtons;
	JPanel activityValues;
	JLabel activityName;
	JLabel activityDescription;
	JLabel activityPlace;
	JButton completeActivity;
	JButton removeActivityButton;
	JButton addCollaboratorsButton;
	
	public ActivityModifierPanel(int projectIndex, int activityIndex, JPanel previousPanel, ProjectListFrame parentFrame, Client client) {
		
		thisPanel = this;
		this.parentPanel = (ActivityPanel) previousPanel;
	    setBounds(100, 100, 500, 336);
	    
	    //INITIALIZATIONS
	    completeActivity = new JButton("Complete");
	    removeActivityButton = new JButton("Remove Activity");
	    addCollaboratorsButton = new JButton("Invite collaborators");
	    
	    //RETRIEVING NEEDED OBJECTS
	    project = client.getManagedProject().get(projectIndex);
		activity = project.getActivities().get(activityIndex);
		collaborators = activity.getActivityCollaborators();
	        
	    //ACTIVITY COLLABORATORS LIST
	    collaboratorsPanel = new JPanel();
		collaboratorsPanel. setLayout(new BoxLayout(collaboratorsPanel, BoxLayout.PAGE_AXIS));
		add(collaboratorsPanel);
		collaboratorsButtons = new ArrayList<JButton>();
		for(int j = 0; j < collaborators.size(); j++){
			if(!collaborators.isEmpty()){
				JButton button = new JButton();
				button.setText(collaborators.get(j).getUsername());
				button.setBackground(Color.CYAN);
				collaboratorsPanel.add(button);		
				collaboratorsButtons.add(button);
				button.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent event){
						int i =  collaboratorsButtons.indexOf(event.getSource());
						int result = JOptionPane.showConfirmDialog(parentFrame, "Are you sure do you want to remove the selected agent?");
						if(result == JOptionPane.YES_OPTION){	
							User collaborator = collaborators.get(i);
							try {
								client.removeAgent(activity.getActivityId(), collaborator.getUserId());
								Window w = SwingUtilities.getWindowAncestor(thisPanel);
								w.setVisible(false);								
								ActivityModifierPanel newactivityPanel = new ActivityModifierPanel(projectIndex, activityIndex, previousPanel, parentFrame, client);
								JOptionPane.showConfirmDialog(parentFrame, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);			
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}				
					}
				});
			}
		}

		
		activityName = new JLabel(activity.getName());
		activityDescription = new JLabel(activity.getDescription());
		activityPlace = new JLabel(activity.getPlace());
	
		activityValues = new JPanel();
		activityValues.setLayout(new BoxLayout(activityValues, BoxLayout.PAGE_AXIS));
		activityValues.add(new JLabel("Name: "));
		activityValues.add(activityName);
		activityValues.add(new JLabel(" "),"span, grow");
		activityValues.add(new JLabel("Description: "));
		activityValues.add(activityDescription);
		activityValues.add(new JLabel(" "),"span, grow");
		activityValues.add(new JLabel("Place: ")); 
		activityValues.add(activityPlace);
		activityValues.add(completeActivity);
		add(activityValues);
		
		if(!activity.isActive() || activity.isCompleted())
		{
			completeActivity.setEnabled(false);
		}
		completeActivity.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					client.completeActivity(activity.getActivityId(), client.getClientId());
					completeActivity.setEnabled(false);
					completeActivity.setText("Completed");
					completeActivity.setBackground(Color.GREEN);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}	
			}
			
		});
		//MODIFY INFO ACTIVITY-------------------------------------------------
		JButton modifyInfo = new JButton("Modify Activity Info");
		add(modifyInfo);
		if(activity.isCompleted()){
			modifyInfo.setEnabled(false);
		}
		modifyInfo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {

				JTextField activityName2 = new JTextField(15);
				JTextField activityDescription2 = new JTextField(40);
				JTextField activityPlace2 = new JTextField(15);
						
				JPanel activityValues = new JPanel();
				activityValues.setLayout(new BoxLayout(activityValues, BoxLayout.PAGE_AXIS));
				activityValues.add(new JLabel("Insert title: "));
				activityValues.add(activityName2);
				activityValues.add(new JLabel(" "),"span, grow");
				activityValues.add(new JLabel("Insert description: "));
				activityValues.add(activityDescription2);
				activityValues.add(new JLabel(" "),"span, grow");
				activityValues.add(new JLabel("Insert place: ")); 
				activityValues.add(activityPlace2);
				activityValues.setVisible(true);
				
				int result = JOptionPane.showConfirmDialog(thisPanel, activityValues, "Modify Activity Values", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION){	
						try {
							client.modifyActivity(activity.getActivityId(), activityName2.getText(), activityDescription2.getText(), activityPlace2.getText(), "Ora attuale");									
							Window w = SwingUtilities.getWindowAncestor(thisPanel);	
							w.setVisible(false);
							Window w2 = SwingUtilities.getWindowAncestor(w);
							w2.setVisible(false);		
							ActivityPanel newactivityPanel = null;				
							newactivityPanel = new ActivityPanel(projectIndex, parentFrame, client);
							JOptionPane.showConfirmDialog(previousPanel, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}					
				}				
			}			
		});
	    //REMOVE ACTIVITY BUTTON--------------------------------------------------------------------
		add(removeActivityButton);
		removeActivityButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					client.removeActivity(project.getProjectId(), activity.getActivityId());
					Window w = SwingUtilities.getWindowAncestor(thisPanel);	
					w.setVisible(false);
					Window w2 = SwingUtilities.getWindowAncestor(w);
					w2.setVisible(false);					
					ActivityPanel newactivityPanel = new ActivityPanel(projectIndex, parentFrame, client);		
					JOptionPane.showConfirmDialog(previousPanel, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				};
				
			}
		});
		
	    //ADD COLLABORATORS BUTTON--------------------------------------------------
		if(activity.isCompleted()){
			addCollaboratorsButton.setEnabled(false);
		}
		add(addCollaboratorsButton);
	    addCollaboratorsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				JPanel inviteFriends = new JPanel();						
				ArrayList<User> collaborators = new ArrayList<User>();
					collaborators = client.getManagedProject().get(projectIndex).getCollaborators();											
					ArrayList<JCheckBox> checkboxes = new ArrayList<>();
					if(!collaborators.isEmpty()){
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
					}
					else{
						JLabel noCollab = new JLabel("There are no collaborators in this project yet");
						inviteFriends.add(noCollab);
					}
					int result2 = JOptionPane.showConfirmDialog(previousPanel, inviteFriends, "Invite your friends",JOptionPane.OK_CANCEL_OPTION); 
					if (result2 == JOptionPane.OK_OPTION){
						
						try {
							if(!checkboxes.isEmpty()){
								for(int i = 0; i < checkboxes.size(); i++){
									for(int j = 0; j < collaborators.size(); j++){
										if(checkboxes.get(i).isSelected()){
											if(collaborators.get(j).getUsername().equals(checkboxes.get(i).getText())){
												client.addAgent(activity.getActivityId(), collaborators.get(j).getUserId());		
											}
										}													
									}	
								}
							}
							else{
								JLabel noCollab = new JLabel("There are no collaborators in this project yet");
								inviteFriends.add(noCollab);
							}
							
							Window w = SwingUtilities.getWindowAncestor(thisPanel);	
							w.setVisible(false);													
							ActivityModifierPanel newactivityPanel = new ActivityModifierPanel(projectIndex, activityIndex, previousPanel, parentFrame, client);
							JOptionPane.showConfirmDialog(previousPanel, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);
						}catch(RemoteException e1) {
							e1.printStackTrace();
						}	
					}
			}
		});
	}
	
}
