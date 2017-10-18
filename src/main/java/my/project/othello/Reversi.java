package my.project.othello;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * Driver class for a game of Reversi (Othello).
 *
 * @author Aleksandr Popov
 */
public class Reversi {

	private static String playerInput;
	private static String lineClear;

	private static Scanner keyboard = new Scanner(System.in);
	private static boolean debug = false;
	private static int time = -1;

	// sets up the game
	private static Player introMessage(char piece) {

		while (true) {

			System.out.println("\nWhat kind of player should Player " + piece + " be (Human or Monte Carlo)?");
			playerInput = keyboard.nextLine().toLowerCase();

			// player 1 will be human
			if (playerInput.equals("human")) {

				System.out.println("");
				return new HumanPlayer(piece);
			}

			// player 1 will be a monte carlo player
			else if (playerInput.equals("monte carlo")) {

				while (true) {

					System.out.println("\nHow many seconds should the computer think (greater than 2)?");

					try {

						time = keyboard.nextInt();
						lineClear = keyboard.nextLine();
					} catch (InputMismatchException mme) {

						System.out.println("Invalid input.");
						continue;
					}

					if (!(time > 2)) {

						System.out.println("Invalid input.");
						continue;
					}

					break;
				}

				while (true) {

					System.out.println("\nDebug mode (y/n)?");
					playerInput = keyboard.nextLine();

					if (playerInput.toLowerCase().equals("y")) {

						debug = true;
					}

					else if (playerInput.toLowerCase().equals("n")) {

						debug = false;
					}

					else {

						System.out.println("Invalid input.");
						continue;
					}

					break;
				}

				System.out.println("");
				return new MonteCarloPlayer(piece, time, debug);
			}

			// user has improper input
			else {

				System.out.println("Invalid input.");
			}
		}
	}

	public static void main(String[] args) {

		// reversi board on which the game will be played
		Board rb = new Board();

		// creation of players/game setup
		Player player1 = introMessage('X');
		Player player2 = introMessage('O');

		System.out.println("\nWelcome to Othello! Human players please enters rows first then columns.");
		System.out.println("Typing q or quit will exit the game.");

		// main loop
		while (true) {

			// analyzes moves player 1 has
			player1.analyzeMoves(rb.getBoard());

			// if player 1 can make a move
			if (player1.hasMoves()) {

				// print board
				rb.printBoard(player1);

				// get input
				playerInput = player1.getInput();

				// update board
				rb.updateBoard(player1.getPiece(), playerInput, player1.getCoordinates(playerInput));

				player1.clearMap();
			}

			else {

				player2.analyzeMoves(rb.getBoard());

				// player 1 and 2 cannot make any moves (game has ended)
				if (!player2.hasMoves()) {

					break;
				}

				// player 1 can't make a move, but player 2 can
				else {

					System.out.println("\nX Player cannot make a move. Play passes to O player.");
				}
			}

			// analyzes moves player 2 has
			player2.analyzeMoves(rb.getBoard());

			// if player 2 can make a move
			if (player2.hasMoves()) {

				// print board
				rb.printBoard(player2);

				// get input
				playerInput = player2.getInput();

				// update board
				rb.updateBoard(player2.getPiece(), playerInput, player2.getCoordinates(playerInput));

				player2.clearMap();
			}

			else {

				player1.analyzeMoves(rb.getBoard());

				// player 1 and 2 cannot make any moves (game has ended)
				if (!player1.hasMoves()) {

					break;
				}

				// player 2 can't make a move, but player 1 can
				else {

					System.out.println("\nO Player cannot make a move. Play passes to X player.");
				}
			}
		}

		// gets piece of the player that won the game
		char winner = Board.getWinner(rb.getBoard());

		System.out.println("");

		if (winner == 'T') {

			System.out.println("Congratulations to both players! You have both won the game.\n");
		}

		else {

			System.out.println("Congratulations to player " + winner + "! You have won the game.\n");
		}
	}
}
