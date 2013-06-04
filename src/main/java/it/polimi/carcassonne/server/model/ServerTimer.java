package it.polimi.carcassonne.server.model;

import static it.polimi.carcassonne.server.model.Settings.MAX_PLAYERS;
import static it.polimi.carcassonne.server.model.Settings.MIN_PLAYERS;
import it.polimi.carcassonne.server.controller.rmi.RMIServerImpl;
import it.polimi.carcassonne.server.controller.socket.SocketServer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Check the network match condition, maintaining a timer
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class ServerTimer {
	private Timer timer = null;
	private RMIServerImpl rmiServer = null;
	private SocketServer socketServer = null;

	/**
	 * Constructor called by a RMI server
	 * 
	 * @param server
	 */
	public ServerTimer(RMIServerImpl server) {
		this.rmiServer = server;
	}

	/**
	 * Constructor called by a Socket server
	 * 
	 * @param server
	 */
	public ServerTimer(SocketServer server) {
		this.socketServer = server;
	}

	/**
	 * If the array size is of 2 (the minimum number of player) start a new
	 * timer, if the size if of 5 (the maximum number of player) directly starts
	 * a new match
	 * 
	 * @param arraySize
	 * @return true if array size is of 5 (max players)
	 */
	public synchronized boolean fiveOrTimeout(int arraySize) {
		if (arraySize >= MIN_PLAYERS) {
			startTimer();
		}

		return (arraySize >= MAX_PLAYERS);
	}

	/**
	 * start a new timer and manage the rimer timeout by calling the
	 * startMatch() method of the caller
	 * 
	 * @see RMIServerImpl
	 * @see SocketServer
	 * 
	 * @return the timer
	 */
	private Timer startTimer() {
		if (timer == null) {
			timer = new Timer();
			TimerTask start = new TimerTask() {
				@Override
				public void run() {
					if (rmiServer != null) {
						rmiServer.startMatch();
					} else if (socketServer != null) {
						socketServer.startMatch();
					}
				}
			};
			timer.schedule(start, Settings.getTimeout());
		}
		return timer;
	}

	/**
	 * stop and reset the timer
	 */
	public void stopTimer() {
		timer.cancel();
		timer.purge();
		timer = null;
	}

}
