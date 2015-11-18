package com;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.awt.Font;

public class Graphics {

	private JFrame loginFrame;
	private JFrame projectFrame;
	private JTextField textField;
	private JPasswordField passwordField;
	private JLabel lblStream;
	private JLabel lblNew;
	private JButton btnNewButton;
	private static Client client = new Client();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Graphics window = new Graphics();
					window.loginFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try{
			client.startup();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public Graphics() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		loginFrame = new JFrame();
		loginFrame.setBounds(100, 100, 500, 336);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		loginFrame.getContentPane().setLayout(springLayout);
		
		textField = new JTextField();
		loginFrame.getContentPane().add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		springLayout.putConstraint(SpringLayout.NORTH, passwordField, 140, SpringLayout.NORTH, loginFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, passwordField);
		springLayout.putConstraint(SpringLayout.SOUTH, textField, -6, SpringLayout.NORTH, passwordField);
		springLayout.putConstraint(SpringLayout.EAST, textField, 0, SpringLayout.EAST, passwordField);
		springLayout.putConstraint(SpringLayout.WEST, passwordField, 192, SpringLayout.WEST, loginFrame.getContentPane());
		loginFrame.getContentPane().add(passwordField);
		
		JLabel lblPassword = new JLabel("Password");
		springLayout.putConstraint(SpringLayout.NORTH, lblPassword, 3, SpringLayout.NORTH, passwordField);
		springLayout.putConstraint(SpringLayout.EAST, lblPassword, -14, SpringLayout.WEST, passwordField);
		loginFrame.getContentPane().add(lblPassword);
		
		JLabel lblUsername = new JLabel("Username");
		springLayout.putConstraint(SpringLayout.NORTH, lblUsername, 3, SpringLayout.NORTH, textField);
		springLayout.putConstraint(SpringLayout.WEST, lblUsername, 0, SpringLayout.WEST, lblPassword);
		loginFrame.getContentPane().add(lblUsername);
		
		JButton btnLogin = new JButton("Login");
		springLayout.putConstraint(SpringLayout.SOUTH, btnLogin, -90, SpringLayout.SOUTH, loginFrame.getContentPane());
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String lUsername = textField.getText();
				String lPassword = passwordField.getText();
				try {
					int verifier = client.check(lUsername, lPassword);
					if (verifier == -1){
						JOptionPane panel = new JOptionPane();
						panel.showMessageDialog(loginFrame, "Wrong Username");
						panel.setVisible(true);
						
					}
					else if(verifier == -2){
						JOptionPane.showMessageDialog(loginFrame, "Wrong Password");
					}
					else{
						client.login(lUsername, lPassword);
 						JOptionPane.showMessageDialog(loginFrame, "Logged!");
 						//PROJECT FRAME------------------------------------------------------------------------
						projectFrame = new JFrame("Projects");
 						projectFrame.setBounds(100, 100, 500, 336);
 						projectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						JPanel projectContainer = new JPanel();
						projectContainer.setLayout(new BoxLayout(projectContainer, BoxLayout.PAGE_AXIS));
						projectFrame.setContentPane(projectContainer);
					
						ArrayList<Project> projects = new ArrayList<Project>();
						ArrayList<JButton> projectButtons = new ArrayList<JButton>();
						projects = client.getManagedProject();
						for(int i = 0; i < projects.size(); i++){
							JButton button = new JButton();
							button.setText(projects.get(i).getTitle());
							projectContainer.add(button);		
							projectButtons.add(button);
							button.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent event) {
									//ACTIVITY FRAME----------------------------------------------------------
									JFrame activityFrame;
								    int i = projectButtons.indexOf(event.getSource());
								    Project project = client.getManagedProject().get(i); //l'indirizzo in projects e buttons � lo stesso
								    activityFrame = new JFrame("Activities");
									activityFrame.setBounds(100, 100, 500, 336);
									activityFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
									JPanel activityContainer = new JPanel();
									activityContainer.setLayout(new BoxLayout(activityContainer, BoxLayout.PAGE_AXIS));
									activityFrame.setContentPane(activityContainer);
									ArrayList<Activity> activities = new ArrayList<Activity>();
									ArrayList<JButton> activitiesButtons = new ArrayList<JButton>();
									activities = project.getActivities();
									for(int j = 0; j < activities.size(); j++){
										JButton button = new JButton();
										button.setText(activities.get(j).getName());
										activityContainer.add(button);		
										activitiesButtons.add(button);
									}
									activityFrame.setVisible(true);
								}
							});
						}
						JPanel projectSettings = new JPanel();
						JButton addProjectButton = new JButton("Add project");
						projectSettings.add(addProjectButton);
						projectFrame.add(projectSettings);
						addProjectButton.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								
								JTextField projectTitle = new JTextField(15);
								JTextField projectDescription = new JTextField(40);
								JTextField projectCategory = new JTextField(15);
								
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
								int result = creatingProject.showConfirmDialog(projectFrame, projectValues, "Please Enter Project Values", JOptionPane.OK_CANCEL_OPTION);
								if (result == creatingProject.OK_OPTION){
									try {
										client.addProject(projectTitle.getText(), projectDescription.getText(), Category.getCategory(projectCategory.getText()), client.getMyUserId());
									} catch (RemoteException e1) {
										e1.printStackTrace();
									}	
								}
							}
							
						});
 						loginFrame.setVisible(false);
 						projectFrame.setVisible(true);			
					}					
				} catch (RemoteException e1){
					e1.printStackTrace();
				}
			}
		});
		springLayout.putConstraint(SpringLayout.WEST, btnLogin, 202, SpringLayout.WEST, loginFrame.getContentPane());
		loginFrame.getContentPane().add(btnLogin);
		
		lblStream = new JLabel("STREAM");
		springLayout.putConstraint(SpringLayout.SOUTH, lblStream, -213, SpringLayout.SOUTH, loginFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, passwordField, 0, SpringLayout.EAST, lblStream);
		springLayout.putConstraint(SpringLayout.EAST, lblStream, -184, SpringLayout.EAST, loginFrame.getContentPane());
		lblStream.setFont(new Font("Tahoma", Font.PLAIN, 28));
		loginFrame.getContentPane().add(lblStream);
		
		lblNew = new JLabel("New user?");
		springLayout.putConstraint(SpringLayout.NORTH, lblNew, 234, SpringLayout.NORTH, loginFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblNew, 159, SpringLayout.WEST, loginFrame.getContentPane());
		loginFrame.getContentPane().add(lblNew);
		
		btnNewButton = new JButton("Click here!");
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton, 230, SpringLayout.NORTH, loginFrame.getContentPane());
		btnNewButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				JTextField registerUsername = new JTextField(15);
				JPasswordField registerPassword = new JPasswordField(15);
				JPasswordField registerConfirmPassword = new JPasswordField(15);
				
				JPanel registerPanel = new JPanel();
				registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.PAGE_AXIS));
				registerPanel.add(new JLabel("Insert Username: "));
				registerPanel.add(registerUsername);
				registerPanel.add(new JLabel(" "),"span, grow");
				registerPanel.add(new JLabel("Insert Password: "));
				registerPanel.add(registerPassword);
				registerPanel.add(new JLabel(" "),"span, grow");
				registerPanel.add(new JLabel("Confirm Password: "));
				registerPanel.add(registerConfirmPassword);
				registerPanel.setVisible(true);
				
				JOptionPane registeringUser = new JOptionPane();
				int result = registeringUser.showConfirmDialog(loginFrame, registerPanel, "Please Enter Username and Password Values", JOptionPane.OK_CANCEL_OPTION);
				
				if (result == registeringUser.OK_OPTION){
					String rUsername = registerUsername.getText();
					String rPassword = registerPassword.getText();
					String rConfPassword = registerConfirmPassword.getText();
					
					if (rPassword.equals(rConfPassword)){
						try{
							client.registerUser(rUsername, rPassword);
						}catch(RemoteException exception){
							exception.printStackTrace();
						}
					}
					else{
						JOptionPane passNotEquals = new JOptionPane();
						passNotEquals.showMessageDialog(registeringUser, "Warning: different passwords inserted!");
						passNotEquals.setVisible(true);
					}

				}
				
				registeringUser.setVisible(true);
			}
		});
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton, 6, SpringLayout.EAST, lblNew);
		loginFrame.getContentPane().add(btnNewButton);
	}
}
