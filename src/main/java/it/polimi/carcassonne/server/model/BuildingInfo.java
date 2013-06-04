package it.polimi.carcassonne.server.model;

import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a building (closure, number of tiles, type of field,
 * etc...)
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class BuildingInfo {
	private boolean isClosed = false;
	private Field field = null;
	private List<Cell> cellsToCheck;
	private List<Cell> cellsOfConstruction;
	private List<CardinalPoint> cardinalsToCheck;
	private List<Cell> cellsWithMarkerPassed;

	/**
	 * Used to calculate how many <b>tiles</b> and <b>markers</b> are in a
	 * building. Also used to determine if a building is closed.
	 * 
	 * @param cell
	 *            from which to start the control
	 * @param startCardinal
	 *            from where to start the control
	 * @param stopIfOpen
	 *            used to truncate the computation whenever discovered that the
	 *            building is not closed
	 */
	public BuildingInfo(Cell cell, CardinalPoint startCardinal,
			boolean stopIfOpen) {
		cellsToCheck = new ArrayList<Cell>();
		cellsToCheck.add(cell);
		cellsOfConstruction = new ArrayList<Cell>();
		cardinalsToCheck = new ArrayList<CardinalPoint>();
		cardinalsToCheck.add(startCardinal);
		cellsWithMarkerPassed = new ArrayList<Cell>();
		field = cell.getTile().getField(startCardinal);
		isClosed = computeClosingInfo(stopIfOpen);
	}

	/**
	 * 
	 * @return the <b>cells</b> that need to be checked by the algorithm
	 */
	public List<Cell> getCellsToCheck() {
		return cellsToCheck;
	}

	/**
	 * 
	 * @return the <b>cells</b> already checked by the algorithm (if a building
	 *         is closed, this number is the number of the tiles that compose
	 *         it)
	 */
	public List<Cell> getCellsOfConstruction() {
		return cellsOfConstruction;
	}

	/**
	 * 
	 * @return the <b>cells</b> of the building with a marker positioned on the
	 *         building (will NOT be considered cells with markers NOT belonging
	 *         at the building itself)
	 */
	public List<Cell> getCellsWithMarkerPassed() {
		return cellsWithMarkerPassed;
	}

	/**
	 * 
	 * @return <i>true</i> if the building checked is <b>closed</b>,
	 *         <i>false</i> otherwise
	 */
	public boolean isClosed() {
		return isClosed;
	}

	/**
	 * 
	 * @return the type of <b>field</b> of the constrction
	 */
	public Field getField() {
		return field;
	}

	/**
	 * This is the algorithm that compute all the informations about a building
	 * 
	 * @param stopIfOpen
	 *            if <i>true</i> the algorithm stops whenever discovers that the
	 *            building is NOT closed. If <i>false</i> it continues with all
	 *            the cells of the building.
	 * @return <i>true</i> if the building is closed, <i>false</i> otherwise
	 */
	private boolean computeClosingInfo(boolean stopIfOpen) {
		boolean notClosed = false;
		Cell cell = cellsToCheck.get(0);
		ArrayList<CardinalPoint> tempCardinals = (ArrayList<CardinalPoint>) cell
				.getTile().whereContinues(cardinalsToCheck.get(0));
		tempCardinals.add(cardinalsToCheck.get(0));
		// Controllo che nelle direzioni del whereContinue (dato un punto
		// cardinale di partenza) le celle vicine abbiano una carta
		for (CardinalPoint cardinal : tempCardinals) {
			if (cell.getTile().getMarkerPosition() == cardinal) {
				cellsWithMarkerPassed.add(cell);
			}
			// se il vicino ha la carta
			if (cell.getNeighbor(cardinal).hasTile()) {
				// e non e' ancora stato controllato
				if (!cellsOfConstruction.contains(cell.getNeighbor(cardinal))) {
					// aggiungilo all'elenco delle celle da controllare
					cellsToCheck.add(cell.getNeighbor(cardinal));
					cardinalsToCheck.add(cardinal.getOpposite());
				}
			} else {
				// se anche solo uno dei vicini non ha la carta, la costruzione
				// e' sicuramente aperta
				notClosed = true;
			}
		}
		if (stopIfOpen) {
			if (notClosed) {
				// e' aperta
				return false;
			} else {
				// rimuovo la cella corrente da quelle da controllare e la
				// aggiungo alla lista di quelle gia' controllate
				updateLists();
			}
		} else {
			updateLists();
		}
		// le ho controllate tutte, sono tutte OK
		if (cellsToCheck.size() == 0) {
			// per essere chiusa la costruzione deve avere almeno 2 tessere
			return (cellsOfConstruction.size() > 1);
		} else {
			// itero sulla nuova lista di celle da controllare
			return computeClosingInfo(stopIfOpen);
		}
	}

	/**
	 * Removes the cells already checked from cellsToCheck and add them to
	 * cellsOK
	 */
	private void updateLists() {
		if (!cellsOfConstruction.contains(cellsToCheck.get(0))) {
			cellsOfConstruction.add(cellsToCheck.get(0));
		}
		cellsToCheck.remove(0);
		cardinalsToCheck.remove(0);
	}

}
