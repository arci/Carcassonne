package it.polimi.carcassonne.server.model;

import static it.polimi.carcassonne.server.model.enums.CellStatus.ENABLED;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.CellStatus;
import it.polimi.carcassonne.server.model.enums.Field;
import it.polimi.carcassonne.server.model.exceptions.IncompatibleFieldsException;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;
import it.polimi.carcassonne.server.model.exceptions.InvalidMarkerPositionException;
import it.polimi.carcassonne.server.model.exceptions.OccupiedFieldException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This is the "play ground" of the match, contains various cells and can be
 * increased dinamically adding new tiles to the <i>ENABLED</i> cells
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class Grid {
	private Map<Coordinate, Cell> grid;
	private int minX, maxX, minY, maxY;
	private List<Cell> cellsWithMarkers;
	private Cell lastInsertedCell = null;

	/**
	 * Initialize a 3x3 grid, insert the tile passed as parameter in the deck at
	 * position (0,0) and enable the neighboring cells
	 * 
	 * @param firstTile
	 *            the tile to position at coordinates (0,0)
	 */
	public Grid(Tile firstTile) {
		grid = new HashMap<Coordinate, Cell>();
		minX = -1;
		minY = -1;
		maxX = 1;
		maxY = 1;
		cellsWithMarkers = new ArrayList<Cell>();
		// crea la griglia 3x3
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				addEmptyCell(new Coordinate(x, y));
			}
		}
		// abilito e inserisco la carta nella cella (0,0)
		Cell firstCell = getCellByCoordinates(new Coordinate(0, 0));
		firstCell.setStatus(ENABLED);
		lastInsertedCell = firstCell;
		enableNeighbors(firstCell);
		firstCell.setTile(firstTile);
	}

	/**
	 * 
	 * @return the grid
	 */
	public Map<Coordinate, Cell> getGrid() {
		return grid;
	}

	private void addCellWithMarkers(Cell cellWithMarker) {
		cellsWithMarkers.add(cellWithMarker);
	}

	private void removeCellWithMarker(Cell cellWithMarker) {
		cellsWithMarkers.remove(cellWithMarker);
	}

	/**
	 * 
	 * @return the last cell inserted in the grid
	 */
	public Cell getLastInsertedCell() {
		return lastInsertedCell;
	}

	public int getMinX() {
		return minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}

	/**
	 * Place a tile on a cell
	 * 
	 * @param coordinate
	 *            of the cell in which to place the tile
	 * @param tile
	 *            that has to be placed
	 * @throws InvalidCellException
	 *             if the cell is not <i>ENABLED</i>
	 * @throws IncompatibleFieldsException
	 *             if the tile's fields are not the same of the neighboring
	 */
	public void placeTile(Coordinate coordinate, Tile tile)
			throws InvalidCellException, IncompatibleFieldsException {
		placeTile(coordinate, tile, true);
	}

	/**
	 * Place a tile on a cell
	 * 
	 * @param coordinate
	 *            of the cell in which to place the tile
	 * @param tile
	 *            that has to be placed
	 * @param control
	 *            <code>true</code> if you want to check the compatibility with
	 *            neighbors tiles, <code>false</code> otherwise (
	 *            <code>false</code> only used for client/server protocol)
	 * 
	 * @throws InvalidCellException
	 *             if the cell is not <i>ENABLED</i>
	 * @throws IncompatibleFieldsException
	 *             if the tile's fields are not the same of the neighboring
	 */

	public void placeTile(Coordinate coordinate, Tile tile, boolean control)
			throws InvalidCellException, IncompatibleFieldsException {
		if (control) {
			Cell cell = getCellByCoordinates(coordinate);
			if (cell != null && cell.isEnabled()) {
				if (tile.isCompatibleWithNeighbors(cell)) {
					reallyInsertTile(tile, cell);
				} else {
					throw new IncompatibleFieldsException();
				}
			} else {
				throw new InvalidCellException();
			}
		} else {
			Cell cell = getCellByCoordinates(coordinate);
			if (cell != null) { // serve se il server dovesse comunicare
				// coordinate fuori dalla griglia
				if (cell.hasTile() && cell.getTile().getMarker() != null
						&& tile.getMarker() == null) {
					// se prima c'era un marker e ora non c'e' piu' (costruzione
					// chiusa) ridai il marker al proprietario
					cell.getTile().returnMarker();
				}
				reallyInsertTile(tile, cell);
			}
		}
	}

	/**
	 * Place a tile on a cell without controls
	 * 
	 * @param tile
	 *            to insert
	 * @param cell
	 *            where to insert the <code>tile</code>
	 */
	private void reallyInsertTile(Tile tile, Cell cell) {
		cell.setTile(tile);
		lastInsertedCell = cell;
		expandGrid(cell);
		enableNeighbors(cell);
	}

	private void enableNeighbors(Cell cell) {
		for (CardinalPoint cardinal : CardinalPoint.values()) {
			if (cell.getNeighbor(cardinal).isDisabled()) {
				cell.getNeighbor(cardinal).setStatus(ENABLED);
			}
		}
	}

	/*
	 * espande la griglia in mondo che sia sempre rettangolare a fronte di un
	 * nuovo inserimento di cella
	 */
	private void expandGrid(Cell cell) {
		if (cell.getX() + 1 > maxX) {
			// aggiunge colonna a destra
			for (int i = minY; i <= maxY; i++) {
				addEmptyCell(new Coordinate(cell.getX() + 1, i));
			}
		}
		if (cell.getX() - 1 < minX) {
			// aggiungi colonna a sinistra
			for (int i = minY; i <= maxY; i++) {
				addEmptyCell(new Coordinate(cell.getX() - 1, i));
			}
		}
		if (cell.getY() + 1 > maxY) {
			// aggiungi riga sopra
			for (int i = minX; i <= maxX; i++) {
				addEmptyCell(new Coordinate(i, cell.getY() + 1));
			}
		}
		if (cell.getY() - 1 < minY) {
			// aggiungi riga sotto
			for (int i = minX; i <= maxX; i++) {
				addEmptyCell(new Coordinate(i, cell.getY() - 1));
			}
		}
		updateGridDimensions(cell.getX(), cell.getY());
	}

	/**
	 * 
	 * @param height
	 * @return the list of cells at a given height
	 */
	public List<Cell> getCellsByHeight(int height) {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		for (int i = minX; i <= maxX; i++) {
			Cell cell = getCellByCoordinates(new Coordinate(i, height));
			cells.add(cell);
		}

		return cells;
	}

	/**
	 * 
	 * @param tile
	 *            to place
	 * @return array of cell in which is possible to place the tile
	 */
	public List<Cell> getPossibleMoves(Tile tile) {
		List<Cell> possibleMoves = new ArrayList<Cell>();
		List<Cell> enabledCell = getCellByStatus(ENABLED);
		for (Cell cell : enabledCell) {
			if (tile.isCompatibleWithNeighbors(cell)) {
				possibleMoves.add(cell);
			}
		}
		return possibleMoves;
	}

	/**
	 * Check if a tile can be placed in the grid (has compatible neighbors)
	 * 
	 * @param tile
	 *            to check
	 * @return boolean <i>true</i> if a tile can be placed, <i>false</i>
	 *         otherwise
	 */
	public boolean canPlaceTile(Tile tile) {
		Tile tileToPlace = tile;
		ArrayList<Cell> enabledCell = (ArrayList<Cell>) getCellByStatus(ENABLED);
		for (int i = 0; i < CardinalPoint.values().length; i++) {
			for (Cell cell : enabledCell) {
				if (tile.isCompatibleWithNeighbors(cell)) {
					return true;
				}
			}
			tileToPlace.rotate();
		}
		return false;
	}

	/**
	 * 
	 * @param status
	 *            to find
	 * @return the list of <b>cells</b> in the grid having the given
	 *         <b>status</b>
	 */
	public List<Cell> getCellByStatus(CellStatus status) {
		ArrayList<Cell> enabledCell = new ArrayList<Cell>();
		Set<Coordinate> list = grid.keySet();
		Iterator<Coordinate> iter = list.iterator();

		while (iter.hasNext()) {
			Coordinate key = iter.next();
			Cell cell = grid.get(key);
			if (cell.getStatus() == status) {
				enabledCell.add(cell);
			}
		}
		return enabledCell;
	}

	/**
	 * @param coordinate
	 *            of the cell
	 * @return <b>cell</b> placed at a given coordinate, <b>null</b> if not
	 *         present
	 */
	public Cell getCellByCoordinates(Coordinate coordinate) {
		if (grid.containsKey(coordinate)) {
			return grid.get(coordinate);
		}
		return null;
	}

	// updates the min/max dimensions of the grid
	private void updateGridDimensions(int x, int y) {
		if (x - 1 < minX) {
			minX = x - 1;
		}
		if (x + 1 > maxX) {
			maxX = x + 1;
		}
		if (y + 1 > maxY) {
			maxY = y + 1;
		}
		if (y - 1 < minY) {
			minY = y - 1;
		}
	}

	/**
	 * Adds to the grid a <i>DISABLED</i> cell at a given coordinate
	 * 
	 * @param coordinate
	 *            where to insert the cell
	 * @return cell just created
	 */
	public Cell addCell(Coordinate coordinate) {
		return addEmptyCell(coordinate);
	}

	// aggiunge una cella disabilitata
	private Cell addEmptyCell(Coordinate coord) {
		Cell cell = new Cell(coord, this);
		grid.put(coord, cell);
		return cell;
	}

	/**
	 * Check if the last inserted cell has closed a building. If so, add points
	 * to players and returns the markers in that building to their owners
	 * 
	 * @param cell
	 *            to check if has closed a building
	 */
	public List<BuildingInfo> checkClosedBuildings(Cell cell) {
		// inizio a preparare i punti cardinali in cui controllare se ho chiuso
		// una costruzione
		List<BuildingInfo> closedBuildings = new ArrayList<BuildingInfo>();

		List<CardinalPoint> cardinalsToCheck = getUtilCardinalPoints(cell);

		// per ogni punto cardinale calcolato, controllo se ho chiuso una
		// costruzione
		for (CardinalPoint cardinal : cardinalsToCheck) {
			BuildingInfo building = new BuildingInfo(cell, cardinal, true);
			if (building.isClosed()) {
				// ho chiuso una costruzione: aggiorno i punteggi
				addPoints(building, true);
				returnMarkers(building);
				closedBuildings.add(building);
			}
		}
		return closedBuildings;
	}

	private List<CardinalPoint> getUtilCardinalPoints(Cell cell) {
		List<CardinalPoint> cardinalsToCheck = new ArrayList<CardinalPoint>();
		for (CardinalPoint cardinal : CardinalPoint.values()) {
			cardinalsToCheck.add(cardinal);
		}
		for (CardinalPoint cardinal : CardinalPoint.values()) {

			if (cell.getTile().getField(cardinal).equals(Field.NIL)) {
				// se e' un NIL non va controllata la chiusura da quel lato
				cardinalsToCheck.remove(cardinal);
			} else if (cardinalsToCheck.contains(cardinal)) {
				// se una costruzione continua, basta controllare uno dei due
				// lati dal quale prosegue
				for (CardinalPoint cardinalToAvoid : cell.getTile()
						.whereContinues(cardinal)) {
					cardinalsToCheck.remove(cardinalToAvoid);
				}
			}
		}
		return cardinalsToCheck;
	}

	private void checkPartialPoints(Cell cell, CardinalPoint cardinalWithMarker) {
		BuildingInfo building = new BuildingInfo(cell, cardinalWithMarker,
				false);
		addPoints(building, false);
		returnMarkers(building);
	}

	/**
	 * Add point to player(s) that have more markers on the building contained
	 * in <i>building</i>
	 * 
	 * @param building
	 *            of the building of which you want to add points
	 * @param fieldMultiplier
	 *            if <i>true</i> the points added to players will be the number
	 *            of tiles of the building will be multiplied for the field
	 *            multiplier (used for the complete building). If <i>false</i>
	 *            the points added to players will be the number of tiles of the
	 *            building (used for the uncomplete buildings, only for the end
	 *            of the game).
	 */

	private void addPoints(BuildingInfo building, boolean fieldMultiplier) {
		Map<Player, Integer> markerNum = new HashMap<Player, Integer>();
		for (Cell cell : building.getCellsWithMarkerPassed()) {
			Player player = cell.getTile().getMarkerOwner();
			if (markerNum.get(player) != null) {
				markerNum.put(player, markerNum.get(player) + 1);
			} else {
				markerNum.put(player, 1);
			}
		}

		for (Player p : findMax(markerNum)) {
			if (fieldMultiplier) {
				p.increaseScore(building.getCellsOfConstruction().size()
						* building.getField().getMultiplier());
			} else {
				p.increaseScore(building.getCellsOfConstruction().size());
			}
		}

	}

	/**
	 * Returns the markers to relative owners when a contruction has been closed
	 * 
	 * @param building
	 *            the building of which you want to return the markers
	 * 
	 * 
	 */
	private void returnMarkers(BuildingInfo building) {
		for (Cell cell : building.getCellsWithMarkerPassed()) {
			cell.getTile().returnMarker();
			removeCellWithMarker(cell);
		}
	}

	private List<Player> findMax(Map<Player, Integer> map) {
		List<Player> playersWithMoreMarkers = new ArrayList<Player>();
		int maxMarkers = 0;
		Set<Entry<Player, Integer>> players = map.entrySet();
		Iterator<Entry<Player, Integer>> iter = players.iterator();
		while (iter.hasNext()) {
			Entry<Player, Integer> entry = iter.next();
			Player player = entry.getKey();
			Integer numMarker = entry.getValue();
			if (numMarker == maxMarkers) {
				playersWithMoreMarkers.add(player);
			} else if (numMarker > maxMarkers) {
				playersWithMoreMarkers.clear();
				playersWithMoreMarkers.add(player);
				maxMarkers++;
			}
		}
		return playersWithMoreMarkers;
	}

	/**
	 * Finds all the incomplete buildings and adds their points to the players
	 * (used for the end of the game).
	 */
	public void checkIncompleteBuildings() {
		for (Cell cell : getCellByStatus(CellStatus.HASTILE)) {
			if (cell.getTile().getMarker() != null) {
				checkPartialPoints(cell, cell.getTile().getMarkerPosition());
			}
		}

		// NB il while non e' infinito in quanto le celle con marker verranno
		// eliminate quando verranno ridati
		// punti/marker ai giocatori con città parziali
		while (cellsWithMarkers.size() > 0) {
			Cell cellToCheck = cellsWithMarkers.get(0);
			checkPartialPoints(cellToCheck, cellToCheck.getTile()
					.getMarkerPosition());
		}
	}

	// controlla se il Field a cardinal in lastInseredCell e' gia' presidiato
	private boolean isPresidiated(CardinalPoint cardinal) {
		BuildingInfo building = new BuildingInfo(lastInsertedCell, cardinal,
				false);
		// se ha almeno una cella con segnalino, e' gia' presidiata
		return (building.getCellsWithMarkerPassed().size() > 0);
	}

	/**
	 * Add a marker to a tile
	 * 
	 * @param player
	 *            owner of the marker
	 * @param cardinal
	 *            where to put the marker
	 * 
	 * @throws InvalidMarkerPositionException
	 *             if the position is not valid (raised when trying to put a
	 *             marker on a NIL field)
	 * @throws NotAvailableMarkerException
	 *             if the player has finished his markers
	 * @throws OccupiedFieldException
	 *             if the building is already presidiated by another marker
	 */
	public void setMarker(Player player, CardinalPoint cardinal)
			throws InvalidMarkerPositionException, OccupiedFieldException {
		if (cardinal == null) {
			throw new InvalidMarkerPositionException();
		}
		if (!isPresidiated(cardinal)) {
			Tile tile = lastInsertedCell.getTile();
			if (tile.getField(cardinal) != Field.NIL) {
				Marker marker = player.getMarker();
				marker.setPosition(cardinal);
				tile.setMarker(marker);
				addCellWithMarkers(lastInsertedCell);
			} else {
				throw new InvalidMarkerPositionException();
			}
		} else {
			throw new OccupiedFieldException(lastInsertedCell.getTile()
					.getField(cardinal));
		}
	}
}
