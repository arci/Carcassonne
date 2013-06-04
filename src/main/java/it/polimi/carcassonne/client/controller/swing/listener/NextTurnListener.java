package it.polimi.carcassonne.client.controller.swing.listener;

import it.polimi.carcassonne.client.view.swing.SwingViewManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Manage a click on the next turn button
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class NextTurnListener implements ActionListener {

	/**
	 * lunch on a separated thread the notification of the button pressing to
	 * the controller, and also log in the history pane the pass turn action
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		TurnChanger c = new TurnChanger();
		Thread t = new Thread(c);
		t.start();
	}
}

class TurnChanger implements Runnable {

	@Override
	public void run() {
		SwingViewManager.getManager().getMatchController().passTurn();
	}
}