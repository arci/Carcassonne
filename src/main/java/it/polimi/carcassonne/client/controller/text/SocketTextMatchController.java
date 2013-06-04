package it.polimi.carcassonne.client.controller.text;

import static it.polimi.carcassonne.server.model.enums.Commands.getCommandByStringASCII;
import it.polimi.carcassonne.client.controller.socket.ClientCmdGenerator;
import it.polimi.carcassonne.client.controller.socket.SocketClient;
import it.polimi.carcassonne.client.view.text.Display;
import it.polimi.carcassonne.server.model.Cell;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.Commands;
import it.polimi.carcassonne.server.model.enums.PlayersColor;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;

import java.util.List;

/**
 * Class used to instantiate a match controller for the ASCII view used with
 * socket protocol
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class SocketTextMatchController extends CommonText {
	private SocketClient client = null;

	/**
	 * create a new socket-ASCII match controller
	 */
	public SocketTextMatchController() {
		match = null;
		client = new SocketClient(this);
		setTurnColor(null);
	}

	@Override
	public void startMatch() {
		client.start();
	}

	@Override
	public void nextTurn(PlayersColor playerColor) {
		super.nextTurn(playerColor);
		match.nextTurnWithoutDrawingTile();
	}

	@Override
	public void setLastDrawedTile(Tile tile) {
		super.setLastDrawedTile(tile);
		match.notifyChange();
		super.showCommand();
	}

	@Override
	public boolean executeTilePositioning(String command, String param) {
		Commands userCmd = getCommandByStringASCII(command);
		if (userCmd != null) {
			switch (userCmd) {
			case ROTATE:
				rotateLastDrawedTile();
				return false;
			case PLACE:
				try {
					placeTile(new Coordinate(param));
				} catch (InvalidCellException e1) {
					Display.printWarning("Coordinate non valide");
					return false;
				}
				return true;
			}
		}
		// se il comando non e' riconosciuto userCmd = null
		Display.printWarning("Comando non riconosciuto");
		return false;
	}

	@Override
	public void rotateLastDrawedTile() {
		client.sendMessage(ClientCmdGenerator.rotate());
	}

	@Override
	public void placeTile(Coordinate coordinate) {
		client.sendMessage(ClientCmdGenerator.placeTile(coordinate));
		match.notifyChange();
	}

	@Override
	public boolean executeMarkerPositioning(String command, String param) {
		Commands userCmd = getCommandByStringASCII(command + " " + param);
		switch (userCmd) {
		case MARKER:
			placeMarker(CardinalPoint.get(param));
			return true;
		case PASS:
			passTurn();
			return true;

		default:
			// se il comando non e' riconosciuto userCmd = UNKNOWN
			Display.printWarning("Comando non riconosciuto");
			return false;
		}
	}

	@Override
	public void placeMarker(CardinalPoint cardinal) {
		client.sendMessage(ClientCmdGenerator.placeMarker(cardinal));
	}

	@Override
	public void notifyMoveNotValid() {
		if (isMyTurn()) {
			showCommand();
		}
	}
	
	

	@Override
	public void passTurn() {
		client.sendMessage(ClientCmdGenerator.pass());
	}

	// NOT USED

	@Override
	public List<Cell> getPossibleMoves() {
		return null;
	}

	@Override
	public void updateTileMarker(Tile tile, Coordinate coord) {
	}

	@Override
	public void updateTile(Tile tile, Coordinate coord) {
	}

	@Override
	public void updateLocalScores(String scores) {
	}

	@Override
	public Tile getLastDrawedTile() {
		return null;
	}

	@Override
	public void notifyEndMessage() {
	}

	@Override
	public void disconnect() {
	}

}
