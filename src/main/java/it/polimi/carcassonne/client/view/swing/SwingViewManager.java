package it.polimi.carcassonne.client.view.swing;

import static it.polimi.carcassonne.server.model.Settings.CARCASSONNE_HEIGHT;
import static it.polimi.carcassonne.server.model.Settings.CARCASSONNE_WIDTH;
import static it.polimi.carcassonne.server.model.Settings.HISTORY_WIDTH;
import static it.polimi.carcassonne.server.model.Settings.SIDEBAR_WIDTH;
import static it.polimi.carcassonne.server.model.Settings.TURN_HEIGHT;
import it.polimi.carcassonne.client.controller.MatchController;
import it.polimi.carcassonne.client.view.swing.component.GridBoard;
import it.polimi.carcassonne.client.view.swing.component.HistoryPanel;
import it.polimi.carcassonne.client.view.swing.component.SideBar;
import it.polimi.carcassonne.client.view.swing.component.TileSearcher;
import it.polimi.carcassonne.client.view.swing.component.TurnPanel;
import it.polimi.carcassonne.client.view.swing.frame.CarcassonneFrame;
import it.polimi.carcassonne.server.model.Grid;
import it.polimi.carcassonne.server.model.Match;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.Settings;
import it.polimi.carcassonne.server.model.Tile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Maintains a ref. to all the graphic element of the game view
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class SwingViewManager implements Observer {

	private static SwingViewManager swingViewManager;
	private CarcassonneFrame carcassonneFrame;
	private SideBar sideBar;
	private GridBoard gridBoard;
	private JScrollPane scrollablePane;
	private MatchController matchController;
	private TurnPanel turnPanel;
	private HistoryPanel historyPanel;
	private JPanel gamePanel;

	/**
	 * Set the component of the game view
	 */
	public SwingViewManager() {
		carcassonneFrame = new CarcassonneFrame("Carcassonne",
				CARCASSONNE_HEIGHT, CARCASSONNE_WIDTH);
		gamePanel = new JPanel();
		gamePanel.setBackground(Color.BLUE);
		historyPanel = new HistoryPanel();
		turnPanel = new TurnPanel();
		gridBoard = new GridBoard();
		scrollablePane = new JScrollPane(gridBoard);
		scrollablePane.setBackground(Settings.TRANSPARENT);
		scrollablePane.setOpaque(true);
		scrollablePane.setPreferredSize(new Dimension(CARCASSONNE_WIDTH
				- SIDEBAR_WIDTH - HISTORY_WIDTH, CARCASSONNE_HEIGHT
				- TURN_HEIGHT));
		sideBar = null;
		matchController = null;
	}

	/**
	 * 
	 * @return a singleton instance of the swing view manager
	 */
	public static synchronized SwingViewManager getManager() {
		if (swingViewManager == null) {
			swingViewManager = new SwingViewManager();
		}
		return swingViewManager;
	}

	public TurnPanel getTurnPanel() {
		return turnPanel;
	}

	public MatchController getMatchController() {
		return matchController;
	}

	/**
	 * set the match controller of the current match, the controller MUST
	 * implement the MatchController interface
	 * 
	 * @param matchController
	 * 
	 * @see MatchController
	 */
	public void setMatchController(MatchController matchController) {
		this.matchController = matchController;
	}

	public SideBar getSideBar() {
		return sideBar;
	}

	public GridBoard getGrid() {
		return gridBoard;
	}

	public CarcassonneFrame getCarcassonneFrame() {
		return carcassonneFrame;
	}

	public HistoryPanel getHistoryPanel() {
		return historyPanel;
	}

	/**
	 * Start a new game view
	 * 
	 * @param numPlayers
	 *            the numbers of the players of the match
	 * @param local
	 *            if true show on the west side the history panel
	 */
	public void startView(int numPlayers, boolean local) {
		carcassonneFrame.setLayout(new BorderLayout());
		if (local) {
			carcassonneFrame.add(historyPanel, BorderLayout.WEST);
		}
		gamePanel.setLayout(new BorderLayout());
		gamePanel.setBackground(Settings.TRANSPARENT);
		gamePanel.setOpaque(true);
		gamePanel.add(turnPanel, BorderLayout.NORTH);
		gamePanel.add(scrollablePane, BorderLayout.CENTER);
		sideBar = new SideBar(numPlayers, local);
		gamePanel.add(sideBar, BorderLayout.EAST);
		carcassonneFrame.add(gamePanel, BorderLayout.CENTER);
		carcassonneFrame.pack();
		carcassonneFrame.setLocationRelativeTo(null);
		carcassonneFrame.setVisible(true);
	}

	/**
	 * update the view after a model change
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		Class<?>[] interfaces = arg.getClass().getInterfaces();
		for (Class<?> i : interfaces) {
			if (i.equals(List.class)) {
				List<Player> winners = (List<Player>) arg;
				StringBuffer message = new StringBuffer();
				if (winners.size() == 1) {
					message.append("La partita e' finita, il vincitore e': \n");
					message.append(winners.get(0).getColor()
							+ " con un punteggio di: "
							+ winners.get(0).getScore() + "\n");
				} else {
					message.append("La partita e' finita in parita', i vincitori sono: \n");
					for (Player p : winners) {
						message.append(p.getColor() + " con un punteggio di: "
								+ p.getScore() + "\n");
					}
				}
				sideBar.disableRotate(true);
				JOptionPane.showMessageDialog(SwingViewManager.getManager()
						.getCarcassonneFrame(), message, "Partita Terminata",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
		if (arg.getClass().equals(Match.class)) {
			Match match = (Match) arg;
			Grid grid = match.getGrid();
			if (grid != null) {
				gridBoard.updateGrid(grid);
				scrollablePane.validate();
				gamePanel.repaint();
				carcassonneFrame.pack();
			}
			Tile lastDrawedTile = matchController.getLastDrawedTile();
			if (lastDrawedTile != null && sideBar.getDrawedTilePanel() != null) {
				TileSearcher tileSearcher = new TileSearcher(lastDrawedTile);
				sideBar.setDrawedTile(tileSearcher.getPath(),
						tileSearcher.getDegree());
			}
			List<Player> players = match.getPlayers();
			for (Player p : players) {
				sideBar.setMarker(p.getColor(), p.getAvailableMarkerCount());
				sideBar.setScore(p.getColor(), p.getScore());
			}
		}
	}
}
