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
import javax.swing.JOptionPane;

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
		
		ArrayList<JCheckBox> checkboxes = new ArrayList<JCheckBox>(); 
		searchButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<User> matchingUsers = new ArrayList<User>();
				try {
					matchingUsers = client.searchUser(searchInput.getText());
								
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				/*
				CheckBoxPanel checkboxPanel = new CheckBoxPanel(matchingUsers);			
				JOptionPane.showConfirmDialog(null, checkboxPanel, "Matched users",JOptionPane.OK_CANCEL_OPTION);
				*/
				JPanel searchUsers = new JPanel();
				searchUsers.setLayout(new BoxLayout(searchUsers, BoxLayout.PAGE_AXIS));
				 for (int i = 0; i < matchingUsers.size(); i++) {
			          JCheckBox b = new JCheckBox(matchingUsers.get(i).getUsername());
			          checkboxes.add(b);
			          searchUsers.add(b);
			      }
				int result = JOptionPane.showConfirmDialog(thisPanel, searchUsers, "Matched users",JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION){
					ArrayList<Integer> selected = new ArrayList<Integer>();
					for(int i = 0; i < checkboxes.size(); i++){
						if(checkboxes.get(i).isSelected()){
							selected.add(matchingUsers.get(i).getUserId());
						}
					}
					for(int i = 0; i < selected.size(); i++){
						try {
							client.addFriend(client.getClientId(), selected.get(i));
							JOptionPane.showMessageDialog(thisPanel, "You have new friends :)");
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
				}
			}		
		});
	}

}
