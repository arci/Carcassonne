package it.polimi.carcassonne.client.controller.text;

import static it.polimi.carcassonne.server.model.enums.Commands.getViewParamsByString;
import it.polimi.carcassonne.client.controller.swing.CommonController;
import it.polimi.carcassonne.client.view.text.Display;
import it.polimi.carcassonne.client.view.text.TextViewManager;
import it.polimi.carcassonne.server.model.Match;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.Commands;
import it.polimi.carcassonne.server.model.enums.PlayersColor;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;
import it.polimi.carcassonne.server.model.exceptions.InvalidMarkerPositionException;

import java.util.Scanner;

/**
 * Abstract class that implements the MatchController interface, extending the
 * CommonController abstract class and overriding the interface methods to
 * implement a common controller for the text view
 * 
 * @see CommonController
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public abstract class CommonText extends CommonController {
	private boolean endTurn = false;

	@Override
	public void createMatch(String matchName, Tile firstTile,
			PlayersColor color, int numPlayers) {
		match = new Match(matchName);
		match.addObserver(TextViewManager.getManager());
		// aggiungo in locale i giocatori di questa partita
		for (int i = 0; i < numPlayers; i++) {
			match.addPlayer(new Player(PlayersColor.getColorByID(i)));
		}
		// salvo un riferimento al colore assegnatomi
		setMyColor(color);
		match.start(firstTile);
	}

	@Override
	public void nextTurn(PlayersColor playerColor) {
		setTurnColor(playerColor);
		endTurn = false;
		setTilePlaced(false);
	}

	@Override
	public void setLastDrawedTile(Tile tile) {
		match.setLastDrawedTile(tile);
	}

	/**
	 * show and start to manage user command if and only if it's the client turn
	 */
	public void showCommand() {
		if (isMyTurn()) {
			askUserCommand();
		}
		return;
	}

	private void askUserCommand() {
		Scanner in = new Scanner(System.in);
		if (!isTilePlaced()) {
			Display.printTilePositioningCommand();
			String command = in.nextLine();
			setTilePlaced(executeTilePositioning(command));
		} else {
			if (match.getCurrentPlayerAvailableMarkers() > 0) {
				if (!endTurn) {
					Display.printMarkerPositioningCommand();
					String command = in.nextLine();
					endTurn = executeMarkerPositioning(command);
				} else {
					return;
				}
			} else {
				Display.printWarning("Nessun segnalino disponibile");
			}
		}
		match.notifyChange();
		showCommand();
	}

	/**
	 * effectively execute the command sending on the net the message This
	 * method is only a place marker, MUST be implemented in the class that
	 * extend this one because of the different type of message to send in the
	 * net
	 * 
	 * @param command
	 * @param param
	 * @return false
	 */
	public boolean executeTilePositioning(String command, String param) {
		// implemented in extender
		return false;
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
		return executeTilePositioning(command, param);
	}

	// Overload
	private boolean executeMarkerPositioning(String command) {
		String param = getViewParamsByString(command);
		String socketCommand = Commands.getCommandByStringASCII(command)
				.toString();
		if (command.equals(Commands.MARKER.toString())
				|| socketCommand.equals(Commands.MARKER.toString())) {
			try {
				param = match.validateCardinals(param);
			} catch (InvalidMarkerPositionException e) {
				Display.printWarning("Posizione non valida");
				return false;
			}
		}
		return executeMarkerPositioning(command, param);
	}

	/**
	 * effectively execute the command sending on the net the message This
	 * method is only a place marker, MUST be implemented in the class that
	 * extend this one because of the different type of message to send in the
	 * net
	 * 
	 * @param command
	 * @param param
	 * @return false
	 */
	public boolean executeMarkerPositioning(String command, String param) {
		// implemented in extender
		return false;
	}

	/**
	 * set the local variable endTurn to true
	 */
	public void setEndTurn() {
		endTurn = true;
	}

	@Override
	public void updateLocalMarker() {
		// need to overwrite the this method because used only for update the
		// swing interface
	}
}
