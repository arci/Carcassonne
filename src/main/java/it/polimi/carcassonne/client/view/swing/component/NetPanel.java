package it.polimi.carcassonne.client.view.swing.component;

import static it.polimi.carcassonne.server.model.Settings.RMI;
import static it.polimi.carcassonne.server.model.Settings.SOCKET;
import it.polimi.carcassonne.client.controller.swing.listener.NetListener;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Create the panel with the net connection mode selection
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class NetPanel extends JPanel {

	private static final long serialVersionUID = 6581751776253590932L;

	private JButton rmi = new JButton(RMI);
	private JButton socket = new JButton(SOCKET);

	/**
	 * Create a panel with the available net mode button
	 * 
	 * @param graphicMode
	 */
	public NetPanel(String graphicMode) {
		super();
		setLayout(new GridLayout(2, 1));
		JLabel label = new JLabel("Quale modalita' di rete vuoi usare?");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		add(label);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		rmi.addActionListener(new NetListener(RMI, graphicMode));
		buttonPanel.add(rmi);
		socket.addActionListener(new NetListener(SOCKET, graphicMode));
		buttonPanel.add(socket);
		add(buttonPanel);
	}
}
