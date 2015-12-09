package server;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import common.ServerInterfaceObserver;

import java.awt.*;


public class ServerGraphics implements ServerInterfaceObserver{

	JFrame serverFrame;
	JPanel statisticsPanel;
	JLabel regUserNumberLabel;
	JLabel onlUserNumberLabel;
	JLabel actProjLabelNumberLabel;
	JLabel complProjNumberLabel;
	JLabel totProjNumberLabel;
	JLabel serverInfoLabel;
	JLabel space;
	JLabel regUserLabel;
	JLabel onlUserLabel;	
	JLabel actProjLabel;
	JLabel complProjLabel;
	JLabel totProjLabel;
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
		
		serverInfoLabel = new JLabel("Server is running!");
		statisticsPanel.add(serverInfoLabel);
		
		space = new JLabel(" ");
		statisticsPanel.add(space);
		
		regUserLabel = new JLabel("Registered users:");
		statisticsPanel.add(regUserLabel);
		
		regUserNumberLabel = new JLabel(Integer.toString(server.getServerImpl().getRegisteredUsers().size()));
		statisticsPanel.add(regUserNumberLabel);
		
		onlUserLabel = new JLabel("Online users:");
		statisticsPanel.add(onlUserLabel);
		
		onlUserNumberLabel = new JLabel(Integer.toString(server.getServerImpl().getOnlineClients().size()));
		statisticsPanel.add(onlUserNumberLabel);
		
		actProjLabel = new JLabel("Active Projects:");
		statisticsPanel.add(actProjLabel);
		
		actProjLabelNumberLabel = new JLabel(Integer.toString(server.getServerImpl().getActiveProjects().size()));
		statisticsPanel.add(actProjLabelNumberLabel);
		
		complProjLabel = new JLabel("Completed Projects:");
		statisticsPanel.add(complProjLabel);
		
		complProjNumberLabel = new JLabel(Integer.toString(server.getServerImpl().getCompletedProjects().size()));
		statisticsPanel.add(complProjNumberLabel);
		
		totProjLabel = new JLabel("Total Projects:");
		statisticsPanel.add(totProjLabel);
		
		totProjNumberLabel = new JLabel(Integer.toString(server.getServerImpl().getRegisteredProjects().size()));
		statisticsPanel.add(totProjNumberLabel);
		
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
