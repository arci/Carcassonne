package it.polimi.carcassonne.server.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * The cardinal points of the tiles
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public enum CardinalPoint {
	NORTH("N") {
		@Override
		public String toString() {
			return "North";
		}
	},
	EAST("E") {
		@Override
		public String toString() {
			return "East";
		}
	},
	SOUTH("S") {
		@Override
		public String toString() {
			return "South";
		}
	},
	WEST("W") {
		@Override
		public String toString() {
			return "West";
		}
	};

	private final String abbreviation;
	private static final Map<String, CardinalPoint> LOOKUP = new HashMap<String, CardinalPoint>();
	static {
		for (CardinalPoint cardinal : CardinalPoint.values()) {
			LOOKUP.put(cardinal.getAbbreviation(), cardinal);
		}
	}

	private CardinalPoint(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * 
	 * @return the abbreviation of the cardinal point
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * 
	 * @param abbreviation
	 * @return CardinalPoint from abbreviation
	 */
	public static CardinalPoint get(String abbreviation) {
		return LOOKUP.get(abbreviation);
	}

	/**
	 * 
	 * @param abbreviation
	 * @return CardinalPoint from abbreviation
	 */
	public static CardinalPoint get(char abbreviation) {
		return get(String.valueOf(abbreviation));
	}

	/**
	 * 
	 * @param cardinal
	 *            of which you want to calculate the opposite
	 * @return the opposite of given cardinal point
	 */
	public static CardinalPoint getOpposite(CardinalPoint cardinal) {
		switch (cardinal) {
		case NORTH:
			return SOUTH;
		case SOUTH:
			return NORTH;
		case EAST:
			return WEST;
		case WEST:
			return EAST;
		default:
			return null;
		}
	}

	/**
	 * 
	 * @return the opposite of <i>this</i> cardinal point
	 */
	// Overload
	public CardinalPoint getOpposite() {
		return getOpposite(this);
	}
}