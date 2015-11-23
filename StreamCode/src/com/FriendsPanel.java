package com;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class FriendsPanel extends JPanel {
	
	private FriendsPanel thisPanel;

	/**
	 * Create the panel.
	 */
	public FriendsPanel(Client client) {
		
		JTextField searchInput = new JTextField();
		add(searchInput);
		searchInput.setColumns(10);
		thisPanel = this;
		
		JButton searchButton = new JButton("Search");
		add(searchButton);
		JLabel matchedUsers = new JLabel();
		matchedUsers.setLayout(new BoxLayout(matchedUsers, BoxLayout.PAGE_AXIS));
		
		ArrayList<JCheckBox> checkboxes = new ArrayList<JCheckBox>(); 
		searchButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ArrayList<User> matchingUsers = client.searchUser(searchInput.getText());
					matchedUsers.setText(matchingUsers.get(0).getUsername());
					for(int i = 0; i < matchingUsers.size(); i++){
						/*
						JCheckBox checkbox = new JCheckBox(matchingUsers.get(i).getUsername());
						checkboxes.add(checkbox);
						matchedUsers.add(checkbox);
						matchedUsers.append(matchingUsers.get(i).getUsername());
						*/
						
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		});
		add(matchedUsers);

	}

}
