package it.polimi.carcassonne.client.view.swing.frame;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * The entire form of the Swing graphics
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class CarcassonneFrame extends JFrame {

	private static final long serialVersionUID = -8008672912487313986L;

	/**
	 * Create a new form for the application
	 * 
	 * @param title
	 *            of the form
	 * @param height
	 *            of the form
	 * @param width
	 *            of the form
	 */
	public CarcassonneFrame(String title, int height, int width) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(width, height));
		this.setResizable(false);
	}
}
