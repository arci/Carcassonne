package it.polimi.carcassonne.server.controller.socket;

import static it.polimi.carcassonne.server.model.enums.Commands.END;
import static it.polimi.carcassonne.server.model.enums.Commands.MOVE_NOT_VALID;
import static it.polimi.carcassonne.server.model.enums.Commands.NEXT_TILE;
import static it.polimi.carcassonne.server.model.enums.Commands.ROTATED;
import static it.polimi.carcassonne.server.model.enums.Commands.SCORE;
import static it.polimi.carcassonne.server.model.enums.Commands.START;
import static it.polimi.carcassonne.server.model.enums.Commands.TURN_OF;
import static it.polimi.carcassonne.server.model.enums.Commands.UPDATE;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

/**
 * Used to create all the messages from the server to the client
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public final class ServerCmdGenerator {

	private ServerCmdGenerator() {
		// don't display my constructor
	}

	/**
	 * Create command to indicate a new match has began
	 * 
	 * @param player
	 *            color of the player the message will be sent
	 * @param tile
	 *            the first tile of the game (positioned in 0,0)
	 * @param name
	 *            of the match
	 * @param numPlayers
	 *            the number of players that will partecipate to the match
	 * @return the formatted START message
	 */
	public static String start(Player player, Tile tile, String name,
			int numPlayers) {
		return START + ":" + tile.getString() + "," + name + ","
				+ player.getColor().toString() + "," + numPlayers;
	}

	/**
	 * Create command to indicate a new turn has began
	 * 
	 * @param color
	 *            of the player that has to play the current turn
	 * @return the formatted TURN OF message
	 */
	public static String turnOf(PlayersColor color) {
		return TURN_OF + ":" + color.toString();
	}

	/**
	 * Create command to indicate the tile drawed
	 * 
	 * @param tileDescription
	 *            the string that describes the tile
	 * @return the formatted TILE message
	 */
	public static String nextTile(String tileDescription) {
		return NEXT_TILE + ":" + tileDescription;
	}

	/**
	 * Create command to indicate that the player that is currently playing has
	 * rotated his tile
	 * 
	 * @param tileDescription
	 *            the string that describes the tile
	 * @return the formatted ROTATED message
	 */
	public static String rotated(String tileDescription) {
		return ROTATED + ":" + tileDescription;
	}

	/**
	 * Create command to indicate that a tile is been placed by someone (or a
	 * marker has been removed after a building has been closed)
	 * 
	 * @param coordinates
	 *            of the cell in which the tile will be placed
	 * @param tile
	 *            to place
	 * @return the formatted UPDATE message
	 */
	public static String update(Coordinate coordinates, Tile tile) {
		return UPDATE + ":" + tile.getString() + "," + coordinates.toString();
	}

	/**
	 * Create command to say at the client the updated scores of all the players
	 * 
	 * @param scores
	 *            of the players
	 * @return the formatted SCORE message
	 */
	public static String score(String scores) {
		return SCORE + ":" + scores;
	}

	/**
	 * Create command to indicate the match is finished
	 * 
	 * @param scores
	 *            the scores of the players after have calculated the partial
	 *            points of the incomplete buildings
	 * @return the formatted END message
	 */
	public static String end(String scores) {
		return END + ":" + scores;
	}

	/**
	 * Create command to indicate the move done is not valid
	 * 
	 * @return the formatted MOVE NOT VALID message
	 */
	public static String moveNotValid() {
		return MOVE_NOT_VALID.toString();
	}
}
