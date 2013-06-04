package it.polimi.carcassonne.server.model.exceptions;

/**
 * Exception raised when trying to place a tile in a cell that is not
 * <i>ENABLED</i>
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class InvalidCellException extends Exception {

	private static final long serialVersionUID = 4830640277014426103L;
	private String message;

	/**
	 * 
	 * @param e
	 *            the exception raused
	 */
	public InvalidCellException(Exception e) {
		message = e.getMessage();
		// qui hanno scritto una lettera invece di un numero
	}

	public InvalidCellException() {
		// qui hanno scritto una cella non enablata
	}

	/**
	 * 
	 * @return the error which raised the exception
	 */
	public String getMessage() {
		return message;
	}
}
