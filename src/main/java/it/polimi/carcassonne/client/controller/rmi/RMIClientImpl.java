package it.polimi.carcassonne.client.controller.rmi;

import static it.polimi.carcassonne.server.model.Settings.LOCALHOST;
import static it.polimi.carcassonne.server.model.Settings.MANAGER_RMI_PORT;
import static it.polimi.carcassonne.server.model.Settings.SERVER_RMI_PORT;
import it.polimi.carcassonne.client.controller.MatchController;
import it.polimi.carcassonne.client.view.text.Display;
import it.polimi.carcassonne.server.controller.rmi.RMIMatchManager;
import it.polimi.carcassonne.server.controller.rmi.RMIServer;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.PlayersColor;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;
import it.polimi.carcassonne.server.model.exceptions.InvalidMarkerPositionException;
import it.polimi.carcassonne.server.model.exceptions.OccupiedFieldException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

/**
 * An implementation of the RMIClient interface
 * 
 * @see RMIClient
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class RMIClientImpl implements RMIClient {
	private RMIMatchManager manager = null;
	private MatchController controller;

	/**
	 * set locally a ref. to the match controller to call to notify net events
	 * 
	 * @param matchController
	 */
	public RMIClientImpl(MatchController matchController) {
		controller = matchController;
	}

	/**
	 * Try to connect to the server and register the client on the server
	 */
	public void connect() {
		try {
			UnicastRemoteObject.exportObject(this, 0);
			Registry registry = LocateRegistry.getRegistry(LOCALHOST,
					SERVER_RMI_PORT);
			RMIServer server = (RMIServer) registry.lookup("CarcassonneServer");
			server.register(this);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null,
					"Remote exception: " + e.toString());
			System.exit(0);
		} catch (NotBoundException e) {
			JOptionPane.showMessageDialog(null,
					"Registry exception: " + e.toString());
			System.exit(0);
		}
	}

	@Override
	public void notifyMatchStart(String matchName, String firstTile,
			PlayersColor myColor, int numPlayers) {
		try {
			Registry registry = LocateRegistry.getRegistry(LOCALHOST,
					MANAGER_RMI_PORT);
			manager = (RMIMatchManager) registry.lookup("MatchManager");
		} catch (Exception e) {
			JOptionPane
					.showMessageDialog(null,
							"Errore irreversibile nella creazione della partita sul server");
			System.exit(0);
		}

		controller.createMatch(matchName, new Tile(firstTile), myColor,
				numPlayers);

	}

	@Override
	public void notifyDrawedTile(String lastDrawedTile) {
		controller.setLastDrawedTile(new Tile(lastDrawedTile));
	}

	@Override
	public void notifyTurnColor(PlayersColor playersColor)
			throws RemoteException {
		controller.nextTurn(playersColor);
	}

	@Override
	public void rotateLastDrawedTile() {
		try {
			manager.rotateLastDrawedTile();
		} catch (RemoteException e) {
			Display.printError(e.getMessage());
		}
	}

	@Override
	public void placeTile(Coordinate coord) throws InvalidCellException {
		try {
			manager.placeTile(coord.toString());
		} catch (RemoteException e) {
			Display.printError(e.getMessage());
		}
	}

	@Override
	public void notifyPlacing(String tile, String coord) throws RemoteException {
		try {
			controller.placeDrawedTileInLocalGrid(new Tile(tile),
					new Coordinate(coord));
		} catch (Exception e) {
			Display.printError(e.getMessage());
		}
	}

	@Override
	public void placeMarker(CardinalPoint cardinal)
			throws InvalidMarkerPositionException, OccupiedFieldException {
		try {
			manager.placeMarker(cardinal);
		} catch (RemoteException e) {
			Display.printError(e.getMessage());
		}
	}

	@Override
	public void notifyMarker(String tile, String coord) throws RemoteException {
		try {
			controller.updateTileMarker(controller.parseMarker(tile),
					new Coordinate(coord));
			controller.updateLocalMarker();
		} catch (InvalidCellException e) {
			Display.printError(e.getMessage());
		}
	}

	@Override
	public void notifyTileUpdate(String tile, String coord) {
		try {
			controller.updateTile(new Tile(tile), new Coordinate(coord));
		} catch (InvalidCellException e) {
			Display.printError(e.getMessage());
		}
	}

	@Override
	public void passTurn() {
		try {
			manager.passTurn();
		} catch (RemoteException e) {
			Display.printError(e.getMessage());
		}
	}

	@Override
	public void notifyScore(String scores) throws RemoteException {
		controller.updateLocalScores(scores);
	}

	@Override
	public void notifyEndMessage() throws RemoteException {
		controller.setEndGame();
		controller.notifyEndMessage();
	}

	@Override
	public void notifyDisconnection() throws RemoteException {
		JOptionPane.showMessageDialog(null, "Disconnected");
	}
}
