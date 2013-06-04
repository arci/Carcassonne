package it.polimi.carcassonne.controller.text;

import static org.junit.Assert.assertEquals;
import it.polimi.carcassonne.client.controller.socket.ClientCmdGenerator;
import it.polimi.carcassonne.server.controller.socket.ServerCmdGenerator;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import org.junit.Test;

public class CmdGeneratorsTest {

	@Test
	public void ServerCmdGenerator() {
		Tile firstTile = new Tile(
				"N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0");
		String matchName = "prova";
		Player p = new Player(PlayersColor.RED);
		int numPlayers = 4;
		assertEquals("start:N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0,"
				+ matchName + ",red," + numPlayers,
				ServerCmdGenerator.start(p, firstTile, matchName, numPlayers));

		assertEquals("turn:black",
				ServerCmdGenerator.turnOf(PlayersColor.BLACK));
		assertEquals("next:N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0",
				ServerCmdGenerator.nextTile(firstTile.getString()));
		assertEquals("rotated:N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0",
				ServerCmdGenerator.rotated(firstTile.getString()));
		assertEquals(
				"update:N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0,7,3",
				ServerCmdGenerator.update(new Coordinate(7, 3), firstTile));
		assertEquals("score:red=1, blue=2, green=3",
				ServerCmdGenerator.score("red=1, blue=2, green=3"));
		assertEquals("end:red=1, blue=2, green=3",
				ServerCmdGenerator.end("red=1, blue=2, green=3"));
		assertEquals("move not valid", ServerCmdGenerator.moveNotValid());
	}

	@Test
	public void ClientCmdGenerator() {
		assertEquals("rotate", ClientCmdGenerator.rotate());
		assertEquals("place:-123,456",
				ClientCmdGenerator.placeTile(new Coordinate(-123, 456)));
		assertEquals("tile:N",
				ClientCmdGenerator.placeMarker(CardinalPoint.NORTH));
		assertEquals("pass", ClientCmdGenerator.pass());
	}
}
