package com;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.SwingConstants;

import javafx.scene.paint.Color;

public class ServerGraphics implements ServerInterfaceObserver{

	private JFrame serverFrame;
	private JPanel statisticsPanel;
	private JLabel regUserNumberLabel;
	private JLabel onlUserNumberLabel;
	private JLabel actProjLabelNumberLabel;
	private JLabel complProjNumberLabel;
	private JLabel totProjNumberLabel;
	private static Server server;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try { 
					server = new Server();
					server.startup();
					ServerGraphics window = new ServerGraphics();
					server.addInterfaceObserver(window);
					window.serverFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerGraphics() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		serverFrame = new JFrame();
		serverFrame.setBounds(100, 100, 500, 336);
		serverFrame.setVisible(true);
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		statisticsPanel = new JPanel();
		GridLayout panelLayout = new GridLayout(7, 2, 2, 1);
		statisticsPanel.setLayout(panelLayout);
		
		JLabel serverInfoLabel = new JLabel("Server is running!");
		statisticsPanel.add(serverInfoLabel);
		
		JLabel space = new JLabel(" ");
		statisticsPanel.add(space);
		
		JLabel regUserLabel = new JLabel("Registered users:");
		statisticsPanel.add(regUserLabel);
		
		regUserNumberLabel = new JLabel(Integer.toString(server.getServerImpl().getRegisteredUsers().size()));
		statisticsPanel.add(regUserNumberLabel);
		
		JLabel onlUserLabel = new JLabel("Online users:");
		statisticsPanel.add(onlUserLabel);
		
		onlUserNumberLabel = new JLabel(Integer.toString(server.getServerImpl().getOnlineClients().size()));
		statisticsPanel.add(onlUserNumberLabel);
		
		JLabel actProjLabel = new JLabel("Active Projects:");
		statisticsPanel.add(actProjLabel);
		
		actProjLabelNumberLabel = new JLabel(Integer.toString(server.getServerImpl().getActiveProjects().size()));
		statisticsPanel.add(actProjLabelNumberLabel);
		
		JLabel complProjLabel = new JLabel("Completed Projects:");
		statisticsPanel.add(complProjLabel);
		
		complProjNumberLabel = new JLabel(Integer.toString(server.getServerImpl().getCompletedProjects().size()));
		statisticsPanel.add(complProjNumberLabel);
		
		JLabel totProjLabel = new JLabel("Total Projects:");
		statisticsPanel.add(totProjLabel);
		
		totProjNumberLabel = new JLabel(Integer.toString(server.getServerImpl().getRegisteredProjects().size()));
		statisticsPanel.add(totProjNumberLabel);
		
		JButton button = new JButton("Refresh!");
		
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		
		statisticsPanel.add(button);
		
		serverFrame.add(statisticsPanel);
		
	}
	
	public void update(){
		regUserNumberLabel.setText(Integer.toString(server.getServerImpl().getRegisteredUsers().size()));;
		onlUserNumberLabel.setText(Integer.toString(server.getServerImpl().getOnlineClients().size()));;
		actProjLabelNumberLabel.setText(Integer.toString(server.getServerImpl().getActiveProjects().size()));;
		complProjNumberLabel.setText(Integer.toString(server.getServerImpl().getCompletedProjects().size()));;
		totProjNumberLabel.setText(Integer.toString(server.getServerImpl().getRegisteredProjects().size()));;
	}
}
