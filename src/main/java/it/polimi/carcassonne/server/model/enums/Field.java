package it.polimi.carcassonne.server.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Possible type of fields that a <b>tile</b> can have
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public enum Field {
	NIL("N", 0) {
		@Override
		public String toString() {
			return "Grass";
		}
	},
	CITY("C", 2) {
		@Override
		public String toString() {
			return "City";
		}
	},
	STREET("S", 1) {
		@Override
		public String toString() {
			return "Street";
		}
	};

	private final String abbreviation;
	private final int multiplier;

	private static final Map<String, Field> LOOKUP = new HashMap<String, Field>();
	static {
		for (Field field : Field.values()) {
			LOOKUP.put(field.getAbbreviation(), field);
		}
	}

	private Field(String abbreviation, int multiplier) {
		this.abbreviation = abbreviation;
		this.multiplier = multiplier;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public int getMultiplier() {
		return multiplier;
	}

	/**
	 * 
	 * @param abbreviation
	 * @return Field from abbreviation
	 */
	public static Field get(String abbreviation) {
		return LOOKUP.get(abbreviation);
	}

	/**
	 * 
	 * @param abbreviation
	 * @return Field from abbreviation
	 */
	public static Field get(char abbreviation) {
		return get(String.valueOf(abbreviation));
	}
};