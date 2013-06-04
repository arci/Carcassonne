package it.polimi.carcassonne.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import it.polimi.carcassonne.client.view.text.GridPrinter;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Grid;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import org.junit.Before;
import org.junit.Test;

public class GridPrinterTest {

	private static final String extremeGameGridResultOK = "                                          +.............+.............+                           \n                                          :             :             :                           \n                                          :             :             :                           \n                                          : (1,4)       : (2,4)       :                           \n                                          :             :             :                           \n                                          :             :             :                           \n              +.............+.............+#############+#############+.............+             \n              :             :             #             :     S1      #             :             \n              :             :             #             :             #             :             \n              : (-1,3)      : (0,3)       #S1         S1:S1(B)        # (3,3)       :             \n              :             :             #             :             #             :             \n              :             :             #             :             #             :             \n+.............+#############+.............+.............+.............+.............+             \n:             #     C1      #             #             :             #             :             \n:             #             #             #             :             #             :             \n: (-2,2)      #C1(K)        # (0,2)       #             :             # (3,2)       :             \n:             #             #             #             :             #             :             \n:             #             #             #     C1      :     C1      #             :             \n+.............+.............+#############+.............+.............+#############+.............+\n:             #             :             :     C1      :     C1      :     C1      #             :\n:             #             :             :             :             :             #             :\n: (-2,1)      #           C1:C1         C1:C1           :             :        C1(G)# (4,1)       :\n:             #             :             :             :             :             #             :\n:             #             :     C1      :     C1      :     C1      :             #             :\n+.............+.............+.............+.............+.............+#############+.............+\n:             #             :     C1      :    C1(R)    :     C1      #             :             \n:             #             :             :             :             #             :             \n: (-2,0)      #           C1:C1         C1:C1         C1:C1           # (3,0)       :             \n:             #             :             :             :             #             :             \n:             #             :             :             :     C1      #             :             \n+.............+#############+#############+.............+.............+.............+             \n              :             :             #             :     C1      #             :             \n              :             :             #             :             #             :             \n              : (-1,-1)     : (0,-1)      #C1(Y)        :             # (3,-1)      :             \n              :             :             #             :             #             :             \n              :             :             #     C1      :             #             :             \n              +.............+.............+.............+.............+.............+             \n                            :             #     C1      :             #             :             \n                            :             #             :             #             :             \n                            : (0,-2)      #             :           C1# (3,-2)      :             \n                            :             #             :             #             :             \n                            :             #     C1      :    C1(K)    #             :             \n                            +.............+#############+#############+.............+             \n                                          :             :             :                           \n                                          :             :             :                           \n                                          : (1,-3)      : (2,-3)      :                           \n                                          :             :             :                           \n                                          :             :             :                           \n                                          +.............+.............+                           \n";
	private static final String startGameGridResultOK = "              +.............+             \n              :             :             \n              :             :             \n              : (0,1)       :             \n              :             :             \n              :             :             \n+.............+#############+.............+\n:             #             #             :\n:             #             #             :\n: (-1,0)      #S1         S1# (1,0)       :\n:             #             #             :\n:             #     C1      #             :\n+.............+#############+.............+\n              :             :             \n              :             :             \n              : (0,-1)      :             \n              :             :             \n              :             :             \n              +.............+             \n";
	private static final Object firstTileResultOK = "Carta pescata:\n+############+\n#            #\n#            #\n#S1        S1#\n#            #\n#     C1     #\n+############+\n";

	private Grid grid;
	private Tile firstTile;
	private Tile elbowStreetNWconn;
	private Tile straightStreetWEconn;
	private Tile elbowCityNEconn;
	private Tile elbowCityNWconn;
	private Tile elbowCitySWconn;
	private Tile elbowCitySEconn;
	private Tile straightCityNSconn;
	private Tile threeWayCityNWE;
	private Tile threeWayCitySWE;
	private Tile threeWayCityNSW;
	private Tile singleCityN;
	private Tile singleCityE;
	private Tile singleCityS;

