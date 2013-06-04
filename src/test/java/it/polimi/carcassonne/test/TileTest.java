package it.polimi.carcassonne.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Grid;
import it.polimi.carcassonne.server.model.Marker;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.Field;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TileTest {

	private static final int MAX_MARKERS = 7;
	private Tile tile;
	private Grid grid;
	private List<CardinalPoint> expected;
	private List<String> tilesString = new ArrayList<String>();

	@Before
	public void setUp() {
		tile = new Tile("N=S S=N W=N E=S NS=0 NE=1 NW=0 WE=0 SE=0 SW=0");
		grid = new Grid(tile);
		expected = new ArrayList<CardinalPoint>();

		tilesString.add("N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0");
		tilesString.add("N=S S=S W=S E=S NS=0 NE=0 NW=0 WE=0 SE=0 SW=0");
		tilesString.add("N=C S=S W=S E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=1");
		tilesString.add("N=C S=N W=N E=C NS=0 NE=0 NW=0 WE=0 SE=0 SW=0");
		tilesString.add("N=C S=S W=N E=S NS=0 NE=0 NW=0 WE=0 SE=1 SW=0");
		tilesString.add("N=N S=N W=C E=C NS=0 NE=0 NW=0 WE=1 SE=0 SW=0");
		tilesString.add("N=C S=S W=C E=C NS=0 NE=1 NW=1 WE=1 SE=0 SW=0");
		tilesString.add("N=C S=C W=N E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=0");
		tilesString.add("N=C S=S W=S E=S NS=0 NE=0 NW=0 WE=0 SE=0 SW=0");
		tilesString.add("N=N S=S W=S E=S NS=0 NE=0 NW=0 WE=0 SE=0 SW=0");
		tilesString.add("N=C S=N W=C E=C NS=0 NE=1 NW=1 WE=1 SE=0 SW=0");
		tilesString.add("N=C S=N W=N E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=0");
		tilesString.add("N=C S=S W=C E=S NS=0 NE=0 NW=1 WE=0 SE=1 SW=0");
		tilesString.add("N=C S=N W=C E=N NS=0 NE=0 NW=1 WE=0 SE=0 SW=0");
		tilesString.add("N=S S=S W=N E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0");
		tilesString.add("N=N S=S W=S E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=1");
	}

	@Test
	public void constructorTest() {
		Tile tileTest = new Tile(
				"N=S E=S S=N W=N NE=1 NW=0 NS=0 SE=0 SW=0 EW=0");
		for (CardinalPoint cardinal : CardinalPoint.values()) {
			assertEquals(tileTest.getField(cardinal), tile.getField(cardinal));
		}
		assertEquals(tile.getField(CardinalPoint.NORTH), Field.STREET);
		assertEquals(tile.getField(CardinalPoint.EAST), Field.STREET);
		assertEquals(tile.getField(CardinalPoint.SOUTH), Field.NIL);
		assertEquals(tile.getField(CardinalPoint.WEST), Field.NIL);

		assertEquals(tileTest.getField(CardinalPoint.NORTH), Field.STREET);
		assertEquals(tileTest.getField(CardinalPoint.EAST), Field.STREET);
		assertEquals(tileTest.getField(CardinalPoint.SOUTH), Field.NIL);
		assertEquals(tileTest.getField(CardinalPoint.WEST), Field.NIL);

	}

	@Test
	public void getStringTest() {

		for (String s : tilesString) {
			tile = new Tile(s);
			assertEquals(s, tile.getString());
		}
	}

	@Test
	public void markerTests() {
		assertNull(tile.getMarker());
		assertNull(tile.getMarkerOwner());
		assertNull(tile.getMarkerPosition());

		Player player = new Player(PlayersColor.BLACK);
		Marker marker = player.getMarker();
		marker.setPosition(CardinalPoint.NORTH);
		tile.setMarker(marker);

		assertNotNull(tile.getMarker());
		assertNotNull(tile.getMarkerOwner());
		assertNotNull(tile.getMarkerPosition());
		assertEquals(marker, tile.getMarker());
		assertEquals(player, tile.getMarkerOwner());
		assertEquals(CardinalPoint.NORTH, tile.getMarkerPosition());
		assertEquals(MAX_MARKERS - 1, player.getAvailableMarkerCount());

		tile.returnMarker();

		assertNull(tile.getMarker());
		assertNull(tile.getMarkerOwner());
		assertNull(tile.getMarkerPosition());
		assertEquals(MAX_MARKERS, player.getAvailableMarkerCount());
	}

	@Test
	public void rotateTest() {
		Tile correctTile = new Tile(
				"N=N E=S S=S W=N NE=0 NW=0 NS=0 SE=1 SW=0 EW=0");
		tile.rotate();
		for (CardinalPoint cardinal : CardinalPoint.values()) {
			assertEquals(correctTile.getField(cardinal),
					tile.getField(cardinal));
			assertEquals(correctTile.whereContinues(cardinal),
					tile.whereContinues(cardinal));
		}
	}

	@Test
	public void isCompatibleWithNeighborsTest() {
		Tile threeWayCityNWE = new Tile(
				"N=C E=C S=N W=C NE=1 NW=1 NS=0 SE=0 SW=0 EW=1");
		assertFalse(threeWayCityNWE.isCompatibleWithNeighbors(grid
				.getCellByCoordinates(new Coordinate(0, 1))));
		assertFalse(threeWayCityNWE.isCompatibleWithNeighbors(grid
				.getCellByCoordinates(new Coordinate(0, -1))));
		assertFalse(threeWayCityNWE.isCompatibleWithNeighbors(grid
				.getCellByCoordinates(new Coordinate(1, 0))));
		assertFalse(threeWayCityNWE.isCompatibleWithNeighbors(grid
				.getCellByCoordinates(new Coordinate(-1, 0))));

		Tile elbowStreetWithCity = new Tile(
				"N=C E=S S=S W=N NE=0 NW=0 NS=0 SE=1 SW=0 EW=0");
		assertTrue(elbowStreetWithCity.isCompatibleWithNeighbors(grid
				.getCellByCoordinates(new Coordinate(0, 1))));
		assertFalse(elbowStreetWithCity.isCompatibleWithNeighbors(grid
				.getCellByCoordinates(new Coordinate(0, -1))));
		assertFalse(elbowStreetWithCity.isCompatibleWithNeighbors(grid
				.getCellByCoordinates(new Coordinate(1, 0))));
		assertFalse(elbowStreetWithCity.isCompatibleWithNeighbors(grid
				.getCellByCoordinates(new Coordinate(-1, 0))));

		// Ora aggiungo delle celle alla griglia lasciando un buco in mezzo, in
		// cui inseriro' la tessera di test
		Tile straightStreetWEWithCityN = new Tile(
				"N=C E=S S=N W=S NE=0 NW=0 NS=0 SE=0 SW=0 EW=1");
		Tile elbowStreetNWconn = new Tile(
				"N=S E=N S=N W=S NE=0 NW=1 NS=0 SE=0 SW=0 EW=0");
		Tile straightStreetNSconn = new Tile(
				"N=S E=N S=S W=N NE=0 NW=0 NS=1 SE=0 SW=0 EW=0");
		Tile elbowStreetSWconn = new Tile(
				"N=N E=N S=S W=S NE=0 NW=0 NS=0 SE=0 SW=1 EW=0");
		Tile straightStreetWEWithStreetS = new Tile(
				"N=N E=S S=S W=S NE=0 NW=0 NS=0 SE=0 SW=0 EW=1");
		Tile elbowStreetSEconn = new Tile(
				"N=N E=S S=S W=N NE=0 NW=0 NS=0 SE=1 SW=0 EW=0");
		Tile straightStreetNSWithCityE = new Tile(
				"N=S E=C S=S W=N NE=0 NW=0 NS=1 SE=0 SW=0 EW=0");
		Tile tileTest = new Tile(
				"N=S E=N S=C W=C NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		try {
			grid.placeTile(new Coordinate(1, 0), straightStreetWEWithCityN);
			grid.placeTile(new Coordinate(2, 0), elbowStreetNWconn);
			grid.placeTile(new Coordinate(2, 1), straightStreetNSconn);
			grid.placeTile(new Coordinate(2, 2), elbowStreetSWconn);
			grid.placeTile(new Coordinate(1, 2), straightStreetWEWithStreetS);
			grid.placeTile(new Coordinate(0, 2), elbowStreetSEconn);
			grid.placeTile(new Coordinate(0, 1), straightStreetNSWithCityE);
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}
		assertTrue(tileTest.isCompatibleWithNeighbors(grid
				.getCellByCoordinates(new Coordinate(1, 1))));
		tileTest.rotate();
		assertFalse(tileTest.isCompatibleWithNeighbors(grid
				.getCellByCoordinates(new Coordinate(1, 1))));
	}

	@Test
	public void whereContinuesTest() {
		ArrayList<CardinalPoint> actual;

		tile = new Tile("N=C E=C S=N W=C NE=1 NW=1 NS=0 SE=0 SW=0 EW=1");
		Tile tileTest = new Tile(
				"N=C E=C S=N W=C NE=1 NW=1 NS=0 SE=0 SW=0 EW=1");
		for (CardinalPoint cardinal : CardinalPoint.values()) {
			for (CardinalPoint card : tileTest.whereContinues(cardinal))
				assertTrue(tile.whereContinues(cardinal).contains(card));
		}

		actual = (ArrayList<CardinalPoint>) tile
				.whereContinues(CardinalPoint.NORTH);
		expected.add(CardinalPoint.WEST);
		expected.add(CardinalPoint.EAST);
		for (CardinalPoint cardinal : expected) {
			assertTrue(actual.contains(cardinal));
		}
		expected.clear();
		actual = (ArrayList<CardinalPoint>) tile
				.whereContinues(CardinalPoint.WEST);
		expected.add(CardinalPoint.NORTH);
		expected.add(CardinalPoint.EAST);
		for (CardinalPoint cardinal : expected) {
			assertTrue(actual.contains(cardinal));
		}
		expected.clear();
		actual = (ArrayList<CardinalPoint>) tile
				.whereContinues(CardinalPoint.EAST);
		expected.add(CardinalPoint.NORTH);
		expected.add(CardinalPoint.WEST);
		for (CardinalPoint cardinal : expected) {
			assertTrue(actual.contains(cardinal));
		}
		actual = (ArrayList<CardinalPoint>) tile
				.whereContinues(CardinalPoint.SOUTH);
		assertTrue(actual.size() == 0);

		tile = new Tile("N=S E=S S=S W=S NE=1 NW=0 NS=0 SE=0 SW=1 EW=0");
		actual = (ArrayList<CardinalPoint>) tile
				.whereContinues(CardinalPoint.NORTH);
		assertTrue(actual.size() == 1 && actual.contains(CardinalPoint.EAST));
		actual = (ArrayList<CardinalPoint>) tile
				.whereContinues(CardinalPoint.EAST);
		assertTrue(actual.size() == 1 && actual.contains(CardinalPoint.NORTH));
		actual = (ArrayList<CardinalPoint>) tile
				.whereContinues(CardinalPoint.WEST);
		assertTrue(actual.size() == 1 && actual.contains(CardinalPoint.SOUTH));
		actual = (ArrayList<CardinalPoint>) tile
				.whereContinues(CardinalPoint.SOUTH);
		assertTrue(actual.size() == 1 && actual.contains(CardinalPoint.WEST));
	}

	@Test
	public void equalsTest() {
		String tileDesc = "N=N E=S S=S W=N NE=0 NW=0 NS=0 SE=1 SW=0 EW=0";
		Player redPlayer = new Player(PlayersColor.RED);
		Player bluePlayer = new Player(PlayersColor.BLUE);
		Tile tile1 = null;
		Tile tile2 = null;
		Tile previous = null;
		Marker marker1 = null;
		Marker marker2 = null;

		for (String s : tilesString) {
			tile1 = new Tile(s);
			tile2 = new Tile(s);
			assertEquals(tile1, tile2);
			assertFalse(tile1.equals(previous));
			marker1 = new Marker(new Player(PlayersColor.RED));
			marker1.setPosition(CardinalPoint.NORTH);
			tile1.setMarker(marker1);
			tile2.setMarker(marker1);
			assertEquals(tile1, tile2);
			assertEquals(tile2, tile1);
			previous = new Tile(s);
		}

		tile1 = new Tile(tileDesc);
		tile2 = null;
		assertFalse(tile1.equals(tile2));

		Object obj = new Object();
		assertFalse(tile1.equals(obj));

		tile1 = new Tile(tileDesc);
		tile2 = new Tile(tileDesc);
		assertEquals(tile1, tile2);
		tile1.setMarker(new Marker(new Player(PlayersColor.RED)));
		assertFalse(tile1.equals(tile2));

		tile1 = new Tile(tileDesc);
		tile2 = new Tile(tileDesc);
		assertEquals(tile1, tile2);
		tile2.setMarker(new Marker(new Player(PlayersColor.RED)));
		assertFalse(tile1.equals(tile2));

		tile1 = new Tile(tileDesc);
		tile2 = new Tile(tileDesc);
		assertEquals(tile1, tile2);
		marker1 = redPlayer.getMarker();
		marker2 = redPlayer.getMarker();
		marker1.setPosition(CardinalPoint.NORTH);
		marker2.setPosition(CardinalPoint.NORTH);
		tile1.setMarker(marker1);
		tile2.setMarker(marker2);
		assertEquals(tile1, tile2);
		marker1 = bluePlayer.getMarker();
		marker1.setPosition(CardinalPoint.EAST);
		tile1.setMarker(marker1);
		assertFalse(tile1.equals(tile2));
		marker2 = bluePlayer.getMarker();
		marker2.setPosition(CardinalPoint.EAST);
		tile2.setMarker(marker2);
		assertEquals(tile1, tile2);
		marker1.setPosition(CardinalPoint.SOUTH);
		tile1.setMarker(marker1);
		assertFalse(tile1.equals(tile2));
		marker2.setPosition(CardinalPoint.SOUTH);
		assertEquals(tile1, tile2);
	}

	@Test
	public void hashCodeUnicityTest() {
		int hashCode = 0;
		Tile tile = null;
		Set<Integer> coord = new HashSet<Integer>();

		for (String s : tilesString) {
			tile = new Tile(s);
			hashCode = tile.hashCode();
			assertFalse(coord.contains(hashCode));
			coord.add(hashCode);
		}
	}

	@Test
	public void hashCodeConsistencyTest() {
		Map<Tile, Integer> map = new HashMap<Tile, Integer>();

		for (String s : tilesString) {
			tile = new Tile(s);
			map.put(tile, tile.hashCode());
		}

		for (String s : tilesString) {
			tile = new Tile(s);
			assertTrue(tile.hashCode() == map.get(tile));
		}
	}
}
