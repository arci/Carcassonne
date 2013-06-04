package it.polimi.carcassonne.server.controller.rmi;

import static it.polimi.carcassonne.server.model.Settings.LOCALHOST;
import static it.polimi.carcassonne.server.model.Settings.MANAGER_RMI_PORT;
import it.polimi.carcassonne.client.controller.rmi.RMIClient;
import it.polimi.carcassonne.client.view.text.Display;
import it.polimi.carcassonne.server.model.BuildingInfo;
import it.polimi.carcassonne.server.model.Cell;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Match;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.Utils;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.PlayersColor;
import it.polimi.carcassonne.server.model.exceptions.IncompatibleFieldsException;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;
import it.polimi.carcassonne.server.model.exceptions.InvalidMarkerPositionException;
import it.polimi.carcassonne.server.model.exceptions.OccupiedFieldException;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of the RMIMatchManager interface
 * 
 * @see RMIMatchManager
 * @author Arcidiacono, Cesana
 * 
 */
public class RMIMatchManagerImpl extends Thread implements RMIMatchManager,
		Serializable {

	private static final long serialVersionUID = 6455982014193348814L;
	private Map<RMIClient, PlayersColor> clients = new HashMap<RMIClient, PlayersColor>();;
	private Match match = null;

	/**
	 * Set locally a copy of the clients that are participating to the match
	 * 
	 * @param clients
	 *            , the clients of this match
	 */
	public RMIMatchManagerImpl(Map<RMIClient, PlayersColor> clients) {
		this.clients.putAll(clients);
	}

	@Override
	public void run() {
		try {
			RMIMatchManager managerStub = (RMIMatchManager) UnicastRemoteObject
					.exportObject(this, 0);
			Registry registry = LocateRegistry.getRegistry(LOCALHOST,
					MANAGER_RMI_PORT);
			registry.rebind("MatchManager", managerStub);
			Display.printMessage("RMI MatchManager ready, stub server is registred on port: "
					+ MANAGER_RMI_PORT); // DEBUG
			startMatch();
		} catch (Exception e) {
			Display.printError("RMI Server exception: " + e.toString());
		}
	}

	private void startMatch() {
		match = new Match(Utils.generateMatchName());
		Display.printMessage("Nuova partita istanziata: " + match.getName()); // DEBUG

		Set<RMIClient> cl = clients.keySet();
		Iterator<RMIClient> iter = cl.iterator();
		while (iter.hasNext()) {
			RMIClient client = iter.next();
			PlayersColor color = clients.get(client);
			match.addPlayer(new Player(color));
		}
		try {
			match.start();
			notifyStart();
			nextTurn();
		} catch (IOException e1) {
			try {
				shutdown(); // manca carcassonne.txt
			} catch (RemoteException e2) {
				Display.printError(e2.getMessage());
			}
		}
	}

	@Override
	public void notifyStart() throws RemoteException {
		// notifico a tutti i client l'inizio della partita
		Set<RMIClient> cl = clients.keySet();
		Iterator<RMIClient> iter = cl.iterator();
		while (iter.hasNext()) {
			RMIClient client = iter.next();
			PlayersColor color = clients.get(client);
			Display.printMessage(match.getName()
					+ ": notifico inizio a client: " + color); // DEBUG
			try {
				client.notifyMatchStart(match.getName(), match
						.getLastDrawedTile().getString(), color, clients.size());
			} catch (RemoteException e) {
				Display.printError(e.getMessage());
			}
		}

		/*
		 * Elimino questo controller dal registry per impedire injecton durante
		 * un match
		 */
		try {
			Registry registry = LocateRegistry.getRegistry(LOCALHOST,
					MANAGER_RMI_PORT);
			registry.unbind("MatchManager");
			Display.printMessage("MatchManager removed from registry");
		} catch (NotBoundException e) {
			Display.printError(e.getMessage());
		}
	}

	private void nextTurn() {
		Display.printMessage(match.getName() + ": cambio turno"); // DEBUG
		match.nextTurn();
		try {
			notifyDrawedTile(match.getLastDrawedTile().getString());
			notifyTurnColor(match.getCurrentPlayer().getColor());
		} catch (RemoteException e) {
			Display.printError(e.getMessage());
		}
	}

	@Override
	public void passTurn() {
		// controllo chiusure e notifico tessere da modificare
		List<BuildingInfo> closedBuildings = match.checkClosedBuildings(match
				.getLastInseredCell());
		for (BuildingInfo b : closedBuildings) {
			for (Cell c : b.getCellsWithMarkerPassed()) {
				notifyUpdate(c.getTile(), c.getCoordinates());
			}
		}
		notifyScore();
		if (match.deckIsEmpty()) {
			match.checkIncompleteBuidings();
			notifyScore();
			notifyWinners();
		}
		nextTurn();
	}

	private void notifyScore() {
		// notifico a tutti i client score update
		Set<RMIClient> cl = clients.keySet();
		Iterator<RMIClient> iter = cl.iterator();
		StringBuffer scores = new StringBuffer();
		while (iter.hasNext()) {
			RMIClient client = iter.next();
			PlayersColor color = clients.get(client);
			scores.append(color.toString() + "="
					+ match.getPlayerByColor(color).getScore() + ", ");
		}
		// elimino ultima virgola e spazio
		scores.delete(scores.length() - 2, scores.length());
		iter = cl.iterator();
		while (iter.hasNext()) {
			RMIClient client = iter.next();
			PlayersColor color = clients.get(client);
			Display.printMessage(match.getName()
					+ ": notifico score update a : " + color); // DEBUG
			try {
				client.notifyScore(scores.toString());
			} catch (RemoteException e) {
				Display.printError(e.getMessage());
			}
		}

	}

	private void notifyUpdate(Tile tile, Coordinate coord) {
		// notifico a tutti i client update di una tessera
		Set<RMIClient> cl = clients.keySet();
		Iterator<RMIClient> iter = cl.iterator();
		while (iter.hasNext()) {
			RMIClient client = iter.next();
			PlayersColor color = clients.get(client);
			Display.printMessage(match.getName()
					+ ": notifico update tessera a : " + color); // DEBUG
			try {
				client.notifyTileUpdate(tile.getString(), coord.toString());
			} catch (RemoteException e) {
				Display.printError(e.getMessage());
			}
		}
	}

	@Override
	public void notifyDrawedTile(String lastDrawedTile) throws RemoteException {
		// notifico a tutti i client la carta pescata
		Set<RMIClient> cl = clients.keySet();
		Iterator<RMIClient> iter = cl.iterator();
		while (iter.hasNext()) {
			RMIClient client = iter.next();
			PlayersColor color = clients.get(client);
			Display.printMessage(match.getName()
					+ ": notifico carta pescata a client: " + color); // DEBUG
			try {
				client.notifyDrawedTile(lastDrawedTile);
			} catch (RemoteException e) {
				Display.printError(e.getMessage());
			}
		}
	}

	@Override
	public void notifyTurnColor(PlayersColor playerColor) {
		// notifico a tutti i client il colore del turno corrente
		Set<RMIClient> cl = clients.keySet();
		Iterator<RMIClient> iter = cl.iterator();
		while (iter.hasNext()) {
			RMIClient client = iter.next();
			PlayersColor color = clients.get(client);
			Display.printMessage(match.getName()
					+ ": notifico colore turno a client: " + color); // DEBUG
			try {
				client.notifyTurnColor(playerColor);
			} catch (RemoteException e) {
				Display.printError(e.getMessage());
			}
		}
	}

	@Override
	public void rotateLastDrawedTile() throws RemoteException {
		Display.printMessage(match.getName() + ": ruoto la carta pescata"); // DEBUG
		match.rotateLastDrawedTile();
		notifyDrawedTile(match.getLastDrawedTile().getString());
	}

	@Override
	public void placeTile(String coord) throws RemoteException,
			InvalidCellException {
		Coordinate coordinates = null;
		try {
			Display.printMessage(match.getName()
					+ ": provo a posizionare la carta"); // DEBUG
			coordinates = new Coordinate(coord);
			match.placeLastDrawedTile(coordinates);
			notifyPlacing(coordinates);
		} catch (IncompatibleFieldsException e) {
			Display.printMessage(match.getName() + ": posizione non valida"); // DEBUG
			throw new InvalidCellException(e);
		}
	}

	private void notifyPlacing(Coordinate coord) {
		// notifico a tutti i client di posizionare
		Set<RMIClient> cl = clients.keySet();
		Iterator<RMIClient> iter = cl.iterator();
		while (iter.hasNext()) {
			RMIClient client = iter.next();
			PlayersColor color = clients.get(client);
			Display.printMessage(match.getName()
					+ ": notifico posizionamento a client: " + color); // DEBUG
			try {
				client.notifyPlacing(match.getLastDrawedTile().getString(),
						coord.toString());
			} catch (RemoteException e) {
				Display.printError(e.getMessage());
			}
		}
	}

	@Override
	public void placeMarker(CardinalPoint cardinal)
			throws InvalidMarkerPositionException, OccupiedFieldException {
		Display.printMessage(match.getName()
				+ ": provo a posizionare il segnalino"); // DEBUG
		try {
			match.setMarker(match.getCurrentPlayer(), cardinal);
			notifyMarker();
		} catch (InvalidMarkerPositionException e) {
			Display.printMessage(match.getName() + ": posizione non valida"); // DEBUG
			throw new InvalidMarkerPositionException(e);
		} catch (OccupiedFieldException e) {
			Display.printMessage(match.getName() + ": posizione non valida"); // DEBUG
			throw new OccupiedFieldException(match.getLastDrawedTile()
					.getField(cardinal));
		}
	}

	private void notifyMarker() {
		// notifico a tutti i client di posizionare
		Set<RMIClient> cl = clients.keySet();
		Iterator<RMIClient> iter = cl.iterator();
		while (iter.hasNext()) {
			RMIClient client = iter.next();
			PlayersColor color = clients.get(client);
			Display.printMessage(match.getName()
					+ ": notifico posizionamento a client: " + color); // DEBUG
			try {
				client.notifyMarker(match.getLastDrawedTile().getString(),
						match.getGrid().getLastInsertedCell().getCoordinates()
								.toString());
			} catch (RemoteException e) {
				Display.printError(e.getMessage());
			}
		}
		passTurn();
	}

	private void notifyWinners() {
		// notifico a tutti i client il/i vincitore/i
		Set<RMIClient> cl = clients.keySet();
		Iterator<RMIClient> iter = cl.iterator();
		while (iter.hasNext()) {
			RMIClient client = iter.next();
			PlayersColor color = clients.get(client);
			Display.printMessage(match.getName()
					+ ": notifico vincitori a client: " + color); // DEBUG
			try {
				client.notifyEndMessage();
			} catch (RemoteException e) {
				Display.printError(e.getMessage());
			}
		}
		// Elimino questo controller dal registry
		try {
			Registry registry = null;
			registry = LocateRegistry.getRegistry(LOCALHOST, MANAGER_RMI_PORT);
			registry.unbind("MatchManager");
			Display.printMessage("MatchManager removed from registry");
		} catch (NotBoundException e) {
			Display.printError(e.getMessage());
		} catch (AccessException e) {
			Display.printError(e.getMessage());
		} catch (RemoteException e) {
			Display.printError(e.getMessage());
		}
	}

	@Override
	public void shutdown() throws RemoteException {
		// notifico a tutti i client chiusura del server
		Set<RMIClient> cl = clients.keySet();
		Iterator<RMIClient> iter = cl.iterator();
		while (iter.hasNext()) {
			RMIClient client = iter.next();
			try {
				client.notifyDisconnection();
			} catch (RemoteException e) {
				Display.printError(e.getMessage());
			}
		}
		// termino il server
		System.exit(0);
	}
}
