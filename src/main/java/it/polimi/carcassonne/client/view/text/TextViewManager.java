package it.polimi.carcassonne.client.view.text;

import it.polimi.carcassonne.client.controller.MatchController;
import it.polimi.carcassonne.server.model.Match;
import it.polimi.carcassonne.server.model.Player;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * The ASCII ViewManager of the match
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public class TextViewManager implements Observer {

	private static TextViewManager textViewManager;
	private MatchController matchController;

	/**
	 * initialize the vie manager
	 */
	public TextViewManager() {
		matchController = null;
	}

	/**
	 * Create or (if already created) returns a ViewManager
	 * 
	 * @return the ViewManager
	 */
	public static synchronized TextViewManager getManager() {
		if (textViewManager == null) {
			textViewManager = new TextViewManager();
		}
		return textViewManager;
	}

	/**
	 * 
	 * @return the match controller for this game
	 */
	public MatchController getMatchController() {
		return matchController;
	}

	/**
	 * set a match controller for he current match
	 * 
	 * @param matchController
	 * 
	 * @see MatchController
	 */
	public void setMatchController(MatchController matchController) {
		this.matchController = matchController;
	}

	/**
	 * Initialize a socket controller for the match
	 * 
	 */
	public void startView() {
		Display.welcome();
		matchController.startMatch();
	}

	/**
	 * Receive a model change and perform view changes
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		if (arg.getClass().equals(Match.class)) {
			Display.printMatchState((Match) arg);
		}
		Class<?>[] interfaces = arg.getClass().getInterfaces();
		for (Class<?> i : interfaces) {
			if (i.equals(List.class)) {
				Display.showWinners((List<Player>) arg);
				return;
			}
		}
	}
}
