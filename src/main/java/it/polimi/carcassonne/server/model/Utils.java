package it.polimi.carcassonne.server.model;

import static it.polimi.carcassonne.server.model.Settings.MATCH_NAME_PREFIX;
import static it.polimi.carcassonne.server.model.Settings.TBLACK;
import static it.polimi.carcassonne.server.model.Settings.TBLUE;
import static it.polimi.carcassonne.server.model.Settings.TGREEN;
import static it.polimi.carcassonne.server.model.Settings.TRED;
import static it.polimi.carcassonne.server.model.Settings.TYELLOW;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import java.awt.Color;

/**
 * Contains some frequently used function
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public final class Utils {
	private static int matchNumber = 0;

	private Utils() {
		// don't display my constructor
	}

	/**
	 * Generate sequential MatchName
	 * 
	 * @return the generated matchName
	 */
	public static synchronized String generateMatchName() {
		matchNumber++;
		return MATCH_NAME_PREFIX + matchNumber;
	}

	/**
	 * 
	 * @param playerColor
	 * 
	 * @return the constant of the class Color corresponding to PlayersColor
	 */
	public static Color getPlayerColor(PlayersColor playerColor) {
		if (playerColor.equals(PlayersColor.RED)) {
			return Color.RED;
		}
		if (playerColor.equals(PlayersColor.BLUE)) {
			return Color.BLUE;
		}
		if (playerColor.equals(PlayersColor.GREEN)) {
			return Color.GREEN;
		}
		if (playerColor.equals(PlayersColor.YELLOW)) {
			return Color.YELLOW;
		}
		if (playerColor.equals(PlayersColor.BLACK)) {
			return Color.BLACK;
		}
		return null;
	}

	/**
	 * 
	 * @param playerColor
	 * @return a color of the class Color semi-transparent
	 */
	public static Color getPlayerAlphaColor(PlayersColor playerColor) {
		if (playerColor.equals(PlayersColor.RED)) {
			return TRED;
		}
		if (playerColor.equals(PlayersColor.BLUE)) {
			return TBLUE;
		}
		if (playerColor.equals(PlayersColor.GREEN)) {
			return TGREEN;
		}
		if (playerColor.equals(PlayersColor.YELLOW)) {
			return TYELLOW;
		}
		if (playerColor.equals(PlayersColor.BLACK)) {
			return TBLACK;
		}
		return null;
	}
}
