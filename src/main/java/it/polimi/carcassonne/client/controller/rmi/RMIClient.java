package it.polimi.carcassonne.client.controller.rmi;

import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.PlayersColor;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;
import it.polimi.carcassonne.server.model.exceptions.InvalidMarkerPositionException;
import it.polimi.carcassonne.server.model.exceptions.OccupiedFieldException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface that define the client method for the RMI connection mode
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public interface RMIClient extends Remote {

	/**
	 * called by the server to notify the match start
	 * 
	 * @param matchName
	 * @param firstTile
	 * @param color
	 * @param numPlayers
	 * @throws RemoteException
	 */
	void notifyMatchStart(String matchName, String firstTile,
			PlayersColor color, int numPlayers) throws RemoteException;

	/**
	 * called by the server to notify the last drawed tile
	 * 
	 * @param lastDrawedTile
	 * @throws RemoteException
	 */
	void notifyDrawedTile(String lastDrawedTile) throws RemoteException;

	/**
	 * called by the server to notify the turn color
	 * 
	 * @param playersColor
	 * @throws RemoteException
	 */
	void notifyTurnColor(PlayersColor playersColor) throws RemoteException;

	/**
	 * ask to the server to rotate the last drawed tile
	 * 
	 * @throws RemoteException
	 */
	void rotateLastDrawedTile() throws RemoteException;

	/**
	 * ask to the server to pass turn
	 * 
	 * @throws RemoteException
	 */
	void passTurn() throws RemoteException;

	/**
	 * ask to the server to place the last drawed tile in the specified position
	 * 
	 * @throws RemoteException
	 *             ,InvalidCellException
	 */
	void placeTile(Coordinate coord) throws RemoteException,
			InvalidCellException;

	/**
	 * called by the server to notify a tile placeing in the grid
	 * 
	 * @param tile
	 * @param coord
	 * @throws RemoteException
	 */
	void notifyPlacing(String tile, String coord) throws RemoteException;

	/**
	 * ask to the server to place the last drawed tile in the specified position
	 * 
	 * @throws RemoteException
	 *             ,InvalidCellException
	 */
	void placeMarker(CardinalPoint cardinal) throws RemoteException,
			InvalidMarkerPositionException, OccupiedFieldException;

	/**
	 * called by the server to notify a marker placing in the last drawed tile
	 * 
	 * @param tile
	 * @param coord
	 * @throws RemoteException
	 */
	void notifyMarker(String tile, String coord) throws RemoteException;

	/**
	 * called by the server to notify a tile update
	 * 
	 * @param tile
	 * @param coord
	 * @throws RemoteException
	 */
	void notifyTileUpdate(String tile, String coord) throws RemoteException;

	/**
	 * called by the server to notify a score update
	 * 
	 * @param scores
	 * @throws RemoteException
	 */
	void notifyScore(String scores) throws RemoteException;

	/**
	 * called by the server to notify the end of the game
	 * 
	 * @throws RemoteException
	 */
	void notifyEndMessage() throws RemoteException;

	/**
	 * called by the server to notify a server shutdown
	 * 
	 * @throws RemoteException
	 */
	void notifyDisconnection() throws RemoteException;

}
