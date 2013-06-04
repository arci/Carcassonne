package it.polimi.carcassonne.test;

import static it.polimi.carcassonne.server.model.Settings.MAX_MARKERS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Grid;
import it.polimi.carcassonne.server.model.Marker;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import org.junit.Before;
import org.junit.Test;

public class GridCheckClosureTest {

	private Grid grid;

	private Player redPlayer;
	private Player bluePlayer;
	private Player greenPlayer;
	private Player yellowPlayer;
	private Player blackPlayer;

	private Tile elbowStreetNEconn;
	private Tile elbowStreetNWconn;
	private Tile elbowStreetSWconn;
	private Tile elbowStreetSEconn;
	private Tile elbowStreetNEnotConn;
	private Tile straightStreetWEconn;
	private Tile straightStreetNSconn;
	private Tile straightStreetWEnotConn;
	private Tile straightStreetNSnotConn;
	private Tile straightStreetWEconnCityN;
	private Tile straightStreetWEnotConnCityS;
	private Tile crossStreetNSWEnotConn;
	private Tile elbowCityNEconn;
	private Tile elbowCityNWconn;
	private Tile elbowCitySWconn;
	private Tile elbowCitySEconn;
	private Tile elbowCityNEnotConn;
	private Tile straightCityWEconn;
	private Tile straightCityNSconn;
	private Tile straightCityWEnotConn;
	private Tile straightCityNSnotConn;
	private Tile threeWayCityNWE;
	private Tile threeWayCitySWE;
	private Tile threeWayCityNSW;
	private Tile singleCityN;
	private Tile singleCityE;
	private Tile singleCityS;

