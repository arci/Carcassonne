package it.polimi.carcassonne.client.controller.socket;

import static it.polimi.carcassonne.server.model.Settings.START_COLOR;
import static it.polimi.carcassonne.server.model.Settings.START_NAME;
import static it.polimi.carcassonne.server.model.Settings.START_NUM_PLAYERS;
import static it.polimi.carcassonne.server.model.Settings.START_TILE;
import static it.polimi.carcassonne.server.model.Settings.TILE;
import static it.polimi.carcassonne.server.model.Settings.TURN_OF;
import static it.polimi.carcassonne.server.model.Settings.UPDATE_TILE;
import static it.polimi.carcassonne.server.model.Settings.UPDATE_X_COORDINATE;
import static it.polimi.carcassonne.server.model.Settings.UPDATE_Y_COORDINATE;
import it.polimi.carcassonne.client.controller.MatchController;
import it.polimi.carcassonne.client.view.text.Display;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.Commands;
import it.polimi.carcassonne.server.model.enums.PlayersColor;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;

import java.util.List;

/**
 * Parse commands received with the socket and execute relative actions
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class CmdParser {

	private MatchController controller;

	/**
	 * Create a command parser associating a match controller
	 * 
	 * @param controller
	 *            of the match
	 */
	public CmdParser(MatchController controller) {
		this.controller = controller;
	}

	/**
	 * Given a command, decide what it indicates and execute it
	 * 
	 * @param command
	 *            to parse
	 */
	public void parseAndExecute(String command) {
		Commands cmd = Commands.getCommandByStringSocket(command);
		List<String> params = Commands.getParamsByString(command);

		switch (cmd) {
		case START:
			createMatch(params);
			break;

		case ROTATED:
			// do same things of NEXT_TILE
		case NEXT_TILE:
			setDrawedTile(params);
			break;

		case UPDATE:
			updateTile(params);
			break;

		case TURN_OF:
			notifyNextTurn(params);
			break;

		case SCORE:
			updateScore(params);
			break;

		case MOVE_NOT_VALID:
			notifyMoveNotValid();
			break;

		case END:
			notifyWinners();
			break;

		default: // case UNKNOWN
			// doNothing();
			// command not recognized
		}
	}

	private void updateScore(List<String> params) {
		controller.updateLocalScores(params);

	}

	private void updateTile(List<String> params) {
		try {
			controller.placeDrawedTileInLocalGrid(
					controller.parseMarker(params.get(UPDATE_TILE)),
					new Coordinate(params.get(UPDATE_X_COORDINATE) + ","
							+ params.get(UPDATE_Y_COORDINATE)));
			controller.updateLocalMarker();
		} catch (InvalidCellException e) {
			Display.printWarning("Mossa non consentita");
		}
	}

	private void createMatch(List<String> params) {
		controller.createMatch(getMatchNameFromParam(params),
				getFirstTileFromParam(params), getMyColorFromParam(params),
				getNumberOfPlayersFromParam(params));
	}

	private Tile getFirstTileFromParam(List<String> params) {
		return new Tile(params.get(START_TILE));
	}

	private String getMatchNameFromParam(List<String> params) {
		return params.get(START_NAME);
	}

	private PlayersColor getMyColorFromParam(List<String> params) {
		return PlayersColor.getColorByString(params.get(START_COLOR));
	}

	private int getNumberOfPlayersFromParam(List<String> params) {
		return Integer.valueOf(params.get(START_NUM_PLAYERS));
	}

	private void setDrawedTile(List<String> params) {
		controller.setLastDrawedTile(new Tile(params.get(TILE)));
	}

	private void notifyNextTurn(List<String> params) {
		PlayersColor playerColor = PlayersColor.getColorByString(params
				.get(TURN_OF));
		controller.nextTurn(playerColor);
	}

	private void notifyWinners() {
		controller.setEndGame();
		controller.notifyEndMessage();
		controller.disconnect();
	}

	private void notifyMoveNotValid() {
		if (controller.isMyTurn()) {
			controller.notifyMoveNotValid();
		}
	}

}