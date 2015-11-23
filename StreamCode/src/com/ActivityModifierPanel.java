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

public class ActivityModifierPanel extends JPanel {
	
	ActivityModifierPanel thisPanel;
	ActivityPanel parentPanel;
	Activity activity;
	
	public ActivityModifierPanel(int projectIndex, int activityIndex, ActivityPanel parentPanel, Client client) {
		
		thisPanel = this;
		this.parentPanel = parentPanel;
		activity = client.getManagedProject().get(projectIndex).getActivities().get(activityIndex);
	    setBounds(100, 100, 500, 336);
	    
	    //ACTIVITY COLLABORATORS LIST
	    JPanel collaboratorsPanel = new JPanel();
		collaboratorsPanel. setLayout(new BoxLayout(collaboratorsPanel, BoxLayout.PAGE_AXIS));
		ArrayList<User> collaborators = new ArrayList<User>();
		ArrayList<JButton> collaboratorsButtons = new ArrayList<JButton>();
		collaborators = activity.getActivityCollaborators();
		for(int j = 0; j < collaborators.size(); j++){
			if(!collaborators.isEmpty()){
			JButton button = new JButton();
			button.setText(collaborators.get(j).getUsername());
			button.setBackground(Color.CYAN);
			collaboratorsPanel.add(button);		
			collaboratorsButtons.add(button);
			}
		}
		add(collaboratorsPanel);
		
		JLabel activityName = new JLabel(activity.getName());
		JLabel activityDescription = new JLabel(activity.getDescription());
		JLabel activityPlace = new JLabel(activity.getPlace());
	
		JPanel activityValues = new JPanel();
		activityValues.setLayout(new BoxLayout(activityValues, BoxLayout.PAGE_AXIS));
		activityValues.add(new JLabel("Name: "));
		activityValues.add(activityName);
		activityValues.add(new JLabel(" "),"span, grow");
		activityValues.add(new JLabel("Description: "));
		activityValues.add(activityDescription);
		activityValues.add(new JLabel(" "),"span, grow");
		activityValues.add(new JLabel("Place: ")); //fare combobox
		activityValues.add(activityPlace);	
		add(activityValues);
		
		//MODIFY INFO ACTIVITY-------------------------------------------------
		JButton modifyInfo = new JButton("Modify Activity Info");
		add(modifyInfo);
		modifyInfo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				JTextField activityName2 = new JTextField(15);
				JTextField activityDescription2 = new JTextField(40);
				JTextField activityPlace2 = new JTextField(15);
						
				JPanel activityValues2 = new JPanel();
				activityValues2.setLayout(new BoxLayout(activityValues2, BoxLayout.PAGE_AXIS));
				activityValues2.add(new JLabel("Insert title: "));
				activityValues2.add(activityName2);
				activityValues2.add(new JLabel(" "),"span, grow");
				activityValues2.add(new JLabel("Insert description: "));
				activityValues2.add(activityDescription2);
				activityValues2.add(new JLabel(" "),"span, grow");
				activityValues2.add(new JLabel("Insert place: ")); //fare combobox
				activityValues2.add(activityPlace2);
				activityValues2.setVisible(true);
				
				JOptionPane creatingProject = new JOptionPane();
				int result = creatingProject.showConfirmDialog(thisPanel, activityValues2, "Modify Activity Values", JOptionPane.OK_CANCEL_OPTION);
				if (result == creatingProject.OK_OPTION){	
						try {
							client.modifyActivity(activity.getActivityId(), activityName2.getText(), activityDescription2.getText(), activityPlace2.getText(), "Ora attuale");
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						Window w = SwingUtilities.getWindowAncestor(thisPanel);	//codice per nascondere 
						w.setVisible(false);									//la vecchia finestra		
						JOptionPane newactivityPane2 = new JOptionPane();
						ActivityModifierPanel newactivityPanel2 = new ActivityModifierPanel(projectIndex, activityIndex, parentPanel, client);
						newactivityPane2.showConfirmDialog(parentPanel, newactivityPanel2,"Activities", JOptionPane.CLOSED_OPTION);
				}
				
			}
			
		});
	    
	    //ADD COLLABORATORS BUTTON--------------------------------------------------
		JButton addCollaboratorsButton = new JButton("Invite collaborators");
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
							JCheckBox box = new JCheckBox(collaborators.get(i).getUsername());
							checkboxes.add(box);
							inviteFriends.add(box);
						}
					}
					else{
						JLabel noCollab = new JLabel("There are no collaborators in this project yet");
						inviteFriends.add(noCollab);
					}
					JOptionPane invitingFriends = new JOptionPane();
					int userId = 0;
					int result2 = invitingFriends.showConfirmDialog(parentPanel, inviteFriends, "Invite your friends",JOptionPane.OK_CANCEL_OPTION); 
					if (result2 == invitingFriends.OK_OPTION){
						
						try {
							if(!checkboxes.isEmpty()){
								for(int i = 0; i < checkboxes.size(); i++){
									if(checkboxes.get(i).isSelected()){
										userId = collaborators.get(i).getUserId();														
									}	
								}
								client.addAgent(activity.getActivityId(), userId);
							}													
							
							Window w = SwingUtilities.getWindowAncestor(thisPanel);	//codice per nascondere 
							w.setVisible(false);									//la vecchia finestra							
							JOptionPane newactivityPane = new JOptionPane();
							ActivityModifierPanel newactivityPanel = new ActivityModifierPanel(projectIndex, activityIndex, parentPanel, client);
							newactivityPane.showConfirmDialog(parentPanel, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);
						}catch(RemoteException e1) {
							e1.printStackTrace();
						}	
					}
			}
		});
	}

}
