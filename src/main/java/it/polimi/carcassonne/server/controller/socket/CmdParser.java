package it.polimi.carcassonne.server.controller.socket;

import it.polimi.carcassonne.client.view.text.Display;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Match;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.Commands;
import it.polimi.carcassonne.server.model.exceptions.IncompatibleFieldsException;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;
import it.polimi.carcassonne.server.model.exceptions.InvalidMarkerPositionException;
import it.polimi.carcassonne.server.model.exceptions.OccupiedFieldException;

import java.util.List;

/**
 * Parse commands received with the socket and execute relative actions
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class CmdParser {

	private static final String INVALID_POSITION = "posizione non valida";
	private Player player = null;

	/**
	 * Create a command parser associating a match controller
	 * 
	 * @param player
	 *            owner of the socket that will receive the commands
	 */
	public CmdParser(Player player) {
		// serve per comando CONNECT
		this.player = player;
	}

	/**
	 * Given a command, decide what it indicates and execute it
	 * 
	 * @param command
	 *            to parse
	 */
	public void parseAndExecute(String command) {
		Commands cmd = Commands.getCommandByStringSocket(command);
		List<String> params = Commands.getParamsByString(command);

		if (cmd == Commands.CONNECT) {
			player.setReadyToPlay();
			return;
		}

		if (player.getMatch() != null
				&& player == player.getMatch().getCurrentPlayer()) {
			switch (cmd) {
			case CONNECT:
				// DoNothing(); //already connected if arrived here
				break;

			case ROTATE:
				rotateLastDrawedTile();
				break;

			case PLACE:
				placeTile(params.get(0) + "," + params.get(1));
				break;

			case MARKER:
				placeMarker(params.get(0));
				break;

			case PASS:
				passTurn();
				break;

			default: // case UNKNOWN
				// doNothing(); // command not recognized
			}
		}
	}

	private void moveNotValid() {
		player.getMatch().getMatchManager().notifyMoveNotValid();
	}

	private void rotateLastDrawedTile() {
		Match match = player.getMatch();

		if (match.getMatchManager().isTilePositioningPhase()) {
			Display.printMessage("ruoto la carta pescata"); // DEBUG
			match.rotateLastDrawedTile();
			match.getMatchManager().notifyRotatedTile(
					match.getLastDrawedTile().getString());
		} else {
			moveNotValid();
		}
	}

	private void placeTile(String coord) {
		Match match = player.getMatch();
		if (match.getMatchManager().isTilePositioningPhase()) {
			try {
				Display.printMessage("provo a posizionare la carta"); // DEBUG
				Coordinate coordinates = new Coordinate(coord);
				match.placeLastDrawedTile(coordinates);
				match.getMatchManager().notifyPlacedTile(coordinates);
			} catch (InvalidCellException e) {
				Display.printMessage(INVALID_POSITION); // DEBUG
				moveNotValid();
			} catch (IncompatibleFieldsException e) {
				Display.printMessage(INVALID_POSITION); // DEBUG
				moveNotValid();
			}
		} else {
			moveNotValid();
		}
	}

	private void placeMarker(String string) {
		Match match = player.getMatch();
		if (match.getMatchManager().isMarkerPositioningPhase()) {
			Display.printMessage("provo a posizionare il segnalino"); // DEBUG
			try {
				match.setMarker(match.getCurrentPlayer(),
						CardinalPoint.get(string));
				match.getMatchManager().notifyMarker();
			} catch (InvalidMarkerPositionException e) {
				Display.printMessage(INVALID_POSITION); // DEBUG
				moveNotValid();
			} catch (OccupiedFieldException e) {
				Display.printMessage(INVALID_POSITION); // DEBUG
				moveNotValid();
			}
		} else {
			moveNotValid();
		}
	}

	private void passTurn() {
		Match match = player.getMatch();
		if (match.getMatchManager().isMarkerPositioningPhase()) {
			match.getMatchManager().notifyPassTurn();
		} else {
			moveNotValid();
		}
	}
}