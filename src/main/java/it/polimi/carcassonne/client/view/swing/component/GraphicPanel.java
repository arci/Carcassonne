package it.polimi.carcassonne.client.view.swing.component;

import static it.polimi.carcassonne.server.model.Settings.ASCII;
import static it.polimi.carcassonne.server.model.Settings.SWING;
import it.polimi.carcassonne.client.controller.swing.listener.GraphicListener;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Create the panel with the graphic mode selection
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class GraphicPanel extends JPanel {

	private static final long serialVersionUID = 3581979118430831730L;
	private JButton ascii = new JButton("Modalita' testuale");
	private JButton swing = new JButton("Modalita' grafica");

	/**
	 * Create a panel with the available graphics mode button
	 * 
	 */
	public GraphicPanel() {
		super();
		setLayout(new GridLayout(2, 1));
		JLabel label = new JLabel("Come vuoi giocare ?");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		add(label);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(ascii);
		//FIX final relese not allow ASCII
		ascii.addActionListener(new GraphicListener(ASCII));
		ascii.setEnabled(false);
		buttonPanel.add(swing);
		swing.addActionListener(new GraphicListener(SWING));
		add(buttonPanel);
	}
}
