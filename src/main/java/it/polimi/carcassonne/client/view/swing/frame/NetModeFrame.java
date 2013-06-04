package it.polimi.carcassonne.client.view.swing.frame;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * The frame with the net connection method selection
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class NetModeFrame extends JFrame {

	private static final long serialVersionUID = 3419343449521750341L;

	/**
	 * build, set and show the empty frame
	 * 
	 * @param title
	 * @param height
	 * @param width
	 */
	public NetModeFrame(String title, int height, int width) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(width, height));
		this.setResizable(false);
	}
}
