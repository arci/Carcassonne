package it.polimi.carcassonne.client.view.swing.component;

import it.polimi.carcassonne.client.controller.swing.listener.PlayersListener;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Create the panel with the number of players selection
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class PlayersPanel extends JPanel {

	private static final long serialVersionUID = 5110770878050410661L;

	/**
	 * Create a panel with a comboBox used to select the number of players in a
	 * local Match
	 * 
	 * @param graphicMode
	 */
	public PlayersPanel(String graphicMode) {
		super();
		setLayout(new GridLayout(2, 1));
		JLabel label = new JLabel("Quanti giocatori parteciperanno ?");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		add(label);
		JPanel comboBoxPanel = new JPanel();
		comboBoxPanel.setLayout(new FlowLayout());

		String[] players = { "2 Giocatori", "3 Giocatori", "4 Giocatori",
				"5 Giocatori" };
		JComboBox playersList = new JComboBox(players);
		playersList.addActionListener(new PlayersListener(graphicMode));
		comboBoxPanel.add(playersList);
		add(comboBoxPanel);
	}
}
