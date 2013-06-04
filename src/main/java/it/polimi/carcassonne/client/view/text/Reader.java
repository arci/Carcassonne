package it.polimi.carcassonne.client.view.text;

import java.util.Scanner;

/**
 * Utility class used to read the user input from the console
 * 
 * @author Cesana, Arcidiacono
 * 
 */
public final class Reader {
	private static Scanner in = new Scanner(System.in);

	private Reader() {
		// don't display my constructor
	}

	/**
	 * Reads an integer from the console (<i>System.in</i>)
	 * 
	 * @return the integer read if is between 2 and 5
	 */
	public static int readInt() {
		int next = -1;
		try {
			next = Integer.parseInt(readLine());
		} catch (Exception e) {
			Display.printError("Inserisci un numero");
		}
		return next;
	}

	/**
	 * Reads a line from the console (<i>System.in</i>)
	 * 
	 * @return the line read
	 */
	public static String readLine() {
		return in.nextLine();
	}

}
