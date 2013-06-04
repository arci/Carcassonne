package it.polimi.carcassonne.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class CoordinateTest {

	// Funziona fino oltre 1400, ma fallisce intorno ai 1400 perche' la virtual
	// machine Java finisce l'heap
	private static final int RANGE = 100;
	private Coordinate coord1, coord2;
	private boolean testOK;

	@Before
	public void setUp() {
		coord1 = new Coordinate(1, 1);
		coord2 = new Coordinate(1, 1);
	}

	@Test
	public void constructorTest() {
		assertEquals(1, coord1.getX());
		assertEquals(1, coord1.getY());
		assertEquals(1, coord2.getX());
		assertEquals(1, coord2.getY());

		try {
			coord2 = new Coordinate("1,1");
		} catch (InvalidCellException e) {
			fail("Errore nel costruttore a stringa delle coordinate!");
		}

		try {
			coord2 = new Coordinate("123,456");
		} catch (InvalidCellException e) {
			fail("Errore nel costruttore a stringa delle coordinate!");
		}

		testOK = false;
		try {
			coord2 = new Coordinate("ciao,123");
		} catch (InvalidCellException e) {
			testOK = true;
		}
		assertTrue(testOK);

		testOK = false;
		try {
			coord2 = new Coordinate("3,t");
		} catch (InvalidCellException e) {
			testOK = true;
		}
		assertTrue(testOK);

		coord2 = new Coordinate(12, 34);
		assertEquals(12, coord2.getX());
		assertEquals(34, coord2.getY());
	}

	@Test
	public void toStringTest() {
		assertEquals("1,1", coord1.toString());
		assertEquals("1,1", coord2.toString());

		coord1 = new Coordinate(2, 3);
		assertEquals("2,3", coord1.toString());

		try {
			coord2 = new Coordinate("123,456");
		} catch (InvalidCellException e) {
			fail("Errore nel costruttore a stringa delle coordinate!");
		}
		assertEquals("123,456", coord2.toString());
	}

	@Test
	public void getNeighborTest() {
		assertEquals(coord1.getX(), coord1.getEast().getX() - 1);
		assertEquals(coord1.getY(), coord1.getEast().getY());
		assertEquals(coord1.getX(), coord1.getWest().getX() + 1);
		assertEquals(coord1.getY(), coord1.getWest().getY());
		assertEquals(coord1.getX(), coord1.getNorth().getX());
		assertEquals(coord1.getY(), coord1.getNorth().getY() - 1);
		assertEquals(coord1.getX(), coord1.getSouth().getX());
		assertEquals(coord1.getY(), coord1.getSouth().getY() + 1);

		try {
			assertEquals(coord1.getNorth(), new Coordinate("1,2"));
		} catch (InvalidCellException e) {
			fail("Errori nella creazione della coordinata");
		}
		try {
			assertEquals(coord1.getEast(), new Coordinate("2,1"));
		} catch (InvalidCellException e) {
			fail("Errori nella creazione della coordinata");
		}
		assertEquals(coord2.getSouth(), new Coordinate(1, 0));
		assertEquals(coord2.getWest(), new Coordinate(0, 1));

	}

	@Test
	public void equalsTest() {
		for (int x = -RANGE; x < RANGE; x++) {
			for (int y = -RANGE; y < RANGE; y++) {
				coord1 = new Coordinate(x, y);
				coord2 = new Coordinate(x, y);
				assertTrue(coord1.equals(coord2));
			}
		}

		coord2 = null;
		assertFalse(coord1.equals(coord2));
		Object obj = new Object();
		assertFalse(coord1.equals(obj));
	}

	@Test
	public void hashCodeUnicityTest() {
		Set<Integer> coord = new HashSet<Integer>();
		int hashCode = 0;
		for (int x = -RANGE; x < RANGE; x++) {
			for (int y = -RANGE; y < RANGE; y++) {
				hashCode = new Coordinate(x, y).hashCode();
				assertFalse(coord.contains(hashCode));
				coord.add(hashCode);
			}
		}
	}

	@Test
	public void hashCodeConsistencyTest() {
		Map<Coordinate, Integer> map = new HashMap<Coordinate, Integer>();
		for (int x = -RANGE; x < RANGE; x++) {
			for (int y = -RANGE; y < RANGE; y++) {
				map.put(new Coordinate(x, y), new Coordinate(x, y).hashCode());
			}
		}

		for (int x = -RANGE; x < RANGE; x++) {
			for (int y = -RANGE; y < RANGE; y++) {
				assertTrue(new Coordinate(x, y).hashCode() == map
						.get(new Coordinate(x, y)));
			}
		}

	}
}
