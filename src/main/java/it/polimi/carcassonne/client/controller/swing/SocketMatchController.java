package it.polimi.carcassonne.client.controller.swing;

import it.polimi.carcassonne.client.controller.socket.ClientCmdGenerator;
import it.polimi.carcassonne.client.controller.socket.SocketClient;
import it.polimi.carcassonne.server.model.Cell;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.Commands;

import java.util.List;

import javax.swing.JOptionPane;

/**
 * match controller for socket net mode
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class SocketMatchController extends CommonController {

	private SocketClient client = null;

	/**
	 * initialize a new socket controller
	 */
	public SocketMatchController() {
		match = null;
		client = new SocketClient(this);
		setTurnColor(null);
	}

	@Override
	public void disconnect() {
		client.disconnect();
	}

	@Override
	public void startMatch() {
		super.startMatch();
		client.start();
	}

	@Override
	public void rotateLastDrawedTile() {
		client.sendMessage(ClientCmdGenerator.rotate());
	}

	@Override
	public void placeTile(Coordinate coord) {
		Coordinate absolute = getAbsolute(coord);
		client.sendMessage(ClientCmdGenerator.placeTile(absolute));
		setPlaceCondition(false);
	}

	@Override
	public void notifyMoveNotValid() {
		if (!isTilePlaced()) {
			setPlaceCondition(true);
		} else {
			setMarkerCondition(true);
		}
		if (isMyTurn()) {
			JOptionPane.showMessageDialog(null,
					Commands.MOVE_NOT_VALID.toString());
		}
	}

	@Override
	public void placeMarker(CardinalPoint cardinal) {
		client.sendMessage(ClientCmdGenerator.placeMarker(cardinal));
	}

	@Override
	public void passTurn() {
		super.passTurn();
		client.sendMessage(ClientCmdGenerator.pass());
	}

	// NOT USED

	@Override
	public void updateTileMarker(Tile tile, Coordinate coord) {
	}

	@Override
	public void updateTile(Tile tile, Coordinate coord) {
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
	public void updateLocalScores(String scores) {
	}
}
