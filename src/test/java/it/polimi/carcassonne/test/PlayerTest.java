package it.polimi.carcassonne.test;

import static it.polimi.carcassonne.server.model.Settings.MAX_MARKERS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import it.polimi.carcassonne.server.model.Marker;
import it.polimi.carcassonne.server.model.Match;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {

	private Player player;
	private Marker marker;

	@Before
	public void setUp() {
		player = new Player(PlayersColor.YELLOW);
	}

	@Test
	public void constructorTest() {
		assertEquals(PlayersColor.YELLOW, player.getColor());
		assertEquals(0, player.getScore());
		assertEquals(MAX_MARKERS, player.getAvailableMarkerCount());
		assertFalse(player.isReadyToPlay());
	}

	@Test
	public void setAndGetMatchTest() {
		Match match = new Match("test");
		player.setMatch(match);
		assertEquals(match, player.getMatch());
	}

	@Test
	public void readyToPlayTest() {
		assertFalse(player.isReadyToPlay());
		player.setReadyToPlay();
		assertTrue(player.isReadyToPlay());
	}

	@Test
	public void colorTest() {
		assertEquals(PlayersColor.YELLOW, player.getColor());
		for (PlayersColor c : PlayersColor.values()) {
			player.setColor(c);
			assertEquals(c, player.getColor());
		}
	}

	@Test
	public void markerTests() {
		for (int i = MAX_MARKERS; i > 0; i--) {
			assertEquals(i, player.getAvailableMarkerCount());
			marker = player.getMarker();
			assertEquals(player, marker.getOwner());
			assertEquals(i - 1, player.getAvailableMarkerCount());
		}
		assertEquals(0, player.getAvailableMarkerCount());
		assertNull(player.getMarker());

		for (int i = 0; i < MAX_MARKERS; i++) {
			assertEquals(i, player.getAvailableMarkerCount());
			player.addMarker();
			assertEquals(i + 1, player.getAvailableMarkerCount());
		}

		assertEquals(MAX_MARKERS, player.getAvailableMarkerCount());
	}

	@Test
	public void scoreTests() {
		assertEquals(0, player.getScore());

		player.setScore(11);
		assertEquals(11, player.getScore());

		player.increaseScore(22);
		assertEquals(33, player.getScore());

		player.setScore(44);
		assertEquals(44, player.getScore());

		player.setScore(0);
		assertEquals(0, player.getScore());

		player.increaseScore(55);
		assertEquals(55, player.getScore());
	}
}