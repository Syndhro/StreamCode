package com;

import java.awt.Color;
import java.awt.Component;
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

public class CollabActivityPanel extends JPanel {

	ProjectListFrame parentFrame;
	CollabActivityPanel thisPanel; 
	
	/**
	 * Create the panel.
	 * @throws RemoteException 
	 */
	public CollabActivityPanel(int k, ProjectListFrame parentFrame, Client client) throws RemoteException {
		
		thisPanel = this;
		this.parentFrame = parentFrame;
		int myId = client.getClientId();
		Project project = client.getCollaborationProject().get(k);
	    setBounds(100, 100, 500, 336);								 
	   					
	    JLabel projectName = new JLabel(project.getTitle());
		JLabel projectDescription = new JLabel(project.getDescription());
		JLabel projectCategory = new JLabel(project.getCategory().toString());
	
		JPanel projectInfoValues = new JPanel();
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
		
	    JPanel activitiesPanel = new JPanel();
	    activitiesPanel.setLayout(new BoxLayout(activitiesPanel, BoxLayout.PAGE_AXIS));
	    ArrayList<Activity> activities = new ArrayList<Activity>();
		ArrayList<JButton> activitiesButtons = new ArrayList<JButton>();
		activities = project.getActivities();
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
					Activity activity = project.getActivities().get(x);
					JLabel activityName = new JLabel(activity.getName());
					JLabel activityDescription = new JLabel(activity.getDescription());
					JLabel activityPlace = new JLabel(activity.getPlace());
					JButton completeActivityButton = new JButton("Complete");
					
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
					for(int i = 0; i < activity.getActivityCollaborators().size();i++){
						if (activity.getActivityCollaborators().get(i).getUserId() == myId){
							activityInfoValues.add(completeActivityButton);
						}	
					}
				
					if(!activity.isActive() || activity.isCompleted())
					{
						completeActivityButton.setEnabled(false);
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
						Window w = SwingUtilities.getWindowAncestor(thisPanel);	//codice per nascondere 
						w.setVisible(false);									//la vecchia finestra
									
						JOptionPane newactivityPane3 = new JOptionPane();
						CollabActivityPanel newactivityPanel3 = null;
						try {
							newactivityPanel3 = new CollabActivityPanel(k, parentFrame, client);
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						newactivityPane3.showConfirmDialog(parentFrame, newactivityPanel3,"Activities", JOptionPane.CLOSED_OPTION);
					}
				}				
			});
		
		}
		add(activitiesPanel);
		
		//PROJECTS COLLABORATORS
		JPanel collaboratorsPanel = new JPanel();
		collaboratorsPanel. setLayout(new BoxLayout(collaboratorsPanel, BoxLayout.PAGE_AXIS));
		ArrayList<User> collaborators = new ArrayList<User>();
		ArrayList<JButton> collaboratorsButtons = new ArrayList<JButton>();
		collaborators = project.getCollaborators();
		for(int j = 0; j < collaborators.size(); j++){
			JButton button = new JButton();
			button.setText(collaborators.get(j).getUsername());
			button.setBackground(Color.CYAN);
			collaboratorsPanel.add(button);		
			collaboratorsButtons.add(button);
		}
		add(collaboratorsPanel);
	}
}

