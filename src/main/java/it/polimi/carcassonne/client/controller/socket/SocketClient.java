package it.polimi.carcassonne.client.controller.socket;

import static it.polimi.carcassonne.server.model.Settings.LOCALHOST;
import static it.polimi.carcassonne.server.model.Settings.SERVER_SOCKET_PORT;
import static it.polimi.carcassonne.server.model.enums.Commands.CONNECT;
import it.polimi.carcassonne.client.controller.MatchController;
import it.polimi.carcassonne.client.view.text.Display;

import java.io.IOException;
import java.net.Socket;

/**
 * Used to connect the client with the server and starts receiving messages from
 * him
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class SocketClient extends Thread {
	private SocketHandler socketHandler = null;
	private MatchController controller;

	/**
	 * Create a client socket associating a match controller
	 * 
	 * @param controller
	 *            of the match
	 */
	public SocketClient(MatchController controller) {
		this.controller = controller;
	}

	/**
	 * Send a message to the server
	 * 
	 * @param message
	 *            to send
	 */
	public void sendMessage(String message) {
		socketHandler.sendMessage(message);
	}

	/**
	 * Close the connection with the server
	 */
	public void disconnect() {
		socketHandler.disconnect();
	}

	/**
	 * Create a socket handler and starts receiving messages over it in
	 * asynchronous mode and send CONNECT message to the server
	 */
	public void run() {
		try {
			Socket client = new Socket(LOCALHOST, SERVER_SOCKET_PORT);
			socketHandler = new SocketHandler(client, controller);
			socketHandler.startListener();
			socketHandler.sendMessage(CONNECT.toString());
		} catch (IOException e) {
			Display.printError(e.getMessage());
		}
	}
}
