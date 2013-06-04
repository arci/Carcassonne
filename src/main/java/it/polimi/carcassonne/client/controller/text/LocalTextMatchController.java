package it.polimi.carcassonne.client.controller.text;

import static it.polimi.carcassonne.server.model.enums.Commands.getCommandByStringASCII;
import static it.polimi.carcassonne.server.model.enums.Commands.getViewParamsByString;
import it.polimi.carcassonne.client.view.text.Display;
import it.polimi.carcassonne.client.view.text.TextViewManager;
import it.polimi.carcassonne.server.model.Cell;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Match;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.Utils;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.Commands;
import it.polimi.carcassonne.server.model.exceptions.IncompatibleFieldsException;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;
import it.polimi.carcassonne.server.model.exceptions.InvalidMarkerPositionException;
import it.polimi.carcassonne.server.model.exceptions.OccupiedFieldException;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Gestisce una partita locale istanziando la logica completa (server)
 * 
 * @author Cesana, Arcidiacono
 * 
 * 
 */
public class LocalTextMatchController extends CommonText {

	private int numPlayers;

	/**
	 * initialize the controller
	 * 
	 * @param numPlayers
	 */
	public LocalTextMatchController(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	@Override
	public void startMatch() {
		match = new Match(Utils.generateMatchName());
		match.addObserver(TextViewManager.getManager());
		try {
			match.start();
		} catch (IOException e) {
			Display.printError("Impossibile aprire il file tessere: "
					+ e.getMessage());
		}
		match.addPlayers(numPlayers);
		nextTurn();
	}

	private void nextTurn() {
		match.nextTurn();

		// se la carta non e' posizionabile ne pesco un'altra
		askUserCommand();

		// controllo chiusure e assegno punteggi
		match.checkClosedBuildings(match.getLastInseredCell());

		if (match.deckIsEmpty()) {
			// aggiunge punteggi per elementi incompleti
			match.checkIncompleteBuidings();
			match.checkVictoryCondition();
		} else {
			nextTurn();
		}
	}

	private void askUserCommand() {
		Scanner in = new Scanner(System.in);
		boolean tilePlaced = false;
		boolean endTurn = false;
		do {
			Display.printTilePositioningCommand();
			String command = in.nextLine();
			tilePlaced = executeTilePositioning(command);
		} while (!tilePlaced);
		// se il giocatore ha segnalini puo' posizionare un segnalino
		if (match.getCurrentPlayerAvailableMarkers() > 0) {
			do {
				Display.printMarkerPositioningCommand();
				String command = in.nextLine();
				endTurn = executeMarkerPositioning(command);
			} while (!endTurn);
		} else {
			Display.printWarning("Nessun segnalino disponibile");
		}
	}

	private boolean executeTilePositioning(Commands userCmd, String param) {
		switch (userCmd) {
		case ROTATE:
			match.rotateLastDrawedTile();
			return false;
		case PLACE:
			try {
				match.placeLastDrawedTile(new Coordinate(param));
			} catch (InvalidCellException e) {
				Display.printWarning("Coordinate non valide");
				return false;
			} catch (IncompatibleFieldsException e) {
				Display.printWarning("La carta non e' compatibile con i suoi vicini");
				return false;
			}
			return true;
		default:
			Display.printWarning("Comando non riconosciuto");
			return false;
		}
	}

	// Overload
	private boolean executeTilePositioning(String command) {
		String param = getViewParamsByString(command);
		if (command.equals(Commands.PLACE.toString())) {
			try {
				param = match.validateCoordinates(param);
			} catch (InvalidCellException e) {
				Display.printWarning("Coordinate non valide");
				return false;
			}
		}
		return executeTilePositioning(getCommandByStringASCII(command), param);
	}

	private boolean executeMarkerPositioning(Commands userCmd, String param) {
		switch (userCmd) {
		case MARKER:
			try {
				match.setMarker(match.getCurrentPlayer(),
						CardinalPoint.get(param));
			} catch (InvalidMarkerPositionException e) {
				Display.printWarning("La posizione specificata non e' valida");
				return false;
			} catch (OccupiedFieldException e) {
				Display.printWarning("La " + e.getField()
						+ " e' gia' presidiata da un'altro giocatore");
				return false;
			}
			return true;
		case PASS:
			return true;
		default:
			Display.printWarning("Comando non riconosciuto");
			return false;
		}
	}

	// Overload
	private boolean executeMarkerPositioning(String command) {
		String param = getViewParamsByString(command);
		if (command.equals(Commands.MARKER.toString())) {
			try {
				param = match.validateCardinals(param);
			} catch (InvalidMarkerPositionException e) {
				Display.printWarning("Posizione non valida");
				return false;
			}
		}
		return executeMarkerPositioning(getCommandByStringASCII(command), param);
	}

	// NOT USED

	@Override
	public void updateTileMarker(Tile tile, Coordinate coord) {
	}

	@Override
	public void placeTile(Coordinate coord) {
	}

	@Override
	public void placeMarker(CardinalPoint cardinal) {
	}

	@Override
	public void updateTile(Tile tile, Coordinate coord) {
	}

	@Override
	public void rotateLastDrawedTile() {
	}

	@Override
	public List<Cell> getPossibleMoves() {
		return null;
	}

	@Override
	public Tile getLastDrawedTile() {
		return null;
	}

	@Override
	public void notifyMoveNotValid() {
	}

	@Override
	public void disconnect() {
	}
}
