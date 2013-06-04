package it.polimi.carcassonne.client.view.swing.component;

import static it.polimi.carcassonne.server.model.Settings.MARKER_SIZE;
import static it.polimi.carcassonne.server.model.Settings.PADDING_50;
import static it.polimi.carcassonne.server.model.Settings.TILE_SIZE;
import static it.polimi.carcassonne.server.model.Settings.TRANSPARENT;
import it.polimi.carcassonne.client.controller.swing.listener.CellListener;
import it.polimi.carcassonne.client.controller.swing.listener.MarkerListener;
import it.polimi.carcassonne.client.view.swing.SwingViewManager;
import it.polimi.carcassonne.resources.Resources;
import it.polimi.carcassonne.server.model.Cell;
import it.polimi.carcassonne.server.model.Coordinate;
import it.polimi.carcassonne.server.model.Grid;
import it.polimi.carcassonne.server.model.Tile;
import it.polimi.carcassonne.server.model.enums.CardinalPoint;
import it.polimi.carcassonne.server.model.enums.Field;
import it.polimi.carcassonne.server.model.enums.PlayersColor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class that create and update the grid board
 * 
 * @author Arcidiacono, Cesana
 * 
 */
public class GridBoard extends JPanel {

	private static final long serialVersionUID = -782705340150385993L;
	private int horizzontalTraslation;
	private int verticalTraslation;
	private List<JButton> enabledCell = new ArrayList<JButton>();

