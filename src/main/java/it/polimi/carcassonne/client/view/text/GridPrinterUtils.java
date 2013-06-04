package it.polimi.carcassonne.client.view.text;

import static it.polimi.carcassonne.server.model.enums.Field.CITY;
import static it.polimi.carcassonne.server.model.enums.Field.NIL;
import static it.polimi.carcassonne.server.model.enums.Field.STREET;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;

/**
 * Contains utilities functions needed to print the grid
 * 
 * @author Cesana, Arcidiacono
 * 
 * 
 */
public final class GridPrinterUtils {

	private GridPrinterUtils() {
		// don't display my constructor
	}

	/**
	 * Calculate the numbers displayed in the ASCII view on each cardinal point
	 * to indicates if a field is connected to the others
	 * 
	 * @param tile
	 *            to calculate the numbers
	 * @return the array of numbers: array[0]: NORTH array[1]: EAST array[2]:
	 *         SOUTH array[3]: WEST
	 */
	protected static String[] generateNumbers(Tile tile) {
		int streets = 1;
		int cities = 1;
		int numbers[] = new int[CardinalPoint.values().length];
		if (tile != null) {
			CardinalPoint[] allCardinals = CardinalPoint.values();
			for (CardinalPoint cardinal : allCardinals) {
				if (tile.getField(cardinal) != NIL) {
					if (tile.getField(cardinal) == CITY
							&& (numbers[cardinal.ordinal()] == 0)) {
						numbers[cardinal.ordinal()] = cities;
					} else if (tile.getField(cardinal) == STREET
							&& (numbers[cardinal.ordinal()] == 0)) {
						numbers[cardinal.ordinal()] = streets;
					}
					for (CardinalPoint cardinalContinues : tile
							.whereContinues(cardinal)) {
						if (tile.getField(cardinalContinues) == CITY) {
							if (numbers[cardinalContinues.ordinal()] == 0) {
								numbers[cardinalContinues.ordinal()] = cities;
							}
						} else if (tile.getField(cardinalContinues) == STREET
								&& (numbers[cardinalContinues.ordinal()] == 0)) {
							numbers[cardinalContinues.ordinal()] = streets;
						}
					}
					if (tile.getField(cardinal) == CITY) {
						cities++;
					} else if (tile.getField(cardinal) == STREET) {
						streets++;
					}
				}
			}
		}
		return GridPrinterUtils.generatePrintableNumbers(numbers);
	}

	/**
	 * Converts the given integer array in a string array
	 * 
	 * @param numbers
	 *            array to convert
	 * @return the String array generated
	 */
	protected static String[] generatePrintableNumbers(int[] numbers) {
		String[] toReturn = new String[CardinalPoint.values().length];
		for (int i = 0; i < CardinalPoint.values().length; i++) {
			if (numbers[i] == 0) {
				toReturn[i] = " ";
			} else {
				toReturn[i] = String.valueOf(numbers[i]);
			}
		}
		return toReturn;
	}

}
