package com;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.awt.Font;

public class ClientGUI {

	private JFrame loginFrame;
	private JTextField textField;
	private JPasswordField passwordField;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblStream;
	private JLabel lblNew;
	private JButton registerButton;
	private JButton loginButton;
	private static Client client; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI clientGUI = new ClientGUI();
					clientGUI.loginFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try{
			client = new Client();
			client.startup();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public ClientGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		loginFrame = new JFrame();
		loginFrame.setBounds(100, 100, 500, 336);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setLocationRelativeTo(null);
		SpringLayout springLayout = new SpringLayout();
		loginFrame.getContentPane().setLayout(springLayout);
		
		textField = new JTextField();
		textField.setColumns(10);
		passwordField = new JPasswordField();
		loginFrame.getContentPane().add(textField);
		loginFrame.getContentPane().add(passwordField);
				
		springLayout.putConstraint(SpringLayout.NORTH, passwordField, 140, SpringLayout.NORTH, loginFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, passwordField);
		springLayout.putConstraint(SpringLayout.SOUTH, textField, -6, SpringLayout.NORTH, passwordField);
		springLayout.putConstraint(SpringLayout.EAST, textField, 0, SpringLayout.EAST, passwordField);
		springLayout.putConstraint(SpringLayout.WEST, passwordField, 192, SpringLayout.WEST, loginFrame.getContentPane());
				
		lblPassword = new JLabel("Password");
		springLayout.putConstraint(SpringLayout.NORTH, lblPassword, 3, SpringLayout.NORTH, passwordField);
		springLayout.putConstraint(SpringLayout.EAST, lblPassword, -14, SpringLayout.WEST, passwordField);
		loginFrame.getContentPane().add(lblPassword);
		
		lblUsername = new JLabel("Username");
		springLayout.putConstraint(SpringLayout.NORTH, lblUsername, 3, SpringLayout.NORTH, textField);
		springLayout.putConstraint(SpringLayout.WEST, lblUsername, 0, SpringLayout.WEST, lblPassword);
		loginFrame.getContentPane().add(lblUsername);
		
		loginButton = new JButton("Login");
		springLayout.putConstraint(SpringLayout.SOUTH, loginButton, -90, SpringLayout.SOUTH, loginFrame.getContentPane());
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textField.getText();
				@SuppressWarnings("deprecation")
				String password = passwordField.getText();
				try {
					int verifier = client.check(username, password);
					if (verifier == -1){
						JOptionPane.showMessageDialog(loginFrame, "Wrong Username");						
					}
					else if(verifier == -2){
						JOptionPane.showMessageDialog(loginFrame, "Wrong Password");
					}
					else if(verifier == -3){
						JOptionPane.showMessageDialog(loginFrame, "Someone has already logged in with this account!");
					}
					
					//LOGIN CORRECT--------------------------------------------------------------------------------------
					else{
						client.login(username, password);
 						JOptionPane.showMessageDialog(loginFrame, "Logged!");
 						loginFrame.dispose();
 						ProjectListFrame projectListFrame = new ProjectListFrame(client);
 						projectListFrame.setLocationRelativeTo(null);
 						projectListFrame.setVisible(true);
 							
					}		
				} catch (RemoteException e1){
					e1.printStackTrace();
				}
			}
		});
		springLayout.putConstraint(SpringLayout.WEST, loginButton, 202, SpringLayout.WEST, loginFrame.getContentPane());
		loginFrame.getContentPane().add(loginButton);
		
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
		
		registerButton = new JButton("Click here!");
		springLayout.putConstraint(SpringLayout.NORTH, registerButton, 230, SpringLayout.NORTH, loginFrame.getContentPane());
		registerButton.addActionListener(new ActionListener() {
			
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
			
				int result = JOptionPane.showConfirmDialog(loginFrame, registerPanel, "Please Enter Username and Password Values", JOptionPane.OK_CANCEL_OPTION);
				
				if (result == JOptionPane.OK_OPTION){
					String rUsername = registerUsername.getText();
					@SuppressWarnings("deprecation")
					String rPassword = registerPassword.getText();
					@SuppressWarnings("deprecation")
					String rConfPassword = registerConfirmPassword.getText();
					
					if (rPassword.equals(rConfPassword)){
						try{
							int verifier = client.check(rUsername, rPassword);
							if(verifier == -1){
								client.registerUser(rUsername, rPassword);
								JOptionPane.showMessageDialog(loginFrame, "Successfully Registered!");
							}
							else{
								JOptionPane.showMessageDialog(loginFrame, "Username already exists");
							}
						}catch(RemoteException exception){
							exception.printStackTrace();
						}
					}
					else{				
						JOptionPane.showMessageDialog(registerPanel, "Warning: different passwords inserted!");
					}
				}	
			}
		});
		
		springLayout.putConstraint(SpringLayout.WEST, registerButton, 6, SpringLayout.EAST, lblNew);
		loginFrame.getContentPane().add(registerButton);
	}
}