	@Before
	public void setUp() {

		redPlayer = new Player(PlayersColor.RED);
		bluePlayer = new Player(PlayersColor.BLUE);
		greenPlayer = new Player(PlayersColor.GREEN);
		yellowPlayer = new Player(PlayersColor.YELLOW);
		blackPlayer = new Player(PlayersColor.BLACK);

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
		straightStreetWEconn = new Tile(
				"N=N E=S S=N W=S NE=0 NW=0 NS=0 SE=0 SW=0 EW=1");
		straightStreetNSconn = new Tile(
				"N=S E=N S=S W=N NE=0 NW=0 NS=1 SE=0 SW=0 EW=0");
		straightStreetWEnotConn = new Tile(
				"N=N E=S S=N W=S NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		straightStreetNSnotConn = new Tile(
				"N=S E=N S=S W=N NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		straightStreetWEconnCityN = new Tile(
				"N=C E=S S=N W=S NE=0 NW=0 NS=0 SE=0 SW=0 EW=1");
		straightStreetWEnotConnCityS = new Tile(
				"N=N E=S S=C W=S NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		crossStreetNSWEnotConn = new Tile(
				"N=S E=S S=S W=S NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
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
		straightCityWEnotConn = new Tile(
				"N=N E=C S=N W=C NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		straightCityNSnotConn = new Tile(
				"N=C E=N S=C W=N NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");

		threeWayCityNWE = new Tile(
				"N=C E=C S=N W=C NE=1 NW=1 NS=0 SE=0 SW=0 EW=1");
		threeWayCitySWE = new Tile(
				"N=N E=C S=C W=C NE=0 NW=0 NS=0 SE=1 SW=1 EW=1");
		threeWayCityNSW = new Tile(
				"N=C E=N S=C W=C NE=0 NW=1 NS=1 SE=0 SW=1 EW=0");
		singleCityN = new Tile("N=C E=N S=N W=N NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		singleCityE = new Tile("N=N E=C S=N W=N NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		singleCityS = new Tile("N=N E=N S=C W=N NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");

		grid = new Grid(singleCityS);
	}

	@Test
	public void checkCrossToCrossStreetClosed() {
		grid = new Grid(straightStreetWEnotConn);
		try {
			grid.placeTile(new Coordinate(1, 0), straightStreetWEconn);
			grid.placeTile(new Coordinate(2, 0), elbowStreetNWconn);
			grid.placeTile(new Coordinate(2, 1), straightStreetNSconn);
			grid.placeTile(new Coordinate(2, 2), straightStreetNSnotConn);

			Marker marker = redPlayer.getMarker();
			marker.setPosition(CardinalPoint.NORTH);
			grid.getCellByCoordinates(new Coordinate(2, 0)).getTile()
					.setMarker(marker);

		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}

		assertEquals(0, redPlayer.getScore());
		assertEquals(MAX_MARKERS - 1, redPlayer.getAvailableMarkerCount());
		grid.checkClosedBuildings(grid
				.getCellByCoordinates(new Coordinate(2, 2)));
		assertEquals(5, redPlayer.getScore());
		assertEquals(MAX_MARKERS, redPlayer.getAvailableMarkerCount());
	}

	@Test
	public void checkCircleStreetClosed() {
		grid = new Grid(elbowStreetNEconn);
		try {
			grid.placeTile(new Coordinate(1, 0), elbowStreetNWconn);
			grid.placeTile(new Coordinate(1, 1), elbowStreetSWconn);
			grid.placeTile(new Coordinate(0, 1), elbowStreetSEconn);

			Marker marker = bluePlayer.getMarker();
			marker.setPosition(CardinalPoint.NORTH);
			grid.getCellByCoordinates(new Coordinate(1, 0)).getTile()
					.setMarker(marker);

		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}

		assertEquals(0, bluePlayer.getScore());
		assertEquals(MAX_MARKERS - 1, bluePlayer.getAvailableMarkerCount());
		grid.checkClosedBuildings(grid
				.getCellByCoordinates(new Coordinate(1, 1)));
		assertEquals(4, bluePlayer.getScore());
		assertEquals(MAX_MARKERS, bluePlayer.getAvailableMarkerCount());
	}

	@Test
	public void checkCircleCrossToSameCrossStreetClosed() {
		grid = new Grid(elbowStreetNEnotConn);
		try {
			grid.placeTile(new Coordinate(1, 0), elbowStreetNWconn);
			grid.placeTile(new Coordinate(1, 1), elbowStreetSWconn);
			grid.placeTile(new Coordinate(0, 1), elbowStreetSEconn);

			Marker marker = redPlayer.getMarker();
			marker.setPosition(CardinalPoint.NORTH);
			grid.getCellByCoordinates(new Coordinate(0, 0)).getTile()
					.setMarker(marker);
			Marker marker2 = bluePlayer.getMarker();
			marker2.setPosition(CardinalPoint.WEST);
			grid.getCellByCoordinates(new Coordinate(1, 0)).getTile()
					.setMarker(marker2);

		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}

		assertEquals(0, redPlayer.getScore());
		assertEquals(MAX_MARKERS - 1, redPlayer.getAvailableMarkerCount());
		assertEquals(0, bluePlayer.getScore());
		assertEquals(MAX_MARKERS - 1, bluePlayer.getAvailableMarkerCount());
		grid.checkClosedBuildings(grid
				.getCellByCoordinates(new Coordinate(0, 0)));
		assertEquals(4, redPlayer.getScore());
		assertEquals(MAX_MARKERS, redPlayer.getAvailableMarkerCount());
		assertEquals(4, bluePlayer.getScore());
		assertEquals(MAX_MARKERS, bluePlayer.getAvailableMarkerCount());
	}

	@Test
	public void checkBoundToBoundCityClosed() {
		grid = new Grid(straightCityWEnotConn);
		try {
			grid.placeTile(new Coordinate(1, 0), straightCityWEconn);
			grid.placeTile(new Coordinate(2, 0), elbowCityNWconn);
			grid.placeTile(new Coordinate(2, 1), straightCityNSconn);
			grid.placeTile(new Coordinate(2, 2), straightCityNSnotConn);

			Marker marker = yellowPlayer.getMarker();
			marker.setPosition(CardinalPoint.EAST);
			grid.getCellByCoordinates(new Coordinate(0, 0)).getTile()
					.setMarker(marker);

		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}

		assertEquals(0, yellowPlayer.getScore());
		assertEquals(MAX_MARKERS - 1, yellowPlayer.getAvailableMarkerCount());
		grid.checkClosedBuildings(grid
				.getCellByCoordinates(new Coordinate(2, 2)));
		assertEquals((5 * 2), yellowPlayer.getScore());
		assertEquals(MAX_MARKERS, yellowPlayer.getAvailableMarkerCount());

	}

	@Test
	public void checkCircleCityClosed() {
		grid = new Grid(elbowCityNEconn);
		try {
			grid.placeTile(new Coordinate(1, 0), elbowCityNWconn);
			grid.placeTile(new Coordinate(1, 1), elbowCitySWconn);
			grid.placeTile(new Coordinate(0, 1), elbowCitySEconn);

			Marker marker = blackPlayer.getMarker();
			marker.setPosition(CardinalPoint.EAST);
			grid.getCellByCoordinates(new Coordinate(0, 0)).getTile()
					.setMarker(marker);

		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}

		assertEquals(0, blackPlayer.getScore());
		assertEquals(MAX_MARKERS - 1, blackPlayer.getAvailableMarkerCount());
		grid.checkClosedBuildings(grid
				.getCellByCoordinates(new Coordinate(1, 1)));
		assertEquals((4 * 2), blackPlayer.getScore());
		assertEquals(MAX_MARKERS, blackPlayer.getAvailableMarkerCount());
	}

	@Test
	public void checkCircleBoundToSameBoundCityClosed() {
		grid = new Grid(elbowCityNEnotConn);
		try {
			grid.placeTile(new Coordinate(1, 0), elbowCityNWconn);
			grid.placeTile(new Coordinate(1, 1), elbowCitySWconn);
			grid.placeTile(new Coordinate(0, 1), elbowCitySEconn);

			Marker marker = redPlayer.getMarker();
			marker.setPosition(CardinalPoint.EAST);
			grid.getCellByCoordinates(new Coordinate(0, 0)).getTile()
					.setMarker(marker);

		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}

		assertEquals(0, redPlayer.getScore());
		assertEquals(MAX_MARKERS - 1, redPlayer.getAvailableMarkerCount());
		grid.checkClosedBuildings(grid
				.getCellByCoordinates(new Coordinate(1, 1)));
		assertEquals((4 * 2), redPlayer.getScore());
		assertEquals(MAX_MARKERS, redPlayer.getAvailableMarkerCount());
	}

	@Test
	public void checkDonutCityClosed() {
		grid = new Grid(straightCityWEconn);
		try {
			grid.placeTile(new Coordinate(1, 0), elbowCityNWconn);
			grid.placeTile(new Coordinate(1, 1), straightCityNSconn);
			grid.placeTile(new Coordinate(1, 2), elbowCitySWconn);
			grid.placeTile(new Coordinate(0, 2), straightCityWEconn);
			grid.placeTile(new Coordinate(-1, 2), elbowCitySEconn);
			grid.placeTile(new Coordinate(-1, 1), straightCityNSconn);
			grid.placeTile(new Coordinate(-1, 0), elbowCityNEconn);

			Marker marker = greenPlayer.getMarker();
			marker.setPosition(CardinalPoint.NORTH);
			grid.getCellByCoordinates(new Coordinate(-1, 0)).getTile()
					.setMarker(marker);

		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}

		assertEquals(0, greenPlayer.getScore());
		assertEquals(MAX_MARKERS - 1, greenPlayer.getAvailableMarkerCount());
		grid.checkClosedBuildings(grid
				.getCellByCoordinates(new Coordinate(1, 1)));
		assertEquals((8 * 2), greenPlayer.getScore());
		assertEquals(MAX_MARKERS, greenPlayer.getAvailableMarkerCount());
	}

	@Test
	public void checkExtremeCityClosed() {
		grid = new Grid(threeWayCityNWE);
		try {
			grid.placeTile(new Coordinate(1, 0), threeWayCityNWE);
			grid.placeTile(new Coordinate(2, 0), threeWayCityNSW);
			grid.placeTile(new Coordinate(2, -1), singleCityN);
			grid.placeTile(new Coordinate(2, 1), straightCityNSconn);
			grid.placeTile(new Coordinate(2, 2), singleCityS);
			grid.placeTile(new Coordinate(1, 1), threeWayCityNSW);
			grid.placeTile(new Coordinate(1, 2), singleCityS);
			grid.placeTile(new Coordinate(0, 1), threeWayCitySWE);
			grid.placeTile(new Coordinate(-1, 1), singleCityE);
			grid.placeTile(new Coordinate(-1, 0), singleCityE);

			Marker marker = redPlayer.getMarker();
			marker.setPosition(CardinalPoint.SOUTH);
			grid.getCellByCoordinates(new Coordinate(2, 2)).getTile()
					.setMarker(marker);

		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}

		assertEquals(0, redPlayer.getScore());
		assertEquals(MAX_MARKERS - 1, redPlayer.getAvailableMarkerCount());
		grid.checkClosedBuildings(grid
				.getCellByCoordinates(new Coordinate(1, 1)));
		assertEquals((11 * 2), redPlayer.getScore());
		assertEquals(MAX_MARKERS, redPlayer.getAvailableMarkerCount());
	}

	@Test
	// chiude 3 costruzioni insieme, controllando che vengano dati punti e
	// segnalini ai relativi giocatori
	public void multiClosuresTest() {
		grid = new Grid(straightStreetWEnotConn);
		try {
			grid.placeTile(new Coordinate(1, 0), straightStreetWEconn);
			Tile straightStreetWEconn2 = new Tile(
					straightStreetWEconn.getString());
			grid.placeTile(new Coordinate(1, -1), straightStreetWEconn2);
			grid.placeTile(new Coordinate(2, -1), straightStreetWEconnCityN);
			grid.placeTile(new Coordinate(3, -1), elbowStreetNWconn);
			grid.placeTile(new Coordinate(3, 0), crossStreetNSWEnotConn);
			grid.placeTile(new Coordinate(2, 0), straightStreetWEnotConnCityS);

			Marker marker = redPlayer.getMarker();
			marker.setPosition(CardinalPoint.WEST);
			grid.getCellByCoordinates(new Coordinate(0, 0)).getTile()
					.setMarker(marker);

			marker = bluePlayer.getMarker();
			marker.setPosition(CardinalPoint.EAST);
			grid.getCellByCoordinates(new Coordinate(1, 0)).getTile()
					.setMarker(marker);

			marker = greenPlayer.getMarker();
			marker.setPosition(CardinalPoint.NORTH);
			grid.getCellByCoordinates(new Coordinate(2, -1)).getTile()
					.setMarker(marker);

			marker = yellowPlayer.getMarker();
			marker.setPosition(CardinalPoint.WEST);
			grid.getCellByCoordinates(new Coordinate(3, 0)).getTile()
					.setMarker(marker);

			marker = blackPlayer.getMarker();
			marker.setPosition(CardinalPoint.NORTH);
			grid.getCellByCoordinates(new Coordinate(3, -1)).getTile()
					.setMarker(marker);

		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}

		assertEquals(0, redPlayer.getScore());
		assertEquals(0, bluePlayer.getScore());
		assertEquals(0, greenPlayer.getScore());
		assertEquals(0, yellowPlayer.getScore());
		assertEquals(0, blackPlayer.getScore());

		assertEquals(MAX_MARKERS - 1, redPlayer.getAvailableMarkerCount());
		assertEquals(MAX_MARKERS - 1, bluePlayer.getAvailableMarkerCount());
		assertEquals(MAX_MARKERS - 1, greenPlayer.getAvailableMarkerCount());
		assertEquals(MAX_MARKERS - 1, yellowPlayer.getAvailableMarkerCount());
		assertEquals(MAX_MARKERS - 1, blackPlayer.getAvailableMarkerCount());

		grid.checkClosedBuildings(grid
				.getCellByCoordinates(new Coordinate(2, 0)));

		assertEquals(0, redPlayer.getScore());
		assertEquals(3, bluePlayer.getScore());
		assertEquals((2 * 2), greenPlayer.getScore());
		assertEquals(2, yellowPlayer.getScore());
		assertEquals(0, blackPlayer.getScore());

		assertEquals(MAX_MARKERS - 1, redPlayer.getAvailableMarkerCount());
		assertEquals(MAX_MARKERS, bluePlayer.getAvailableMarkerCount());
		assertEquals(MAX_MARKERS, greenPlayer.getAvailableMarkerCount());
		assertEquals(MAX_MARKERS, yellowPlayer.getAvailableMarkerCount());
		assertEquals(MAX_MARKERS - 1, blackPlayer.getAvailableMarkerCount());
	}
}
