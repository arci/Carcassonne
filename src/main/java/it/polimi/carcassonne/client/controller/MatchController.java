package it.polimi.carcassonne.client.controller;

import it.polimi.carcassonne.server.model.Cell;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import java.util.List;

/**
 * Interface that define the method to implement if you want to write a new
 * match controller
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public interface MatchController {

	/**
	 * try network connection
	 */
	void startMatch();

	/**
	 * create a new match after receiving the first message from the net
	 * 
	 * @param matchName
	 * @param firstTile
	 * @param color
	 * @param numPlayers
	 */
	void createMatch(String matchName, Tile firstTile, PlayersColor color,
			int numPlayers);

	/**
	 * set local changes to the model after a net message of turn change
	 * 
	 * @param playerColor
	 */
	void nextTurn(PlayersColor playerColor);

	/**
	 * 
	 * @return true if it's client turn
	 */
	boolean isMyTurn();

	/**
	 * set locally the last drawed tile, after receiving a net message
	 * 
	 * @param tile
	 */
	void setLastDrawedTile(Tile tile);

	/**
	 * 
	 * @return the color of the player of the current turn
	 */
	PlayersColor getTurnColor();

	/**
	 * set the current turn color to the specified color
	 * 
	 * @param playerColor
	 */
	void setTurnColor(PlayersColor playerColor);

	/**
	 * ask to the server to place the last drawed tile in the specified
	 * coordinates
	 * 
	 * @param coord
	 */
	void placeTile(Coordinate coord);

	/**
	 * place the last drawed tile in the local grid
	 * 
	 * @param tile
	 * @param coordinate
	 */
	void placeDrawedTileInLocalGrid(Tile tile, Coordinate coordinate);

	/**
	 * set the right enable/disable condition to the elements of the GUI
	 * 
	 * @param reset
	 *            , must be true if you want to reset the components state after
	 *            a move not valid error
	 */
	void setPlaceCondition(boolean reset);

	/**
	 * set the boolean state of the variable tilePlaced
	 * 
	 * @param b
	 */
	void setTilePlaced(boolean b);

	/**
	 * 
	 * @return true, if and only if the client have already placed a tile in the
	 *         current turn
	 */
	boolean isTilePlaced();

	/**
	 * 
	 * @return true, if and only if the client have already placed a marker in
	 *         the current turn
	 */
	boolean isMarkerPlaced();

	/**
	 * set the boolean state of the variable markerPlaced
	 * 
	 * @param b
	 */
	void setMarkerPlaced(boolean b);

	/**
	 * send a message to the server specifying that client want to pass his turn
	 */
	void passTurn();

	/**
	 * 
	 * @return the number of available marker of the current turn player
	 */
	int getCurrentPlayerMarker();

	/**
	 * send a message to the server asking for placeing a marker in the
	 * specified position in the last drawed tile
	 * 
	 * @param cardinal
	 */
	void placeMarker(CardinalPoint cardinal);

	/**
	 * given a tile description, return a Tile object with marker, if the tile
	 * description contains a marker indication, without marker otherwise. This
	 * method also pop a marker from the correspondent player in the match
	 * 
	 * @param tileDescription
	 * @return the tile parsed
	 */
	Tile parseMarker(String tileDescription);

	/**
	 * update a tile removing/adding a marker
	 * 
	 * @param tile
	 * @param coord
	 */
	void updateTileMarker(Tile tile, Coordinate coord);

	/**
	 * set the right enable/disable condition to the elements of the GUI
	 * 
	 * @param reset
	 *            , must be true if you want to reset the components state after
	 *            a move not valid error
	 */
	void setMarkerCondition(boolean reset);

	/**
	 * update the cell in the local grid, in the specified coordinates, with the
	 * tile passed
	 * 
	 * @param tile
	 * @param coord
	 */
	void updateTile(Tile tile, Coordinate coord);

	/**
	 * update all local players score after receiving a net message
	 * 
	 * @param scores
	 */
	void updateLocalScores(String scores);

	/**
	 * used for socket purpose
	 * 
	 * @param score
	 */
	void updateLocalScores(List<String> score);

	/**
	 * manage component state and notify to the user a move not valid, after
	 * reciving the error from the server
	 */
	void notifyMoveNotValid();

	/**
	 * 
	 * @return true, if and only if the game is ended
	 */
	boolean isGameEnded();

	/**
	 * set the value of the boolean variable gameEnd to true
	 */
	void setEndGame();

	/**
	 * ask to the server to rotate the last drawed tile
	 */
	void rotateLastDrawedTile();

	/**
	 * this method in unusable in the net mode, only local game
	 * 
	 * @return an array list of enabled cell in witch is possible to place the
	 *         last drawed tile
	 */
	List<Cell> getPossibleMoves();

	/**
	 * 
	 * @return the last drawed tile
	 */
	Tile getLastDrawedTile();

	/**
	 * set clients color, to call after receiving the first message of the game
	 * in witch is specified client color
	 * 
	 * @param myColor
	 */
	void setMyColor(PlayersColor myColor);

	/**
	 * after receiving the end message, check locally the victory condition and
	 * show the winners of the match
	 */
	void notifyEndMessage();

	/**
	 * Update the available marker indication in the side bar for each player
	 */
	void updateLocalMarker();

	/**
	 * Disconnects from the server
	 */
	void disconnect();
}
