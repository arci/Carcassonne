package it.polimi.carcassonne.client.controller.swing;

import it.polimi.carcassonne.client.controller.rmi.RMIClientImpl;
import it.polimi.carcassonne.client.view.swing.SwingViewManager;
import it.polimi.carcassonne.server.model.Cell;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;
import it.polimi.carcassonne.server.model.exceptions.InvalidMarkerPositionException;
import it.polimi.carcassonne.server.model.exceptions.OccupiedFieldException;

import java.util.List;

import javax.swing.JOptionPane;

/**
 * match controller for RMI net mode
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class RMIMatchController extends CommonController {

	private RMIClientImpl netManager;

	/**
	 * initialize a new RMI controller
	 */
	public RMIMatchController() {
		match = null;
		netManager = new RMIClientImpl(this);
		setTurnColor(null);
	}

	@Override
	public void startMatch() {
		super.startMatch();
		netManager.connect();
	}

	@Override
	public void rotateLastDrawedTile() {
		netManager.rotateLastDrawedTile();
	}

	@Override
	public void passTurn() {
		super.passTurn();
		SwingViewManager.getManager().getSideBar().disableRotate(false);
		SwingViewManager.getManager().getSideBar().disableNextTurn(true);
		netManager.passTurn();
	}

	@Override
	public void placeTile(Coordinate coord) {
		try {
			setPlaceCondition(false);
			Coordinate absolute = getAbsolute(coord);
			netManager.placeTile(absolute);
		} catch (InvalidCellException e) {
			setPlaceCondition(true);
			JOptionPane.showMessageDialog(SwingViewManager.getManager()
					.getCarcassonneFrame(), "Mossa non consentita",
					"Attenzione !!", JOptionPane.WARNING_MESSAGE);
		}
	}

	@Override
	public void placeMarker(CardinalPoint cardinal) {
		try {
			setMarkerCondition(false);
			netManager.placeMarker(cardinal);
		} catch (InvalidMarkerPositionException e) {
			setMarkerCondition(true);
			JOptionPane.showMessageDialog(SwingViewManager.getManager()
					.getCarcassonneFrame(), "Mossa non consentita",
					"Attenzione !!", JOptionPane.WARNING_MESSAGE);
		} catch (OccupiedFieldException e) {
			setMarkerCondition(true);
			JOptionPane.showMessageDialog(SwingViewManager.getManager()
					.getCarcassonneFrame(), e.getField() + " gia' presidiata",
					"Attenzione !!", JOptionPane.WARNING_MESSAGE);
		}
	}

	@Override
	public void updateTileMarker(Tile tile, Coordinate coord) {
		match.update(tile, coord);
		setMarkerPlaced(true);
	}

	@Override
	public void updateTile(Tile tile, Coordinate coord) {
		match.update(tile, coord);
		for (Player p : match.getPlayers()) {
			SwingViewManager.getManager().getSideBar()
					.setMarker(p.getColor(), p.getAvailableMarkerCount());
		}
	}

	// NOT USED

	@Override
	public void notifyMoveNotValid() {
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
	public void updateLocalScores(List<String> score) {
	}

	@Override
	public void disconnect() {
	}
}
