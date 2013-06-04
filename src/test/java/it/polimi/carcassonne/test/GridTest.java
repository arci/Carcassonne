package it.polimi.carcassonne.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.polimi.carcassonne.server.model.Cell;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Grid;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.CellStatus;
import it.polimi.carcassonne.server.model.enums.Field;
import it.polimi.carcassonne.server.model.enums.PlayersColor;
import it.polimi.carcassonne.server.model.exceptions.IncompatibleFieldsException;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;
import it.polimi.carcassonne.server.model.exceptions.OccupiedFieldException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class GridTest {

	private Grid grid;
	private Tile firstTile;

	private Tile elbowStreetNEconn;
	private Tile elbowStreetNWconn;
	private Tile elbowStreetSWconn;
	private Tile elbowStreetSEconn;
	private Tile elbowStreetNEnotConn;
	private Tile elbowStreetNWnotConn;
	private Tile straightStreetWEconn;
	private Tile straightStreetNSconn;
	private Tile straightStreetNSnotConn;
	private Tile elbowCityNEconn;
	private Tile elbowCitySEconn;
	private Tile elbowCityNEnotConn;
	private Tile straightCityWEconn;
	private Tile straightCityNSconn;
	private Tile singleCityN;
	private Tile singleCityS;

	private Tile allCity;

	private boolean testOK;
	private Cell cell;

	@Before
	public void setUp() {

		testOK = false;

		firstTile = new Tile("N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0");
		elbowStreetNEconn = new Tile(
				"N=S E=S S=N W=N NE=1 NW=0 NS=0 SE=0 SW=0 EW=0");
		elbowStreetNWconn = new Tile(
				"N=S E=N S=N W=S NE=0 NW=1 NS=0 SE=0 SW=0 EW=0");
		elbowStreetSWconn = new Tile(
				"N=N E=N S=S W=S NE=0 NW=0 NS=0 SE=0 SW=1 EW=0");
		elbowStreetSEconn = new Tile(
				"N=N E=S S=S W=N NE=0 NW=0 NS=0 SE=1 SW=0 EW=0");
		elbowStreetNEnotConn = new Tile(
				"N=S E=S S=N W=N NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		elbowStreetNWnotConn = new Tile(
				"N=S E=N S=N W=S NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		straightStreetWEconn = new Tile(
				"N=N E=S S=N W=S NE=0 NW=0 NS=0 SE=0 SW=0 EW=1");
		straightStreetNSconn = new Tile(
				"N=S E=N S=S W=N NE=0 NW=0 NS=1 SE=0 SW=0 EW=0");
		straightStreetNSnotConn = new Tile(
				"N=S E=N S=S W=N NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		elbowCityNEconn = new Tile(
				"N=C E=C S=N W=N NE=1 NW=0 NS=0 SE=0 SW=0 EW=0");
		elbowCitySEconn = new Tile(
				"N=N E=C S=C W=N NE=0 NW=0 NS=0 SE=1 SW=0 EW=0");
		elbowCityNEnotConn = new Tile(
				"N=C E=C S=N W=N NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		straightCityWEconn = new Tile(
				"N=N E=C S=N W=C NE=0 NW=0 NS=0 SE=0 SW=0 EW=1");
		straightCityNSconn = new Tile(
				"N=C E=N S=C W=N NE=0 NW=0 NS=1 SE=0 SW=0 EW=0");
		singleCityN = new Tile("N=C E=N S=N W=N NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		singleCityS = new Tile("N=N E=N S=C W=N NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");

		allCity = new Tile("N=C E=C S=C W=C NE=1 NW=1 NS=1 SE=1 SW=1 EW=1");

		grid = new Grid(singleCityS);
	}

	@Test
	public void constructorTest() {
		int enabled = 0;
		int disabled = 0;
		int hascard = 0;
		assertEquals(1, grid.getMaxX());
		assertEquals(1, grid.getMaxY());
		assertEquals(-1, grid.getMinX());
		assertEquals(-1, grid.getMinY());
		for (int x = grid.getMinX(); x <= grid.getMaxX(); x++) {
			for (int y = grid.getMinY(); y <= grid.getMaxY(); y++) {
				cell = grid.getCellByCoordinates(new Coordinate(x, y));
				if (cell.getStatus().equals(CellStatus.DISABLED))
					disabled++;
				else if (cell.getStatus().equals(CellStatus.ENABLED))
					enabled++;
				else if (cell.getStatus().equals(CellStatus.HASTILE))
					hascard++;
			}
		}
		assertEquals(4, disabled);
		assertEquals(4, enabled);
		assertEquals(1, hascard);
		assertEquals(grid.getCellByCoordinates(new Coordinate(0, 0)),
				grid.getLastInsertedCell());
	}

	@Test
	public void getGridTest() {
		Map<Coordinate, Cell> gridMap = grid.getGrid();
		assertEquals((3 * 3), gridMap.size()); // deve avere una griglia 3x3
		assertTrue(gridMap.get(new Coordinate(-1, -1)).isDisabled());
		assertTrue(gridMap.get(new Coordinate(-1, 0)).isEnabled());
		assertTrue(gridMap.get(new Coordinate(-1, 1)).isDisabled());
		assertTrue(gridMap.get(new Coordinate(0, -1)).isEnabled());
		assertTrue(gridMap.get(new Coordinate(0, 0)).hasTile());
		assertTrue(gridMap.get(new Coordinate(0, 1)).isEnabled());
		assertTrue(gridMap.get(new Coordinate(1, -1)).isDisabled());
		assertTrue(gridMap.get(new Coordinate(1, 0)).isEnabled());
		assertTrue(gridMap.get(new Coordinate(1, 1)).isDisabled());
	}

	@Test
	public void placeTileCheckingCompatibilityWithNeighbors() {
		testOK = false;

		// enabled and compatible cell
		grid = new Grid(singleCityS);
		testOK = false;
		try {
			grid.placeTile(new Coordinate(1, 0), straightCityNSconn);
		} catch (InvalidCellException e) {
			fail("Avrebbe dovuto posizionarla");
		} catch (IncompatibleFieldsException e) {
			fail("Avrebbe dovuto posizionarla");
		}

		// disabled cell
		grid = new Grid(singleCityS);
		testOK = false;
		try {
			grid.placeTile(new Coordinate(1, 1), straightCityNSconn);
		} catch (InvalidCellException e) {
			testOK = true;
		} catch (IncompatibleFieldsException e) {
			fail("Non avrebbe dovuto entrare in questo catch, ma solo nel precedente");
		}
		assertTrue(testOK);

		// non-existent cell
		grid = new Grid(singleCityS);
		testOK = false;
		try {
			grid.placeTile(new Coordinate(4, 4), straightCityNSconn);
		} catch (InvalidCellException e) {
			testOK = true;
		} catch (IncompatibleFieldsException e) {
			fail("Non avrebbe dovuto entrare in questo catch, ma solo nel precedente");
		}
		assertTrue(testOK);

		// incompatible cell
		grid = new Grid(singleCityS);
		testOK = false;
		try {
			grid.placeTile(new Coordinate(0, -1), straightCityWEconn);
		} catch (InvalidCellException e) {
			fail("Non avrebbe dovuto entrare in questo catch, ma solo nel successivo");
		} catch (IncompatibleFieldsException e) {
			testOK = true;
		}
		assertTrue(testOK);
	}

	@Test
	public void placeTileWithoutCheckingCompatibilityWithNeighbors() {
		// enabled and compatible cell
		try {
			grid.placeTile(new Coordinate(1, 0), straightCityNSconn, false);
		} catch (InvalidCellException e) {
			fail("Avrebbe dovuto posizionarla");
		} catch (IncompatibleFieldsException e) {
			fail("Avrebbe dovuto posizionarla");
		}

		// disabled cell
		grid = new Grid(singleCityS);
		try {
			grid.placeTile(new Coordinate(1, 1), straightCityNSconn, false);
		} catch (InvalidCellException e) {
			fail("Avrebbe dovuto posizionarla");
		} catch (IncompatibleFieldsException e) {
			fail("Avrebbe dovuto posizionarla");
		}

		// non-existent cell
		grid = new Grid(singleCityS);
		try {
			grid.placeTile(new Coordinate(123, 456), straightCityNSconn, false);
		} catch (InvalidCellException e) {
			fail("Non deve notificare nulla all'utente");
		} catch (IncompatibleFieldsException e) {
			fail("Non deve notificare nulla all'utente");
		}
		// deve arrivare qui senza crashare

		testOK = false;

		// incompatible cell
		grid = new Grid(singleCityS);
		try {
			grid.placeTile(new Coordinate(0, -1), straightCityWEconn, false);
		} catch (InvalidCellException e) {
			fail("Avrebbe dovuto posizionarla");
		} catch (IncompatibleFieldsException e) {
			fail("Avrebbe dovuto posizionarla");
		}
	}

	@Test
	public void getCellByStatusTest() {
		List<Cell> disabledCells = new ArrayList<Cell>();
		List<Cell> enabledCells = new ArrayList<Cell>();
		List<Cell> hasTileCells = new ArrayList<Cell>();
		Cell currentCell = null;

		for (int x = grid.getMinX(); x <= grid.getMaxX(); x++) {
			for (int y = grid.getMinY(); y <= grid.getMaxY(); y++) {
				currentCell = grid.getCellByCoordinates(new Coordinate(x, y));
				if (currentCell.isDisabled()) {
					disabledCells.add(currentCell);
				} else if (currentCell.isEnabled()) {
					enabledCells.add(currentCell);
				} else if (currentCell.hasTile()) {
					hasTileCells.add(currentCell);
				}
			}
		}

		assertTrue(UtilitiesForJUnit.isEquivalentArray(disabledCells,
				grid.getCellByStatus(CellStatus.DISABLED)));
		assertTrue(UtilitiesForJUnit.isEquivalentArray(enabledCells,
				grid.getCellByStatus(CellStatus.ENABLED)));
		assertTrue(UtilitiesForJUnit.isEquivalentArray(hasTileCells,
				grid.getCellByStatus(CellStatus.HASTILE)));
	}

	@Test
	public void automaticGridExpansionTest() {
		try {
			grid.placeTile(new Coordinate(1, 0), straightCityNSconn);
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}
		assertEquals(2, grid.getMaxX());
		assertEquals(1, grid.getMaxY());
		assertEquals(-1, grid.getMinX());
		assertEquals(-1, grid.getMinY());
		assertEquals(grid.getCellByCoordinates(new Coordinate(1, 0)),
				grid.getLastInsertedCell());
		try {
			grid.placeTile(new Coordinate(0, -1), straightCityNSconn);
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}
		assertEquals(2, grid.getMaxX());
		assertEquals(1, grid.getMaxY());
		assertEquals(-1, grid.getMinX());
		assertEquals(-2, grid.getMinY());
		assertEquals(grid.getCellByCoordinates(new Coordinate(0, -1)),
				grid.getLastInsertedCell());
		try {
			grid.placeTile(new Coordinate(-1, -1), straightCityNSconn);
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}
		assertEquals(2, grid.getMaxX());
		assertEquals(1, grid.getMaxY());
		assertEquals(-2, grid.getMinX());
		assertEquals(-2, grid.getMinY());
		assertEquals(grid.getCellByCoordinates(new Coordinate(-1, -1)),
				grid.getLastInsertedCell());
		try {
			grid.placeTile(new Coordinate(12, -1), straightCityNSconn);
		} catch (InvalidCellException e) {
			testOK = true;
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}
		assertTrue(testOK);
	}

	@Test
	public void getLastInsertedCellTest() {
		try {
			grid.placeTile(new Coordinate(1, 0), straightCityNSconn);
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}
		assertEquals(grid.getCellByCoordinates(new Coordinate(1, 0)),
				grid.getLastInsertedCell());
		try {
			grid.placeTile(new Coordinate(0, -1), straightCityNSconn);
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}
		assertEquals(grid.getCellByCoordinates(new Coordinate(0, -1)),
				grid.getLastInsertedCell());
		try {
			grid.placeTile(new Coordinate(-1, -1), straightCityNSconn);
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}
		assertEquals(grid.getCellByCoordinates(new Coordinate(-1, -1)),
				grid.getLastInsertedCell());
	}

	@Test
	public void getCellsByHeightTest() {
		ArrayList<Cell> cells;
		for (int i = grid.getMinX(); i < grid.getMaxX(); i++) {
			cells = (ArrayList<Cell>) grid.getCellsByHeight(-1);
			assertEquals(3, cells.size());
			assertTrue(grid.getCellByCoordinates(new Coordinate(i, -1)) == cells
					.get(i + 1));
		}
	}

	@Test
	public void foundMovesTest() {
		grid = new Grid(firstTile);
		List<Cell> cells;
		Tile tile;
		tile = singleCityN;
		cells = grid.getPossibleMoves(tile);
		assertEquals(2, cells.size());
		assertEquals(grid.getCellByCoordinates(new Coordinate(0, 1)),
				cells.get(0));
		assertEquals(grid.getCellByCoordinates(new Coordinate(0, -1)),
				cells.get(1));

		tile = singleCityS;
		cells = grid.getPossibleMoves(tile);
		assertEquals(0, cells.size());

		tile = elbowStreetSWconn;
		cells = grid.getPossibleMoves(tile);
		assertEquals(1, cells.size());
		assertEquals(grid.getCellByCoordinates(new Coordinate(1, 0)),
				cells.get(0));
	}

	@Test
	public void addCellTest() {
		Cell cell = grid.addCell(new Coordinate(3, 0));
		assertEquals(cell, grid.getCellByCoordinates(new Coordinate(3, 0)));
	}

	@Test
	public void canPlaceTileTest() {
		grid = new Grid(allCity);
		Tile tile;

		tile = singleCityN;
		assertTrue(grid.canPlaceTile(tile));

		tile = elbowCityNEconn;
		assertTrue(grid.canPlaceTile(tile));

		tile = elbowCityNEnotConn;
		assertTrue(grid.canPlaceTile(tile));

		tile = elbowStreetNEconn;
		assertFalse(grid.canPlaceTile(tile));

		tile = straightStreetNSconn;
		assertFalse(grid.canPlaceTile(tile));

		tile = straightStreetNSnotConn;
		assertFalse(grid.canPlaceTile(tile));

		grid = new Grid(firstTile);

		tile = singleCityS;
		assertTrue(grid.canPlaceTile(tile));

		tile = elbowCityNEconn;
		assertTrue(grid.canPlaceTile(tile));

		tile = elbowCitySEconn;
		assertTrue(grid.canPlaceTile(tile));

		tile = elbowStreetNEconn;
		assertTrue(grid.canPlaceTile(tile));

		tile = straightStreetNSconn;
		assertTrue(grid.canPlaceTile(tile));

		tile = straightStreetNSnotConn;
		assertTrue(grid.canPlaceTile(tile));

	}

	@Test
	public void setMarker_DoubleStreet_Test() {
		grid = new Grid(straightStreetWEconn);
		try {
			grid.placeTile(new Coordinate(1, 0), straightStreetWEconn);
			grid.placeTile(new Coordinate(2, 0), elbowStreetNWnotConn);
			grid.setMarker(new Player(PlayersColor.BLUE), CardinalPoint.WEST);
			grid.placeTile(new Coordinate(2, 1), elbowStreetSWconn);
			grid.placeTile(new Coordinate(1, 1), straightStreetWEconn);
			grid.placeTile(new Coordinate(0, 1), straightStreetWEconn);
			grid.placeTile(new Coordinate(-1, 1), elbowStreetSEconn);
			grid.setMarker(new Player(PlayersColor.RED), CardinalPoint.SOUTH);
			grid.placeTile(new Coordinate(-1, 0), elbowStreetNEconn);
			try {
				grid.setMarker(new Player(PlayersColor.YELLOW),
						CardinalPoint.NORTH);
			} catch (OccupiedFieldException e) {
				assertEquals(Field.STREET, e.getField());
				testOK = true;
			}
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}
		assertTrue(testOK);
	}

	@Test
	public void setMarker_CircleStreet_Test() {
		grid = new Grid(elbowStreetNEnotConn);
		try {
			grid.placeTile(new Coordinate(1, 0), elbowStreetNWconn);
			grid.setMarker(new Player(PlayersColor.BLACK), CardinalPoint.NORTH);
			grid.placeTile(new Coordinate(1, 1), elbowStreetSWconn);
			grid.placeTile(new Coordinate(0, 1), elbowStreetSEconn);
			try {
				grid.setMarker(new Player(PlayersColor.RED), CardinalPoint.EAST);
			} catch (OccupiedFieldException e) {
				testOK = true;
			}
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}
		assertTrue(testOK);
	}
}
