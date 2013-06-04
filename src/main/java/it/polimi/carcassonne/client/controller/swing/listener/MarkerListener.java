package it.polimi.carcassonne.client.controller.swing.listener;

import static it.polimi.carcassonne.server.model.Settings.TRANSPARENT;
import it.polimi.carcassonne.client.view.swing.SwingViewManager;
import it.polimi.carcassonne.server.model.Utils;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

/**
 * 
 * This class manage the mouse event (enter, click and exit) on the markers
 * button changing the color of the button in the current player turn color
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class MarkerListener implements ActionListener, MouseListener {

	private CardinalPoint cardinal;

	/**
	 * Initialize a marker listener
	 * 
	 * @param cardinal
	 */
	public MarkerListener(CardinalPoint cardinal) {
		this.cardinal = cardinal;
	}

	/**
	 * lunch on a new thread the notification to the controller.
	 */
	@Override
	public void actionPerformed(ActionEvent arg) {
		if (!SwingViewManager.getManager().getMatchController()
				.isMarkerPlaced()) {
			MarkerPlacer p = new MarkerPlacer(cardinal);
			Thread t = new Thread(p);
			t.start();
		}
	}

	/**
	 * set the button background to the current player color
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		JButton button = (JButton) e.getSource();
		button.setOpaque(true);
		button.setBackground(Utils.getPlayerAlphaColor(SwingViewManager
				.getManager().getMatchController().getTurnColor()));
	}

	/**
	 * set a transparent color to the button
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		JButton button = (JButton) e.getSource();
		button.setBackground(TRANSPARENT);
		button.setOpaque(false);
	}

	/**
	 * set a transparent color to the button
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		JButton button = (JButton) e.getSource();
		button.setBackground(TRANSPARENT);
		button.setOpaque(false);
	}

	// NOT USED

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}

class MarkerPlacer implements Runnable {
	private CardinalPoint cardinal;

	MarkerPlacer(CardinalPoint cardinal) {
		this.cardinal = cardinal;
	}

	@Override
	public void run() {
		SwingViewManager.getManager().getMatchController()
				.placeMarker(cardinal);
	}
}
