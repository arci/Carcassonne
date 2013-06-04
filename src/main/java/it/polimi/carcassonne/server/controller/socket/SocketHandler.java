package it.polimi.carcassonne.server.controller.socket;

import it.polimi.carcassonne.client.view.text.Display;
import it.polimi.carcassonne.server.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Handler of incoming and outcoming data of a socket
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class SocketHandler {
	private BufferedReader in = null;
	private PrintStream out = null;
	private Socket clientSocket = null;
	private SocketReceiver receiver = null;

	/**
	 * Create a new socket handler
	 * 
	 * @param clientSocket
	 *            the socket of which to receive messages
	 * @param player
	 *            the owner of the socket <code>client</code>
	 */
	public SocketHandler(Socket clientSocket, Player player) {
		this.clientSocket = clientSocket;
		try {
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			out = new PrintStream(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			try {
				clientSocket.close();
			} catch (Exception e1) {
				Display.printError(e1.getMessage());
			}
			return;
		}
		receiver = new SocketReceiver(clientSocket, player);
	}

	/**
	 * Send a message with the socket
	 * 
	 * @param message
	 *            to send
	 */
	public void sendMessage(String message) {
		out.println(message);
	}

	/**
	 * Close the socket associated to the current socket handler
	 */
	public void closeSocket() {
		out.close();
		try {
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			Display.printError(e.getMessage());
		}
	}

	/**
	 * If the socket receiver associated to the current socket handler is not
	 * started, start it
	 */
	public void startListener() {
		if (!receiver.isAlive()) {
			receiver.start();
		}
	}
}
