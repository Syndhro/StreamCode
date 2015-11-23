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

public class ActivityPanel extends JPanel {

	ProjectListFrame parentFrame;
	ActivityPanel thisPanel; 
	
	/**
	 * Create the panel.
	 * @throws RemoteException 
	 */
	public ActivityPanel(int i, ProjectListFrame parentFrame, Client client) throws RemoteException {
		
		thisPanel = this;
		this.parentFrame = parentFrame;
		Project project = client.getManagedProject().get(i);
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
			button.setBackground(Color.ORANGE);
			activitiesPanel.add(button);		
			activitiesButtons.add(button);
			button.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					int x = activitiesButtons.indexOf(e.getSource());
					Activity activity = project.getActivities().get(x);
					//creare un dialog per modificare l'activity
					ActivityModifierPanel activityModifier = new ActivityModifierPanel(i, x, thisPanel, client); 
					  
				    JOptionPane activityModifierPane = new JOptionPane();
				    activityModifierPane.showConfirmDialog(thisPanel, activityModifier, "Modify activity", JOptionPane.OK_CANCEL_OPTION);	
				}
			});
		}
		add(activitiesPanel);
		
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
		
		//ACTIVITY SETTINGS-----------------------------------------------------------------
		JPanel activitySettings = new JPanel();
		JButton addActivityButton = new JButton("Add activity");
		JButton addCollaboratorsButton = new JButton("Invite collaborators");
		activitySettings.add(addActivityButton);
		activitySettings.add(addCollaboratorsButton);
		add(activitySettings);
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
		
				JOptionPane creatingActivity = new JOptionPane();
				int result = creatingActivity.showConfirmDialog(parentFrame, activityValues, "Please Enter Activity Values", JOptionPane.OK_CANCEL_OPTION);
				if (result == creatingActivity.OK_OPTION){
					Project managedProject = project;
					try{		
						Window w = SwingUtilities.getWindowAncestor(thisPanel);	//codice per nascondere 
						w.setVisible(false);									//la vecchia finestra
						client.addActivity(project.getProjectId(), activityName.getText(), activityDescription.getText(), activityPlace.getText(), "Ora_attuale");				
						JOptionPane newactivityPane = new JOptionPane();
						ActivityPanel newactivityPanel = new ActivityPanel(i, parentFrame, client);
						newactivityPane.showConfirmDialog(parentFrame, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);			
					}catch(RemoteException e2) {
						e2.printStackTrace();
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
					JOptionPane invitingFriends = new JOptionPane();
					ArrayList<Integer> selected = new ArrayList<Integer>();
					int result2 = invitingFriends.showConfirmDialog(parentFrame, inviteFriends, "Invite your friends",JOptionPane.OK_CANCEL_OPTION); 
					if (result2 == invitingFriends.OK_OPTION){
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
							Window w = SwingUtilities.getWindowAncestor(thisPanel);	//codice per nascondere 
							w.setVisible(false);									//la vecchia finestra							
							JOptionPane newactivityPane = new JOptionPane();
							ActivityPanel newactivityPanel = new ActivityPanel(i, parentFrame, client);
							newactivityPane.showConfirmDialog(parentFrame, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);
						}catch(RemoteException e1) {
							e1.printStackTrace();
						}	
					}
			}
		});
	}	
}

