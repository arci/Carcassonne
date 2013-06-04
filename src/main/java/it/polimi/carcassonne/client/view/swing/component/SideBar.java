package it.polimi.carcassonne.client.view.swing.component;

import static it.polimi.carcassonne.server.model.Settings.CARCASSONNE_HEIGHT;
import static it.polimi.carcassonne.server.model.Settings.PADDING_10;
import static it.polimi.carcassonne.server.model.Settings.PADDING_15;
import static it.polimi.carcassonne.server.model.Settings.PADDING_5;
import static it.polimi.carcassonne.server.model.Settings.PADDING_NULL;
import static it.polimi.carcassonne.server.model.Settings.PLAYERS_PANEL_HEIGHT;
import static it.polimi.carcassonne.server.model.Settings.PLAYERS_PANEL_WIDTH;
import static it.polimi.carcassonne.server.model.Settings.SIDEBAR_FIRST_ELEMENT;
import static it.polimi.carcassonne.server.model.Settings.SIDEBAR_FOURTH_ELEMENT;
import static it.polimi.carcassonne.server.model.Settings.SIDEBAR_SECOND_ELEMENT;
import static it.polimi.carcassonne.server.model.Settings.SIDEBAR_THIRD_ELEMENT_2;
import static it.polimi.carcassonne.server.model.Settings.SIDEBAR_WIDTH;
import static it.polimi.carcassonne.server.model.Settings.SIDEBAR_X;
import static it.polimi.carcassonne.server.model.Settings.TURN_HEIGHT;
import it.polimi.carcassonne.client.controller.swing.listener.ConnectListener;
import it.polimi.carcassonne.client.controller.swing.listener.NextTurnListener;
import it.polimi.carcassonne.client.controller.swing.listener.RotateListener;
import it.polimi.carcassonne.client.controller.swing.listener.ScreenshotListener;
import it.polimi.carcassonne.resources.Resources;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Create and manage the side bar that contains: the players with theirs score
 * and markers, the last drawed tile with the button to rotate it, the button to
 * pass turn and the button to take screenshot of the grid
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class SideBar extends JPanel {

	private static final long serialVersionUID = 8094677353031445615L;
	private JPanel playersPanel = new JPanel();
	private JPanel drawingPanel = new JPanel();
	private JPanel connectPanel = new JPanel();
	private JPanel tilePanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private Map<PlayersColor, JLabel> playersScore = new HashMap<PlayersColor, JLabel>();
	private Map<PlayersColor, JLabel> playersMarker = new HashMap<PlayersColor, JLabel>();
	private TilePanel drawedTile;
	private JButton rotateButton;
	private JButton nextTurnButton;
	private JButton connectButton = new JButton();
	private JLabel deckSize = new JLabel();

	/**
	 * Display a side bar with game controls
	 * 
	 * @param numPlayers
	 *            numbers of players for the match
	 * @param local
	 *            if it's a local match, local must be true, will not be
	 *            displayed the "connect" button
	 */
	public SideBar(int numPlayers, boolean local) {
		super();
		setBackground(Color.DARK_GRAY);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(SIDEBAR_WIDTH, CARCASSONNE_HEIGHT
				- TURN_HEIGHT));
		if (local) {
			addPlayersPanel(numPlayers);
			addSideBarComponent();
		} else {
			addConnectPanel();
		}
	}

	/**
	 * remove from the side bar the connect panel (that contains the connect
	 * button)
	 */
	public void removeConnect() {
		remove(connectPanel);
		validate();
	}

	/**
	 * Set the deckSize label to the specified integer
	 * 
	 * @param size
	 */
	public void setDeckSize(int size) {
		Integer s = size;
		deckSize.setText(s.toString());
	}

	/**
	 * decrease the number of tile in the deck
	 */
	public void decreaseDeckSize() {
		Integer size = Integer.parseInt(deckSize.getText()) - 1;
		if (size < 0) {
			size = 0;
		}
		deckSize.setText(size.toString());
	}

	/**
	 * 
	 * @return a reefer to the panel that contains the last drawed tile
	 */
	public TilePanel getDrawedTilePanel() {
		return drawedTile;
	}

	/**
	 * set the specified number of Marker for the player with the specified
	 * color
	 * 
	 * @param color
	 * @param number
	 */
	public void setMarker(PlayersColor color, int number) {
		JLabel label = playersMarker.get(color);
		if (label != null) {
			Integer num = number;
			label.setText(num.toString());
		}
	}

	/**
	 * remove a marker from the specified player
	 * 
	 * @param color
	 */
	public void removeMarker(PlayersColor color) {
		JLabel label = playersMarker.get(color);
		if (label != null) {
			Integer markers = Integer.parseInt(label.getText()) - 1;
			label.setText(markers.toString());
		}
	}

	/**
	 * add a marker from the specified player
	 * 
	 * @param color
	 */
	public void addMarker(PlayersColor color) {
		JLabel label = playersMarker.get(color);
		if (label != null) {
			Integer score = Integer.parseInt(label.getText()) + 1;
			label.setText(score.toString());
		}
	}

	/**
	 * 
	 * set the specified score for the player with the specified color
	 * 
	 * @param color
	 * @param score
	 */
	public void setScore(PlayersColor color, int score) {
		JLabel label = playersScore.get(color);
		if (label != null) {
			Integer s = score;
			label.setText(s.toString());
		}
	}

	/**
	 * increase the score for the specified player
	 * 
	 * @param color
	 * @param score
	 */
	public void increaseScore(PlayersColor color, int score) {
		JLabel label = playersScore.get(color);
		if (label != null) {
			Integer newScore = Integer.parseInt(label.getText()) + score;
			label.setText(newScore.toString());
		}
	}

	/**
	 * refresh the drawed tile panel with the new image
	 * 
	 * @param i
	 * @param degree
	 */
	public void setDrawedTile(int i, int degree) {
		drawedTile.update(i, degree);
		drawedTile.repaint();
		tilePanel.add(drawedTile);
		tilePanel.repaint();
	}

	/**
	 * disable the rotate button
	 * 
	 * @param b
	 *            true for disable the button, false to enable it
	 */
	public void disableRotate(boolean b) {
		rotateButton.setEnabled(!b);
	}

	/**
	 * disable the nextTurn button
	 * 
	 * @param b
	 *            true for disable the button, false to enable it
	 */
	public void disableNextTurn(boolean b) {
		nextTurnButton.setEnabled(!b);
	}

	// Crea il pannello connect
	private void addConnectPanel() {
		connectPanel.setLayout(new FlowLayout());
		connectPanel.setBackground(Color.DARK_GRAY);
		connectButton.setText("Connettiti");
		connectButton.addActionListener(new ConnectListener());
		connectPanel.add(connectButton);
		add(connectPanel, BorderLayout.NORTH);
	}

	/**
	 * create and add the players panel with the specified numbers of players
	 * 
	 * @param numPlayers
	 */
	public void addPlayersPanel(int numPlayers) {
		playersPanel.setLayout(new GridBagLayout());
		playersPanel.setBackground(Color.DARK_GRAY);

		// intestazione
		JLabel label = new JLabel();
		label.setText("Giocatori:");
		label.setBorder(BorderFactory.createCompoundBorder(null, BorderFactory
				.createEmptyBorder(PADDING_10, PADDING_10, PADDING_5,
						PADDING_10)));
		label.setOpaque(true);
		label.setBackground(Color.DARK_GRAY);
		label.setForeground(Color.WHITE);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		playersPanel.add(label, c);

		// pannelli players
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(PLAYERS_PANEL_HEIGHT,
				PLAYERS_PANEL_WIDTH));
		panel.setBackground(Color.DARK_GRAY);
		for (int i = 0; i < numPlayers; i++) {
			JPanel playerContainer = addPlayer(i);
			panel.add(playerContainer);
		}
		c.gridx = 0;
		c.gridy = 1;
		playersPanel.add(panel, c);
		add(playersPanel, BorderLayout.CENTER);
	}

	// crea una riga del tipo "Punteggio: 0 Markers: 7"
	private JPanel addPlayer(int i) {
		JPanel playerContainer = new JPanel();
		playerContainer.setBackground(Color.DARK_GRAY);

		// punteggio
		JLabel score;
		score = new JLabel();
		score.setText("Punteggio: ");
		score.setForeground(Color.WHITE);
		playerContainer.add(score);
		score = new JLabel();
		score.setText("0");
		score.setForeground(Color.WHITE);
		playerContainer.add(score);
		// aggiungo all'arrayList
		playersScore.put(PlayersColor.getColorByID(i), score);

		// separatore
		JLabel flush = new JLabel();
		flush.setBackground(Color.DARK_GRAY);
		playerContainer.add(flush);
		playerContainer.add(flush);

		// segnalini
		playerContainer.setLayout(new FlowLayout());
		JLabel color = null;

		PlayersColor playerColor = PlayersColor.getColorByID(i);
		color = new JLabel(new ImageIcon(Resources.class.getResource("img/markers/"+playerColor
				+ "Marker.png")));
		playerContainer.add(color);

		// numero di segnalini
		JLabel markers = new JLabel();
		markers.setText("7");
		markers.setForeground(Color.WHITE);
		playerContainer.add(markers);
		// aggiungo all'arrayList
		playersMarker.put(PlayersColor.getColorByID(i), markers);

		return playerContainer;
	}

	/**
	 * Create and add the panel with the lastDrawed tile, the rotate button and
	 * the nextTurn button
	 */
	public void addSideBarComponent() {
		drawingPanel.setLayout(new GridBagLayout());
		drawingPanel.setBackground(Color.DARK_GRAY);
		GridBagConstraints c = new GridBagConstraints();

		// intestazione
		JLabel label = new JLabel();
		label.setText("Carta pescata:");
		label.setBorder(BorderFactory.createCompoundBorder(null, BorderFactory
				.createEmptyBorder(PADDING_15, PADDING_10, PADDING_5,
						PADDING_10)));
		label.setOpaque(true);
		label.setBackground(Color.DARK_GRAY);
		label.setForeground(Color.WHITE);
		c.gridx = SIDEBAR_X;
		c.gridy = SIDEBAR_FIRST_ELEMENT;
		drawingPanel.add(label, c);

		// drawing panel
		addDrawingPanel();

		// button panel
		buttonPanel();

		// screenshot
		JButton screenshot = new JButton(new ImageIcon(Resources.class.getResource("img/screenshot.png")));
		screenshot.addActionListener(new ScreenshotListener());
		screenshot.setBorder(BorderFactory.createCompoundBorder(null,
				BorderFactory.createEmptyBorder(PADDING_5, 0, 0, 0)));
		screenshot.setOpaque(true);
		screenshot.setBackground(Color.DARK_GRAY);
		screenshot.setForeground(Color.WHITE);

		c.gridx = SIDEBAR_X;
		c.gridy = SIDEBAR_FOURTH_ELEMENT;
		drawingPanel.add(screenshot, c);

		add(drawingPanel, BorderLayout.SOUTH);
		drawingPanel.setBorder(BorderFactory.createCompoundBorder(null,
				BorderFactory.createEmptyBorder(PADDING_NULL, PADDING_NULL,
						PADDING_15, PADDING_10)));
	}

	private void buttonPanel() {
		GridBagConstraints c = new GridBagConstraints();

		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.setBackground(Color.DARK_GRAY);
		// pulsante rotate
		rotateButton = new JButton(new ImageIcon(Resources.class.getResource("img/rotate.png")));
		rotateButton.setBackground(Color.DARK_GRAY);
		rotateButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		rotateButton.addActionListener(new RotateListener());
		rotateButton.setBorder(BorderFactory.createCompoundBorder(null,
				BorderFactory.createEmptyBorder(PADDING_5, PADDING_10,
						PADDING_10, PADDING_5)));
		buttonPanel.add(rotateButton);

		// pulsante nextTurn
		nextTurnButton = new JButton(
				new ImageIcon(Resources.class.getResource("img/nextTurn.png")));
		nextTurnButton.setBackground(Color.DARK_GRAY);
		nextTurnButton.setBorder(BorderFactory
				.createLineBorder(Color.DARK_GRAY));
		nextTurnButton.addActionListener(new NextTurnListener());
		nextTurnButton.setEnabled(false);
		nextTurnButton.setBorder(BorderFactory.createCompoundBorder(null,
				BorderFactory.createEmptyBorder(PADDING_5, PADDING_10,
						PADDING_10, PADDING_10)));
		buttonPanel.add(nextTurnButton);

		c.gridx = SIDEBAR_X;
		c.gridy = SIDEBAR_THIRD_ELEMENT_2;
		drawingPanel.add(buttonPanel, c);
	}

	private void addDrawingPanel() {
		GridBagConstraints c = new GridBagConstraints();

		// carta pescata
		tilePanel.setBackground(Color.DARK_GRAY);
		tilePanel.setLayout(new BorderLayout());
		drawedTile = new TilePanel();
		c.gridx = SIDEBAR_X;
		c.gridy = SIDEBAR_SECOND_ELEMENT;
		tilePanel.add(drawedTile, BorderLayout.WEST);

		// numero di carte restanti nel mazzo
		deckSize.setText("0");
		deckSize.setOpaque(true);
		deckSize.setForeground(Color.WHITE);
		deckSize.setBackground(Color.DARK_GRAY);
		deckSize.setVerticalAlignment(JLabel.TOP);
		deckSize.setBorder(BorderFactory.createCompoundBorder(null,
				BorderFactory.createEmptyBorder(PADDING_NULL, PADDING_10,
						PADDING_NULL, PADDING_NULL)));
		tilePanel.add(deckSize, BorderLayout.EAST);

		drawingPanel.add(tilePanel, c);
	}
}
