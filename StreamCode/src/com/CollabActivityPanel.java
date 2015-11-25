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
			button.setBackground(Color.ORANGE);
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
					JOptionPane.showConfirmDialog(parentFrame, activityInfoValues, "Activity Info", JOptionPane.CLOSED_OPTION);
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
	}
}

