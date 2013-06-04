package it.polimi.carcassonne.server.model;

import static it.polimi.carcassonne.server.model.enums.CardinalPoint.EAST;
import static it.polimi.carcassonne.server.model.enums.CardinalPoint.SOUTH;
import static it.polimi.carcassonne.server.model.enums.CardinalPoint.WEST;
import static it.polimi.carcassonne.server.model.enums.CellStatus.DISABLED;
import static it.polimi.carcassonne.server.model.enums.CellStatus.ENABLED;
import static it.polimi.carcassonne.server.model.enums.CellStatus.HASTILE;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.CellStatus;

/**
 * A cell can be placed in a grid
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class Cell {

	private Coordinate coordinates;
	private Tile tile;
	private Grid grid;
	private CellStatus status;

	/**
	 * Create a new <b>cell</b> (default: <i>DISABLED</i>)
	 * 
	 * @param coordinates
	 *            of the cell
	 * @param grid
	 *            in which to put the cell just created
	 */
	public Cell(Coordinate coordinates, Grid grid) {
		this.coordinates = coordinates;
		tile = null;
		status = DISABLED;
		this.grid = grid;
	}

	/**
	 * 
	 * @return The <b>grid</b> coordinates of the cell
	 */
	public Coordinate getCoordinates() {
		return coordinates;
	}

	/**
	 * 
	 * @return the <b>X coordinate</b> of the cell
	 */
	public int getX() {
		return coordinates.getX();
	}

	/**
	 * 
	 * @return the <b>Y coordinate</b> of the cell
	 */
	public int getY() {
		return coordinates.getY();
	}

	/**
	 * 
	 * @return the <b>status</b> of the cell
	 */
	public CellStatus getStatus() {
		return status;
	}

	/**
	 * Set the passed <b>status</b> to the cell
	 * 
	 * @param status
	 *            to set
	 */
	public void setStatus(CellStatus status) {
		this.status = status;
	}

	/**
	 * 
	 * @return the <b>tile</b> of the cell (if present, otherwise <i>null</i>)
	 */
	public Tile getTile() {
		return tile;
	}

	/**
	 * Modify the <b>status</b> of the cell specifying that contains a
	 * <b>tile</b> and place the tile on it
	 * 
	 * @param tile
	 *            to insert in the <b>cell</b>
	 */
	public void setTile(Tile tile) {
		this.tile = tile;
		status = HASTILE;
	}

	/**
	 * 
	 * @param cardinalPoint
	 *            where you want to get the neighbor
	 * @return the neighbor cell in <b>cardinalPoint</b> direction
	 */
	public Cell getNeighbor(CardinalPoint cardinalPoint) {
		switch (cardinalPoint) {
		case NORTH:
			return grid.getCellByCoordinates(new Coordinate(coordinates.getX(),
					coordinates.getY()).getNorth());
		case EAST:
			return grid.getCellByCoordinates(new Coordinate(coordinates.getX(),
					coordinates.getY()).getEast());
		case SOUTH:
			return grid.getCellByCoordinates(new Coordinate(coordinates.getX(),
					coordinates.getY()).getSouth());
		case WEST:
			return grid.getCellByCoordinates(new Coordinate(coordinates.getX(),
					coordinates.getY()).getWest());
		}
		return null;
	}

	/**
	 * 
	 * @return <b>true</b> if the cell is on the <i>WEST</i> border of the
	 *         <b>grid</b>, <b>false</b> otherwise
	 */
	public boolean isOnWestBorder() {
		if (this.getNeighbor(WEST) == null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return <b>true</b> if the cell is on the <i>SOUTH</i> border of the
	 *         <b>grid</b>, <b>false</b> otherwise
	 */
	public boolean isOnSouthBorder() {
		if (this.getNeighbor(SOUTH) == null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return <b>true</b> if the cell is on the <i>EAST</i> border of the
	 *         <b>grid</b>, <b>false</b> otherwise
	 */
	public boolean isOnEastBorder() {
		if (this.getNeighbor(EAST) == null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return <b>true</b> if the cell's <b>status</b> is <i>ENABLED</i>,
	 *         <b>false</b> otherwise
	 */
	public boolean isEnabled() {
		if (status == ENABLED) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return <b>true</b> if the cell's <b>status</b> is <i>DISABLED</i>,
	 *         <b>false</b> otherwise
	 */
	public boolean isDisabled() {
		if (status == DISABLED) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return <b>true</b> if the cell's <b>status</b> is <i>HASTILE</i>,
	 *         <b>false</b> otherwise
	 */
	public boolean hasTile() {
		if (status == HASTILE) {
			return true;
		}
		return false;
	}
}
