package client;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import server.Activity;
import server.ActivityAttachment;
import server.Project;

public class AdminNotesView extends JPanel {

	private static final long serialVersionUID = 1L;
	int myId;
	Project project;
	Activity activity;
	ArrayList<ActivityAttachment> attachments;
	JPanel thisPanel;
	JButton writeNote;
	String input;
	
	ArrayList<JLabel> notes;
	ArrayList<JButton> notesButtons;
	
	public AdminNotesView(int projectIndex, int activityIndex, Client client){
		
		//RETRIEVE OBJECTS
		try {
			myId = client.getClientId();
		} catch (RemoteException e2) {
			e2.printStackTrace();
		}
		project = client.getManagedProject().get(projectIndex);		
		activity = project.getActivities().get(activityIndex);
		attachments = activity.getAttachments();
		thisPanel = this;
		notes = new ArrayList<JLabel>();
		notesButtons = new ArrayList<JButton>();
		input = "";
		
		writeNote = new JButton("Write a note");
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(writeNote);
		writeNote.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String input = JOptionPane.showInputDialog("Write a short note :)");
				if(!input.equals("")){
					try {
						client.addAttachment(input, activity.getActivityId(), myId);
						Window w = SwingUtilities.getWindowAncestor(thisPanel);	
						w.setVisible(false);									
						AdminNotesView newNotesView = null;
						newNotesView = new AdminNotesView(projectIndex, activityIndex, client);
						JOptionPane.showConfirmDialog(thisPanel, newNotesView,"Notes", JOptionPane.CLOSED_OPTION);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}	
				}
			}		
		});
		for(int i = 0;  i < attachments.size(); i++){
			JPanel miniPanel = new JPanel();
			String username = attachments.get(i).getAuthor().getUsername();
			JLabel text = new JLabel(username + " wrote: " + attachments.get(i).getText() + "\n");
			JButton delete = new JButton("delete");
			notes.add(text);
			notesButtons.add(delete);
			miniPanel.add(text);
			miniPanel.add(delete);
			thisPanel.add(miniPanel);
			delete.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					int x = notesButtons.indexOf(e.getSource());
					ActivityAttachment selected = attachments.get(x);
					try {
						client.removeAttachment(selected.getAttachmentId(), activity.getActivityId());
						Window w = SwingUtilities.getWindowAncestor(thisPanel);	
						w.setVisible(false);									
						AdminNotesView newNotesView = null;
						newNotesView = new AdminNotesView(projectIndex, activityIndex, client);
						JOptionPane.showConfirmDialog(thisPanel, newNotesView,"Notes", JOptionPane.CLOSED_OPTION);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}		
			});
		}
	}
}
