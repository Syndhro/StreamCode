package com;

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

public class ActivityPanel extends JPanel {

	ProjectListFrame parentFrame;
	ActivityPanel thisPanel = this;
	
	/**
	 * Create the panel.
	 * @throws RemoteException 
	 */
	public ActivityPanel(int i, ProjectListFrame parentFrame) throws RemoteException {
		
		this.parentFrame = parentFrame;
		Project project = Client.getInstance().getManagedProject().get(i);
	    setBounds(100, 100, 500, 336);								 
	    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));					
		ArrayList<Activity> activities = new ArrayList<Activity>();
		ArrayList<JButton> activitiesButtons = new ArrayList<JButton>();
		activities = project.getActivities();
		for(int j = 0; j < activities.size(); j++){
			JButton button = new JButton();
			button.setText(activities.get(j).getName());
			add(button);		
			activitiesButtons.add(button);
		}
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
		int result = creatingActivity.showConfirmDialog(null, activityValues, "Please Enter Activity Values", JOptionPane.OK_CANCEL_OPTION);
		if (result == creatingActivity.OK_OPTION){
			Project managedProject = project;
			try {
				Client.getInstance().addActivity(project.getProjectId(), activityName.getText(), activityDescription.getText(), activityPlace.getText(), "Ora_attuale");
				JOptionPane newactivityPane = new JOptionPane();
				ActivityPanel newactivityPanel = new ActivityPanel(i, parentFrame);
				newactivityPane.showConfirmDialog(null, newactivityPanel,"Activities", JOptionPane.CLOSED_OPTION);
				
			} catch (RemoteException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
				
		//ADD COLLABORATORS---------------------------------------------------------------------
		addCollaboratorsButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				JPanel inviteFriends = new JPanel();
				ArrayList<User> collaborators = new ArrayList<User>();
				try {
					collaborators = Client.getInstance().getUserFriends();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}											
				ArrayList<JCheckBox> checkboxes = new ArrayList<>();
				for(int i = 0; i < collaborators.size(); i++) {
				    JCheckBox box = new JCheckBox(collaborators.get(i).getUsername());
				    checkboxes.add(box);
				    inviteFriends.add(box);
				}
				
				JOptionPane invitingFriends = new JOptionPane();
				ArrayList<Integer> selected = new ArrayList<Integer>();
				int result2 = invitingFriends.showConfirmDialog(null, inviteFriends, "Invite your friends",JOptionPane.OK_CANCEL_OPTION); 
				if (result2 == invitingFriends.OK_OPTION){
					try {
						for(int i = 0; i < checkboxes.size(); i++){
							if(checkboxes.get(i).isSelected()){
								selected.add(collaborators.get(i).getUserId());														
							}
						}
						Client.getInstance().addCollaborators(project.getProjectId(), selected);
						
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}	
				}
			}
		});
		}
			}
		});
	}
}
