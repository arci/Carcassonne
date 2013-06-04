package it.polimi.carcassonne.client.view.swing.frame;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * The frame with the number of players selection
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class PlayersFrame extends JFrame {

	private static final long serialVersionUID = 1581205722287437485L;

	/**
	 * build, set and show the empty frame
	 * 
	 * @param title
	 * @param height
	 * @param width
	 */
	public PlayersFrame(String title, int height, int width) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(width, height));
		this.setResizable(false);
	}
}
