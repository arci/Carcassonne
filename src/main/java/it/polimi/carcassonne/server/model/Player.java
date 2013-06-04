package it.polimi.carcassonne.server.model;

import static it.polimi.carcassonne.server.model.Settings.MAX_MARKERS;
import it.polimi.carcassonne.server.controller.socket.SocketHandler;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import java.net.Socket;
import java.util.ArrayDeque;

/**
 * The player is a person who play with Carcassonne. It has <b>markers</b>,
 * <b>score</b> and a <b>color</b>
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class Player {

	private PlayersColor playerColor;
	private int score;
	// usata come stack, ma da documentazione Java e' piu' performante
	// l'ArrayDeque
	private ArrayDeque<Marker> markers = new ArrayDeque<Marker>();
	private SocketHandler socketHandler = null;
	// diventa true quando l'utente invia il comando CONNECT
	private boolean readyToPlay = false;
	private Match match = null;

	/**
	 * Creates a new player, given a color
	 * 
	 * @param playersColor
	 *            to assign at the player just created
	 */
	public Player(PlayersColor playersColor) {
		this.playerColor = playersColor;
		score = 0;
		initializeMarkers();
	}

	/**
	 * Create a player given his socket
	 * 
	 * @param socket
	 *            of the player
	 */
	public Player(Socket socket) {
		socketHandler = new SocketHandler(socket, this);
		socketHandler.startListener();
		score = 0;
		initializeMarkers();
	}

	/**
	 * Send a message via socket to the player
	 * 
	 * @param message
	 *            to send
	 */
	public void sendMessage(String message) {
		socketHandler.sendMessage(message);
	}

	/**
	 * close the socket connection
	 */
	public void disconnect() {
		socketHandler.closeSocket();
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	/**
	 * Check if a player has sent the "connect" command to the server (socket
	 * mode)
	 * 
	 * @return <code>true</code> if the player has sent the "connect" command to
	 *         the server, <code>false</code> otherwise
	 */
	public boolean isReadyToPlay() {
		return readyToPlay;
	}

	/**
	 * the player has sent the "connect" command to the server, set him as ready
	 * to play
	 */
	public void setReadyToPlay() {
		readyToPlay = true;
	}

	private void initializeMarkers() {
		for (int i = 0; i < MAX_MARKERS; i++) {
			markers.push(new Marker(this));
		}
	}

	/**
	 * 
	 * @param playersColor
	 *            to assign at the player
	 */
	public void setColor(PlayersColor playersColor) {
		this.playerColor = playersColor;
	}

	/**
	 * 
	 * @return the color of the player
	 */
	public PlayersColor getColor() {
		return playerColor;
	}

	/**
	 * 
	 * @return the actual score of the player
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Increases the score of a player by the value passed
	 * 
	 * @param increment
	 *            of the score
	 */
	public void increaseScore(int increment) {
		score = getScore() + increment;
	}

	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * Add the passed marker to the player (used to return markers when a
	 * building has been closed)
	 */

	public void addMarker() {
		markers.add(new Marker(this));
	}

	/**
	 * Remove a marker from the list of available ones
	 * 
	 * @return the marker just removed
	 */
	public Marker getMarker() {
		if (!markers.isEmpty()) {
			return markers.pop();
		}
		return null;

	}

	/**
	 * 
	 * @return an integer that represents the number of available markers of the
	 *         player
	 */
	public int getAvailableMarkerCount() {
		return markers.size();
	}
}