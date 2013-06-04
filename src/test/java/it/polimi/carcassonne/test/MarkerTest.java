package it.polimi.carcassonne.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import it.polimi.carcassonne.server.model.Marker;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import org.junit.Before;
import org.junit.Test;

public class MarkerTest {

	private static final int MAX_MARKERS = 7;
	private Player player;
	private Marker marker;

	@Before
	public void setUp() {
		player = new Player(PlayersColor.RED);
		marker = new Marker(player);
	}

	@Test
	public void ownerTest() {
		assertEquals(player.getColor(), marker.getOwner().getColor());
		assertEquals(player.getColor(), marker.getColor());
	}

	@Test
	public void positionTest() {
		assertEquals(null, marker.getPosition());
		marker.setPosition(CardinalPoint.EAST);
		assertEquals(CardinalPoint.EAST, marker.getPosition());
		marker.clearPosition();
		assertNull(marker.getPosition());
	}

	@Test
	public void colorTest() {
		assertEquals(PlayersColor.RED, marker.getColor());
	}

	@Test
	public void addRemoveTest() {
		marker = player.getMarker();
		assertEquals(MAX_MARKERS - 1, player.getAvailableMarkerCount());
		for (int i = 1; i < MAX_MARKERS; i++) {
			player.getMarker();
			assertEquals(MAX_MARKERS - i - 1, player.getAvailableMarkerCount());
		}
		assertEquals(0, player.getAvailableMarkerCount());

		for (int i = 1; i <= MAX_MARKERS; i++) {
			player.addMarker();
			assertEquals(i, player.getAvailableMarkerCount());
		}
		assertEquals(7, player.getAvailableMarkerCount());
	}
}
