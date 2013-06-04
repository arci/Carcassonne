package it.polimi.carcassonne.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.polimi.carcassonne.server.model.BuildingInfo;
import it.polimi.carcassonne.server.model.Cell;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Grid;
import it.polimi.carcassonne.server.model.Marker;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.CellStatus;
import it.polimi.carcassonne.server.model.enums.Field;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class BuildingInfoTest {
	private Grid grid;
	private BuildingInfo building;

	private ArrayList<Cell> cells1;
	private ArrayList<Cell> cells2;

	private Tile elbowStreetNEconn;
	private Tile elbowStreetSWconn;
	private Tile elbowStreetSEconn;
	private Tile elbowStreetNWnotConn;
	private Tile straightStreetWEconn;
	private Tile elbowCityNEconn;
	private Tile elbowCityNWconn;
	private Tile elbowCitySWconn;
	private Tile elbowCitySEconn;
	private Tile elbowCityNEnotConn;
	private Tile straightCityWEconn;
	private Tile straightCityNSconn;

	boolean testOK;

	@Before
	public void setUp() throws Exception {

		cells1 = new ArrayList<Cell>();
		cells2 = new ArrayList<Cell>();

		testOK = false;

		elbowStreetNEconn = new Tile(
				"N=S E=S S=N W=N NE=1 NW=0 NS=0 SE=0 SW=0 EW=0");
		elbowStreetSWconn = new Tile(
				"N=N E=N S=S W=S NE=0 NW=0 NS=0 SE=0 SW=1 EW=0");
		elbowStreetSEconn = new Tile(
				"N=N E=S S=S W=N NE=0 NW=0 NS=0 SE=1 SW=0 EW=0");
		elbowStreetNWnotConn = new Tile(
				"N=S E=N S=N W=S NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		straightStreetWEconn = new Tile(
				"N=N E=S S=N W=S NE=0 NW=0 NS=0 SE=0 SW=0 EW=1");
		elbowCityNEconn = new Tile(
				"N=C E=C S=N W=N NE=1 NW=0 NS=0 SE=0 SW=0 EW=0");
		elbowCityNWconn = new Tile(
				"N=C E=N S=N W=C NE=0 NW=1 NS=0 SE=0 SW=0 EW=0");
		elbowCitySWconn = new Tile(
				"N=N E=N S=C W=C NE=0 NW=0 NS=0 SE=0 SW=1 EW=0");
		elbowCitySEconn = new Tile(
				"N=N E=C S=C W=N NE=0 NW=0 NS=0 SE=1 SW=0 EW=0");

		elbowCityNEnotConn = new Tile(
				"N=C E=C S=N W=N NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		straightCityWEconn = new Tile(
				"N=N E=C S=N W=C NE=0 NW=0 NS=0 SE=0 SW=0 EW=1");
		straightCityNSconn = new Tile(
				"N=C E=N S=C W=N NE=0 NW=0 NS=1 SE=0 SW=0 EW=0");

		grid = new Grid(straightCityWEconn);

	}

	@Test
	public void getCellsToCheckTest() {
		building = new BuildingInfo(grid.getCellByCoordinates(new Coordinate(0,
				0)), CardinalPoint.WEST, true);
		cells1.add(grid.getCellByCoordinates(new Coordinate(0, 0)));
		cells2.addAll(building.getCellsToCheck());
		assertEquals(cells1, cells2);
	}

	@Test
	public void getCellsCheckedTest() {
		try {
			grid.placeTile(new Coordinate(1, 0), elbowCityNWconn);
			grid.placeTile(new Coordinate(1, 1), straightCityNSconn);
			grid.placeTile(new Coordinate(1, 2), elbowCitySWconn);
			grid.placeTile(new Coordinate(0, 2), straightCityWEconn);
			grid.placeTile(new Coordinate(-1, 2), elbowCitySEconn);
			grid.placeTile(new Coordinate(-1, 1), straightCityNSconn);
			grid.placeTile(new Coordinate(-1, 0), elbowCityNEconn);
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}

		building = new BuildingInfo(grid.getCellByCoordinates(new Coordinate(0,
				0)), CardinalPoint.WEST, true);

		cells1.addAll(grid.getCellByStatus(CellStatus.HASTILE));
		cells2.addAll(building.getCellsOfConstruction());
		assertTrue(UtilitiesForJUnit.isEquivalentArray(cells1, cells2));
	}

	@Test
	public void getCellsWithMarkerPassed_closedBuilding_Test() {
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
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}
		building = new BuildingInfo(grid.getCellByCoordinates(new Coordinate(0,
				0)), CardinalPoint.WEST, true);
		assertEquals(8, building.getCellsOfConstruction().size());
		assertEquals(2, building.getCellsWithMarkerPassed().size());

		int red = 0;
		int blue = 0;
		for (Cell cell : building.getCellsWithMarkerPassed()) {
			if (cell.getTile().getMarker().getColor() == PlayersColor.RED) {
				red++;
			} else if (cell.getTile().getMarker().getColor() == PlayersColor.BLUE) {
				blue++;
			}
		}
		assertEquals(1, red);
		assertEquals(1, blue);

		grid.checkClosedBuildings(grid.getCellByCoordinates(new Coordinate(-1,
				0)));
		building = new BuildingInfo(grid.getCellByCoordinates(new Coordinate(0,
				0)), CardinalPoint.WEST, true);
		assertEquals(8, building.getCellsOfConstruction().size());
		assertEquals(0, building.getCellsWithMarkerPassed().size());
	}

	@Test
	public void getCellsWithMarkerPassed_openBuilding_Test() {
		grid = new Grid(straightStreetWEconn);
		try {
			grid.placeTile(new Coordinate(1, 0), straightStreetWEconn);
			grid.placeTile(new Coordinate(1, 1), straightStreetWEconn);
			grid.placeTile(new Coordinate(0, 1), straightStreetWEconn);
			grid.placeTile(new Coordinate(-1, 1), elbowStreetSEconn);
			grid.setMarker(new Player(PlayersColor.RED), CardinalPoint.SOUTH);
			grid.placeTile(new Coordinate(2, 1), elbowStreetSWconn);
			grid.placeTile(new Coordinate(-1, 0), elbowStreetNEconn);
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}
		building = new BuildingInfo(grid.getCellByCoordinates(new Coordinate(0,
				0)), CardinalPoint.WEST, false);
		assertEquals(7, building.getCellsOfConstruction().size());
		assertEquals(1, building.getCellsWithMarkerPassed().size());

		// elimina segnalini solo se chiusa
		grid.checkClosedBuildings(grid.getCellByCoordinates(new Coordinate(-1,
				0)));
		building = new BuildingInfo(grid.getCellByCoordinates(new Coordinate(0,
				0)), CardinalPoint.WEST, false);
		assertEquals(7, building.getCellsOfConstruction().size());
		assertEquals(1, building.getCellsWithMarkerPassed().size());

		// elimina segnalini anche se aperta
		grid.checkIncompleteBuildings();
		building = new BuildingInfo(grid.getCellByCoordinates(new Coordinate(0,
				0)), CardinalPoint.WEST, false);
		assertEquals(7, building.getCellsOfConstruction().size());
		assertEquals(0, building.getCellsWithMarkerPassed().size());
	}

	@Test
	public void getFieldTest() {
		try {
			grid.placeTile(new Coordinate(1, 0), elbowCityNWconn);
			grid.placeTile(new Coordinate(1, 1), straightCityNSconn);
			grid.placeTile(new Coordinate(1, 2), elbowCitySWconn);
			grid.placeTile(new Coordinate(0, 2), straightCityWEconn);
			grid.placeTile(new Coordinate(-1, 2), elbowCitySEconn);
			grid.placeTile(new Coordinate(-1, 1), straightCityNSconn);
			grid.placeTile(new Coordinate(-1, 0), elbowCityNEconn);
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}

		building = new BuildingInfo(grid.getCellByCoordinates(new Coordinate(0,
				0)), CardinalPoint.WEST, true);

		cells1.addAll(grid.getCellByStatus(CellStatus.HASTILE));
		cells2.addAll(building.getCellsOfConstruction());
		assertTrue(UtilitiesForJUnit.isEquivalentArray(cells1, cells2));
	}

	@Test
	public void singleTileTests() {
		grid = new Grid(straightStreetWEconn);
		building = new BuildingInfo(grid.getCellByCoordinates(new Coordinate(0,
				0)), CardinalPoint.WEST, false);
		assertEquals(1, building.getCellsOfConstruction().size());
		assertEquals(0, building.getCellsWithMarkerPassed().size());
		assertFalse(building.isClosed());

		grid = new Grid(elbowCityNEnotConn);
		Player player = new Player(PlayersColor.BLACK);
		Marker marker = player.getMarker();
		marker.setPosition(CardinalPoint.EAST);
		grid.getCellByCoordinates(new Coordinate(0, 0)).getTile()
				.setMarker(marker);
		building = new BuildingInfo(grid.getCellByCoordinates(new Coordinate(0,
				0)), CardinalPoint.EAST, false);
		assertEquals(1, building.getCellsOfConstruction().size());
		assertEquals(1, building.getCellsWithMarkerPassed().size());
		assertFalse(building.isClosed());
		assertEquals(Field.CITY, building.getField());

		building = new BuildingInfo(grid.getCellByCoordinates(new Coordinate(0,
				0)), CardinalPoint.NORTH, false);
		assertEquals(0, building.getCellsWithMarkerPassed().size());
		assertEquals(Field.CITY, building.getField());
	}
}