	/**
	 * create the new gridBoard and set a padding
	 */
	public GridBoard() {
		super();
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createCompoundBorder(null, BorderFactory
				.createEmptyBorder(PADDING_50, PADDING_50, PADDING_50,
						PADDING_50)));
	}

	/**
	 * Enable/Disable all the displayed cell
	 * 
	 * @param b
	 *            true for enabling, false otherwise
	 */
	public void enableCells(boolean b) {
		for (JButton button : enabledCell) {
			button.setEnabled(b);
		}
	}

	/**
	 * grid redraw
	 * 
	 * @param grid
	 */
	public void updateGrid(Grid grid) {
		verticalTraslation = grid.getMaxY();
		horizzontalTraslation = -grid.getMinX();
		enabledCell.clear();
		List<Cell> placeable = SwingViewManager.getManager()
				.getMatchController().getPossibleMoves();

		Set<Coordinate> list = grid.getGrid().keySet();
		Iterator<Coordinate> iter = list.iterator();
		removeAll();
		while (iter.hasNext()) {
			Coordinate key = iter.next();
			Cell cell = grid.getGrid().get(key);
			if (cell.isDisabled()) {
				addDisabledCellToGrid(cell.getCoordinates());
			} else if (cell.isEnabled()
					&& !SwingViewManager.getManager().getMatchController()
							.isGameEnded()) {
				evaluateDrawingEnabledCell(placeable, cell);
			} else if (cell.hasTile()) {
				drawTilePanel(grid, cell);
			}
		}
	}

	// genera un pannello con immagine della carta
	private void drawTilePanel(Grid grid, Cell cell) {
		TileSearcher tileSearcher = new TileSearcher(cell.getTile());
		TilePanel tilePanel = addTiledCellToGrid(cell.getCoordinates(),
				tileSearcher.getPath(), tileSearcher.getDegree());
		if (SwingViewManager.getManager().getMatchController().isTilePlaced()
				&& SwingViewManager.getManager().getMatchController()
						.isMyTurn()
				&& cell.equals(grid.getLastInsertedCell())
				&& SwingViewManager.getManager().getMatchController()
						.getCurrentPlayerMarker() > 0) {
			addMarkesButton(tilePanel, cell);
		}
		if (cell.getTile().getMarker() != null) {
			addMarkers(tilePanel, cell.getTile());
		}
	}

	/*
	 * se c'e' il suggerimento mossa mostra solo le celle in cui e' possibile
	 * posizionare, altrimenti le mostra tutte
	 */
	private void evaluateDrawingEnabledCell(List<Cell> placeable, Cell cell) {
		if (placeable != null) {
			if (!SwingViewManager.getManager().getMatchController()
					.isTilePlaced()
					&& placeable.contains(cell)) {
				addEnabledCellToGrid(cell.getCoordinates());
			}
		} else {
			if (!SwingViewManager.getManager().getMatchController()
					.isTilePlaced()) {
				addEnabledCellToGrid(cell.getCoordinates());
			}
		}
	}

	/*
	 * Aggiunge al bottone specificato un layer con i 4 pulsanti per i marker
	 */
	private void addMarkesButton(TilePanel tilePanel, Cell cell) {
		MarkerListener listener;
		JButton button;
		tilePanel.setLayout(new BorderLayout());

		for (CardinalPoint cardinal : CardinalPoint.values()) {
			if (cell.getTile().getField(cardinal) != Field.NIL) {
				button = new JButton(" ");
				button.setBorder(null);
				button.setBackground(TRANSPARENT);
				button.setPreferredSize(new Dimension(MARKER_SIZE, MARKER_SIZE));
				listener = new MarkerListener(cardinal);
				button.addMouseListener(listener);
				button.addActionListener(listener);
				if (cardinal.equals(CardinalPoint.NORTH)) {
					tilePanel.add(button, BorderLayout.NORTH);
				} else if (cardinal.equals(CardinalPoint.SOUTH)) {
					tilePanel.add(button, BorderLayout.SOUTH);
				} else if (cardinal.equals(CardinalPoint.EAST)) {
					tilePanel.add(button, BorderLayout.EAST);
				} else if (cardinal.equals(CardinalPoint.WEST)) {
					tilePanel.add(button, BorderLayout.WEST);
				}
			}
		}
	}

	/**
	 * Aggiunge i marker dove la carta ne ha
	 * 
	 * @param tilePanel
	 * @param tile
	 */
	public void addMarkers(TilePanel tilePanel, Tile tile) {
		if (tile.getMarker() == null) {
			return;
		}
		PlayersColor markerColor = tile.getMarkerOwner().getColor();
		JLabel marker = new JLabel(new ImageIcon(Resources.class.getResource("img/markers/" + markerColor
				+ "Marker.png")));
		CardinalPoint markerPosition = tile.getMarkerPosition();
		tilePanel.setLayout(new BorderLayout());
		if (markerPosition.equals(CardinalPoint.NORTH)) {
			tilePanel.add(marker, BorderLayout.NORTH);
		} else if (markerPosition.equals(CardinalPoint.SOUTH)) {
			tilePanel.add(marker, BorderLayout.SOUTH);
		} else if (markerPosition.equals(CardinalPoint.EAST)) {
			tilePanel.add(marker, BorderLayout.EAST);
		} else if (markerPosition.equals(CardinalPoint.WEST)) {
			tilePanel.add(marker, BorderLayout.WEST);
		}
	}

	private void addEnabledCellToGrid(Coordinate coord) {
		Coordinate newCoordinates = getRelative(coord);
		JButton button;
		button = new JButton(new ImageIcon(Resources.class.getResource("img/enabled.png")));
		button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		button.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
		enabledCell.add(button);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = newCoordinates.getX();
		c.gridy = newCoordinates.getY();
		add(button, c);
		button.addActionListener(new CellListener(newCoordinates.getX(),
				newCoordinates.getY()));
	}

	private void addDisabledCellToGrid(Coordinate coord) {
		Coordinate newCoordinates = getRelative(coord);
		JLabel label;
		label = new JLabel("");
		label.setBorder(BorderFactory.createEmptyBorder());
		label.setEnabled(false);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = newCoordinates.getX();
		c.gridy = newCoordinates.getY();
		add(label, c);
	}

	private TilePanel addTiledCellToGrid(Coordinate coord, int i,
			int degree) {
		Coordinate newCoordinates = getRelative(coord);
		TilePanel tilePanel;
		tilePanel = new TilePanel(i, degree);
		tilePanel.setBorder(BorderFactory.createEmptyBorder());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = newCoordinates.getX();
		c.gridy = newCoordinates.getY();
		add(tilePanel, c);
		return tilePanel;
	}

	private Coordinate getRelative(Coordinate coord) {
		int newX = coord.getX() + horizzontalTraslation;
		int newY = -coord.getY() + verticalTraslation;
		return new Coordinate(newX, newY);
	}

}