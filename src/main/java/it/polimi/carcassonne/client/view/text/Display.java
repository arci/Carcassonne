package it.polimi.carcassonne.client.view.text;

import static it.polimi.carcassonne.client.view.text.GridPrinter.printGrid;
import static it.polimi.carcassonne.client.view.text.GridPrinter.printTile;
import it.polimi.carcassonne.server.model.Match;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.enums.Commands;

import java.util.List;

/**
 * Class that displays various messages, errors, warnings, etc... to the console
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public final class Display {

	private Display() {
		// don't display my constructor
	}

	/**
	 * Prints to the console the message passed
	 * 
	 * @param message
	 *            to display
	 */
	public static void printMessage(String message) {
		System.out.println(message);
	}

	/**
	 * Prints to the console the warning passed
	 * 
	 * @param message
	 *            to display
	 */
	public static void printWarning(String message) {
		System.out.println("WARNING: " + message);
	}

	/**
	 * Prints to the console the error passed
	 * 
	 * @param message
	 *            to display
	 */
	public static void printError(String message) {
		System.out.println("ERROR: " + message);
	}

	/**
	 * Print a welcome message
	 */
	public static void welcome() {
		String welcomeMessage = "                                                                                                        *                                       \n        ######*****                                                                                    **                                       \n        ##########**                                                                                   ##                                       \n        ##     ***#*                                                                                   ##                                       \n        ##       *#*                                                                                   ##                                       \n        ##        #*     *******     ## ******  *#*      **#*  *******     ## ******     ##      ##  #######   *******                          \n        ##       *#*    **#####**    ##**####**  *#*     *#*  **#####**    ##**####**    ##      ##  #######  **#####**                         \n        ##     ***#*   **#*****#**   ##*** **#*  *#*     *#* **#*****#**   ##*** **#*    ##      ##    ##    **#** **#**                        \n        ##########*    *#*     *#*   ##*    *#*  ****   **** *#*     *#*   ##*    *#*    ##      ##    ##    *#*     *#*                        \n        ##########**   *#*     *#*   ##*    *#*   *#*   *#*  *#*     *#*   ##*    *#*    ##      ##    ##    *#*     *#*                        \n        ##     ***#**  *#########*   ##      ##   *#*   *#*  *#########*   ##      ##    ##      ##    ##    *#*     *#*                        \n        ##        *#*  *#########*   ##      ##    *#* *#*   *#########*   ##      ##    ##      ##    ##    *#       #*                        \n        ##        *#*  *#*           ##      ##    *#* *#*   *#*           ##      ##    ##     *##    ##    *#*     *#*                        \n        ##        *#*  *#*           ##      ##    *******   *#*           ##      ##    *#*    *##    ##    *#*     *#*                        \n        ##        *#*  *#*     *#*   ##      ##     *#*#*    *#*     *#*   ##      ##    *#*    *##    ##    *#*     *#*                        \n        ##      ***#*  **#** *****   ##      ##     *#*#*    **#** *****   ##      ##    *#** ***##    *#*   **#** **#**                        \n        ###########*    **######*    ##      ##      *#*      **######*    ##      ##    **#####*##    *####  **#####**                         \n        #######****      *******     ##      ##      *#*       *******     ##      ##     ****** ##    *****   *******    \n\n\n\n  \n                                                         ##                                                                                     \n                                                         ##                                                                                     \n                                                              ## ******                                                                         \n                                                         ##   ##**####**                                                                        \n                                                         ##   ##*** **#*                                                                        \n                                                         ##   ##*    *#*                                                                        \n                                                         ##   ##*    *#*                                                                        \n                                                         ##   ##      ##                                                                        \n                                                         ##   ##      ##                                                                        \n                                                         ##   ##      ##                                                                        \n                                                         ##   ##      ##                                                                        \n                                                         ##   ##      ##                                                                        \n                                                         ##   ##      ##                                                                        \n                                                         ##   ##      ##                                                                        \n                                                         ##   ##      ##\n\n\n\n          *******                                                                                                          ##                   \n         *######**                                                                                                         ##                   \n        *#*** **#**                                                                                                        ##                   \n       *#**     *#*                                                                                                        ##                   \n       *#*      ***   *******   ##****  ******    *******    ******    ******    *******   ##******  ##******   *******    ##                   \n       *#*           *######*   ##*#** **####**  *######*   *######*  *######*  **#####**  ##*####*  ##*####*   *#####**   ##                   \n       *#*           *#****#*   ##**   *#****#*  *#****#*   *#****#*  *#****#*  *#** **#*  ##****#*  ##****#*  *#** **#*   ##                   \n       ##                  #*   ##*    *#*  *#*        #*   *#*       *#*       *#*   *#*  ##*  *#*  ##*  *#*  *#*   *#*   ##                   \n       *#*             ****##   ##*    *#*         ****##   *##****   *##****   *#*   *#*  ##*   ##  ##*   ##  *#######*   ##                   \n       *#*      ***  **######   ##     *#        **######   ***###**  ***###**  *#     #*  ##    ##  ##    ##  *#######*   ##                   \n       *#*      *#*  *#****##   ##     *#*       *#****##     ****#*    ****#*  *#*   *#*  ##    ##  ##    ##  *#*         ##                   \n       *#**    **#*  *#*  *##   ##     *#*  *#*  *#*  *##        *#*       *#*  *#*   *#*  ##    ##  ##    ##  *#*                              \n        *#*** **#**  *#* **##*  ##     *#****#*  *#* **##*  *#****#*  *#****#*  *#** **#*  ##    ##  ##    ##  *#** **#*                        \n        **######**   *#######*  ##     **####**  *#######*  *######*  *######*  **#####**  ##    ##  ##    ##  **#####**   ##                   \n         ********    *******#*  ##      ******   *********   ******    ******    *******   ##    ##  ##    ##   *******    ##    \n";
		printMessage(welcomeMessage);
	}

	/**
	 * Visualize available commands in tile positioning phase
	 */
	public static void printTilePositioningCommand() {
		printMessage("Puoi digitare i comandi:");
		printMessage(Commands.PLACE.toString() + " x,y");
		printMessage(Commands.ROTATE.toString());
	}

	/**
	 * Visualize available commands in marker positioning phase
	 */
	public static void printMarkerPositioningCommand() {
		printMessage("Puoi digitare i comandi:");
		printMessage(Commands.MARKER.toString());
		printMessage(Commands.PASS.toString());
	}

	/**
	 * Visualize the players
	 * 
	 * @param players
	 *            the list of players to display
	 */
	public static void displayPlayers(List<Player> players) {
		printBlankLine();
		printMessage("Elenco giocatori: ");
		for (Player p : players) {
			printMessage("color[" + p.getColor() + "] markers["
					+ p.getAvailableMarkerCount() + "] score[" + p.getScore()
					+ "]");
		}
	}

	/**
	 * Visualize the match state
	 * 
	 * @param match
	 *            of which to display the state
	 */
	public static void printMatchState(Match match) {
		if (match.getPlayers() != null) {
			displayPlayers(match.getPlayers());
			printBlankLine();
		}
		if (match.getCurrentPlayer() != null) {
			turnOf(match.getCurrentPlayer());
			printBlankLine();
			displayPlayers(match.getPlayers());
			printBlankLine();
		}
		if (match.getLastDrawedTile() != null) {
			printTile(match.getLastDrawedTile());
			printBlankLine();
		}
		if (match.getGrid() != null) {
			printGrid(match.getGrid());
			printBlankLine();
		}
	}

	/**
	 * Visualize the winner(s)
	 * 
	 * @param matchWinners
	 *            the winner(s) to display
	 */
	public static void showWinners(List<Player> matchWinners) {
		printBlankLine();
		List<Player> winners = matchWinners;
		if (winners.size() == 1) {
			printMessage("La partita e' finita, il vincitore e': "
					+ winners.get(0).getColor() + " con un punteggio di: "
					+ winners.get(0).getScore());
		} else {
			printMessage("La partita e' finita in parita', i vincitori sono:");
			for (Player winner : winners) {
				printMessage(winner.getColor() + " con un punteggio di: "
						+ winner.getScore());
			}
		}
	}

	private static void turnOf(Player player) {
		printMessage("<----------  E' il turno di [" + player.getColor()
				+ "] marker[" + player.getAvailableMarkerCount() + "] score["
				+ player.getScore() + "]" + "  ---------->");
	}

	private static void printBlankLine() {
		printMessage("");
	}

}