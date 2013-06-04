package it.polimi.carcassonne.server.controller.rmi;

import static it.polimi.carcassonne.server.model.Settings.MANAGER_RMI_PORT;
import static it.polimi.carcassonne.server.model.Settings.SERVER_RMI_PORT;
import it.polimi.carcassonne.client.controller.rmi.RMIClient;
import it.polimi.carcassonne.client.view.text.Display;
import it.polimi.carcassonne.server.model.ServerTimer;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An implementation of the RMIServer interface
 * 
 * @see RMIServer
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class RMIServerImpl extends Thread implements RMIServer {

	private ServerTimer timer;
	private Map<RMIClient, PlayersColor> clients;
	private int assignedColor;
	private RMIMatchManagerImpl manager;

	/**
	 * create a new timer and initialize the other properties
	 */
	public RMIServerImpl() {
		timer = new ServerTimer(this);
		clients = new ConcurrentHashMap<RMIClient, PlayersColor>();
		assignedColor = 0;
	}

	private void resetServerCondition() {
		timer = new ServerTimer(this);
		assignedColor = 0;
		manager = null;
		clients.clear();
	}

	@Override
	public void run() {
		try {
			// registry for connection listener
			Registry registry = LocateRegistry.createRegistry(SERVER_RMI_PORT);
			// registry for match manager
			LocateRegistry.createRegistry(MANAGER_RMI_PORT);
			RMIServer serverStub = (RMIServer) UnicastRemoteObject
					.exportObject(this, 0);
			registry.rebind("CarcassonneServer", serverStub);
			Display.printMessage("RMI Server ready, stub server is registred on port: "
					+ SERVER_RMI_PORT); // DEBUG
		} catch (Exception e) {
			Display.printError("RMI Server exception: " + e.toString());
		}
	}

	@Override
	public void register(RMIClient client) throws RemoteException {
		clients.put(client, PlayersColor.getColorByID(assignedColor));
		assignedColor++;
		Display.printMessage("client connesso"); // DEBUG
		if (timer.fiveOrTimeout(clients.size())) {
			startMatch();
		}
	}

	/**
	 * Stop the timer and start a new match creating a new match controller and
	 * resetting the client list
	 */
	public void startMatch() {
		timer.stopTimer();
		Map<RMIClient, PlayersColor> clientOfTheMatch = new HashMap<RMIClient, PlayersColor>();
		clientOfTheMatch.putAll(clients);
		manager = new RMIMatchManagerImpl(clientOfTheMatch);
		manager.start();
		resetServerCondition();
	}
}
