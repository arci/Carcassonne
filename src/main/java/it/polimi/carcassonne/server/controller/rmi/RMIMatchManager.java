package it.polimi.carcassonne.server.controller.rmi;

import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.PlayersColor;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;
import it.polimi.carcassonne.server.model.exceptions.InvalidMarkerPositionException;
import it.polimi.carcassonne.server.model.exceptions.OccupiedFieldException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface showing the method for managing a match using RMi
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public interface RMIMatchManager extends Remote {

	/**
	 * notify to all clients the start information: match name, first tile,
	 * client color and number of players
	 * 
	 * @throws RemoteException
	 */
	void notifyStart() throws RemoteException;

	/**
	 * notify to all clients the turn color
	 * 
	 * @param playerColor
	 * @throws RemoteException
	 */
	void notifyTurnColor(PlayersColor playerColor) throws RemoteException;

	/**
	 * notify to all clients the last drawed tile
	 * 
	 * @param lastDrawedTile
	 * @throws RemoteException
	 */
	void notifyDrawedTile(String lastDrawedTile) throws RemoteException;

	/**
	 * rotate the last drawed tile and notify the rotation to all clients
	 * 
	 * @throws RemoteException
	 */
	void rotateLastDrawedTile() throws RemoteException;

	/**
	 * check if in the grid exist some buildings closed, assign locally the
	 * points and notify it to the clients, after that change the game turn
	 * 
	 * @throws RemoteException
	 */
	void passTurn() throws RemoteException;

	/**
	 * try to place the last drawed tile in the grid and notify the result to
	 * the clients
	 * 
	 * @param coord
	 * @throws RemoteException
	 * @throws InvalidCellException
	 */
	void placeTile(String coord) throws RemoteException, InvalidCellException;

	/**
	 * try to place on the last drawed tile a marker and notify the result to
	 * the clients
	 * 
	 * @param cardinal
	 * @throws RemoteException
	 * @throws InvalidMarkerPositionException
	 * @throws OccupiedFieldException
	 */
	void placeMarker(CardinalPoint cardinal) throws RemoteException,
			InvalidMarkerPositionException, OccupiedFieldException;

	/**
	 * notify to all clients a server shutdown
	 */
	void shutdown() throws RemoteException;
}
