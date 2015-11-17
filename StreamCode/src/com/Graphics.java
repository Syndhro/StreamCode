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
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.awt.Font;

public class Graphics {

	private JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;
	private JLabel lblStream;
	private JLabel lblNew;
	private JButton btnNewButton;
	static Client client = new Client();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Graphics window = new Graphics();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		client = new Client();
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
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 336);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.EAST, textField, -180, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		springLayout.putConstraint(SpringLayout.SOUTH, passwordField, -163, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, passwordField, -180, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, textField, 6, SpringLayout.SOUTH, passwordField);
		frame.getContentPane().add(passwordField);
		
		JLabel lblPassword = new JLabel("Password");
		springLayout.putConstraint(SpringLayout.EAST, lblPassword, -308, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, passwordField, 20, SpringLayout.EAST, lblPassword);
		springLayout.putConstraint(SpringLayout.NORTH, lblPassword, 3, SpringLayout.NORTH, passwordField);
		frame.getContentPane().add(lblPassword);
		
		JLabel lblUsername = new JLabel("Username");
		springLayout.putConstraint(SpringLayout.WEST, textField, 18, SpringLayout.EAST, lblUsername);
		springLayout.putConstraint(SpringLayout.NORTH, lblUsername, 3, SpringLayout.NORTH, textField);
		springLayout.putConstraint(SpringLayout.WEST, lblUsername, 0, SpringLayout.WEST, lblPassword);
		frame.getContentPane().add(lblUsername);
		
		JButton btnLogin = new JButton("Login");
		springLayout.putConstraint(SpringLayout.NORTH, btnLogin, 24, SpringLayout.SOUTH, textField);
		springLayout.putConstraint(SpringLayout.WEST, btnLogin, 202, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(btnLogin);
		
		lblStream = new JLabel("STREAM");
		springLayout.putConstraint(SpringLayout.SOUTH, lblStream, -30, SpringLayout.NORTH, passwordField);
		springLayout.putConstraint(SpringLayout.EAST, lblStream, -184, SpringLayout.EAST, frame.getContentPane());
		lblStream.setFont(new Font("Tahoma", Font.PLAIN, 28));
		frame.getContentPane().add(lblStream);
		
		lblNew = new JLabel("New user?");
		springLayout.putConstraint(SpringLayout.NORTH, lblNew, 27, SpringLayout.SOUTH, btnLogin);
		springLayout.putConstraint(SpringLayout.WEST, lblNew, 159, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(lblNew);
		
		btnNewButton = new JButton("Click here!");
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
				int result = registeringUser.showConfirmDialog(frame, registerPanel, "Please Enter Username and Password Values", JOptionPane.OK_CANCEL_OPTION);
				
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
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton, -4, SpringLayout.NORTH, lblNew);
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton, 6, SpringLayout.EAST, lblNew);
		frame.getContentPane().add(btnNewButton);
	}
}
