package it.polimi.carcassonne.client.controller.swing.listener;

import static it.polimi.carcassonne.server.model.Settings.ASCII;
import static it.polimi.carcassonne.server.model.Settings.SWING;
import it.polimi.carcassonne.client.controller.MatchController;
import it.polimi.carcassonne.client.controller.swing.LocalMatchController;
import it.polimi.carcassonne.client.controller.text.LocalTextMatchController;
import it.polimi.carcassonne.client.view.swing.SwingViewManager;
import it.polimi.carcassonne.client.view.swing.ViewManager;
import it.polimi.carcassonne.client.view.text.TextViewManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

/**
 * Listen for a click on the select box and start a new game in the pre-selected
 * graphic mode with the number of player selected. Only used in local match
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class PlayersListener implements ActionListener {

	private String graphicModeToLoad;

	/**
	 * Initialize a player Listner
	 * 
	 * @param graphicMode
	 */
	public PlayersListener(String graphicMode) {
		this.graphicModeToLoad = graphicMode;
	}

	/**
	 * manage a number of player selection by starting a new match in the
	 * selected graphic mode with the specified numbers of player
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox) e.getSource();
		ViewManager.getManager().disposePlayersFrame();
		int numPlayers = cb.getSelectedIndex() + 2;
		if (graphicModeToLoad.equals(SWING)) {
			LocalMatchController controller = new LocalMatchController(
					numPlayers);
			SwingViewManager.getManager().setMatchController(controller);
			SwingViewManager.getManager().startView(numPlayers, true);
			controller.startMatch();
		} else if (graphicModeToLoad.equals(ASCII)) {
			MatchController controller = new LocalTextMatchController(
					numPlayers);
			TextViewManager.getManager().setMatchController(controller);
			TextViewManager.getManager().startView();
		}
	}

}
