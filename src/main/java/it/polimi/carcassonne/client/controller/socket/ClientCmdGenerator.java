package it.polimi.carcassonne.client.controller.socket;

import static it.polimi.carcassonne.server.model.enums.Commands.PLACE;
import static it.polimi.carcassonne.server.model.enums.Commands.ROTATE;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.Commands;

/**
 * Used to create all the messages from the client to the server
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public final class ClientCmdGenerator {

	private ClientCmdGenerator() {
		// don't display my constructor
	}

	/**
	 * Create command to indicate that the player want to rotate his tile
	 * 
	 * @return the formatted ROTATE message
	 */
	public static String rotate() {
		return ROTATE.toString();
	}

	/**
	 * Create command to indicate that the player want to place his tile
	 * 
	 * @param coordinate
	 *            where to place the tile
	 * @return the formatted PLACE message
	 */
	public static String placeTile(Coordinate coordinate) {
		return PLACE + ":" + coordinate.toString();
	}

	/**
	 * Create command to indicate that the player want to place a marker on the
	 * last inserted tile
	 * 
	 * @param cardinal
	 *            where to place the marker
	 * @return the formatted PLACE MARKER message
	 */
	public static String placeMarker(CardinalPoint cardinal) {
		return Commands.MARKER + ":" + cardinal.getAbbreviation();
	}

	/**
	 * Create command to indicate that the player want to pass his turn without
	 * placing any marker
	 * 
	 * @return the formatted PASS message
	 */
	public static String pass() {
		return Commands.PASS.toString();
	}
}
