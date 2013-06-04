package it.polimi.carcassonne.client.controller.swing.listener;

import it.polimi.carcassonne.client.view.swing.SwingViewManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listen for the click on the rotate button and, on action performed, inform
 * the controller to rotate the last drawed tile
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class RotateListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		SwingViewManager.getManager().getMatchController()
				.rotateLastDrawedTile();
	}
}
