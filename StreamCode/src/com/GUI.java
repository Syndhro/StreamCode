package com;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI extends JFrame{
	
	JPanel p = new JPanel();
	JButton b = new JButton("Login");

	JTextField username = new JTextField("", 20);
	JTextField password = new JTextField("", 20);
	JTextArea a = new JTextArea("Insert username\nand\npassword :)", 3, 10);
	
	public static void main(String args[]){
		new GUI();
	}
	
	public GUI(){
		super("GUI");
		setSize(300,400);
		setResizable(true);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		b.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                System.out.println("You clicked the button");
            }
		});  
		p.add(a);
		p.add(username);
		p.add(password);
		p.add(b);
		add(p);
	}
}
