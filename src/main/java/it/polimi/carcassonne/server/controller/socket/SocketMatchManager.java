package it.polimi.carcassonne.server.controller.socket;

import it.polimi.carcassonne.client.view.text.Display;
import it.polimi.carcassonne.server.model.BuildingInfo;
import it.polimi.carcassonne.server.model.Cell;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Match;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.Utils;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import java.io.IOException;
import java.util.List;

/**
 * The match manager used to handle a match over the socket protocol
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class SocketMatchManager extends Thread {
	private Match match;
	private boolean tilePositioningPhase;
	private boolean markerPositioningPhase;

	/**
	 * Given some players, creates a match with them in it
	 * 
	 * @param players
	 *            which will play the match generated
	 */
	public SocketMatchManager(List<Player> players) {
		// va creato un nuovo array di player in quanto quello originario verra'
		// cancellato, di conseguenza verrebbero tolti anche dalla partita
		match = new Match(Utils.generateMatchName(), this);
		for (Player p : players) {
			match.addPlayer(p);
		}
		assignColors();
		assignMatch();
		tilePositioningPhase = true;
		markerPositioningPhase = false;
	}

	/**
	 * Starts a match in asynchronous mode
	 */
	public void run() {
		Display.printMessage("Match " + match.getName() + " started! :D");
		try {
			match.start();
		} catch (IOException e) {
			// non trovo carcassonne.txt
			Display.printError(e.getMessage());
		}
		notifyStart();
		nextTurn();
	}

	/**
	 * Determine if the current phase of the game is the tile positioning one
	 * 
	 * @return <code>true</code> if the player can only place a tile,
	 *         <code>false</false> otherwise
	 */
	public boolean isTilePositioningPhase() {
		return tilePositioningPhase;
	}

	/**
	 * Determine if the current phase of the game is the marker positioning one
	 * 
	 * @return <code>true</code> if the player can only place a marker or pass
	 *         the turn, <code>false</false> otherwise
	 */
	public boolean isMarkerPositioningPhase() {
		return markerPositioningPhase;
	}

	private void nextTurn() {
		tilePositioningPhase = true;
		markerPositioningPhase = false;
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
			match.checkVictoryCondition();
			notifyWinners();
			return;
		}
		Display.printMessage("cambio turno"); // DEBUG
		match.nextTurn();
		notifyTurnColor(match.getCurrentPlayer().getColor());
		notifyDrawedTile(match.getLastDrawedTile().getString());
	}

	/**
	 * Send a message to the players of the match communicating them their
	 * respective color, the first tile drawed, the match name and the number of
	 * players in the match
	 */
	private void notifyStart() {
		for (Player p : match.getPlayers()) {
			p.sendMessage(ServerCmdGenerator.start(p,
					match.getLastDrawedTile(), match.getName(), match
							.getPlayers().size()));
		}
	}

	/**
	 * Send a message to the players of the match communicating them that is
	 * just started another turn
	 * 
	 * @param color
	 *            of the turn just began
	 */
	private void notifyTurnColor(PlayersColor color) {
		broadcast(ServerCmdGenerator.turnOf(color));
	}

	/**
	 * Send a message to the players of the match communicating them that a new
	 * tile was drawed
	 * 
	 * @param tileDescription
	 *            the string that describes the tile
	 */
	private void notifyDrawedTile(String tileDescription) {
		broadcast(ServerCmdGenerator.nextTile(tileDescription));
	}

	/**
	 * Send a message to the players of the match communicating them that the
	 * player that is playing the current turn decided to turn the tile
	 * 
	 * @param tileDescription
	 *            the string that describes the rotated tile
	 */
	public void notifyRotatedTile(String tileDescription) {
		broadcast(ServerCmdGenerator.rotated(tileDescription));
	}

	/**
	 * Send a message to the players of the match communicating them that a tile
	 * was placed by the player that is playing the current turn
	 * 
	 * @param coordinates
	 *            where to put the last drawed tile
	 */
	public void notifyPlacedTile(Coordinate coordinates) {
		tilePositioningPhase = false;
		markerPositioningPhase = true;
		broadcast(ServerCmdGenerator.update(coordinates,
				match.getLastDrawedTile()));
	}

	/**
	 * Send a message to the players of the match communicating them that a tile
	 * was updated, respect to the previous one
	 * 
	 * @param tile
	 *            updated
	 * @param coordinate
	 *            where to put the updated tile
	 */
	private void notifyUpdate(Tile tile, Coordinate coordinate) {
		broadcast(ServerCmdGenerator.update(coordinate, tile));
	}

	/**
	 * Send a message to the players of the match communicating them that a
	 * marker was placed by the player that is playing the current turn
	 */
	public void notifyMarker() {
		broadcast(ServerCmdGenerator.update(match.getGrid()
				.getLastInsertedCell().getCoordinates(),
				match.getLastDrawedTile()));
		nextTurn();
	}

	/**
	 * Send a message to the players of the match communicating them that the
	 * player that is playing the current turn decided to pass the turn without
	 * placing any marker
	 */
	public void notifyPassTurn() {
		nextTurn();
	}

	/**
	 * Send a message to the players of the match communicating them the updated
	 * scores of all the players
	 */
	private void notifyScore() {
		Display.printMessage("notifico score update"); // DEBUG
		broadcast(ServerCmdGenerator.score(createScores()));
	}

	/**
	 * Send a message to the players of the match communicating them that the
	 * match is over, and send them the updated scores with also the incomplete
	 * buildings points
	 */
	private void notifyWinners() {
		Display.printMessage("notifico fine match"); // DEBUG
		broadcast(ServerCmdGenerator.end(createScores()));
		closeSockets();
	}

	/**
	 * Send a message to the players of the match communicating them that the
	 * current player has done an invalid move
	 */
	public void notifyMoveNotValid() {
		broadcast(ServerCmdGenerator.moveNotValid());
	}

	private void broadcast(String message) {
		for (Player p : match.getPlayers()) {
			p.sendMessage(message);
		}
	}

	private void closeSockets() {
		for (int i = 0; i < match.getPlayers().size(); i++) {
			match.getPlayers().get(i).disconnect();
		}
	}

	private void assignColors() {
		for (int i = 0; i < match.getPlayers().size(); i++) {
			match.getPlayers().get(i).setColor(PlayersColor.getColorByID(i));
		}
	}

	private void assignMatch() {
		for (int i = 0; i < match.getPlayers().size(); i++) {
			match.getPlayers().get(i).setMatch(match);
		}
	}

	private String createScores() {
		StringBuffer scores = new StringBuffer();
		for (Player p : match.getPlayers()) {
			PlayersColor color = p.getColor();
			scores.append(color.toString() + "="
					+ match.getPlayerByColor(color).getScore() + ", ");
		}
		// elimino ultima virgola e spazio
		scores.delete(scores.length() - 2, scores.length());
		return scores.toString();
	}
}
