package it.polimi.carcassonne.server.main;

import it.polimi.carcassonne.client.view.text.Display;
import it.polimi.carcassonne.client.view.text.Reader;
import it.polimi.carcassonne.server.controller.rmi.RMIServerImpl;
import it.polimi.carcassonne.server.controller.socket.SocketServer;
import it.polimi.carcassonne.server.model.Settings;

/**
 * Class used to launch both the RMI and the socket servers
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public final class CarcassonneServer {

	private CarcassonneServer() {
		// don't display my constructor
	}

	/**
	 * Start the RMI and the socket servers
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		Display.printMessage("inserisci tempo di timeout (ms): ");
		int timeout = Reader.readInt();
		if (timeout < 0) {
			Display.printMessage("Verra' utilizzato il timeout di default: "
					+ Settings.getTimeout());
		} else {
			Settings.setTimeout(timeout);
		}
		RMIServerImpl rmiServer = new RMIServerImpl();
		SocketServer socketServer = new SocketServer();
		rmiServer.start();
		socketServer.start();
	}
}