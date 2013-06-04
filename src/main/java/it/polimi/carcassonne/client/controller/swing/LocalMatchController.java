package it.polimi.carcassonne.client.controller.swing;

import it.polimi.carcassonne.client.view.swing.SwingViewManager;
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
import java.util.List;

import javax.swing.JOptionPane;

/**
 * a match controller for local match
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class LocalMatchController extends CommonController {

	private static final String ATTENTION = "Attenzione !!!";

	/**
	 * Start a new match for the specified numbres of player
	 * 
	 * @param numPlayers
	 */
	public LocalMatchController(int numPlayers) {
		match = new Match(Utils.generateMatchName());
		match.addObserver(SwingViewManager.getManager());
		try {
			match.start();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(SwingViewManager.getManager()
					.getCarcassonneFrame(),
					"Impossibile aprire il file tessere: " + e.getMessage(),
					"Errore irreversibile", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		SwingViewManager.getManager().getHistoryPanel()
				.message("Il nome della partita e' " + match.getName());
		SwingViewManager.getManager().getHistoryPanel()
				.message("Inizia la partita");
		match.addPlayers(numPlayers);
	}

	@Override
	public void startMatch() {
		nextTurn();
	}

	@Override
	public boolean isMyTurn() {
		return true;
	}

	/**
	 * 
	 * @return the Player object playing in the curret turn
	 */
	public Player getTurnPlayer() {
		return match.getCurrentPlayer();
	}

	@Override
	public Tile getLastDrawedTile() {
		return match.getLastDrawedTile();
	}

	@Override
	public List<Cell> getPossibleMoves() {
		return match.helpPlacing();
	}

	@Override
	public int getCurrentPlayerMarker() {
		return match.getCurrentPlayerAvailableMarkers();
	}

	@Override
	public PlayersColor getTurnColor() {
		return match.getCurrentPlayer().getColor();
	}

	@Override
	public void rotateLastDrawedTile() {
		match.rotateLastDrawedTile();
		SwingViewManager.getManager().getHistoryPanel()
				.log("ha ruotato la sua tessera");
	}

	@Override
	public void placeTile(Coordinate coord) {
		try {
			setPlaceCondition(false);
			setTilePlaced(true);
			Coordinate absolute = getAbsolute(coord);
			match.placeLastDrawedTile(absolute);
			SwingViewManager
					.getManager()
					.getHistoryPanel()
					.log("ha posizionato una tessera in " + coord.getX() + ","
							+ coord.getY());
		} catch (InvalidCellException e) {
			setPlaceCondition(true);
			JOptionPane.showMessageDialog(SwingViewManager.getManager()
					.getCarcassonneFrame(), "Mossa non consentita", ATTENTION,
					JOptionPane.WARNING_MESSAGE);
		} catch (IncompatibleFieldsException e) {
			setPlaceCondition(true);
			JOptionPane.showMessageDialog(SwingViewManager.getManager()
					.getCarcassonneFrame(),
					"La cella non e' compatibile con i suoi vicini", ATTENTION,
					JOptionPane.WARNING_MESSAGE);
		}
	}

	@Override
	public void passTurn() {
		SwingViewManager.getManager().getSideBar().disableRotate(false);
		SwingViewManager.getManager().getSideBar().disableNextTurn(true);
		List<BuildingInfo> closedBuildings = match.checkClosedBuildings(match
				.getLastInseredCell());
		for (BuildingInfo b : closedBuildings) {
			SwingViewManager.getManager().getHistoryPanel()
					.log("ha chiuso una " + b.getField());
		}
		if (match.deckIsEmpty()) {
			setEndGame();
			// aggiunge punteggi per elementi incompleti
			match.checkIncompleteBuidings();
			match.checkVictoryCondition();
		}
		nextTurn();
	}

	@Override
	public void placeMarker(CardinalPoint cardinal) {
		try {
			match.setMarker(match.getCurrentPlayer(), cardinal);
			SwingViewManager.getManager().getHistoryPanel()
					.log("ha posizionato segnalino a " + cardinal.toString());
			setMarkerPlaced(true);
			passTurn();
		} catch (InvalidMarkerPositionException e) {
			// superfluo, grazie a GUI errore impossibile
			setMarkerCondition(true);
			JOptionPane.showMessageDialog(SwingViewManager.getManager()
					.getCarcassonneFrame(), "Mossa non consentita", ATTENTION,
					JOptionPane.WARNING_MESSAGE);
		} catch (OccupiedFieldException e) {
			setMarkerCondition(true);
			JOptionPane.showMessageDialog(SwingViewManager.getManager()
					.getCarcassonneFrame(), e.getField() + " gia' presidiata",
					ATTENTION, JOptionPane.WARNING_MESSAGE);
		}
	}

	private void nextTurn() {
		setTilePlaced(false);
		setMarkerPlaced(false);
		match.nextTurn();
		SwingViewManager.getManager().getTurnPanel()
				.update(match.getCurrentPlayer().getColor());
		SwingViewManager.getManager().getHistoryPanel()
				.setPlayerColor(match.getCurrentPlayer().getColor());
		SwingViewManager.getManager().getSideBar()
				.setDeckSize(match.getDeckSize());
	}

	// NOT USED

	@Override
	public void updateTileMarker(Tile tile, Coordinate coord) {
	}

	@Override
	public void updateTile(Tile tile, Coordinate coord) {
	}

	@Override
	public void notifyMoveNotValid() {
	}

	@Override
	public void notifyEndMessage() {
	}

	@Override
	public void updateLocalScores(List<String> score) {
	}

	@Override
	public void updateLocalScores(String scores) {
	}

	@Override
	public void disconnect() {
	}
}