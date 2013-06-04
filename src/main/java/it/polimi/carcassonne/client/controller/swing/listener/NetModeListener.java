package it.polimi.carcassonne.client.controller.swing.listener;

import static it.polimi.carcassonne.server.model.Settings.LOCAL;
import static it.polimi.carcassonne.server.model.Settings.NET;
import it.polimi.carcassonne.client.view.swing.ViewManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Manage the user selection and show the frame to choose the net mode
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class NetModeListener implements ActionListener {

	private String netModeToLoad;

	/**
	 * Initialize a net mode listener
	 * 
	 * @param netMode
	 */
	public NetModeListener(String netMode) {
		this.netModeToLoad = netMode;
	}

	/**
	 * Start the players pane if the net mode is Local, instead show the net
	 * mode selection
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (netModeToLoad.equals(NET)) {
			ViewManager.getManager().netSelection();
		} else if (netModeToLoad.equals(LOCAL)) {
			ViewManager.getManager().playersSelection();
		}
		ViewManager.getManager().disposeNetModeFrame();
	}

}
