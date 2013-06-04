package it.polimi.carcassonne.server.model.exceptions;

/**
 * Exception raised when trying to place a tile in a cell but the neighboring
 * tiles have at least one different type of field
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class IncompatibleFieldsException extends Exception {

	private static final long serialVersionUID = -8164108390288733025L;

}
