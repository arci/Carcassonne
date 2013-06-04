package it.polimi.carcassonne.client.view.swing.component;

import static it.polimi.carcassonne.server.model.Settings.DEGREE_90;
import static it.polimi.carcassonne.server.model.Settings.MAX_ROTATIONS;
import static it.polimi.carcassonne.server.model.Settings.TILE_1;
import static it.polimi.carcassonne.server.model.Settings.TILE_10;
import static it.polimi.carcassonne.server.model.Settings.TILE_11;
import static it.polimi.carcassonne.server.model.Settings.TILE_12;
import static it.polimi.carcassonne.server.model.Settings.TILE_13;
import static it.polimi.carcassonne.server.model.Settings.TILE_14;
import static it.polimi.carcassonne.server.model.Settings.TILE_15;
import static it.polimi.carcassonne.server.model.Settings.TILE_16;
import static it.polimi.carcassonne.server.model.Settings.TILE_2;
import static it.polimi.carcassonne.server.model.Settings.TILE_3;
import static it.polimi.carcassonne.server.model.Settings.TILE_4;
import static it.polimi.carcassonne.server.model.Settings.TILE_5;
import static it.polimi.carcassonne.server.model.Settings.TILE_6;
import static it.polimi.carcassonne.server.model.Settings.TILE_7;
import static it.polimi.carcassonne.server.model.Settings.TILE_8;
import static it.polimi.carcassonne.server.model.Settings.TILE_9;
import it.polimi.carcassonne.server.model.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a tile search for the image path and rotation degree
 * 
 * @author Arcidiacono,Cesana
 * 
 */
public class TileSearcher {

	private static List<String> tilesString;
	private int path;
	private int degree;

	static {
		tilesString = new ArrayList<String>();
		tilesString.add(TILE_1);
		tilesString.add(TILE_2);
		tilesString.add(TILE_3);
		tilesString.add(TILE_4);
		tilesString.add(TILE_5);
		tilesString.add(TILE_6);
		tilesString.add(TILE_7);
		tilesString.add(TILE_8);
		tilesString.add(TILE_9);
		tilesString.add(TILE_10);
		tilesString.add(TILE_11);
		tilesString.add(TILE_12);
		tilesString.add(TILE_13);
		tilesString.add(TILE_14);
		tilesString.add(TILE_15);
		tilesString.add(TILE_16);
	}

	/**
	 * create the TileManager object and parse the tile to find his image and
	 * the degree of rotation
	 * 
	 * @param tile
	 */
	public TileSearcher(Tile tile) {
		setPathAndDegree(new Tile(tile.getString()));
	}

	/**
	 * Find path and degree of rotation of the image corresponding to the tile
	 * 
	 * @param tile
	 */
	public void setPathAndDegree(Tile tile) {
		Tile tileTemp = new Tile(tile.getString());
		degree = 0;
		boolean goOn = true;
		for (int j = 0; j < MAX_ROTATIONS && goOn; j++) {
			for (int i = 0; i < tilesString.size() && goOn; i++) {
				if (tileTemp.getString().equals(tilesString.get(i))) {
					// path = TILES_PATH + (i + 1) + ".png";
					path = i + 1;
					goOn = false;
				}
			}
			if (goOn) {
				tileTemp.rotate();
				degree += DEGREE_90;
			}
		}
	}

	public int getPath() {
		return path;
	}

	public int getDegree() {
		return degree;
	}
}
