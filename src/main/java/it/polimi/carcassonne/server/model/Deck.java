package it.polimi.carcassonne.server.model;

import it.polimi.carcassonne.resources.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains the list of the tiles
 * 
 * @author Cesana, Lorenzo
 * 
 */
public class Deck {

	private List<Tile> deck;
	private Tile lastDrawedTile;

	/**
	 * Create an empty new deck
	 */
	public Deck() {
		deck = new ArrayList<Tile>();
		lastDrawedTile = null;
	}

	/**
	 * Adds the passed <b>tile</b> to the deck
	 * 
	 * @param tile
	 *            to add to the deck
	 */
	public void addTile(Tile tile) {
		deck.add(tile);
	}

	/**
	 * 
	 * @return the number of remaining tiles in the deck
	 */
	public int getSize() {
		return deck.size();
	}

	/**
	 * Draw a tile from the top of the deck and removes it from it
	 * 
	 * @return <b>drawed tile</b>
	 */
	public Tile drawTile() {
		if (!deck.isEmpty()) {
			lastDrawedTile = ((ArrayList<Tile>) deck).get(0);
			deck.remove(lastDrawedTile);
		}
		return lastDrawedTile;
	}

	/**
	 * 
	 * @return <b>Last drawed tile</b>
	 */
	public Tile getLastDrawedTile() {
		return lastDrawedTile;
	}

	/**
	 * Shuffles the deck
	 */
	public void shuffle() {
		Collections.shuffle((List<?>) deck);
	}

	/**
	 * Populates the deck from the attached resource file containing the tiles
	 * 
	 * @throws IOException
	 *             if the resource file does not exist
	 */
	public void populate() throws IOException {
		URL file = Resources.class.getResource("text/carcassonne.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.openStream()));
		try {
			String strLine;
			while ((strLine = reader.readLine()) != null) {
				addTile(new Tile(strLine));
			}
		} finally {
			reader.close();
		}
	}

	/**
	 * 
	 * @return <i>true</i> if the deck contains no more tiles, <i>false</i>
	 *         otherwise
	 */
	public boolean isEmpty() {
		return deck.isEmpty();
	}

	public void setLastDrawedTile(Tile tile) {
		this.lastDrawedTile = tile;
	}
}
