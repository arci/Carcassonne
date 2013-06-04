package it.polimi.carcassonne.server.controller.socket;

import it.polimi.carcassonne.client.view.text.Display;
import it.polimi.carcassonne.server.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Asynchronous thread that will receive all the data from the client and parse
 * the commands received
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class SocketReceiver extends Thread {
	private BufferedReader in = null;
	private Socket client = null;
	private boolean isReallyConnected = false;
	private CmdParser cmdParser = null;

	/**
	 * Create a new socket receiver
	 * 
	 * @param client
	 *            the socket of which to receive messages
	 * @param player
	 *            the owner of the socket <code>client</code>
	 */
	public SocketReceiver(Socket client, Player player) {
		cmdParser = new CmdParser(player);
		this.client = client;

		try {
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			isReallyConnected = true;
		} catch (IOException e) {
			Display.printError("Errore nell'inizializzazione dello stream reader");
		}
	}

	/**
	 * Start listening for server data in asynchronous mode
	 */
	public void run() {
		while (client.isConnected() && isReallyConnected) {
			Display.printMessage("Client>" + receiveMessage());
		}
		Display.printError("Client disconnesso!!!");
	}

	private String receiveMessage() {
		try {
			String message = in.readLine();
			if (message != null) {
				cmdParser.parseAndExecute(message);
				return message;
			}
		} catch (IOException e) {
			Display.printError(e.getMessage()); // DEBUG
			isReallyConnected = false;
		}
		return "";
	}
}