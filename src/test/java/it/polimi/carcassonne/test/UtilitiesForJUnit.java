package it.polimi.carcassonne.test;

import it.polimi.carcassonne.server.model.Cell;

import java.util.List;

public class UtilitiesForJUnit {

	/**
	 * Dati due array, verifica che uno sia combinazione dell'altro
	 * 
	 * @param disabledCells
	 * @param list
	 * @return
	 */
	static boolean isEquivalentArray(List<Cell> disabledCells, List<Cell> list) {
		boolean testOK = true;

		// ogni elemento del primo deve esistere nel secondo
		for (Cell cell : disabledCells) {
			if (!list.contains(cell)) {
				testOK = false;
			}
		}

		// ogni elemento del secondo deve esistere nel primo
		for (Cell cell : list) {
			if (!disabledCells.contains(cell)) {
				testOK = false;
			}
		}

		// i due ArrayList devono avere la stessa lunghezza
		if (disabledCells.size() != list.size()) {
			testOK = false;
		}

		return testOK;
	}

}
