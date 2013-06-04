package it.polimi.carcassonne.server.model;

import static it.polimi.carcassonne.server.model.enums.CardinalPoint.EAST;
import static it.polimi.carcassonne.server.model.enums.CardinalPoint.NORTH;
import static it.polimi.carcassonne.server.model.enums.CardinalPoint.SOUTH;
import static it.polimi.carcassonne.server.model.enums.CardinalPoint.WEST;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A tile can be placed on a cell of the grid
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class Tile {

	private static final int LINE_ELEMENTS = 10;
	// connections NE,NS,NO,OE,SE,SO
	private Map<String, Boolean> connections;
	// fields at 4 cardinal points
	private Map<CardinalPoint, Field> fields;
	// if the card has a marker will be saved here
	private Marker marker;

	/**
	 * Creates a new tile given its description
	 * 
	 * @param tileDescription
	 *            the string that describes a tile
	 */
	public Tile(String tileDescription) {
		fields = new HashMap<CardinalPoint, Field>();
		connections = new HashMap<String, Boolean>();
		marker = null;
		String[] strArray = tileDescription.split(" ");
		for (int i = 0; i < CardinalPoint.values().length; i++) {
			String cardinal = strArray[i].split("=")[0];
			String field = strArray[i].split("=")[1];
			field = field.split("\\+")[0];
			setField(CardinalPoint.get(cardinal), Field.get(field));
		}

		for (int i = CardinalPoint.values().length; i < LINE_ELEMENTS; i++) {
			String cPoint = strArray[i].split("=")[0];

			if (strArray[i].split("=")[1].charAt(0) == '1') {

				setConnection(CardinalPoint.get(cPoint.substring(0, 1)),
						CardinalPoint.get(cPoint.substring(1)), true);
			} else {
				setConnection(CardinalPoint.get(cPoint.substring(0, 1)),
						CardinalPoint.get(cPoint.substring(1)), false);
			}
		}
	}

	/**
	 * 
	 * @return stringa costruttrice della carta
	 */
	public String getString() {
		StringBuffer string = new StringBuffer();

		CardinalPoint[] cardinalOrder = { NORTH, SOUTH, WEST, EAST };
		String[] connectionsOrder = {
				NORTH.getAbbreviation() + SOUTH.getAbbreviation(),
				NORTH.getAbbreviation() + EAST.getAbbreviation(),
				NORTH.getAbbreviation() + WEST.getAbbreviation(),
				WEST.getAbbreviation() + EAST.getAbbreviation(),
				SOUTH.getAbbreviation() + EAST.getAbbreviation(),
				SOUTH.getAbbreviation() + WEST.getAbbreviation() };

		// Aggiungo punti cardinali e relativi campi
		for (CardinalPoint c : cardinalOrder) {
			if (marker != null && marker.getPosition().equals(c)) {
				string.append(c.getAbbreviation() + "="
						+ fields.get(c).getAbbreviation() + "+"
						+ marker.getColor().getAbbreviation() + " ");
			} else {
				string.append(c.getAbbreviation() + "="
						+ fields.get(c).getAbbreviation() + " ");
			}
		}

		// Aggiungo connessioni
		for (String s : connectionsOrder) {
			string.append(s);
			if (connections.get(s)) {
				string.append("=1 ");
			} else {
				string.append("=0 ");

			}
		}

		// elimino l'ultimo spazio alla fine
		string.deleteCharAt(string.length() - 1);
		return string.toString();
	}

	/**
	 * 
	 * @param cardinalPoint
	 *            where you want to know the field
	 * @return the type of field placed <i>cardinalPoint</i> position
	 */
	public Field getField(CardinalPoint cardinalPoint) {
		return fields.get(cardinalPoint);
	}

	/**
	 * Place a marker on the tile
	 * 
	 * @param marker
	 *            to place on the tile
	 */
	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	/**
	 * Used when a building is closed: it returns the marker placed on the tile
	 * to its owners and delete it from the tile
	 */
	public void returnMarker() {
		if (marker != null) {
			marker.getOwner().addMarker();
			marker = null;
		}
	}

	/**
	 * 
	 * @return the marker placed on the tile, <i>null</i> if the tile has not a
	 *         marker
	 */
	public Marker getMarker() {
		return marker;
	}

	/**
	 * @return returns the <b>player</b> that owns the marker if the tile has
	 *         one, <i>null</i> otherwise
	 */
	public Player getMarkerOwner() {
		if (marker != null) {
			return marker.getOwner();
		}
		return null;
	}

	/**
	 * @return <b>cardinal point</b> where the marker is positioned if the tile
	 *         has one, <i>null</i> otherwise
	 */
	public CardinalPoint getMarkerPosition() {
		if (marker != null) {
			return marker.getPosition();
		}
		return null;
	}

	private void setField(CardinalPoint cardinalPoint, Field field) {
		this.fields.put(cardinalPoint, field);
	}

	/**
	 * Inserisce un riferimento alla connessione fra cardinalPoint1 e
	 * cardinalPoint2
	 */
	private void setConnection(CardinalPoint cardinalPoint1,
			CardinalPoint cardinalPoint2, boolean bool) {
		connections.put(
				cardinalPoint1.getAbbreviation()
						+ cardinalPoint2.getAbbreviation(), bool);
		connections.put(
				cardinalPoint2.getAbbreviation()
						+ cardinalPoint1.getAbbreviation(), bool);
	}

	/**
	 * Check if two cardinal points of a tile are connected
	 * 
	 * @param cardinalPoint1
	 * @param cardinalPoint2
	 * @return <code>true</code> if <code>cardinalPoint1</code> is connected
	 *         with <code>cardinalPoint2</code>
	 */
	public boolean isConnected(CardinalPoint cardinalPoint1,
			CardinalPoint cardinalPoint2) {
		if (connections.containsKey(cardinalPoint1.getAbbreviation()
				+ cardinalPoint2.getAbbreviation())) {
			return connections.get(cardinalPoint1.getAbbreviation()
					+ cardinalPoint2.getAbbreviation());
		} else {
			return false;
		}

	}

	/**
	 * @param cardinalPoint
	 *            where the building starts
	 * @return the list of cardinal points where the building that starts at the
	 *         given cardinal point continues
	 */
	public List<CardinalPoint> whereContinues(CardinalPoint cardinalPoint) {
		ArrayList<CardinalPoint> whereContinues = new ArrayList<CardinalPoint>();

		for (CardinalPoint cardinal : CardinalPoint.values()) {
			if (isConnected(cardinalPoint, cardinal)) {
				whereContinues.add(cardinal);
			}
		}
		return whereContinues;
	}

	/**
	 * Check if the tile could be placed in a given <b>cell</b> (checking the
	 * neighboring tiles and verifying that they have the same type of field)
	 * 
	 * @param cell
	 *            where to check if the tile is compatible
	 * @return <i>true</i> if the tile could be placed in that <b>cell</b>,
	 *         <i>false</i> otherwise
	 */
	public boolean isCompatibleWithNeighbors(Cell cell) {
		for (CardinalPoint cardinal : CardinalPoint.values()) {
			if (cell.getNeighbor(cardinal) != null
					&& cell.getNeighbor(cardinal).hasTile()
					&& (cell.getNeighbor(cardinal).getTile()
							.getField(cardinal.getOpposite()) != this
							.getField(cardinal))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * rotate the tile by 90 degrees clockwise
	 */
	public void rotate() {
		rotateFields();
		rotateConnections();
	}

	private void rotateFields() {
		Field nordTemp = getField(NORTH);
		setField(NORTH, getField(WEST));
		setField(WEST, getField(SOUTH));
		setField(SOUTH, getField(EAST));
		setField(EAST, nordTemp);
	}

	private void rotateConnections() {
		boolean nordSudTemp = isConnected(NORTH, SOUTH);
		boolean nordWestTemp = isConnected(NORTH, WEST);
		setConnection(NORTH, WEST, isConnected(SOUTH, WEST));
		setConnection(SOUTH, WEST, isConnected(SOUTH, EAST));
		setConnection(SOUTH, EAST, isConnected(NORTH, EAST));
		setConnection(NORTH, EAST, nordWestTemp);
		setConnection(NORTH, SOUTH, isConnected(WEST, EAST));
		setConnection(WEST, EAST, nordSudTemp);
	}

	@Override
	public int hashCode() {
		// AutoGenerated
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((connections == null) ? 0 : connections.hashCode());
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((marker == null) ? 0 : marker.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object objToCompare) {
		if (this == objToCompare) {
			return true;
		}
		if (objToCompare == null) {
			return false;
		}
		if (getClass() != objToCompare.getClass()) {
			return false;
		}

		Tile tileToCompare = (Tile) objToCompare;

		if (!hasSameConnections(tileToCompare)) {
			return false;
		}

		if (!hasSameFields(tileToCompare)) {
			return false;
		}

		if (!hasSameMarker(tileToCompare)) {
			return false;
		}
		return true;
	}

	/**
	 * Verifica che due tile abbiano le stesse connessioni
	 * 
	 * @param tileToCompare
	 * @return <code>true</code> if the tile has the same connections of
	 *         tileToCompare, <code>false</code> otherwise
	 */
	private boolean hasSameConnections(Tile tileToCompare) {
		Set<Entry<String, Boolean>> connections2 = connections.entrySet();
		Iterator<Entry<String, Boolean>> iter = connections2.iterator();

		while (iter.hasNext()) {
			Entry<String, Boolean> entry = iter.next();
			String cardinal = entry.getKey();
			Boolean connected = entry.getValue();
			CardinalPoint field1 = CardinalPoint.get(cardinal.charAt(0));
			CardinalPoint field2 = CardinalPoint.get(cardinal.charAt(1));
			if (tileToCompare.isConnected(field1, field2) != connected) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Verifica che due tile abbiano gli stessi campi
	 * 
	 * @param tileToCompare
	 * @return <code>true</code> if the tile has the same fields of
	 *         tileToCompare, <code>false</code> otherwise
	 */
	private boolean hasSameFields(Tile tileToCompare) {
		Set<Entry<CardinalPoint, Field>> connections2 = fields.entrySet();
		Iterator<Entry<CardinalPoint, Field>> iter = connections2.iterator();

		while (iter.hasNext()) {
			Entry<CardinalPoint, Field> entry = iter.next();
			CardinalPoint cardinal = entry.getKey();
			Field field = entry.getValue();
			if (!tileToCompare.getField(cardinal).equals(field)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Verifica che due tile abbiano lo stesso segnalino
	 * 
	 * @param tileToCompare
	 * @return <code>true</code> if the tile has the same marker of
	 *         tileToCompare, <code>false</code> otherwise
	 */
	private boolean hasSameMarker(Tile tileToCompare) {
		if (getMarker() == null && tileToCompare.getMarker() != null) {
			return false;
		}
		if (getMarker() != null && tileToCompare.getMarker() == null) {
			return false;
		}
		if (getMarker() == null && tileToCompare.getMarker() == null) {
			return true;
		}
		if (!getMarkerOwner().equals(tileToCompare.getMarkerOwner())) {
			return false;
		}
		if (!getMarkerPosition().equals(tileToCompare.getMarkerPosition())) {
			return false;
		}
		return true;
	}

}