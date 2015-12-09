package client;

import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JTextField;

import server.User;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

public class FriendsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private FriendsPanel thisPanel;
	private int myId;

	JTextField searchInput;
	JButton searchButton; 
	/**
	 * Create the panel.
	 */
	public FriendsPanel(Client client) {
		
		searchInput = new JTextField();
		add(searchInput);
		searchInput.setColumns(10);
		thisPanel = this;
		searchButton = new JButton("Search");
		add(searchButton);
		try {
			myId = client.getClientId();
		} catch (RemoteException e2) {
			e2.printStackTrace();
		}
		
		searchButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<User> matchingUsers = new ArrayList<User>();
				ArrayList<JCheckBox> checkboxes = new ArrayList<JCheckBox>(); 
				try {
					matchingUsers = client.searchUser(searchInput.getText());
								
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				JPanel searchUsers = new JPanel();
				searchUsers.setLayout(new BoxLayout(searchUsers, BoxLayout.PAGE_AXIS));
				ArrayList<User> userFriends = client.getUserFriends();
				
				for (int i = 0; i < matchingUsers.size(); i++) {
					boolean exist = false;
					if(matchingUsers.get(i).getUserId() == myId){
						exist = true;
					}
					else{
					for(int j = 0; j < userFriends.size(); j++){
						if(matchingUsers.get(i).getUsername().equals(userFriends.get(j).getUsername())){						 
								exist = true;
								break;
							}
						}
					}
					if(!exist){
						JCheckBox b = new JCheckBox(matchingUsers.get(i).getUsername());
						checkboxes.add(b);
						searchUsers.add(b);			
					}
				}
				if(!checkboxes.isEmpty()){
				int result = JOptionPane.showConfirmDialog(thisPanel, searchUsers, "Matched users",JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION){
					ArrayList<Integer> selected = new ArrayList<Integer>();
					for(int i = 0; i < checkboxes.size(); i++){
						if(checkboxes.get(i).isSelected()){
							for(int j = 0; j < matchingUsers.size(); j++){		
								if(matchingUsers.get(j).getUsername().equals(checkboxes.get(i).getText())){
									selected.add(matchingUsers.get(j).getUserId());					
								}
							}
						}
					}
					for(int i = 0; i < selected.size(); i++){
						try {
							client.addFriend(client.getClientId(), selected.get(i));
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
					JOptionPane.showMessageDialog(thisPanel, "You have new friends :)");
					
					}
				}
				else{
					JOptionPane.showMessageDialog(thisPanel, "No matches");
				}
				
			}		
		});
	}

}
