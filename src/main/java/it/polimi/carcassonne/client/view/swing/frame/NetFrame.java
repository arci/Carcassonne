package it.polimi.carcassonne.client.view.swing.frame;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * The frame with the net mode selection
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class NetFrame extends JFrame {

	private static final long serialVersionUID = -2760051868507054771L;

	/**
	 * build, set and show the empty frame
	 * 
	 * @param title
	 * @param height
	 * @param width
	 */
	public NetFrame(String title, int height, int width) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(width, height));
		this.setResizable(false);
	}
}
