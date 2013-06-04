package it.polimi.carcassonne.client.controller.swing.listener;

import it.polimi.carcassonne.client.view.swing.SwingViewManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Manage the user click on the button "connect" and call the controller method
 * in witch the connection will be performed
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class ConnectListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		SwingViewManager.getManager().getMatchController().startMatch();
	}
}
