package it.polimi.carcassonne.client.view.swing.frame;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * The frame with the graphic mode selection
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class GraphicFrame extends JFrame {

	private static final long serialVersionUID = 4781084875061293987L;

	/**
	 * build, set and show the empty frame
	 * 
	 * @param title
	 * @param height
	 * @param width
	 */
	public GraphicFrame(String title, int height, int width) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(width, height));
		this.setResizable(false);
	}
}
