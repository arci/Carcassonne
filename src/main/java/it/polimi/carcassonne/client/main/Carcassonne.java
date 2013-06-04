package it.polimi.carcassonne.client.main;

import it.polimi.carcassonne.client.view.swing.ViewManager;

/**
 * * Main class that starts the game
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public final class Carcassonne {

	private Carcassonne() {
		// don't display my constructor
	}

	/**
	 * Starts the game
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		ViewManager.getManager().graphicSelection();
	}
}