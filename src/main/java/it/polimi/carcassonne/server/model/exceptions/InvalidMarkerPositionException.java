package it.polimi.carcassonne.server.model.exceptions;

/**
 * Exception raised when trying to place a marker on an invalid position
 * (Example: <i>Field.NIL</i> or <i>null</i>)
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class InvalidMarkerPositionException extends Exception {

	private static final long serialVersionUID = 3045083037581513434L;

	private String message;

	public InvalidMarkerPositionException() {
		// not preserve stack trace
	}

	/**
	 * 
	 * @param e
	 *            the exception raised
	 */
	public InvalidMarkerPositionException(Exception e) {
		message = e.getMessage();
	}

	/**
	 * 
	 * @return the error which raised the exception
	 */
	public String getMessage() {
		return message;
	}
}
