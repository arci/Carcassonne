package it.polimi.carcassonne.client.view.swing.component;

import static it.polimi.carcassonne.server.model.Settings.CARCASSONNE_WIDTH;
import static it.polimi.carcassonne.server.model.Settings.HISTORY_WIDTH;
import static it.polimi.carcassonne.server.model.Settings.TURN_HEIGHT;
import it.polimi.carcassonne.server.model.Utils;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Create and offer the method to update the turn panel
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class TurnPanel extends JPanel {

	private static final long serialVersionUID = 5276285075819793027L;
	private JLabel label = new JLabel();
	private JLabel player = new JLabel();

	/**
	 * Create a panel that contains an indication of the current turn player
	 */
	public TurnPanel() {
		super();
		setBackground(Color.DARK_GRAY);
		setPreferredSize(new Dimension(CARCASSONNE_WIDTH - HISTORY_WIDTH,
				TURN_HEIGHT));
		setLayout(new FlowLayout());
		label.setOpaque(false);
		label.setForeground(Color.WHITE);
		label.setBackground(Color.DARK_GRAY);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		add(label);
		player.setHorizontalAlignment(JLabel.CENTER);
		player.setVerticalAlignment(JLabel.CENTER);
		add(player);

	}

	/**
	 * update the panel due to a turn change
	 * 
	 * @param playerColor
	 */
	public void update(PlayersColor playerColor) {
		label.setText("E' il turno di ");
		player.setText(playerColor.toString());
		player.setOpaque(true);
		player.setForeground(Utils.getPlayerColor(playerColor));
		player.setBackground(Color.DARK_GRAY);
	}

	/**
	 * Update the panel's text showing "it's your turn" if it's current player
	 * turn, otherwise show "it's color turn" where color is the color of the
	 * player in the current turn
	 * 
	 * @param playerColor
	 * @param myTurn
	 */
	public void update(PlayersColor playerColor, boolean myTurn) {
		if (!myTurn) {
			update(playerColor);
		} else {
			label.setText("E' il tuo turno !!!! ");
			player.setText("");
		}

	}

	/**
	 * set the specified message in the panel
	 * 
	 * @param string
	 */
	public void setText(String string) {
		label.setText(string);
	}
}
