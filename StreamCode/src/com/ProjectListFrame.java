package com;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProjectListFrame extends JFrame {
	
	ProjectListFrame thisFrame = this;

	private JPanel contentPane;
	Project project = null;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 * @throws RemoteException 
	 */
	public ProjectListFrame(Client client) throws RemoteException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		Container mainPanel = getContentPane();						
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		ArrayList<Project> projects = new ArrayList<Project>();
		ArrayList<JButton> projectButtons = new ArrayList<JButton>();
		
		projects = client.getManagedProject();
		
		for(int i = 0; i < projects.size(); i++){
			JButton button = new JButton();
			button.setText(projects.get(i).getTitle());
			button.setBackground(Color.YELLOW);
			mainPanel.add(button);
			projectButtons.add(button);
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event) {
					int i = projectButtons.indexOf(event.getSource());
					project = client.getManagedProject().get(i);
					//OPEN ACTIVITY PANEL------------------------------------------------------------------
						JOptionPane activityPane = new JOptionPane();
						ActivityPanel activityPanel = null;
						try {
							activityPanel = new ActivityPanel(i, thisFrame, client);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						activityPane.showConfirmDialog(thisFrame, activityPanel,"Activities", JOptionPane.CLOSED_OPTION);
					}
			});				
		}
		//PROJECT SETTINGS--------------------------------------------------------------------------------
		JPanel projectSettings = new JPanel();
		JButton addProjectButton = new JButton("Add project");
		projectSettings.add(addProjectButton);
		add(projectSettings);
		addProjectButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				JTextField projectTitle = new JTextField(15);
				JTextField projectDescription = new JTextField(40);
				//JTextField projectCategory = new JTextField(15);
				String[] categories = Category.getStringsArray();			
				JComboBox projectCategory = new JComboBox(categories);
				
				
				JPanel projectValues = new JPanel();
				projectValues.setLayout(new BoxLayout(projectValues, BoxLayout.PAGE_AXIS));
				projectValues.add(new JLabel("Insert title: "));
				projectValues.add(projectTitle);
				projectValues.add(new JLabel(" "),"span, grow");
				projectValues.add(new JLabel("Insert description: "));
				projectValues.add(projectDescription);
				projectValues.add(new JLabel(" "),"span, grow");
				projectValues.add(new JLabel("Insert category: ")); //fare combobox
				projectValues.add(projectCategory);
				projectValues.setVisible(true);
				//
				JOptionPane creatingProject = new JOptionPane();
				int result = creatingProject.showConfirmDialog(thisFrame, projectValues, "Please Enter Project Values", JOptionPane.OK_CANCEL_OPTION);
				if (result == creatingProject.OK_OPTION){
					try {
						client.addProject(projectTitle.getText(), projectDescription.getText(), Category.getCategory(projectCategory.getSelectedItem().toString()), client.getClientId());
						dispose();
						ProjectListFrame projectListFrame = new ProjectListFrame(client);
						projectListFrame.setLocationRelativeTo(null);
 						projectListFrame.setVisible(true);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}	
				}
			}
		});
	}
}
