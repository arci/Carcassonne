package it.polimi.carcassonne.test;

import static it.polimi.carcassonne.server.model.Settings.MAX_MARKERS;
import static it.polimi.carcassonne.server.model.Settings.MAX_PLAYERS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Grid;
import it.polimi.carcassonne.server.model.Match;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.CellStatus;
import it.polimi.carcassonne.server.model.enums.PlayersColor;
import it.polimi.carcassonne.server.model.exceptions.IncompatibleFieldsException;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;
import it.polimi.carcassonne.server.model.exceptions.InvalidMarkerPositionException;
import it.polimi.carcassonne.server.model.exceptions.OccupiedFieldException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MatchTest {
	private Match match;
	private Tile firstTile;
	boolean testOK;

	@Before
	public void setUp() {
		testOK = false;
		firstTile = new Tile("N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0");
		match = new Match("matchProva");
		try {
			match.start();
		} catch (IOException e) {
			fail("Non trovo carcassonne.txt");
		}
	}

	@Test
	public void constructorTest() {
		assertEquals("matchProva", match.getName());
		assertEquals(0, match.getPlayers().size());

		// Test correttezza griglia del match
		Grid grid = match.getGrid();
		assertEquals(1, match.getGrid().getCellByStatus(CellStatus.HASTILE)
				.size());
		assertEquals(4, match.getGrid().getCellByStatus(CellStatus.ENABLED)
				.size());
		assertEquals(4, match.getGrid().getCellByStatus(CellStatus.DISABLED)
				.size());

		assertEquals(grid.getCellByCoordinates(new Coordinate(0, 0)).getTile(),
				match.getLastDrawedTile());

		assertTrue(grid.getCellByCoordinates(new Coordinate(-1, -1))
				.isDisabled());
		assertTrue(grid.getCellByCoordinates(new Coordinate(-1, 0)).isEnabled());
		assertTrue(grid.getCellByCoordinates(new Coordinate(-1, 1))
				.isDisabled());
		assertTrue(grid.getCellByCoordinates(new Coordinate(0, -1)).isEnabled());
		assertTrue(grid.getCellByCoordinates(new Coordinate(0, 0)).hasTile());
		assertTrue(grid.getCellByCoordinates(new Coordinate(0, 1)).isEnabled());
		assertTrue(grid.getCellByCoordinates(new Coordinate(1, -1))
				.isDisabled());
		assertTrue(grid.getCellByCoordinates(new Coordinate(1, 0)).isEnabled());
		assertTrue(grid.getCellByCoordinates(new Coordinate(1, 1)).isDisabled());

		for (int i = 1; i < 56; i++) {
			assertFalse(match.deckIsEmpty());
			match.drawTile();
		}
		assertTrue(match.deckIsEmpty());
	}

	@Test
	public void startClientServerTest() {
		testOK = false;
		match = new Match("provaOverloadStart");
		match.start(firstTile);
		try {
			match.drawTile();
		} catch (NullPointerException e) {
			// non deve avere carte nel mazzo, e' lo start di client/server
			testOK = true;
		}
		assertTrue(testOK);

		// Test correttezza griglia del match
		Grid grid = match.getGrid();
		assertEquals(1, match.getGrid().getCellByStatus(CellStatus.HASTILE)
				.size());
		assertEquals(4, match.getGrid().getCellByStatus(CellStatus.ENABLED)
				.size());
		assertEquals(4, match.getGrid().getCellByStatus(CellStatus.DISABLED)
				.size());

		assertEquals(firstTile, grid.getCellByCoordinates(new Coordinate(0, 0))
				.getTile());

		assertTrue(grid.getCellByCoordinates(new Coordinate(-1, -1))
				.isDisabled());
		assertTrue(grid.getCellByCoordinates(new Coordinate(-1, 0)).isEnabled());
		assertTrue(grid.getCellByCoordinates(new Coordinate(-1, 1))
				.isDisabled());
		assertTrue(grid.getCellByCoordinates(new Coordinate(0, -1)).isEnabled());
		assertTrue(grid.getCellByCoordinates(new Coordinate(0, 0)).hasTile());
		assertTrue(grid.getCellByCoordinates(new Coordinate(0, 1)).isEnabled());
		assertTrue(grid.getCellByCoordinates(new Coordinate(1, -1))
				.isDisabled());
		assertTrue(grid.getCellByCoordinates(new Coordinate(1, 0)).isEnabled());
		assertTrue(grid.getCellByCoordinates(new Coordinate(1, 1)).isDisabled());
	}

	@Test
	public void nextTurnTest() {
		for (int i = 1; i < 56; i++) {
			assertFalse(match.deckIsEmpty());
			match.nextTurn();
		}
		assertTrue(match.deckIsEmpty());
	}

	@Test
	public void placeLastDrawedTileTest() {
		try {
			match.placeLastDrawedTile(new Coordinate(0, 0));
		} catch (InvalidCellException e) {
			testOK = true;
		} catch (IncompatibleFieldsException e) {
			fail("Non doveva entrare in questo catch");
		}
		assertTrue(testOK);
	}

	@Test
	public void placeTileWithoutCheckingNwighboringTest() {
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				try {
					match.placeTileWithoutCheckingNeighboring(firstTile,
							new Coordinate(x, y));
				} catch (InvalidCellException e) {
					fail("Doveva riuscire a posizionarla");
				} catch (IncompatibleFieldsException e) {
					fail("Doveva riuscire a posizionarla");
				}
				assertEquals(
						firstTile,
						match.getGrid()
								.getCellByCoordinates(new Coordinate(x, y))
								.getTile());
			}
		}
		assertEquals(-2, match.getGrid().getMinX());
		assertEquals(-2, match.getGrid().getMinY());
		assertEquals(2, match.getGrid().getMaxX());
		assertEquals(2, match.getGrid().getMaxY());
	}

	@Test
	public void rotateLastDrawedTileTest() {
		for (int i = 0; i < 4; i++) {
			assertEquals(firstTile, match.getLastDrawedTile());
			match.rotateLastDrawedTile();
			firstTile.rotate();
		}

		for (int counter = 1; counter < 56; counter++) {
			Tile lastDrawedTile = new Tile(match.getLastDrawedTile()
					.getString());
			for (int i = 0; i < 4; i++) {
				assertEquals(lastDrawedTile, match.getLastDrawedTile());
				match.rotateLastDrawedTile();
				lastDrawedTile.rotate();
			}
			match.drawTile();
		}
	}

	@Test
	public void addPlayerTest() {
		assertEquals(0, match.getPlayers().size());
		List<Player> players = new ArrayList<Player>();
		for (int i = 0; i < MAX_PLAYERS; i++) {
			players.add(new Player(PlayersColor.getColorByID(i)));
		}
		for (Player p : players) {
			match.addPlayer(p);
		}
		assertEquals(MAX_PLAYERS, match.getPlayers().size());
		assertEquals(players, match.getPlayers());
	}

	@Test
	public void addPlayersTest() {
		assertEquals(0, match.getPlayers().size());
		match.addPlayers(MAX_PLAYERS);
		assertEquals(MAX_PLAYERS, match.getPlayers().size());

		for (Player player : match.getPlayers()) {
			assertEquals(Player.class, player.getClass());
		}
	}

	@Test
	public void checkVictoryConditionTest_UniqueWinner() {
		for (int i = 0; i < PlayersColor.values().length; i++) {
			Player player = new Player(PlayersColor.getColorByID(i));
			player.increaseScore(i);
			match.addPlayer(player);
		}
		assertTrue(match.checkVictoryCondition().size() == 1);
	}

	@Test
	public void checkVictoryConditionTest() {
		int i = 1;

		match.addPlayers(MAX_PLAYERS);
		// tutti vincitori con 0 punti
		assertTrue(match.checkVictoryCondition().size() == 5);

		for (Player p : match.getPlayers()) {
			p.increaseScore(10);
		}
		// tutti vincitori con 10 punti
		assertTrue(match.checkVictoryCondition().size() == 5);

		for (Player p : match.getPlayers()) {
			p.increaseScore(i);
			i++;
		}
		// un solo vincitore
		assertTrue(match.checkVictoryCondition().size() == 1);
	}

	@Test
	public void getLastInseredCellTest() {
		Grid grid = match.getGrid();
		assertEquals(match.getLastInseredCell(),
				grid.getCellByCoordinates(new Coordinate(0, 0)));
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				try {
					match.placeTileWithoutCheckingNeighboring(firstTile,
							new Coordinate(x, y));
				} catch (InvalidCellException e) {
					fail("Doveva riuscire a posizionarla");
				} catch (IncompatibleFieldsException e) {
					fail("Doveva riuscire a posizionarla");
				}
				assertEquals(match.getLastInseredCell(),
						grid.getCellByCoordinates(new Coordinate(x, y)));
			}
		}
	}

	@Test
	public void checkClosureTest() {
		Grid grid = match.getGrid();
		assertEquals(grid.checkClosedBuildings(grid
				.getCellByCoordinates(new Coordinate(0, 0))),
				match.checkClosedBuildings(match.getGrid()
						.getCellByCoordinates(new Coordinate(0, 0))));

		firstTile.rotate();
		firstTile.rotate();

		try {
			match.placeTileWithoutCheckingNeighboring(firstTile,
					new Coordinate(0, -1));
		} catch (InvalidCellException e) {
			fail("Doveva riuscire a posizionarla");
		} catch (IncompatibleFieldsException e) {
			fail("Doveva riuscire a posizionarla");
		}

		assertEquals(
				1,
				grid.checkClosedBuildings(
						grid.getCellByCoordinates(new Coordinate(0, 0))).size());
		assertEquals(
				1,
				match.checkClosedBuildings(
						match.getGrid().getCellByCoordinates(
								new Coordinate(0, 0))).size());
		assertEquals(
				1,
				grid.checkClosedBuildings(
						grid.getCellByCoordinates(new Coordinate(0, -1)))
						.size());
		assertEquals(
				1,
				match.checkClosedBuildings(
						match.getGrid().getCellByCoordinates(
								new Coordinate(0, -1))).size());
	}

	@Test
	public void setMarker() {
		Player player = new Player(PlayersColor.RED);
		try {
			match.setMarker(player, CardinalPoint.SOUTH);
		} catch (InvalidMarkerPositionException e) {
			fail("Doveva riuscire a posizionare il marker");
		} catch (OccupiedFieldException e) {
			fail("Doveva riuscire a posizionare il marker");
		}
		assertEquals(MAX_MARKERS - 1, player.getAvailableMarkerCount());

		assertEquals(CardinalPoint.SOUTH, match.getLastDrawedTile()
				.getMarkerPosition());
		assertEquals(CardinalPoint.SOUTH, match.getLastInseredCell().getTile()
				.getMarkerPosition());
	}

	@Test
	public void helpPlacing() {
		assertEquals(match.helpPlacing(),
				match.getGrid().getPossibleMoves(match.getLastDrawedTile()));
	}

	@Test
	public void getCurrentPlayerAttributesTests() {

		match.addPlayers(MAX_PLAYERS);

		// prima di fare il primo nextTurn() nessun giocatore e' corrente
		assertNull(match.getCurrentPlayer());

		for (int i = 0; i < 55; i++) {
			match.nextTurn();
			assertEquals(PlayersColor.getColorByID(i % 5), match
					.getCurrentPlayer().getColor());
			assertEquals(7, match.getCurrentPlayerAvailableMarkers());
		}
	}

	@Test
	public void getPlayerByColorTest() {
		Player redPlayer = new Player(PlayersColor.RED);
		Player bluePlayer = new Player(PlayersColor.BLUE);
		Player greenPlayer = new Player(PlayersColor.GREEN);
		Player yellowPlayer = new Player(PlayersColor.YELLOW);
		Player blackPlayer = new Player(PlayersColor.BLACK);

		match.addPlayer(redPlayer);
		match.addPlayer(bluePlayer);
		match.addPlayer(greenPlayer);
		match.addPlayer(yellowPlayer);
		match.addPlayer(blackPlayer);

		assertEquals(redPlayer, match.getPlayerByColor(PlayersColor.RED));
		assertEquals(bluePlayer, match.getPlayerByColor(PlayersColor.BLUE));
		assertEquals(greenPlayer, match.getPlayerByColor(PlayersColor.GREEN));
		assertEquals(yellowPlayer, match.getPlayerByColor(PlayersColor.YELLOW));
		assertEquals(blackPlayer, match.getPlayerByColor(PlayersColor.BLACK));
	}

	@Test
	public void validateCardinalPointTest() {
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				String param = "";
				try {
					param = match.validateCoordinates(Integer.toString(x) + ","
							+ Integer.toString(y));
				} catch (InvalidCellException e) {
					fail("Cella invalida");
				}
				assertEquals(Integer.toString(x) + "," + Integer.toString(y),
						param);
			}
		}
		String param = "";
		try {
			param = match.validateCoordinates("2,2");
		} catch (InvalidCellException e) {
			assertNull(param);
		}
		assertNull(param);

		param = "";
		try {
			param = match.validateCoordinates("-2,2");
		} catch (InvalidCellException e) {
			assertNull(param);
		}
		assertNull(param);

		param = "";
		try {
			param = match.validateCoordinates("2,-2");
		} catch (InvalidCellException e) {
			assertNull(param);
		}
		assertNull(param);

	}

	@Test
	public void validateCoordinatesTest() {
		for (CardinalPoint c : CardinalPoint.values()) {
			String param = "";
			try {
				param = match.validateCardinals(c.getAbbreviation());
			} catch (InvalidMarkerPositionException e) {
				fail("Parametro invalido");
			}
			assertEquals(c.getAbbreviation(), param);
		}
	}
}
