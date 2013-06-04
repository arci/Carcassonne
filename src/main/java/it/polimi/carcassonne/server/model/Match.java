package it.polimi.carcassonne.server.model;

import static it.polimi.carcassonne.server.model.enums.PlayersColor.getColorByID;
import it.polimi.carcassonne.server.controller.socket.SocketMatchManager;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.PlayersColor;
import it.polimi.carcassonne.server.model.exceptions.IncompatibleFieldsException;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;
import it.polimi.carcassonne.server.model.exceptions.InvalidMarkerPositionException;
import it.polimi.carcassonne.server.model.exceptions.OccupiedFieldException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Contains all the necessary informations to play a game: <b>players</b>,
 * <b>deck</b>, <b>grid</b>, etc...
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class Match extends Observable {
	private String name;
	private List<Player> players;
	private Grid grid;
	private Deck deck;
	private int turn;
	private SocketMatchManager matchManager;

	/**
	 * Initialize a match (creating a deck and a grid)
	 * 
	 * @param matchName
	 *            the name of the match
	 * @throws IOException
	 *             if the resource deck file does not exist
	 */
	public Match(String matchName) {
		this.name = matchName;
		players = new ArrayList<Player>();
		// i colori partono da 0 e la prima istruzione e' nextTurn()
		turn = -1;
	}

	/**
	 * Initialize a match (creating a deck and a grid)
	 * 
	 * @param matchName
	 *            the name of the match
	 * @param matchManager
	 *            the matchManager for the sockets
	 * @throws IOException
	 *             if the resource deck file does not exist
	 */
	public Match(String matchName, SocketMatchManager matchManager) {
		this(matchName);
		this.matchManager = matchManager;
	}

	/**
	 * 
	 * @return the match manager of the current match
	 */
	public SocketMatchManager getMatchManager() {
		return matchManager;
	}

	/**
	 * Start a new <b>match</b>, creates a <b>deck</b>, a <b>game grid</b> and
	 * shuffles the deck
	 * 
	 * @throws IOException
	 *             if the file which contains the tiles is not found
	 */
	public void start() throws IOException {
		initializeDeck();
		inizializeGrid();
		deck.shuffle();
	}

	/**
	 * Starts a match in client/server mode
	 * 
	 * @param firstTile
	 *            to insert in position 0,0 in the grid
	 */
	public void start(Tile firstTile) {
		inizializeGrid(firstTile);
	}

	/**
	 * 
	 * @return the name of the match
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the grid of the match
	 */
	public Grid getGrid() {
		return grid;
	}

	/**
	 * 
	 * @return the list of the players of the match
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Add the specified numbers of players at the match
	 * 
	 * @param noPlayers
	 *            the number of players to add
	 */
	public void addPlayers(int noPlayers) {
		for (int i = 0; i <= noPlayers - 1; i++) {
			players.add(new Player(getColorByID(i)));
		}
	}

	/**
	 * Add a player to the match
	 * 
	 * @param player
	 *            to add to the match
	 */
	public void addPlayer(Player player) {
		players.add(player);
	}

	/**
	 * Proceed with the next turn
	 */
	public void nextTurn() {
		turn++;
		if (turn > players.size() - 1) {
			turn = 0;
		}
		drawTile();
		notifyChange();
	}

	/**
	 * Proceed with the next turn, without drawing a tile from the deck, used by
	 * the client
	 */
	public void nextTurnWithoutDrawingTile() {
		turn++;
		if (turn > players.size() - 1) {
			turn = 0;
		}
		notifyChange();
	}

	/**
	 * force the model to notify internal change to the observer
	 */
	public void notifyChange() {
		setChanged();
		notifyObservers(this);
	}

	/**
	 * 
	 * @return the winner(s) of the match
	 */
	public List<Player> checkVictoryCondition() {
		List<Player> winners = new ArrayList<Player>();
		int maxScore = 0;
		for (Player p : players) {
			if (p.getScore() == maxScore) {
				winners.add(p);
			} else if (p.getScore() > maxScore) {
				winners.clear();
				winners.add(p);
				maxScore = p.getScore();
			}
		}
		notifyChange();
		setChanged();
		notifyObservers(winners);
		return winners;
	}

	/**
	 * 
	 * @return array of cell in which is possible to place the tile
	 */
	public List<Cell> helpPlacing() {
		return grid.getPossibleMoves(getLastDrawedTile());
	}

	/**
	 * Places on the grid the last drawed tile, checking if the move is valid
	 * 
	 * @param coordinate
	 *            where to put the tile
	 * @throws InvalidCellException
	 *             if the cell at the <i>coordinate</i> is not <i>ENABLED</i>
	 * @throws IncompatibleFieldsException
	 *             if the tile is not compatible with the neighboring ones
	 */
	public void placeLastDrawedTile(Coordinate coordinate)
			throws InvalidCellException, IncompatibleFieldsException {
		grid.placeTile(coordinate, deck.getLastDrawedTile());
		notifyChange();
	}

	/**
	 * Places on the grid the <code>tile</code> passed, even if the move is
	 * invalid
	 * 
	 * @param tile
	 *            to place
	 * @param coordinate
	 *            where to put the tile
	 * @throws InvalidCellException
	 *             if the cell at the <i>coordinate</i> is not <i>ENABLED</i>
	 * @throws IncompatibleFieldsException
	 *             if the tile is not compatible with the neighboring ones
	 */
	public void placeTileWithoutCheckingNeighboring(Tile tile,
			Coordinate coordinate) throws InvalidCellException,
			IncompatibleFieldsException {
		grid.placeTile(coordinate, tile, false);
		notifyChange();
	}

	/**
	 * Rotate the last drawed tile
	 */
	public void rotateLastDrawedTile() {
		deck.getLastDrawedTile().rotate();
		notifyChange();
	}

	/**
	 * 
	 * @return the number of available markers of the player of the current turn
	 */
	public int getCurrentPlayerAvailableMarkers() {
		return getCurrentPlayer().getAvailableMarkerCount();
	}

	/**
	 * @return Player that have to play in the present turn
	 */
	public Player getCurrentPlayer() {
		return getPlayerByColor(turnColor());
	}

	/**
	 * @return Player from his color
	 */
	public Player getPlayerByColor(PlayersColor playersColor) {
		for (Player p : players) {
			if (p.getColor() == playersColor) {
				return p;
			}
		}
		return null;
	}

	private PlayersColor turnColor() {
		return getColorByID(turn);
	}

	private void initializeDeck() throws IOException {
		deck = new Deck();
		deck.populate();
	}

	private void inizializeGrid() {
		Tile firstTile = deck.drawTile();
		inizializeGrid(firstTile);
	}

	private void inizializeGrid(Tile firstTile) {
		grid = new Grid(firstTile);
	}

	/**
	 * Draw a tile and check if it can be placed on the grid. If NOT: draw a new
	 * one;
	 */
	public void drawTile() {
		boolean canPlace = false;
		do {
			deck.drawTile();
			canPlace = canPlaceTile(getLastDrawedTile());
		} while (!canPlace);
	}

	/**
	 * 
	 * @return the last drawed tile
	 */
	public Tile getLastDrawedTile() {
		if (deck == null) {
			return null;
		}
		return deck.getLastDrawedTile();
	}

	private boolean canPlaceTile(Tile tile) {
		return grid.canPlaceTile(tile);
	}

	/**
	 * 
	 * @return last inserted cell
	 */
	public Cell getLastInseredCell() {
		return grid.getLastInsertedCell();
	}

	/**
	 * Given a cell, check if it has closed a construction
	 * 
	 * @param cell
	 *            to check
	 * @return the building info data structure containing all closure
	 *         information
	 */
	public List<BuildingInfo> checkClosedBuildings(Cell cell) {
		return grid.checkClosedBuildings(cell);
	}

	/**
	 * 
	 * @return <i>true</i> if the deck contains no more tiles, <i>false</i>
	 *         otherwise
	 */
	public boolean deckIsEmpty() {
		return deck.isEmpty();
	}

	/**
	 * Finds all the incomplete buildings and adds their points to the players
	 * (used for the end of the game).
	 */
	public void checkIncompleteBuidings() {
		grid.checkIncompleteBuildings();
	}

	/**
	 * 
	 * @return the size of the deck
	 */
	public int getDeckSize() {
		return deck.getSize();
	}

	/**
	 * Add a marker to a tile
	 * 
	 * @param player
	 *            owner of the marker
	 * @param cardinalPoint
	 *            where to put the marker
	 * 
	 * @throws InvalidMarkerPositionException
	 *             if the position is not valid (raised when trying to put a
	 *             marker on a NIL field)
	 * @throws NotAvailableMarkerException
	 *             if the player has finished his markers
	 * @throws OccupiedFieldException
	 *             if the building is already occupied by another marker
	 */
	public void setMarker(Player player, CardinalPoint cardinalPoint)
			throws InvalidMarkerPositionException, OccupiedFieldException {
		grid.setMarker(player, cardinalPoint);
	}

	/**
	 * 
	 * @param param
	 * @return check if <code>param</code> is a valid coordinate in the grid
	 * @throws InvalidCellException
	 */
	public String validateCoordinates(String param) throws InvalidCellException {
		if (param == null) {
			return null;
		}
		Coordinate coord = new Coordinate(param);
		if ((coord.getX() <= grid.getMaxX() && coord.getX() >= grid.getMinX())
				&& (coord.getY() <= grid.getMaxY() && coord.getY() >= grid
						.getMinY())) {
			return param;
		}
		return null;
	}

	/**
	 * 
	 * @param param
	 * @return check if <code>param</code> is a valid cardinal point
	 * @throws InvalidMarkerPositionException
	 */
	public String validateCardinals(String param)
			throws InvalidMarkerPositionException {
		if (param == null) {
			return null;
		}
		for (CardinalPoint c : CardinalPoint.values()) {
			if (param.equals(c.getAbbreviation())) {
				return param;
			}
		}
		throw new InvalidMarkerPositionException();
	}

	/**
	 * Update the local grid in the specified coordinates with the new tile
	 * passed by parameter
	 * 
	 * @param tile
	 * @param coordinate
	 */
	public void update(Tile tile, Coordinate coordinate) {
		try {
			placeTileWithoutCheckingNeighboring(tile, coordinate);
		} catch (InvalidCellException e) {
		} catch (IncompatibleFieldsException e) {
		}
	}

	/**
	 * Set the last drawed tile in the deck, and if deck does not exist a new
	 * deck will be created, used by the text client.
	 * 
	 * @param tile
	 */
	public void setLastDrawedTile(Tile tile) {
		if (deck == null) {
			deck = new Deck();
		}
		deck.setLastDrawedTile(tile);
	}
}