	@Before
	public void setUp() {

		firstTile = new Tile("N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0");

		elbowStreetNWconn = new Tile(
				"N=S E=N S=N W=S NE=0 NW=1 NS=0 SE=0 SW=0 EW=0");
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
		straightCityNSconn = new Tile(
				"N=C E=N S=C W=N NE=0 NW=0 NS=1 SE=0 SW=0 EW=0");
		threeWayCityNWE = new Tile(
				"N=C E=C S=N W=C NE=1 NW=1 NS=0 SE=0 SW=0 EW=1");
		threeWayCitySWE = new Tile(
				"N=N E=C S=C W=C NE=0 NW=0 NS=0 SE=1 SW=1 EW=1");
		threeWayCityNSW = new Tile(
				"N=C E=N S=C W=C NE=0 NW=1 NS=1 SE=0 SW=1 EW=0");
		singleCityN = new Tile("N=C E=N S=N W=N NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		singleCityE = new Tile("N=N E=C S=N W=N NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
		singleCityS = new Tile("N=N E=N S=C W=N NE=0 NW=0 NS=0 SE=0 SW=0 EW=0");
	}

	@Test
	public void singleTileTest() {
		assertEquals(firstTileResultOK, GridPrinter.printTile(firstTile));
	}

	@Test
	public void startGameGridTest() {
		grid = new Grid(firstTile);
		assertEquals(startGameGridResultOK, GridPrinter.printGrid(grid));
	}

	@Test
	public void extremeGridTest() {
		initializeExtremeGameGrid();
		assertEquals(extremeGameGridResultOK, GridPrinter.printGrid(grid));
	}

	private void initializeExtremeGameGrid() {
		grid = new Grid(threeWayCityNWE);
		Player redPlayer = new Player(PlayersColor.RED);
		Player bluePlayer = new Player(PlayersColor.BLUE);
		Player greenPlayer = new Player(PlayersColor.GREEN);
		Player yellowPlayer = new Player(PlayersColor.YELLOW);
		Player blackPlayer = new Player(PlayersColor.BLACK);

		threeWayCityNWE = new Tile(
				"N=C E=C S=N W=C NE=1 NW=1 NS=0 SE=0 SW=0 EW=1");
		try {
			grid.placeTile(new Coordinate(1, 0), threeWayCityNWE);
			grid.setMarker(redPlayer, CardinalPoint.NORTH);
			grid.placeTile(new Coordinate(2, 0), threeWayCityNSW);
			grid.placeTile(new Coordinate(2, -1), singleCityN);
			grid.placeTile(new Coordinate(2, 1), straightCityNSconn);
			grid.placeTile(new Coordinate(2, 2), singleCityS);
			grid.placeTile(new Coordinate(1, 1), threeWayCityNSW);
			grid.placeTile(new Coordinate(1, 2), singleCityS);
			grid.placeTile(new Coordinate(0, 1), threeWayCitySWE);
			grid.placeTile(new Coordinate(-1, 1), singleCityE);
			grid.placeTile(new Coordinate(-1, 0), singleCityE);
			grid.placeTile(new Coordinate(1, -1), elbowCitySWconn);
			grid.setMarker(yellowPlayer, CardinalPoint.WEST);
			grid.placeTile(new Coordinate(1, -2), straightCityNSconn);
			grid.placeTile(new Coordinate(-1, 2), elbowCityNWconn);
			grid.setMarker(blackPlayer, CardinalPoint.WEST);
			grid.placeTile(new Coordinate(3, 1), elbowCityNEconn);
			grid.setMarker(greenPlayer, CardinalPoint.EAST);
			grid.placeTile(new Coordinate(1, 3), straightStreetWEconn);
			grid.placeTile(new Coordinate(2, 3), elbowStreetNWconn);
			grid.setMarker(bluePlayer, CardinalPoint.WEST);
			grid.placeTile(new Coordinate(2, -2), elbowCitySEconn);
			grid.setMarker(blackPlayer, CardinalPoint.SOUTH);
		} catch (Exception e) {
			fail("Errore nel posizionamento delle tessere");
		}
	}
}
