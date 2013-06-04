package it.polimi.carcassonne.client.controller.swing.listener;

import it.polimi.carcassonne.client.view.swing.SwingViewManager;
import it.polimi.carcassonne.server.model.Coordinate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Manage a click on a enabled cell
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class CellListener implements ActionListener {

	private int x;
	private int y;

	/**
	 * Initialize a cell listener
	 * 
	 * @param x
	 * @param y
	 */
	public CellListener(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * send to the controller a request of tile placing in the clicked cell
	 */
	@Override
	public void actionPerformed(ActionEvent arg) {
		if (!SwingViewManager.getManager().getMatchController().isTilePlaced()) {
			SwingViewManager.getManager().getMatchController()
					.placeTile(new Coordinate(x, y));
		}
	}

}
