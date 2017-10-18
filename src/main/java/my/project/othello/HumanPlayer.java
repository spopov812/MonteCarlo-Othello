package my.project.othello;

import java.lang.NumberFormatException;
import java.util.Scanner;

/**
 *
 * Reversi player that is controlled by a human.
 * 
 * @author Aleksandr Popov
 */
public class HumanPlayer extends Player {

	Scanner keyboard = new Scanner(System.in);

	/**
	 *
	 * Constructor for an object of type HumanPlayer, controlled by actual human
	 * and not computer. Sets the piece that the player will be playing with.
	 *
	 * @param piece
	 *            type of piece that the player will be playing with, also
	 *            deduced piece of opponent.
	 */
	public HumanPlayer(char piece) {

		this.piece = piece;

		if (piece == 'X') {

			otherPiece = 'O';
		}

		else {

			otherPiece = 'X';
		}
	}

	/**
	 *
	 * Takes input from the user as to what move should be played.
	 *
	 * @return string representation of the row and column that the user wishes
	 *         to move to.
	 */
	public String getInput() {

		String lineUserInput;
		boolean notValid = true;
		CoordinatePair humanCoords = null;

		while (notValid) {
			System.out.print(
					"Player " + (piece == 'X' ? "1" : "2") + " please make a move with the " + piece + " piece: ");
			lineUserInput = keyboard.nextLine();

			String[] userInput = lineUserInput.trim().split(" ");

			// error checking

			//user wants to quit
			try {
				if (userInput[0].toLowerCase().equals("q") || userInput[0].toLowerCase().equals("quit")) {

					System.out.println("\nExiting game...");
					System.exit(0);
				}
			} catch (ArrayIndexOutOfBoundsException oob) {

			}

			//user doesn't want to quit but entered invalid number of arguments
			if (userInput.length != 2) {

				System.out.println("\nInvalid input.\n");
				continue;
			}
			
			//checks if the input was a number
			try {

				humanCoords = new CoordinatePair(Integer.parseInt(userInput[0]) - 1,
						Integer.parseInt(userInput[1]) - 1);

			} catch (NumberFormatException nfe) {

				System.out.println("\nInvalid input.\n");
				continue;
			}

			//input is in bounds
			if (humanCoords.getRow() > 7 || humanCoords.getRow() < 0 || humanCoords.getRow() > 7
					|| humanCoords.getCol() < 0) {

				System.out.println("\nInvalid input.\n");
				continue;
			}

			//this is a valid move
			if (!hasKey(humanCoords)) {

				System.out.println("\nInvalid input.\n");
				continue;
			}

			notValid = false;
		}

		return humanCoords.toString();
	}
}
