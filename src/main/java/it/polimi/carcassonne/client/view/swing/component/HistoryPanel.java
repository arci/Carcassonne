package it.polimi.carcassonne.client.view.swing.component;

import static it.polimi.carcassonne.server.model.Settings.CARCASSONNE_HEIGHT;
import static it.polimi.carcassonne.server.model.Settings.HISTORY_PANEL_FONT_SIZE;
import static it.polimi.carcassonne.server.model.Settings.HISTORY_WIDTH;
import static it.polimi.carcassonne.server.model.Settings.PADDING_10;
import static it.polimi.carcassonne.server.model.Settings.PADDING_5;
import static it.polimi.carcassonne.server.model.Settings.TURN_HEIGHT;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Create and offer the method to update the history sidebar
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class HistoryPanel extends JPanel {

	private static final long serialVersionUID = 2468859713196386285L;
	private JTextArea logArea;
	private PlayersColor playerColor;

	/**
	 * Create a disabled JTextArea in which can be displayed the history of the
	 * match
	 */
	public HistoryPanel() {
		super();
		setLayout(new BorderLayout());
		setBackground(Color.DARK_GRAY);
		JLabel label = new JLabel("Mosse Effettuate:");
		label.setOpaque(false);
		label.setForeground(Color.WHITE);
		label.setBackground(Color.DARK_GRAY);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		label.setBorder(BorderFactory.createCompoundBorder(null,
				BorderFactory.createEmptyBorder(PADDING_5, PADDING_10,
						PADDING_5, PADDING_10)));
		add(label, BorderLayout.NORTH);
		logArea = new JTextArea();
		logArea.setMargin(new Insets(PADDING_5, PADDING_5, PADDING_5, PADDING_5));
		logArea.setFont(new Font("Helvetica", Font.ITALIC,
				HISTORY_PANEL_FONT_SIZE));
		logArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(logArea);
		scroll.setPreferredSize(new Dimension(HISTORY_WIDTH, CARCASSONNE_HEIGHT
				- TURN_HEIGHT));
		add(scroll, BorderLayout.CENTER);
	}

	/**
	 * Set the PlayerColor which is shown first of each log messagge
	 * 
	 * @param playerColor
	 */
	public void setPlayerColor(PlayersColor playerColor) {
		this.playerColor = playerColor;
	}

	/**
	 * Append a message in the history bar
	 * 
	 * @param message
	 */
	public void message(String message) {
		logArea.append(message + "\n");
	}

	/**
	 * Append a message logged with the current player color
	 * 
	 * @param message
	 */
	public void log(String message) {
		logArea.append(playerColor + ": " + message + "\n");
	}
}
