package it.polimi.carcassonne.client.controller.socket;

import it.polimi.carcassonne.client.controller.MatchController;
import it.polimi.carcassonne.client.view.text.Display;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Asynchronous thread that will receive all the data from the server and parse
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
	 * 
	 * @param client
	 *            the socket of which to receive messages
	 * @param controller
	 *            of the match
	 */
	public SocketReceiver(Socket client, MatchController controller) {
		cmdParser = new CmdParser(controller);
		try {
			this.client = client;
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			isReallyConnected = true;
		} catch (IOException e) {
			Display.printError("Errore nell'inizializzazione dello stream reader");
		}
	}

	private String receiveMessage() {
		try {
			String message = in.readLine();
			if (message != null) {
				cmdParser.parseAndExecute(message);
				return message;
			}
		} catch (IOException e) {
			isReallyConnected = false;
		}
		return "";
	}

	/**
	 * Start listening for server data in asynchronous mode
	 */
	public void run() {
		while (client.isConnected() && isReallyConnected) {
			receiveMessage();
		}
		Display.printError("SERVER DISCONNESSO");
	}
}