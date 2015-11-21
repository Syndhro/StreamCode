package com;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ActivityModifierPanel extends JPanel {
	
	ActivityModifierPanel thisPanel;
	ActivityPanel parentPanel;
	
	public ActivityModifierPanel(int projectIndex, int activityIndex, ActivityPanel parentPanel) {
		
		thisPanel = this;
		this.parentPanel = parentPanel;
		//retrieve activity
		try {
			Activity activity = Client.getInstance().getManagedProject().get(projectIndex).getActivities().get(activityIndex);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	    setBounds(100, 100, 500, 336);
	  
	}

}
