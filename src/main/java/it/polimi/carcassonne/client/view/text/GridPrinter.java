package it.polimi.carcassonne.client.view.text;

import static it.polimi.carcassonne.server.model.enums.CardinalPoint.EAST;
import static it.polimi.carcassonne.server.model.enums.CardinalPoint.NORTH;
import static it.polimi.carcassonne.server.model.enums.CardinalPoint.SOUTH;
import static it.polimi.carcassonne.server.model.enums.CardinalPoint.WEST;
import static it.polimi.carcassonne.server.model.enums.Field.CITY;
import static it.polimi.carcassonne.server.model.enums.Field.NIL;
import static it.polimi.carcassonne.server.model.enums.Field.STREET;
import it.polimi.carcassonne.server.model.Cell;
import it.polimi.carcassonne.server.model.Grid;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.Field;

import java.util.Iterator;
import java.util.List;

/**
 * Class which has all the functions needed to display the grid in ASCII mode
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public final class GridPrinter {
	private static final int INTERNAL_CELL_LENGTH = 13;
	private static StringBuffer str = new StringBuffer();

	private GridPrinter() {
		// don't display my constructor
	}

	/**
	 * Print to screen without going to a new line
	 * 
	 * @param message
	 *            to print
	 */
	private static void print(String message) {
		str.append(message);
	}

	private static void printMessage(String message) {
		str.append(message);
		str.append("\n");
	}

	/**
	 * Visualize the passed <b>tile</b> in ASCII mode
	 * 
	 * @param tile
	 *            to display
	 */
	public static String printTile(Tile tile) {
		str = new StringBuffer();
		printMessage("Carta pescata:");
		String[] numbers = GridPrinterUtils.generateNumbers(tile);
		printMessage("+############+");
		printMessage("#     " + getPrintable(tile.getField(NORTH))
				+ numbers[NORTH.ordinal()] + "     #");
		printMessage("#            #");
		printMessage("#" + getPrintable(tile.getField(WEST))
				+ numbers[WEST.ordinal()] + "        "
				+ getPrintable(tile.getField(EAST)) + numbers[EAST.ordinal()]
				+ "#");
		printMessage("#            #");
		printMessage("#     " + getPrintable(tile.getField(SOUTH))
				+ numbers[SOUTH.ordinal()] + "     #");
		printMessage("+############+");
		Display.printMessage(str.toString());
		return str.toString();
	}

	/**
	 * Visualize the <b>grid</b> in ASCII mode
	 * 
	 * @param grid
	 *            to display
	 */
	public static String printGrid(Grid grid) {
		str = new StringBuffer();
		for (int i = grid.getMaxY(); i >= grid.getMinY(); i--) {
			printGridRow(grid.getCellsByHeight(i));
		}
		Display.printMessage(str.toString());
		return str.toString();
	}

	private static void printGridRow(List<Cell> cells) {
		printRowTop(cells);
		printRowBlank(cells, NORTH);
		printRowBlank(cells);
		printRowBlank(cells, EAST);
		printRowBlank(cells);
		printRowBlank(cells, SOUTH);
		printRowBottom(cells);
	}

	private static void printRowTop(List<Cell> cells) {
		Iterator<Cell> itr = cells.iterator();

		while (itr.hasNext()) {
			Cell cell = itr.next();
			printCellTop(cell);
		}
		printMessage("");
	}

	private static void printRowBlank(List<Cell> cells,
			CardinalPoint writeTileField) {
		Iterator<Cell> itr = cells.iterator();
		Cell cell = itr.next();
		while (itr.hasNext()) {
			printCellBlank(cell, writeTileField,
					GridPrinterUtils.generateNumbers(cell.getTile()));
			cell = itr.next();
		}
		printCellBlank(cell, writeTileField,
				GridPrinterUtils.generateNumbers(cell.getTile()));
		printMessage("");
	}

	// Overload
	private static void printRowBlank(List<Cell> cells) {
		printRowBlank(cells, null);
	}

	private static void printRowBottom(List<Cell> cells) {
		Iterator<Cell> itr = cells.iterator();
		Cell cell = itr.next();
		if (cell.isOnSouthBorder()) {
			while (itr.hasNext()) {
				printCellBottom(cell);
				cell = itr.next();
			}
			printCellBottom(cell);
			printMessage("");
		}
	}

	private static void printPlusLeft(Cell cell) {

		// tratto celle null come disabled
		boolean northCellDisabled = (cell.getNeighbor(NORTH) == null || cell
				.getNeighbor(NORTH).isDisabled());
		boolean westCellDisabled = (cell.getNeighbor(WEST) == null || cell
				.getNeighbor(WEST).isDisabled());
		boolean northWestEnabled = (cell.getNeighbor(WEST) != null
				&& cell.getNeighbor(WEST).getNeighbor(NORTH) != null && cell
				.getNeighbor(WEST).getNeighbor(NORTH).isEnabled());

		if (cell.isDisabled() && westCellDisabled && northCellDisabled
				&& !(cell.getNeighbor(WEST) != null && northWestEnabled)) {
			print(" ");
		} else {
			print("+");
		}
	}

	private static void printNorthBorder(Cell cell) {
		boolean northCellEnabled = (cell.getNeighbor(NORTH) != null && cell
				.getNeighbor(NORTH).isEnabled());
		boolean northCellHasTile = (cell.getNeighbor(NORTH) != null && cell
				.getNeighbor(NORTH).hasTile());

		if ((cell.hasTile() && cell.getNeighbor(NORTH).isEnabled())
				|| (cell.isEnabled() && northCellHasTile)) {
			print("#############");
		} else if ((cell.hasTile() && cell.getNeighbor(NORTH).hasTile())
				|| cell.isEnabled()
				|| (cell.isDisabled() && (northCellEnabled))) {
			print(".............");
		} else {
			printSpaces();
		}
	}

	private static void printPlusRight(Cell cell) {
		boolean northCellEnabled = (cell.getNeighbor(NORTH) != null && cell
				.getNeighbor(NORTH).isEnabled());

		if ((cell.isOnEastBorder() && (!cell.isDisabled() || (cell.isDisabled() && northCellEnabled)))) {
			print("+");
		}
	}

	private static void printCellTop(Cell cell) {
		printPlusLeft(cell);
		printNorthBorder(cell);
		printPlusRight(cell);
	}

	private static void printStartCharCenter(Cell cell) {
		boolean westCellEnabled = (cell.getNeighbor(WEST) != null && cell
				.getNeighbor(WEST).isEnabled());
		boolean westCellHasTile = (cell.getNeighbor(WEST) != null && cell
				.getNeighbor(WEST).hasTile());

		if ((cell.hasTile() && westCellEnabled)
				|| (cell.isEnabled() && westCellHasTile)) {
			print("#");
		} else if (cell.isEnabled() || (cell.isDisabled() && westCellEnabled)
				|| (cell.hasTile() && westCellHasTile)) {
			print(":");
		} else {
			print(" ");
		}
	}

	private static void printEndCharCenter(Cell cell) {
		if (cell.isOnEastBorder() && cell.isEnabled()) {
			print(":");
		}
	}

	private static void printSpaces() {
		print("             ");
	}

	private static void printFieldLine(Cell cell, CardinalPoint cardinalPoint,
			String number) {
		if (cell.hasTile()) {
			if (cell.getTile().getMarkerPosition() == cardinalPoint) {
				print("    "
						+ getPrintable(cell.getTile().getField(cardinalPoint))
						+ number
						+ "("
						+ cell.getTile().getMarker().getColor()
								.getAbbreviation() + ")" + "    ");
			} else {
				print("     "
						+ getPrintable(cell.getTile().getField(cardinalPoint))
						+ number + "      ");
			}
		} else {
			printSpaces();
		}
	}

	private static void printCoordinates(Cell cell) {
		if (cell.isEnabled()) {
			String coordinate = " (" + cell.getX() + "," + cell.getY() + ")";
			print(coordinate);
			for (int i = 0; (i + coordinate.length() < INTERNAL_CELL_LENGTH); i++) {
				print(" ");
			}
		} else {
			printSpaces();
		}
	}

	private static void printCenterFieldLine(Cell cell, String[] numbers) {
		if (cell.hasTile()) {
			boolean hasCentralMarkers = (cell.getTile().getMarkerPosition() == EAST || cell
					.getTile().getMarkerPosition() == WEST);
			if (!hasCentralMarkers) {
				// non ho segnalini a est o a ovest
				print(getPrintable(cell.getTile().getField(WEST))
						+ numbers[WEST.ordinal()] + "         "
						+ getPrintable(cell.getTile().getField(EAST))
						+ numbers[EAST.ordinal()]);
			} else {
				// ho segnalini a est oppure a ovest
				if (cell.getTile().getMarkerPosition() == WEST) {
					// ho il segnalino a OVEST
					print(getPrintable(cell.getTile().getField(WEST))
							+ numbers[WEST.ordinal()]
							+ "("
							+ cell.getTile().getMarker().getColor()
									.getAbbreviation() + ")      "
							+ getPrintable(cell.getTile().getField(EAST))
							+ numbers[EAST.ordinal()]);
				} else {
					// ho il segnalino a EST
					print(getPrintable(cell.getTile().getField(WEST))
							+ numbers[WEST.ordinal()]
							+ "      "
							+ getPrintable(cell.getTile().getField(EAST))
							+ numbers[EAST.ordinal()]
							+ "("
							+ cell.getTile().getMarker().getColor()
									.getAbbreviation() + ")");
				}
			}
		} else {
			// Non ho nessuna tessera, cella enablata: printa coordinate
			printCoordinates(cell);
		}
	}

	private static void printCellBlank(Cell cell, CardinalPoint writeTileField,
			String[] numbers) {

		printStartCharCenter(cell);
		if (writeTileField == NORTH || writeTileField == SOUTH) {
			// stampa prima o ultima linea interna alla cella
			printFieldLine(cell, writeTileField,
					numbers[writeTileField.ordinal()]);
		} else if (writeTileField == EAST) {
			// stampa linea WEST-EAST o coordinate
			printCenterFieldLine(cell, numbers);
		} else {
			// stampa linea vuota
			printSpaces();
		}
		printEndCharCenter(cell);
	}

	private static void printCellBottom(Cell cell) {
		if (cell.isEnabled() && cell.getNeighbor(EAST).isDisabled()) {
			print("+.............+");
		} else if (cell.isEnabled() && cell.getNeighbor(EAST).isEnabled()) {
			print("+.............");
		} else if (cell.isDisabled() && cell.getNeighbor(WEST) != null
				&& cell.getNeighbor(WEST).isEnabled()) {
			printSpaces();
		} else {
			printSpaces();
			print(" ");
		}
	}

	private static String getPrintable(Field field) {
		if (field.equals(NIL)) {
			return " ";
		} else if (field.equals(CITY)) {
			return "C";
		} else if (field.equals(STREET)) {
			return "S";
		}
		return "";
	}
}