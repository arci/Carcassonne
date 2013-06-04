package it.polimi.carcassonne.server.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Possible colors assignable to a <b>player</b>
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public enum PlayersColor {

	RED("R") {
		@Override
		public String toString() {
			return "red";
		}
	},
	BLUE("B") {
		@Override
		public String toString() {
			return "blue";
		}

	},
	GREEN("G") {
		@Override
		public String toString() {
			return "green";
		}

	},
	YELLOW("Y") {
		@Override
		public String toString() {
			return "yellow";
		}

	},
	BLACK("K") {
		@Override
		public String toString() {
			return "black";
		}

	};

	private final String abbreviation;
	private static final Map<String, PlayersColor> LOOKUP = new HashMap<String, PlayersColor>();
	static {
		for (PlayersColor playersColor : PlayersColor.values()) {
			LOOKUP.put(playersColor.getAbbreviation(), playersColor);
		}
	}

	private PlayersColor(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * 
	 * @return the abbreviation of the color
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * 
	 * @param abbreviation
	 * @return Color from abbreviation
	 */
	public static PlayersColor get(String abbreviation) {
		return LOOKUP.get(abbreviation);
	}

	/**
	 * 
	 * @param identifier
	 *            of the color
	 * @return the color assigned to <i>identifier</i>
	 */
	public static PlayersColor getColorByID(int identifier) {
		if (identifier == RED.ordinal()) {
			return RED;
		}
		if (identifier == BLUE.ordinal()) {
			return BLUE;
		}
		if (identifier == GREEN.ordinal()) {
			return GREEN;
		}
		if (identifier == YELLOW.ordinal()) {
			return YELLOW;
		}
		if (identifier == BLACK.ordinal()) {
			return BLACK;
		}
		return null;
	}

	/**
	 * 
	 * @param string
	 *            of the color
	 * @return the color assigned to <i>string</i>
	 */
	public static PlayersColor getColorByString(String string) {
		if (string.equals(RED.toString())) {
			return RED;
		}
		if (string.equals(BLUE.toString())) {
			return BLUE;
		}
		if (string.equals(GREEN.toString())) {
			return GREEN;
		}
		if (string.equals(YELLOW.toString())) {
			return YELLOW;
		}
		if (string.equals(BLACK.toString())) {
			return BLACK;
		}
		return null;
	}
};