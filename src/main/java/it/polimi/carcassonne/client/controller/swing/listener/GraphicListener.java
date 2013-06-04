package it.polimi.carcassonne.client.controller.swing.listener;

import static it.polimi.carcassonne.server.model.Settings.ASCII;
import static it.polimi.carcassonne.server.model.Settings.SWING;
import it.polimi.carcassonne.client.view.swing.ViewManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Manage the user selection disposing the precedent frame and showing the net
 * mode selection frame
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class GraphicListener implements ActionListener {

	private String viewToLoad;

	/**
	 * Initialize a graphic listener
	 * 
	 * @param viewToLoad
	 */
	public GraphicListener(String viewToLoad) {
		this.viewToLoad = viewToLoad;
	}

	/**
	 * Set in the ViewManager selected the type of view , than show the net mode
	 * frame
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (viewToLoad.equals(SWING)) {
			ViewManager.getManager().setGraphicMode(SWING);
		} else if (viewToLoad.equals(ASCII)) {
			ViewManager.getManager().setGraphicMode(ASCII);
		}
		ViewManager.getManager().disposeGraphicsFrame();
		ViewManager.getManager().netModeSelection();
	}
}
