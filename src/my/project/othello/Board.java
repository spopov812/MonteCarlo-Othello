package my.project.othello;

import java.util.ArrayList;

/**
 *
 * Represents the board used by a game of reversi
 *
 * @author Aleksandr Popov
 */
public class Board{

	private char[][] board = new char[8][8];


	/**
	*
	* Constructor for object of type Board. Sets pieces in proper starting
	* positions
	*/
	public Board(){

		board[3][3] = 'X';
		board[4][3] = 'O';
		board[3][4] = 'O';
		board[4][4] = 'X';

	}

	/**
	*
	* Returns the piece type of the player that won the game given a board state
	*
	* @param board board state for which a winner will be determined
	*
	* @return piece type of the player that won
	*/
	public static char getWinner(char[][] board){

		int xCount = 0;
		int oCount = 0;

		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){

				if (board[i][j] == 'X'){

					xCount++;
				}

				if (board[i][j] == 'O'){

					oCount++;
				}
			}
		}

		if (xCount > oCount){

			return 'X';
		}

		else if (oCount > xCount){

			return 'O';
		}

		else{

			return 'T';
		}
	}

	/**
	*
	* Copy of the board state within the object. Used to preserve encapsulation
	*
	* @return returns a copy of the current board state
	*/
	public char[][] getBoard(){

		char[][] temp = new char[8][8];

		for (int row = 0; row < board.length; row++){
			for (int col = 0; col < board[0].length; col++){

				temp[row][col] = board[row][col];
			}
		}

		return temp;
	}

	/**
	*
	* Updates the positions of piece on the board
	*
	* @param piece the piece that is making a move
	* @param position the position to which a player is making a move
	* @param list list of coordinates that should be flipped from the opponent's piece type to the piece passeed as a parameter
	*/
	public void updateBoard(char piece, String position, ArrayList<ArrayList<CoordinatePair>> list){

		board[CoordinatePair.decodeRow(position)][CoordinatePair.decodeCol(position)] = piece;
	
		for (int i = 0; i < list.size(); i++){
			for (int j = 0; j < list.get(i).size(); j++){

				board[list.get(i).get(j).getRow()][list.get(i).get(j).getCol()] = piece;
			}
		}
	}

	/**
	*
	* Prints the board state with dashes at the locations where a player can move to
	*
	* @param p player type for which the method will print dashes where they can move
	*/
	public void printBoard(Player p) {

		String temp = "";

		// first row printing
		System.out.println("");
		System.out.print("\t    ");

		for (int i = 1; i < 9; i++) {

			temp += (i == 8) ? i : i + " ";
		}

		System.out.print(temp + "\n");

		// rest of board
		for (int i = 1; i < 9; i++) {

			System.out.print("\t  " + i);

			for (int j = 0; j < board.length; j++) {

				if (p.hasKey(i - 1, j)) {

					System.out.print(" ?");
				}

				else if (board[i - 1][j] != 'X' && board[i - 1][j] != 'O') {

					System.out.print(" .");
				}

				else {

					System.out.print(" " + board[i - 1][j]);
				}
			}

			System.out.println(" " + i);
		}

		System.out.print("\t    ");

		temp = "";

		// last row printing
		for (int i = 1; i < 9; i++) {

			temp += (i == 8) ? i : i + " ";
		}

		System.out.print(temp + "\n");

		System.out.println("");
	}
}
