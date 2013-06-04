package it.polimi.carcassonne.client.controller.swing.listener;

import static it.polimi.carcassonne.server.model.Settings.ASCII;
import static it.polimi.carcassonne.server.model.Settings.RMI;
import static it.polimi.carcassonne.server.model.Settings.SOCKET;
import static it.polimi.carcassonne.server.model.Settings.SWING;
import it.polimi.carcassonne.client.controller.MatchController;
import it.polimi.carcassonne.client.controller.swing.RMIMatchController;
import it.polimi.carcassonne.client.controller.swing.SocketMatchController;
import it.polimi.carcassonne.client.controller.text.RMITextMatchController;
import it.polimi.carcassonne.client.controller.text.SocketTextMatchController;
import it.polimi.carcassonne.client.view.swing.SwingViewManager;
import it.polimi.carcassonne.client.view.swing.ViewManager;
import it.polimi.carcassonne.client.view.text.TextViewManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * If the user choose to play online, this class will create the right match
 * controller and starts the view mode selected my the user
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class NetListener implements ActionListener {

	private String netModeToLoad;
	private String graphicMode;

	/**
	 * Initialize a net listener
	 * 
	 * @param netModeToLoad
	 * @param graphicMode
	 */
	public NetListener(String netModeToLoad, String graphicMode) {
		this.netModeToLoad = netModeToLoad;
		this.graphicMode = graphicMode;
	}

	/**
	 * Start a new match creating a new controller for the net mode selected
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		ViewManager.getManager().disposeNetFrame();
		if (netModeToLoad.equals(RMI) && graphicMode.equals(SWING)) {
			SwingViewManager.getManager().startView(0, false);
			MatchController controller = new RMIMatchController();
			SwingViewManager.getManager().setMatchController(controller);

		} else if (netModeToLoad.equals(SOCKET) && graphicMode.equals(SWING)) {
			SwingViewManager.getManager().startView(0, false);
			MatchController controller = new SocketMatchController();
			SwingViewManager.getManager().setMatchController(controller);

		} else if (netModeToLoad.equals(RMI) && graphicMode.equals(ASCII)) {
			MatchController controller = new RMITextMatchController();
			TextViewManager.getManager().setMatchController(controller);
			TextViewManager.getManager().startView();

		} else if (netModeToLoad.equals(SOCKET) && graphicMode.equals(ASCII)) {
			MatchController controller = new SocketTextMatchController();
			TextViewManager.getManager().setMatchController(controller);
			TextViewManager.getManager().startView();
		}
	}
}
