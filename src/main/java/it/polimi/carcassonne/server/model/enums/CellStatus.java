package it.polimi.carcassonne.server.model.enums;

/**
 * Possible status assignable to a <b>cell</b>
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public enum CellStatus {

	ENABLED {
		@Override
		public String toString() {
			return "Enabled";
		}
	},
	HASTILE {
		@Override
		public String toString() {
			return "Has Tile";
		}

	},
	DISABLED {
		@Override
		public String toString() {
			return "Disabled";
		}
	};
}
