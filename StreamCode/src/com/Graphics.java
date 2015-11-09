package com;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Graphics {

	private JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;

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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, textField, 173, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, textField, -201, SpringLayout.SOUTH, frame.getContentPane());
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		springLayout.putConstraint(SpringLayout.NORTH, passwordField, 6, SpringLayout.SOUTH, textField);
		springLayout.putConstraint(SpringLayout.WEST, passwordField, 173, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, passwordField, 0, SpringLayout.EAST, textField);
		frame.getContentPane().add(passwordField);
		
		JLabel lblPassword = new JLabel("Password");
		springLayout.putConstraint(SpringLayout.SOUTH, lblPassword, 0, SpringLayout.SOUTH, passwordField);
		springLayout.putConstraint(SpringLayout.EAST, lblPassword, -25, SpringLayout.WEST, passwordField);
		frame.getContentPane().add(lblPassword);
		
		JLabel lblUsername = new JLabel("Username");
		springLayout.putConstraint(SpringLayout.SOUTH, lblUsername, 0, SpringLayout.SOUTH, textField);
		springLayout.putConstraint(SpringLayout.EAST, lblUsername, 0, SpringLayout.EAST, lblPassword);
		frame.getContentPane().add(lblUsername);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String username = textField.getText();
				String password = passwordField.getText();
				//devo darli al server
				System.out.println(username + password);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnLogin, 20, SpringLayout.SOUTH, passwordField);
		springLayout.putConstraint(SpringLayout.WEST, btnLogin, 0, SpringLayout.WEST, textField);
		frame.getContentPane().add(btnLogin);
	}
}
