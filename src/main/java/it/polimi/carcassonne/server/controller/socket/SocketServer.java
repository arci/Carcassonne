package it.polimi.carcassonne.server.controller.socket;

import static it.polimi.carcassonne.server.model.Settings.SERVER_SOCKET_PORT;
import it.polimi.carcassonne.client.view.text.Display;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.ServerTimer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is needed to start a server to play at Carcassonne on Internet
 * using socket protocol
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class SocketServer extends Thread {

	private ServerTimer timer;
	private ServerSocket server;
	private static List<Player> availablePlayers = new ArrayList<Player>();

	/**
	 * Creates a new server and start listening on the default port
	 * <code>SERVER_SOCKET_PORT</code>, configurable in "Settings"
	 */
	public SocketServer() {
		try {
			server = new ServerSocket(SERVER_SOCKET_PORT);
			timer = new ServerTimer(this);
		} catch (IOException e) {
			Display.printError(e.getMessage());
		}
	}

	/**
	 * Start accepting all the incoming requests of connection in asynchronous
	 * mode
	 */
	public void run() {
		while (true) {
			try {
				Display.printMessage("Socket server ready on port: "
						+ SERVER_SOCKET_PORT);
				Socket clientSocket = server.accept();
				Display.printMessage("Connection accepted by: "
						+ clientSocket.getInetAddress());

				Player player = new Player(clientSocket);
				addPlayer(player);

			} catch (IOException e) {
				Display.printError("Error accepting connection request");
			}
		}
	}

	/**
	 * Adds a player to the current list of players that are not playing any
	 * match already
	 * 
	 * @param player
	 *            to add
	 */
	public void addPlayer(Player player) {
		availablePlayers.add(player);
		if (timer.fiveOrTimeout(availablePlayers.size())) {
			startMatch();
		}
	}

	/**
	 * Start a match with the players that are not playing any match already
	 */
	public void startMatch() {
		timer.stopTimer();
		SocketMatchManager game = new SocketMatchManager(availablePlayers);
		availablePlayers.clear();
		game.start();
	}
}