package it.polimi.carcassonne.server.model;

import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

/**
 * A marker can be placed on a tile to obtain point closing a building or taking
 * partial points at the end of the game
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class Marker {

	private CardinalPoint position;
	private Player owner;

	/**
	 * Create a new marker given a player (the <i>owner</i>)
	 * 
	 * @param owner
	 *            of the marker
	 */
	public Marker(Player owner) {
		this.owner = owner;
		position = null;
	}

	/**
	 * Set the cardinal point of the tile where the marker will be placed
	 * 
	 * @param position
	 *            of the tile where the marker will be placed
	 */
	public void setPosition(CardinalPoint position) {
		this.position = position;
	}

	/**
	 * Clear the position of the marker
	 */
	public void clearPosition() {
		setPosition(null);
	}

	/**
	 * 
	 * @return marker's position on the tile
	 */
	public CardinalPoint getPosition() {
		return position;
	}

	/**
	 * 
	 * @return the owner of the marker
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * 
	 * @return the marker's owner color
	 */
	public PlayersColor getColor() {
		return getOwner().getColor();
	}
}