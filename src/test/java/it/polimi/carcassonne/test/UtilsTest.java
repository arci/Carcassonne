package it.polimi.carcassonne.test;

import static it.polimi.carcassonne.server.model.Settings.MATCH_NAME_PREFIX;
import static org.junit.Assert.assertEquals;
import it.polimi.carcassonne.server.model.Utils;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import java.awt.Color;

import org.junit.Test;

public class UtilsTest {

	private static final int RANGE = 200;

	@Test
	public void generateMatchNameTest() {
		for (int i = 1; i < RANGE; i++) {
			assertEquals(MATCH_NAME_PREFIX + i, Utils.generateMatchName());
		}
	}

	@Test
	public void getPlayerColorTest() {
		assertEquals(Color.RED, Utils.getPlayerColor(PlayersColor.RED));
		assertEquals(Color.BLUE, Utils.getPlayerColor(PlayersColor.BLUE));
		assertEquals(Color.GREEN, Utils.getPlayerColor(PlayersColor.GREEN));
		assertEquals(Color.YELLOW, Utils.getPlayerColor(PlayersColor.YELLOW));
		assertEquals(Color.BLACK, Utils.getPlayerColor(PlayersColor.BLACK));
	}

}
