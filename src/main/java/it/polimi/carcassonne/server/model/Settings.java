package it.polimi.carcassonne.server.model;

import java.awt.Color;

/**
 * Global class with all the constant used in the application
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public final class Settings {

	private Settings() {
		// don't display my constructor
	}

	/**
	 * Set the server timeout before starting a match when at least 2 players
	 * are connected
	 * 
	 * @param timeout
	 *            to set
	 */
	public static void setTimeout(int timeout) {
		Settings.timeout = timeout;
	}

	/**
	 * Get the timeout of the server
	 * 
	 * @return timeout
	 */
	public static int getTimeout() {
		return timeout;
	}

	// Match
	public static final String MATCH_NAME_PREFIX = "carkMatch_";
	// Markers
	public static final int MAX_MARKERS = 7;
	// Server Timer
	private static final int DEFAULT_TIMEOUT = 5000;
	private static int timeout = DEFAULT_TIMEOUT;
	public static final int MAX_PLAYERS = 5;
	public static final int MIN_PLAYERS = 2;
	// Server RMI
	public static final String LOCALHOST = "127.0.0.1";
	public static final int SERVER_RMI_PORT = 4662;
	public static final int MANAGER_RMI_PORT = 4663;
	// Server Socket
	public static final int SERVER_SOCKET_PORT = 2004;
	// Socket order of parameters received
	public static final int UPDATE_TILE = 0;
	public static final int UPDATE_X_COORDINATE = 1;
	public static final int UPDATE_Y_COORDINATE = 2;
	public static final int START_TILE = 0;
	public static final int START_NAME = 1;
	public static final int START_COLOR = 2;
	public static final int START_NUM_PLAYERS = 3;
	public static final int TILE = 0;
	public static final int TURN_OF = 0;
	// Frame Size
	public static final int CARCASSONNE_HEIGHT = 565;
	public static final int CARCASSONNE_WIDTH = 1000;
	public static final int PRE_HEIGHT = 200;
	public static final int PRE_WIDTH = 454;
	public static final int NET_HEIGHT = 120;
	public static final int NET_WIDTH = 400;
	// Component Size
	public static final int SIDEBAR_WIDTH = 200;
	public static final int PLAYERS_PANEL_WIDTH = 1;
	public static final int PLAYERS_PANEL_HEIGHT = 5;
	public static final int TURN_HEIGHT = 27;
	public static final int HISTORY_WIDTH = 220;
	// Side Bar
	public static final int SIDEBAR_FOURTH_ELEMENT = 3;
	public static final int SIDEBAR_THIRD_ELEMENT_2 = 2;
	public static final int SIDEBAR_SECOND_ELEMENT = 1;
	public static final int SIDEBAR_FIRST_ELEMENT = 0;
	public static final int SIDEBAR_X = 0;
	// Custom Color
	public static final Color TRANSPARENT = new Color(0.0f, 0.0f, 0.0f, 0.0f);
	public static final Color TRED = new Color(1.0f, 0.0f, 0.0f, 0.5f);
	public static final Color TBLUE = new Color(0.0f, 0.0f, 1.0f, 0.5f);
	public static final Color TGREEN = new Color(0.0f, 1.0f, 0.0f, 0.5f);
	public static final Color TYELLOW = new Color(1.0f, 1.0f, 0.0f, 0.5f);
	public static final Color TBLACK = new Color(0.0f, 0.0f, 0.0f, 0.5f);
	// Font size
	public static final int HISTORY_PANEL_FONT_SIZE = 11;
	// Graphic Mode
	public static final String SWING = "swing";
	public static final String ASCII = "ascii";
	// Game Mode
	public static final String LOCAL = "Locale";
	public static final String NET = "Rete";
	// Net Mode
	public static final String RMI = "RMI";
	public static final String SOCKET = "Socket";
	// Images
	public static final int MARKER_SIZE = 30;
	public static final int TILE_SIZE = 120;
	public static final int MAX_ROTATIONS = 4;
	// Padding
	public static final int PADDING_NULL = 0;
	public static final int PADDING_5 = 5;
	public static final int PADDING_10 = 10;
	public static final int PADDING_15 = 15;
	public static final int PADDING_50 = 50;
	// Image Degree
	public static final int DEGREE_270 = 270;
	public static final int DEGREE_180 = 180;
	public static final int DEGREE_90 = 90;
	// Unique Tiles
	public static final String TILE_1 = "N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0";
	public static final String TILE_2 = "N=S S=S W=S E=S NS=0 NE=0 NW=0 WE=0 SE=0 SW=0";
	public static final String TILE_3 = "N=C S=S W=S E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=1";
	public static final String TILE_4 = "N=C S=N W=N E=C NS=0 NE=0 NW=0 WE=0 SE=0 SW=0";
	public static final String TILE_5 = "N=C S=S W=N E=S NS=0 NE=0 NW=0 WE=0 SE=1 SW=0";
	public static final String TILE_6 = "N=N S=N W=C E=C NS=0 NE=0 NW=0 WE=1 SE=0 SW=0";
	public static final String TILE_7 = "N=C S=S W=C E=C NS=0 NE=1 NW=1 WE=1 SE=0 SW=0";
	public static final String TILE_8 = "N=C S=C W=N E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=0";
	public static final String TILE_9 = "N=C S=S W=S E=S NS=0 NE=0 NW=0 WE=0 SE=0 SW=0";
	public static final String TILE_10 = "N=N S=S W=S E=S NS=0 NE=0 NW=0 WE=0 SE=0 SW=0";
	public static final String TILE_11 = "N=C S=N W=C E=C NS=0 NE=1 NW=1 WE=1 SE=0 SW=0";
	public static final String TILE_12 = "N=C S=N W=N E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=0";
	public static final String TILE_13 = "N=C S=S W=C E=S NS=0 NE=0 NW=1 WE=0 SE=1 SW=0";
	public static final String TILE_14 = "N=C S=N W=C E=N NS=0 NE=0 NW=1 WE=0 SE=0 SW=0";
	public static final String TILE_15 = "N=S S=S W=N E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0";
	public static final String TILE_16 = "N=N S=S W=S E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=1";

}
