package it.polimi.carcassonne.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.polimi.carcassonne.server.model.Deck;
import it.polimi.carcassonne.server.model.Tile;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class DeckTest {
	private static final int TOT_TILE = 56;
	private Deck deck;
	private Tile drawedTile;

	@Before
	public void setUp() {
		deck = new Deck();
		try {
			deck.populate();
		} catch (IOException e) {
			fail("Non trovo carcassonne.txt");
		}
	}

	@Test
	public void getSizeTest() {
		assertEquals(TOT_TILE, deck.getSize());
	}

	@Test
	public void drawTileTest() {
		int previousSize = deck.getSize();
		int tileCount = 0;
		while (!deck.isEmpty()) {
			drawedTile = deck.drawTile();
			assertEquals(drawedTile, deck.getLastDrawedTile());
			assertEquals(previousSize - 1, deck.getSize());
			tileCount++;
			previousSize = deck.getSize();
		}
		assertEquals(TOT_TILE, tileCount);
	}

	@Test
	public void isEmptyTest() {
		for (int i = 0; i < TOT_TILE; i++) {
			deck.drawTile();
		}

		assertTrue(deck.isEmpty());
	}

	@Test
	public void setLastDrawedTest() {
		Tile tile = new Tile("N=N E=S S=C W=S NE=0 NW=0 NS=0 SE=0 SW=0 EW=1");
		deck.setLastDrawedTile(tile);
		assertEquals(tile, deck.getLastDrawedTile());
	}

}
