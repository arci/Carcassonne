package it.polimi.carcassonne.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.polimi.carcassonne.server.model.Cell;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Grid;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.CellStatus;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;

import org.junit.Before;
import org.junit.Test;

public class CellTest {
	private Grid grid;
	private Cell cell;

	@Before
	public void setUp() {
		cell = null;
		grid = new Grid(new Tile(
				"N=N E=S S=C W=S NE=0 NW=0 NS=0 SE=0 SW=0 EW=1"));
	}

	@Test
	public void getCoordinatesTest() {
		cell = grid.getCellByCoordinates(new Coordinate(0, 0));
		assertEquals(new Coordinate(0, 0), cell.getCoordinates());
		assertEquals(0, cell.getX());
		assertEquals(0, cell.getY());

		try {
			cell = new Cell(new Coordinate("5,7"), grid);
		} catch (InvalidCellException e) {
			fail("Sbagliato a creare coordinate!");
		}
		assertEquals(new Coordinate(5, 7), cell.getCoordinates());
		assertEquals(5, cell.getX());
		assertEquals(7, cell.getY());
	}

	@Test
	public void statusTests() {
		cell = new Cell(new Coordinate(1, 0), grid);
		assertEquals(CellStatus.DISABLED, cell.getStatus());
		assertTrue(cell.isDisabled());
		assertEquals("Disabled", cell.getStatus().toString());

		cell.setStatus(CellStatus.ENABLED);
		assertEquals(CellStatus.ENABLED, cell.getStatus());
		assertTrue(cell.isEnabled());
		assertEquals("Enabled", cell.getStatus().toString());

		Tile tile = new Tile("N=N E=S S=C W=S NE=0 NW=0 NS=0 SE=0 SW=0 EW=1");
		try {
			grid.placeTile(new Coordinate(1, 0), tile);
		} catch (Exception e) {
			fail("Errori nel posizionamento: " + e.getMessage());
		}
		cell = grid.getCellByCoordinates((new Coordinate(1, 0)));
		assertEquals(tile, cell.getTile());
		assertEquals(CellStatus.HASTILE, cell.getStatus());
		assertTrue(cell.hasTile());
		assertEquals("Has Tile", cell.getStatus().toString());

		cell.setStatus(CellStatus.ENABLED);
		assertEquals(CellStatus.ENABLED, cell.getStatus());
		assertTrue(cell.isEnabled());
		assertEquals("Enabled", cell.getStatus().toString());

		cell.setStatus(CellStatus.DISABLED);
		assertEquals(CellStatus.DISABLED, cell.getStatus());
		assertTrue(cell.isDisabled());
		assertEquals("Disabled", cell.getStatus().toString());

		cell.setStatus(CellStatus.HASTILE);
		assertEquals(CellStatus.HASTILE, cell.getStatus());
		assertTrue(cell.hasTile());
		assertEquals("Has Tile", cell.getStatus().toString());
	}

	@Test
	public void tileTests() {
		cell = grid.getCellByCoordinates(new Coordinate(0, 0));
		cell.setTile(new Tile("N=S S=N W=N E=S NS=0 NE=1 NW=0 WE=0 SE=0 SW=0"));
		assertTrue(cell.hasTile());

		assertEquals(new Tile("N=S S=N W=N E=S NS=0 NE=1 NW=0 WE=0 SE=0 SW=0"),
				cell.getTile());
	}

	@Test
	public void getNeighborTest() {
		cell = grid.getCellByCoordinates(new Coordinate(0, 0));
		assertEquals(cell.getCoordinates().getNorth(),
				cell.getNeighbor(CardinalPoint.NORTH).getCoordinates());
		assertEquals(cell.getCoordinates().getSouth(),
				cell.getNeighbor(CardinalPoint.SOUTH).getCoordinates());
		assertEquals(cell.getCoordinates().getWest(),
				cell.getNeighbor(CardinalPoint.WEST).getCoordinates());
		assertEquals(cell.getCoordinates().getEast(),
				cell.getNeighbor(CardinalPoint.EAST).getCoordinates());
	}

	@Test
	public void isOnBorderTests() {
		cell = grid.getCellByCoordinates(new Coordinate(-1, 0));
		assertTrue(cell.isOnWestBorder());
		assertFalse(cell.isOnEastBorder());
		assertFalse(cell.isOnSouthBorder());
		cell = grid.getCellByCoordinates(new Coordinate(-1, -1));
		assertTrue(cell.isOnWestBorder());
		assertFalse(cell.isOnEastBorder());
		assertTrue(cell.isOnSouthBorder());
		cell = grid.getCellByCoordinates(new Coordinate(0, -1));
		assertFalse(cell.isOnWestBorder());
		assertFalse(cell.isOnEastBorder());
		assertTrue(cell.isOnSouthBorder());
		cell = grid.getCellByCoordinates(new Coordinate(1, -1));
		assertFalse(cell.isOnWestBorder());
		assertTrue(cell.isOnEastBorder());
		assertTrue(cell.isOnSouthBorder());
		cell = grid.getCellByCoordinates(new Coordinate(1, 0));
		assertFalse(cell.isOnWestBorder());
		assertTrue(cell.isOnEastBorder());
		assertFalse(cell.isOnSouthBorder());
	}
}
