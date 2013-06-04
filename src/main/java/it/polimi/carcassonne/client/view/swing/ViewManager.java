package it.polimi.carcassonne.client.view.swing;

import static it.polimi.carcassonne.server.model.Settings.ASCII;
import static it.polimi.carcassonne.server.model.Settings.NET_HEIGHT;
import static it.polimi.carcassonne.server.model.Settings.NET_WIDTH;
import static it.polimi.carcassonne.server.model.Settings.PRE_HEIGHT;
import static it.polimi.carcassonne.server.model.Settings.PRE_WIDTH;
import static it.polimi.carcassonne.server.model.Settings.SWING;
import it.polimi.carcassonne.client.view.swing.component.GraphicPanel;
import it.polimi.carcassonne.client.view.swing.component.NetModePanel;
import it.polimi.carcassonne.client.view.swing.component.NetPanel;
import it.polimi.carcassonne.client.view.swing.component.PlayersPanel;
import it.polimi.carcassonne.client.view.swing.frame.GraphicFrame;
import it.polimi.carcassonne.client.view.swing.frame.NetFrame;
import it.polimi.carcassonne.client.view.swing.frame.NetModeFrame;
import it.polimi.carcassonne.client.view.swing.frame.PlayersFrame;
import it.polimi.carcassonne.resources.Resources;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Create all game mode selection frame and manage the user choice
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class ViewManager {
	private String graphicMode = null;
	private static ViewManager viewManager;
	private GraphicFrame graphicFrame;
	private PlayersFrame playersFrame;
	private NetFrame netFrame;
	private NetModeFrame netModeFrame;

	/**
	 * set all the initial frame of game model selection
	 */
	public ViewManager() {
		graphicFrame = new GraphicFrame("Scegli la modalita' grafica",
				PRE_HEIGHT, PRE_WIDTH);
		netModeFrame = new NetModeFrame("Modalita' di gioco", NET_HEIGHT,
				NET_WIDTH);
		netFrame = new NetFrame("Modalita' di rete", NET_HEIGHT, NET_WIDTH);
		playersFrame = new PlayersFrame("Numero di giocatori", NET_HEIGHT,
				NET_WIDTH);
	}

	/**
	 * 
	 * @return a singleton instance of the view manager
	 */
	public static synchronized ViewManager getManager() {
		if (viewManager == null) {
			viewManager = new ViewManager();
		}
		return viewManager;
	}

	/**
	 * Set the user selected graphic mode to load
	 * 
	 * @param mode
	 *            , must be SWING or ASCII
	 */
	public void setGraphicMode(String mode) {
		if (mode.equals(SWING)) {
			graphicMode = SWING;
		} else if (mode.equals(ASCII)) {
			graphicMode = ASCII;
		}
	}

	/**
	 * Show the graphic selection frame (ASCII, Swing)
	 */
	public void graphicSelection() {
		graphicFrame.setLayout(new BorderLayout());
		JLabel logo = new JLabel(new ImageIcon(Resources.class.getResource("img/logo.png")));
		graphicFrame.add(logo, BorderLayout.NORTH);
		GraphicPanel graphicPanel = new GraphicPanel();
		graphicFrame.add(graphicPanel, BorderLayout.CENTER);
		graphicFrame.pack();
		graphicFrame.setLocationRelativeTo(null);
		graphicFrame.setVisible(true);
	}

	/**
	 * Show the game mode selection frame (Local, Net)
	 */
	public void netModeSelection() {
		netModeFrame.setLayout(new BorderLayout());
		NetModePanel netModePanel = new NetModePanel();
		netModeFrame.add(netModePanel, BorderLayout.CENTER);
		netModeFrame.pack();
		netModeFrame.setLocationRelativeTo(null);
		netModeFrame.setVisible(true);
	}

	/**
	 * Show the net mode selection frame (RMI, Soket)
	 */
	public void netSelection() {
		netFrame.setLayout(new BorderLayout());
		NetPanel netPanel = new NetPanel(graphicMode);
		netFrame.add(netPanel, BorderLayout.CENTER);
		netFrame.pack();
		netFrame.setLocationRelativeTo(null);
		netFrame.setVisible(true);
	}

	/**
	 * Show the number of player selection frame
	 */
	public void playersSelection() {
		playersFrame.setLayout(new BorderLayout());
		PlayersPanel playersPanel = new PlayersPanel(graphicMode);
		playersFrame.add(playersPanel, BorderLayout.CENTER);
		playersFrame.pack();
		playersFrame.setLocationRelativeTo(null);
		playersFrame.setVisible(true);
	}

	/**
	 * dispose the graphic selection frame
	 */
	public void disposeGraphicsFrame() {
		graphicFrame.dispose();
	}

	/**
	 * dispose the net mode selection frame
	 */
	public void disposeNetFrame() {
		netFrame.dispose();
	}

	/**
	 * dispose the net connection mode selection frame
	 */
	public void disposeNetModeFrame() {
		netModeFrame.dispose();
	}

	/**
	 * dispose the numbre of players selection frame
	 */
	public void disposePlayersFrame() {
		playersFrame.dispose();
	}

}
