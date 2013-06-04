package it.polimi.carcassonne.client.controller.socket;

import it.polimi.carcassonne.client.controller.MatchController;
import it.polimi.carcassonne.client.view.text.Display;

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
	 * @param controller
	 *            of the match
	 */
	public SocketHandler(Socket clientSocket, MatchController controller) {
		try {
			this.clientSocket = clientSocket;
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			out = new PrintStream(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			try {
				clientSocket.close();
			} catch (Exception e1) {
				Display.printError(e.getMessage());
			}
			return;
		}
		receiver = new SocketReceiver(clientSocket, controller);
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
	public void disconnect() {
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