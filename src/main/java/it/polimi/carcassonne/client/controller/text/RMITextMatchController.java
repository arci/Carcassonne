package it.polimi.carcassonne.client.controller.text;

import static it.polimi.carcassonne.server.model.enums.Commands.getCommandByStringASCII;
import it.polimi.carcassonne.client.controller.rmi.RMIClientImpl;
import it.polimi.carcassonne.client.view.text.Display;
import it.polimi.carcassonne.server.model.Cell;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.Commands;
import it.polimi.carcassonne.server.model.enums.PlayersColor;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;
import it.polimi.carcassonne.server.model.exceptions.InvalidMarkerPositionException;
import it.polimi.carcassonne.server.model.exceptions.OccupiedFieldException;

import java.util.List;

/**
 * Class used to instantiate a match controller for the ASCII view used with RMI
 * protocol
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class RMITextMatchController extends CommonText implements Runnable {

	private RMIClientImpl netManager;

	/**
	 * create a new RMI-ASCII match controller
	 */
	public RMITextMatchController() {
		match = null;
		netManager = new RMIClientImpl(this);
		setTurnColor(null);
	}

	@Override
	public void startMatch() {
		netManager.connect();
	}

	@Override
	public void run() {
		super.showCommand();
	}

	@Override
	public void nextTurn(PlayersColor playerColor) {
		super.nextTurn(playerColor);
		match.nextTurnWithoutDrawingTile();
		// faccio partire la gestione dei comandi su un'altro thread
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public boolean executeTilePositioning(String command, String param) {
		Commands userCmd = getCommandByStringASCII(command);
		switch (userCmd) {
		case ROTATE:
			netManager.rotateLastDrawedTile();
			match.notifyChange();
			return false;
		case PLACE:
			try {
				netManager.placeTile(new Coordinate(param));
			} catch (InvalidCellException e1) {
				Display.printWarning("Coordinate non valide");
				return false;
			}
			return true;
		default:
			// se il comando non e' riconosciuto userCmd = UNKNOWN
			Display.printWarning("Comando non riconosciuto");
			return false;
		}

	}

	@Override
	public void rotateLastDrawedTile() {
		netManager.rotateLastDrawedTile();
	}

	@Override
	public void placeTile(Coordinate coord) {
		try {
			netManager.placeTile(coord);
		} catch (InvalidCellException e) {
			Display.printWarning("Posizione non valida");
		}
	}

	@Override
	public boolean executeMarkerPositioning(String command, String param) {
		Commands userCmd = getCommandByStringASCII(command);
		switch (userCmd) {
		case MARKER:
			try {
				netManager.placeMarker(CardinalPoint.get(param));
			} catch (InvalidMarkerPositionException e1) {
				Display.printWarning("La posizione specificata non e' valida");
				return false;
			} catch (OccupiedFieldException e) {
				Display.printWarning(e.getField() + " gia' occupata");
				return false;
			}
			return true;
		case PASS:
			netManager.passTurn();
			return true;
		default:
			Display.printWarning("Comando non riconosciuto");
			return false;
		}
	}

	@Override
	public void placeMarker(CardinalPoint cardinal) {
		try {
			netManager.placeMarker(cardinal);
			passTurn();
		} catch (InvalidMarkerPositionException e) {
			Display.printWarning("La posizione specificata non e' valida");
		} catch (OccupiedFieldException e) {
			Display.printWarning(e.getField() + " gia' occupata");
		}
	}

	@Override
	public void updateTileMarker(Tile tile, Coordinate coord) {
		match.update(tile, coord);
		setEndTurn();
	}

	@Override
	public void setLastDrawedTile(Tile tile) {
		match.setLastDrawedTile(tile);
		match.notifyChange();
	}

	@Override
	public void updateTile(Tile tile, Coordinate coord) {
		match.update(tile, coord);
	}

	@Override
	public void passTurn() {
		netManager.passTurn();
	}

	// NOT USED

	@Override
	public void notifyEndMessage() {
	}

	@Override
	public void notifyMoveNotValid() {
	}

	@Override
	public Tile getLastDrawedTile() {
		return null;
	}

	@Override
	public void updateLocalScores(List<String> score) {
	}

	@Override
	public List<Cell> getPossibleMoves() {
		return null;
	}

	@Override
	public void disconnect() {
	}
}
