package it.polimi.carcassonne.client.controller.swing;

import it.polimi.carcassonne.client.controller.MatchController;
import it.polimi.carcassonne.client.view.swing.SwingViewManager;
import it.polimi.carcassonne.client.view.swing.component.TileSearcher;
import it.polimi.carcassonne.resources.Resources;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Marker;
import it.polimi.carcassonne.server.model.Match;
import it.polimi.carcassonne.server.model.Player;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.PlayersColor;
import it.polimi.carcassonne.server.model.exceptions.IncompatibleFieldsException;
import it.polimi.carcassonne.server.model.exceptions.InvalidCellException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Abstract class that implements the MatchController interface, implementing
 * all the common method managing the swing interface
 * 
 * @see MatchController
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public abstract class CommonController implements MatchController {

	protected Match match;
	private boolean endGame;
	private boolean tilePlaced;
	private boolean markerPlaced;
	private PlayersColor myColor;
	private PlayersColor turnColor = null;

	@Override
	public void setEndGame() {
		endGame = true;
	}

	@Override
	public boolean isGameEnded() {
		return endGame;
	}

	@Override
	public boolean isTilePlaced() {
		return tilePlaced;
	}

	@Override
	public void setTilePlaced(boolean b) {
		tilePlaced = b;
	}

	@Override
	public boolean isMarkerPlaced() {
		return markerPlaced;
	}

	@Override
	public void setMarkerPlaced(boolean b) {
		markerPlaced = b;
	}

	@Override
	public boolean isMyTurn() {
		return turnColor.equals(myColor);
	}

	@Override
	public PlayersColor getTurnColor() {
		return turnColor;
	}

	@Override
	public void setTurnColor(PlayersColor playerColor) {
		turnColor = playerColor;
	}

	@Override
	public void setMyColor(PlayersColor myColor) {
		this.myColor = myColor;
	}

	@Override
	public int getCurrentPlayerMarker() {
		for (Player p : match.getPlayers()) {
			if (p.getColor().equals(myColor)) {
				return p.getAvailableMarkerCount();
			}
		}
		return 0;
	}

	@Override
	public void startMatch() {
		SwingViewManager.getManager().getSideBar().removeConnect();
		SwingViewManager.getManager().getTurnPanel()
				.setText("Sei connesso, in attesa di altri giocatori ....");
	}

	@Override
	public void createMatch(String matchName, Tile firstTile,
			PlayersColor color, int numPlayers) {
		match = new Match(matchName);
		match.addObserver(SwingViewManager.getManager());
		// aggiungo in locale i giocatori di questa partita
		for (int i = 0; i < numPlayers; i++) {
			match.addPlayer(new Player(PlayersColor.getColorByID(i)));
		}
		// salvo un rifermineto al colore assegnatomi
		myColor = color;
		// inizializzo la view
		SwingViewManager.getManager().getSideBar().addPlayersPanel(numPlayers);
		SwingViewManager.getManager().getSideBar().addSideBarComponent();
		// inizializzo un match locale
		match.start(firstTile);
		try {
			SwingViewManager.getManager().getSideBar()
					.setDeckSize(countDeckSize());
		} catch (IOException e) {
			SwingViewManager.getManager().getSideBar().setDeckSize(0);
		}
	}

	/*
	 * conta il numero di righe nel file carcassonne.txt per capire quante carte
	 * ci sono nel mazzo
	 */
	private int countDeckSize() throws IOException {
		int cont = -1;
		URL file = Resources.class.getResource("text/carcassonne.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.openStream()));
		try {
			while (reader.readLine() != null) {
				cont++;
			}
		} finally {
			reader.close();
		}
		return cont;
	}

	@Override
	public void nextTurn(PlayersColor playerColor) {
		this.turnColor = playerColor;
		tilePlaced = false;
		markerPlaced = false;
		match.notifyChange();
		// aggiorno turno e carte restanti
		SwingViewManager.getManager().getTurnPanel()
				.update(playerColor, isMyTurn());
		SwingViewManager.getManager().getSideBar().decreaseDeckSize();
		manageComponent();
	}

	private void manageComponent() {
		if (isMyTurn() && endGame) {
			SwingViewManager.getManager().getSideBar().disableRotate(true);
			return;
		}
		if (isMyTurn()) {
			// abilito
			SwingViewManager.getManager().getSideBar().disableRotate(false);
			SwingViewManager.getManager().getSideBar().disableNextTurn(true);
			SwingViewManager.getManager().getGrid().enableCells(true);
		} else {
			// disabilito tutto
			SwingViewManager.getManager().getSideBar().disableNextTurn(true);
			SwingViewManager.getManager().getSideBar().disableRotate(true);
			SwingViewManager.getManager().getGrid().enableCells(false);
		}
	}

	@Override
	public void setLastDrawedTile(Tile tile) {
		TileSearcher tileSearcher = new TileSearcher(tile);
		// aggiorno carta pescata
		SwingViewManager
				.getManager()
				.getSideBar()
				.setDrawedTile(tileSearcher.getPath(), tileSearcher.getDegree());
	}

	@Override
	public void setPlaceCondition(boolean reset) {
		if (!reset) {
			SwingViewManager.getManager().getSideBar().disableRotate(true);
			SwingViewManager.getManager().getSideBar().disableNextTurn(false);
		} else {
			setTilePlaced(false);
			SwingViewManager.getManager().getSideBar().disableNextTurn(true);
			SwingViewManager.getManager().getSideBar().disableRotate(false);
		}
	}

	@Override
	public void setMarkerCondition(boolean reset) {
		if (!reset) {
			SwingViewManager.getManager().getSideBar().disableNextTurn(true);
			SwingViewManager.getManager().getSideBar().disableRotate(false);
		} else {
			SwingViewManager.getManager().getSideBar().disableNextTurn(false);
			SwingViewManager.getManager().getSideBar().disableRotate(true);
		}
	}

	@Override
	public void placeDrawedTileInLocalGrid(Tile tile, Coordinate coordinate) {
		tilePlaced = true;
		try {
			match.placeTileWithoutCheckingNeighboring(tile, coordinate);
		} catch (InvalidCellException e) {
		} catch (IncompatibleFieldsException e) {
		}
	}

	@Override
	public void passTurn() {
		SwingViewManager.getManager().getHistoryPanel()
				.log("ha passato il turno");
		SwingViewManager.getManager().getSideBar().disableRotate(false);
		SwingViewManager.getManager().getSideBar().disableNextTurn(true);
	}

	@Override
	public void notifyEndMessage() {
		match.checkVictoryCondition();
	}

	/**
	 * 
	 * @param coord
	 *            relative coordinates
	 * @return coordinates absolute for tile insert
	 */
	public Coordinate getAbsolute(Coordinate coord) {
		int newX = coord.getX() - (-match.getGrid().getMinX());
		int newY = -(coord.getY() - match.getGrid().getMaxY());
		return new Coordinate(newX, newY);
	}

	@Override
	public Tile parseMarker(String tileDescription) {
		Tile tileToParse = new Tile(tileDescription);
		String[] strArray = tileDescription.split(" ");
		for (int i = 0; i < CardinalPoint.values().length; i++) {
			String[] markerColor = strArray[i].split("=")[1].split("\\+");
			if (markerColor.length > 1) {
				String cardinal = strArray[i].split("=")[0];
				Player p = match.getPlayerByColor(PlayersColor
						.get(markerColor[1]));
				Marker m = p.getMarker();
				m.setPosition(CardinalPoint.get(cardinal));
				tileToParse.setMarker(m);
				return tileToParse;
			}
		}
		return tileToParse;
	}

	@Override
	public void updateLocalMarker() {
		for (Player p : match.getPlayers()) {
			SwingViewManager.getManager().getSideBar()
					.setMarker(p.getColor(), p.getAvailableMarkerCount());
		}
	}

	@Override
	public void updateLocalScores(String scores) {
		String[] scoreArray = scores.split(", ");
		for (String s : scoreArray) {
			PlayersColor color = PlayersColor.getColorByString(s.split("=")[0]);
			int score = Integer.valueOf(s.split("=")[1]);
			match.getPlayerByColor(color).setScore(score);
		}
	}

	@Override
	public void updateLocalScores(List<String> score) {
		for (String s : score) {
			PlayersColor color = PlayersColor.getColorByString(s.split("=")[0]
					.trim());
			int point = Integer.valueOf(s.split("=")[1]);
			match.getPlayerByColor(color).setScore(point);
		}
	}
}
