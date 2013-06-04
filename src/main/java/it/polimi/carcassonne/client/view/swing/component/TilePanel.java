package it.polimi.carcassonne.client.view.swing.component;

import static it.polimi.carcassonne.server.model.Settings.DEGREE_180;
import static it.polimi.carcassonne.server.model.Settings.DEGREE_270;
import static it.polimi.carcassonne.server.model.Settings.DEGREE_90;
import static it.polimi.carcassonne.server.model.Settings.TILE_SIZE;
import it.polimi.carcassonne.resources.Resources;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Create and offer method to update a panel that contains the image of the tile
 * in the background
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class TilePanel extends JPanel {

	private static final long serialVersionUID = -9059486993450478573L;

	private Image background;
	private int degree;
	private int index;
	private URL path;

	/**
	 * Draw a JPanel witch the image of the tile in background
	 * 
	 * @param index
	 * @param degree
	 */
	public TilePanel(int index, int degree) {
		this.degree = degree;
		this.index = index;
		update();
	}

	/**
	 * Draw a JPanel witch the image of the tile in background
	 * 
	 */
	public TilePanel() {
		degree = 0;
		background = null;
		setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
	}

	private void update() {
		if (degree == 0) {
			path = Resources.class.getResource("img/tiles/"+index+".png");
		} else if (degree == DEGREE_90) {
			path = Resources.class.getResource("img/tiles/"+index+"-3.png");
		} else if (degree == DEGREE_180) {
			path = Resources.class.getResource("img/tiles/"+index+"-2.png");
		} else if (degree == DEGREE_270) {
			path = Resources.class.getResource("img/tiles/"+index+"-1.png");
		}
		background = new ImageIcon(this.path).getImage();
		setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
	}

	/**
	 * Update the background of the panel
	 */
	public void update(int index, int degree) {
		this.degree = degree;
		this.index = index;
		update();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null);
	}

	// @Override
	// public void paint(Graphics g) {
	// Graphics2D g2d = (Graphics2D) g;
	// AffineTransform tx = new AffineTransform();
	//
	// double radians = Math.toRadians(degree);
	// double scalex = 1;
	// double scaley = 1;
	// double anchorx = (double) TILE_SIZE / 2;
	// double anchory = (double) TILE_SIZE / 2;
	//
	// tx.rotate(radians, anchorx, anchory);
	// tx.scale(scalex, scaley);
	//
	// g2d.setTransform(tx);
	//
	// g2d.drawImage(background, tx, this);
	// g2d.finalize();
	// }
}