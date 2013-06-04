package it.polimi.carcassonne.server.model.exceptions;

import it.polimi.carcassonne.server.model.enums.Field;

/**
 * Exception raised when trying to place a marker on a tile, but the building on
 * which it will be placed is already presidiated by another marker
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class OccupiedFieldException extends Exception {

	private static final long serialVersionUID = 7983828594561982352L;
	private Field field;

	/**
	 * 
	 * @param field
	 *            that caused the error
	 */
	public OccupiedFieldException(Field field) {
		this.field = field;
	}

	public Field getField() {
		return field;
	}

}
