package it.polimi.carcassonne.server.model;

import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;

/**
 * Contains <b>x</b> and <b>y</b> parameters
 * 
 * @author Cesana, Arcidiacono
 * 
 */

public class Coordinate {
	private static final int USED_BIT = 16;
	private int x, y;

	/**
	 * Construct a coordinate from it <i>x</i> and <i>y</i> values
	 * 
	 * @param x
	 *            of the coordinate
	 * @param y
	 *            of the coordinate
	 */
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Construct a coordinate from a string like "<i>x,y</i>"
	 * 
	 * @param coordinate
	 *            : a string like "<i>x,y</i>"
	 * @throws InvalidCellException
	 */
	public Coordinate(String coordinate) throws InvalidCellException {
		try {
			this.x = Integer.parseInt(coordinate.split(",")[0]);
			this.y = Integer.parseInt(coordinate.split(",")[1]);
		} catch (NumberFormatException e) {
			throw new InvalidCellException(e);
		}
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	/**
	 * 
	 * @return the <i>x</i> value of the coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * 
	 * @return the <i>y</i> value of the coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * 
	 * @return the coordinate at <i>NORTH</i>
	 */
	public Coordinate getNorth() {
		return new Coordinate(x, y + 1);
	}

	/**
	 * 
	 * @return the coordinate at <i>EAST</i>
	 */
	public Coordinate getEast() {
		return new Coordinate(x + 1, y);
	}

	/**
	 * 
	 * @return the coordinate at <i>SOUTH</i>
	 */
	public Coordinate getSouth() {
		return new Coordinate(x, y - 1);
	}

	/**
	 * 
	 * @return the coordinate at <i>WEST</i>
	 */
	public Coordinate getWest() {
		return new Coordinate(x - 1, y);
	}

	@Override
	public boolean equals(Object coordinateToCompare) {
		if (this == coordinateToCompare) {
			return true;
		}
		if (coordinateToCompare == null) {
			return false;
		}
		// non e' neanche una coordinata!
		if (getClass() != coordinateToCompare.getClass()) {
			return false;
		}
		if (((Coordinate) coordinateToCompare).getX() != x) {
			return false;
		}
		if (((Coordinate) coordinateToCompare).getY() != y) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return (y << USED_BIT) ^ x;
	}
}
