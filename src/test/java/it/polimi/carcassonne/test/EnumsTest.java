package it.polimi.carcassonne.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.Commands;
import it.polimi.carcassonne.server.model.enums.Field;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class EnumsTest {

	private PlayersColor color;
	private Commands command;
	private CardinalPoint cardinal;
	private Field field;

	@Test
	public void commandsASCIITests() {

		// Commands with no parameters
		command = Commands.getCommandByStringASCII("rotate");
		assertEquals(Commands.ROTATE, command);
		assertEquals("rotate", command.toString());
		command = Commands.getCommandByStringASCII("rotate qwer");
		assertFalse(Commands.ROTATE.equals(command));

		command = Commands.getCommandByStringASCII("pass");
		assertEquals(Commands.PASS, command);
		assertEquals("pass", command.toString());
		command = Commands.getCommandByStringASCII("pass qwert");
		assertFalse(Commands.PASS.equals(command));

		// Commands with parameters
		command = Commands.getCommandByStringASCII("place");
		assertFalse(Commands.PLACE.equals(command));
		command = Commands.getCommandByStringASCII("place 1,0");
		assertEquals("place", command.toString());
		assertEquals("1,0", Commands.getViewParamsByString("place 1,0"));
		command = Commands.getCommandByStringASCII("place qwert");
		assertEquals("place", command.toString());
		assertEquals("qwert", Commands.getViewParamsByString("place qwert"));

		command = Commands.getCommandByStringASCII("tile");
		assertFalse(Commands.MARKER.equals(command));
		command = Commands.getCommandByStringASCII("tile N");
		assertEquals("tile", command.toString());
		assertEquals("N", Commands.getViewParamsByString("tile N"));
		command = Commands.getCommandByStringASCII("tile qwert");
		assertEquals("tile", command.toString());
		assertEquals("qwert", Commands.getViewParamsByString("tile qwert"));

		command = Commands.getCommandByStringASCII("qwertyuiop");
		assertEquals(Commands.UNKNOWN, command);
		assertEquals("UNKNOWN", command.toString());
	}

	@Test
	public void commandsSocketTests() {

		List<String> params = new ArrayList<String>();

		// Commands with no parameters
		command = Commands.getCommandByStringSocket("rotate");
		assertEquals(Commands.ROTATE, command);
		assertEquals("rotate", command.toString());
		command = Commands.getCommandByStringSocket("rotate qwer");
		assertFalse(Commands.ROTATE.equals(command));

		command = Commands.getCommandByStringSocket("pass");
		assertEquals(Commands.PASS, command);
		assertEquals("pass", command.toString());
		command = Commands.getCommandByStringSocket("pass qwert");
		assertFalse(Commands.PASS.equals(command));

		command = Commands.getCommandByStringSocket("connect");
		assertEquals(Commands.CONNECT, command);
		assertEquals("connect", command.toString());
		command = Commands.getCommandByStringSocket("connect qwer");
		assertFalse(Commands.CONNECT.equals("connect"));

		command = Commands.getCommandByStringSocket("move not valid");
		assertEquals(Commands.MOVE_NOT_VALID, command);
		assertEquals("move not valid", command.toString());
		command = Commands.getCommandByStringSocket("move not valid qwer");
		assertFalse(Commands.MOVE_NOT_VALID.equals("move not valid"));

		command = Commands.getCommandByStringSocket("lock");
		assertEquals(Commands.LOCK, command);
		assertEquals("lock", command.toString());
		command = Commands.getCommandByStringSocket("lock qwer");
		assertFalse(Commands.LOCK.equals("lock"));

		command = Commands.getCommandByStringSocket("unlock");
		assertEquals(Commands.UNLOCK, command);
		assertEquals("unlock", command.toString());
		command = Commands.getCommandByStringSocket("unlock qwer");
		assertFalse(Commands.UNLOCK.equals("unlock"));

		// Commands with parameters
		command = Commands.getCommandByStringSocket("place");
		assertFalse(Commands.PLACE.equals(command));
		command = Commands.getCommandByStringSocket("place:1,0");
		assertEquals("place", command.toString());
		params.clear();
		params.add("1");
		params.add("0");
		assertEquals(params, Commands.getParamsByString("place:1,0"));
		command = Commands.getCommandByStringSocket("place:qwert");
		assertEquals("place", command.toString());
		params.clear();
		params.add("qwert");
		assertEquals(params, Commands.getParamsByString("place:qwert"));

		command = Commands.getCommandByStringSocket("tile");
		assertFalse(Commands.MARKER.equals(command));
		command = Commands.getCommandByStringSocket("tile:N");
		assertEquals("tile", command.toString());
		assertEquals("N", Commands.getViewParamsByString("tile N"));
		command = Commands.getCommandByStringSocket("tile:qwert");
		assertEquals("tile", command.toString());
		params.clear();
		params.add("qwert");
		assertEquals(params, Commands.getParamsByString("tile:qwert"));

		command = Commands.getCommandByStringSocket("start");
		assertFalse(Commands.START.equals(command));
		command = Commands.getCommandByStringSocket("start:N");
		assertEquals("start", command.toString());
		assertEquals("N", Commands.getViewParamsByString("start N"));
		command = Commands.getCommandByStringSocket("start:qwert");
		assertEquals("start", command.toString());
		params.clear();
		params.add("qwert");
		assertEquals(params, Commands.getParamsByString("start:qwert"));

		command = Commands.getCommandByStringSocket("turn");
		assertFalse(Commands.TURN_OF.equals(command));
		command = Commands.getCommandByStringSocket("turn:N");
		assertEquals("turn", command.toString());
		assertEquals("N", Commands.getViewParamsByString("turn N"));
		command = Commands.getCommandByStringSocket("turn:qwert");
		assertEquals("turn", command.toString());
		params.clear();
		params.add("qwert");
		assertEquals(params, Commands.getParamsByString("turn:qwert"));

		command = Commands.getCommandByStringSocket("next");
		assertFalse(Commands.NEXT_TILE.equals(command));
		command = Commands.getCommandByStringSocket("next:N");
		assertEquals("next", command.toString());
		assertEquals("N", Commands.getViewParamsByString("next N"));
		command = Commands.getCommandByStringSocket("next:qwert");
		assertEquals("next", command.toString());
		params.clear();
		params.add("qwert");
		assertEquals(params, Commands.getParamsByString("next:qwert"));

		command = Commands.getCommandByStringSocket("rotated");
		assertFalse(Commands.ROTATED.equals(command));
		command = Commands.getCommandByStringSocket("rotated:N");
		assertEquals("rotated", command.toString());
		assertEquals("N", Commands.getViewParamsByString("rotated N"));
		command = Commands.getCommandByStringSocket("rotated:qwert");
		assertEquals("rotated", command.toString());
		params.clear();
		params.add("qwert");
		assertEquals(params, Commands.getParamsByString("rotated:qwert"));

		command = Commands.getCommandByStringSocket("update");
		assertFalse(Commands.UPDATE.equals(command));
		command = Commands.getCommandByStringSocket("update:N");
		assertEquals("update", command.toString());
		assertEquals("N", Commands.getViewParamsByString("update N"));
		command = Commands.getCommandByStringSocket("update:qwert");
		assertEquals("update", command.toString());
		params.clear();
		params.add("qwert");
		assertEquals(params, Commands.getParamsByString("update:qwert"));

		command = Commands.getCommandByStringSocket("score");
		assertFalse(Commands.SCORE.equals(command));
		command = Commands.getCommandByStringSocket("score:N");
		assertEquals("score", command.toString());
		assertEquals("N", Commands.getViewParamsByString("score N"));
		command = Commands.getCommandByStringSocket("score:qwert");
		assertEquals("score", command.toString());
		params.clear();
		params.add("qwert");
		assertEquals(params, Commands.getParamsByString("score:qwert"));

		command = Commands.getCommandByStringSocket("leave");
		assertFalse(Commands.LEAVE.equals(command));
		command = Commands.getCommandByStringSocket("leave:N");
		assertEquals("leave", command.toString());
		assertEquals("N", Commands.getViewParamsByString("leave N"));
		command = Commands.getCommandByStringSocket("leave:qwert");
		assertEquals("leave", command.toString());
		params.clear();
		params.add("qwert");
		assertEquals(params, Commands.getParamsByString("leave:qwert"));

		command = Commands.getCommandByStringSocket("reconnect");
		assertFalse(Commands.RECONNECT.equals(command));
		command = Commands.getCommandByStringSocket("reconnect:N");
		assertEquals("reconnect", command.toString());
		assertEquals("N", Commands.getViewParamsByString("reconnect N"));
		command = Commands.getCommandByStringSocket("reconnect:qwert");
		assertEquals("reconnect", command.toString());
		params.clear();
		params.add("qwert");
		assertEquals(params, Commands.getParamsByString("reconnect:qwert"));

		command = Commands.getCommandByStringSocket("end");
		assertFalse(Commands.END.equals(command));
		command = Commands.getCommandByStringSocket("end:N");
		assertEquals("end", command.toString());
		assertEquals("N", Commands.getViewParamsByString("end N"));
		command = Commands.getCommandByStringSocket("end:qwert");
		assertEquals("end", command.toString());
		params.clear();
		params.add("qwert");
		assertEquals(params, Commands.getParamsByString("end:qwert"));

		command = Commands.getCommandByStringSocket("qwertyuiop");
		assertEquals(Commands.UNKNOWN, command);
		assertEquals("UNKNOWN", command.toString());
	}

	@Test
	public void cardinalPointTests() {
		cardinal = CardinalPoint.get("N");
		assertEquals(CardinalPoint.NORTH, cardinal);
		assertEquals("North", cardinal.toString());
		assertEquals(CardinalPoint.SOUTH, cardinal.getOpposite());

		cardinal = CardinalPoint.get("S");
		assertEquals(CardinalPoint.SOUTH, cardinal);
		assertEquals("South", cardinal.toString());
		assertEquals(CardinalPoint.NORTH, cardinal.getOpposite());

		cardinal = CardinalPoint.get("E");
		assertEquals(CardinalPoint.EAST, cardinal);
		assertEquals("East", cardinal.toString());
		assertEquals(CardinalPoint.WEST, cardinal.getOpposite());

		cardinal = CardinalPoint.get("W");
		assertEquals(CardinalPoint.WEST, cardinal);
		assertEquals("West", cardinal.toString());
		assertEquals(CardinalPoint.EAST, cardinal.getOpposite());
	}

	@Test
	public void fieldTests() {
		field = Field.get("S");
		assertEquals(Field.STREET, field);
		assertEquals("Street", field.toString());
		assertEquals(1, field.getMultiplier());

		field = Field.get("C");
		assertEquals(Field.CITY, field);
		assertEquals("City", field.toString());
		assertEquals(2, field.getMultiplier());

		field = Field.get("N");
		assertEquals(Field.NIL, field);
		assertEquals("Grass", field.toString());
		assertEquals(0, field.getMultiplier());

	}

	@Test
	public void colorTests() {
		color = PlayersColor.get("R");
		assertEquals(PlayersColor.RED, color);
		assertEquals("red", color.toString());
		assertEquals(color, PlayersColor.getColorByString("red"));
		assertEquals(color, PlayersColor.getColorByString(color.toString()));
		color = PlayersColor.getColorByID(0);
		assertEquals(PlayersColor.RED, color);

		color = PlayersColor.get("B");
		assertEquals(PlayersColor.BLUE, color);
		assertEquals("blue", color.toString());
		assertEquals(color, PlayersColor.getColorByString("blue"));
		assertEquals(color, PlayersColor.getColorByString(color.toString()));
		color = PlayersColor.getColorByID(1);
		assertEquals(PlayersColor.BLUE, color);

		color = PlayersColor.get("G");
		assertEquals(PlayersColor.GREEN, color);
		assertEquals("green", color.toString());
		assertEquals(color, PlayersColor.getColorByString("green"));
		assertEquals(color, PlayersColor.getColorByString(color.toString()));
		color = PlayersColor.getColorByID(2);
		assertEquals(PlayersColor.GREEN, color);

		color = PlayersColor.get("Y");
		assertEquals(PlayersColor.YELLOW, color);
		assertEquals("yellow", color.toString());
		assertEquals(color, PlayersColor.getColorByString("yellow"));
		assertEquals(color, PlayersColor.getColorByString(color.toString()));
		color = PlayersColor.getColorByID(3);
		assertEquals(PlayersColor.YELLOW, color);

		color = PlayersColor.get("K");
		assertEquals(PlayersColor.BLACK, color);
		assertEquals("black", color.toString());
		assertEquals(color, PlayersColor.getColorByString("black"));
		assertEquals(color, PlayersColor.getColorByString(color.toString()));
		color = PlayersColor.getColorByID(4);
		assertEquals(PlayersColor.BLACK, color);

		color = PlayersColor.getColorByID(10);
		assertNull(color);
	}

}