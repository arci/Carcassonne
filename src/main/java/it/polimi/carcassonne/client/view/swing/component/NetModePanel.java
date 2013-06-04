package it.polimi.carcassonne.client.view.swing.component;

import static it.polimi.carcassonne.server.model.Settings.LOCAL;
import static it.polimi.carcassonne.server.model.Settings.NET;
import it.polimi.carcassonne.client.controller.swing.listener.NetModeListener;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Create the panel with the net mode selection
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class NetModePanel extends JPanel {

	private static final long serialVersionUID = -4393488514687441271L;

	private JButton local = new JButton(LOCAL);
	private JButton net = new JButton(NET);

	/**
	 * Create a panel with the avaiable game mode button
	 * 
	 */
	public NetModePanel() {
		super();
		setLayout(new GridLayout(2, 1));
		JLabel label = new JLabel("In che modo vuoi giocare ?");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		add(label);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		local.addActionListener(new NetModeListener(LOCAL));
		buttonPanel.add(local);
		//FIX final relese not allow net mode
		net.addActionListener(new NetModeListener(NET));
		net.setEnabled(false);
		buttonPanel.add(net);
		add(buttonPanel);
	}

}
