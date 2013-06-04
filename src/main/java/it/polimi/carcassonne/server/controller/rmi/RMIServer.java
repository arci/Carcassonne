package it.polimi.carcassonne.server.controller.rmi;

import it.polimi.carcassonne.client.controller.rmi.RMIClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface showing the method for the RMI server
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public interface RMIServer extends Remote {

	/**
	 * register a new client on the server and assign him a color
	 * 
	 * @param client
	 * @throws RemoteException
	 */
	void register(RMIClient client) throws RemoteException;

}
