package it.polimi.carcassonne.server.model.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Possible command that the user can send/receive with socket, and the command
 * for the ASCII interface
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public enum Commands {
	CONNECT {
		@Override
		public String toString() {
			return "connect";
		}
	},
	START {
		@Override
		public String toString() {
			return "start";
		}
	},
	TURN_OF {
		@Override
		public String toString() {
			return "turn";
		}
	},
	NEXT_TILE {
		@Override
		public String toString() {
			return "next";
		}
	},
	ROTATE {
		@Override
		public String toString() {
			return "rotate";
		}
	},
	ROTATED {
		@Override
		public String toString() {
			return "rotated";
		}
	},
	PLACE {
		@Override
		public String toString() {
			return "place";
		}
	},
	UPDATE {
		@Override
		public String toString() {
			return "update";
		}
	},
	MOVE_NOT_VALID {
		@Override
		public String toString() {
			return "move not valid";
		}
	},
	MARKER {
		@Override
		public String toString() {
			return "tile";
		}
	},
	PASS {
		@Override
		public String toString() {
			return "pass";
		}
	},
	SCORE {
		@Override
		public String toString() {
			return "score";
		}
	},
	LOCK {
		@Override
		public String toString() {
			return "lock";
		}
	},
	UNLOCK {
		@Override
		public String toString() {
			return "unlock";
		}
	},
	LEAVE {
		@Override
		public String toString() {
			return "leave";
		}
	},
	RECONNECT {
		@Override
		public String toString() {
			return "reconnect";
		}
	},
	END {
		@Override
		public String toString() {
			return "end";
		}
	},
	UNKNOWN {
		@Override
		public String toString() {
			return "UNKNOWN";
		}
	};

	private static Commands getCommandByString(String command, String splitter) {
		String cmd = "";
		String param = "";
		try {
			cmd = command.split(splitter)[0];
			param = command.split(splitter)[1];
		} catch (Exception e) {
			cmd = command;
			param = null;
		}

		return getCommandByStringSplitted(cmd, param);
	}

	/**
	 * Transform a given string to a command (if possible)
	 * 
	 * @param command
	 *            String that contains the toString() value of a command
	 * @return the command if the string was recognized as valid command,
	 *         <i>null</i> otherwise
	 */
	public static Commands getCommandByStringSocket(String command) {
		return getCommandByString(command, ":");
	}

	/**
	 * Transform a given string to a command (if possible)
	 * 
	 * @param command
	 *            String that contains the toString() value of a command
	 * @return the command if the string was recognized as valid command,
	 *         <i>null</i> otherwise
	 */
	public static Commands getCommandByStringASCII(String command) {
		return getCommandByString(command, " ");
	}

	private static Commands getCommandByStringSplitted(String cmd, String param) {
		if (cmd.equals(CONNECT.toString()) && param == null) {
			return CONNECT;
		}
		if (cmd.equals(START.toString()) && param != null) {
			return START;
		}
		if (cmd.equals(TURN_OF.toString()) && param != null) {
			return TURN_OF;
		}
		if (cmd.equals(NEXT_TILE.toString()) && param != null) {
			return NEXT_TILE;
		}
		if (cmd.equals(ROTATE.toString()) && param == null) {
			return ROTATE;
		}
		if (cmd.equals(ROTATED.toString()) && param != null) {
			return ROTATED;
		}
		if (cmd.equals(PLACE.toString()) && param != null) {
			return PLACE;
		}
		if (cmd.equals(UPDATE.toString()) && param != null) {
			return UPDATE;
		}
		if (cmd.equals(MOVE_NOT_VALID.toString()) && param == null) {
			return MOVE_NOT_VALID;
		}
		if (cmd.equals(PASS.toString()) && param == null) {
			return PASS;
		}
		if (cmd.equals(MARKER.toString()) && param != null) {
			return MARKER;
		}
		if (cmd.equals(SCORE.toString()) && param != null) {
			return SCORE;
		}
		if (cmd.equals(LOCK.toString()) && param == null) {
			return LOCK;
		}
		if (cmd.equals(UNLOCK.toString()) && param == null) {
			return UNLOCK;
		}
		if (cmd.equals(LEAVE.toString()) && param != null) {
			return LEAVE;
		}
		if (cmd.equals(RECONNECT.toString()) && param != null) {
			return RECONNECT;
		}
		if (cmd.equals(END.toString()) && param != null) {
			return END;
		}

		return UNKNOWN;
	}

	/**
	 * Extract parameters from a command, use in the socket protocol
	 * 
	 * @param command
	 *            from which to extract the parameters
	 * @return <i>parameter</i> if found, <i>empty string</i> otherwise
	 */
	public static List<String> getParamsByString(String command) {
		List<String> param = new ArrayList<String>();
		try {
			StringTokenizer strToken = new StringTokenizer(
					command.split(":")[1], ",");
			while (strToken.hasMoreTokens()) {
				param.add(strToken.nextToken());
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// non ci sono parametri
			param.clear();
		}
		return param;
	}

	/**
	 * Extract parameters from a command, used in the ASCII mode
	 * 
	 * @param command
	 *            from which to extract the parameters
	 * @return <i>parameter</i> if found, <i>empty string</i> otherwise
	 */
	public static String getViewParamsByString(String command) {
		String param = "";
		try {
			param = command.split(" ")[1];
		} catch (Exception e) {
			// param = "";
		}
		return param;
	}
}
