package com;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ProjectListFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProjectListFrame frame = new ProjectListFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws RemoteException 
	 */
	public ProjectListFrame() throws RemoteException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		Container mainPanel = getContentPane();						
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		ArrayList<Project> projects = new ArrayList<Project>();
		ArrayList<JButton> projectButtons = new ArrayList<JButton>();
		
		projects = Client.getInstance().getManagedProject();
		
		for(int i = 0; i < projects.size(); i++){
			JButton button = new JButton();
			button.setText(projects.get(i).getTitle());
			mainPanel.add(button);		
			projectButtons.add(button);
		}
	}
}
