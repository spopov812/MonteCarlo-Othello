package my.project.othello;

import java.util.*;

/**
 *
 * Abstract class that contains methods used by all players of a game of reversi.
 *
 * @author Aleksandr Popov
 */
public abstract class Player {

	// stores which pieces a possible move would have to flip
	private Map<String, List<List<CoordinatePair>>> map = new HashMap<String, List<List<CoordinatePair>>>();

	protected char piece;
	protected char otherPiece;

	public abstract String getInput();

	/**
	 * Getter for type of piece a player has control over.
	 *
	 * @return Type of piece a player owns.
	 */
	public char getPiece() {

		return piece;
	}

	/**
	 *
	 * Getter for piece other player has.
	 *
	 * @return Type of piece the other player owns.
	 */
	public char getOtherPiece() {

		return otherPiece;
	}

	/**
	 * 
	 * Getter for the list of locations to flip associated with a move.
	 *
	 * @return Coordinates of piece that are to be flipped to the other piece
	 *         than the current player.
	 */
	public List<List<CoordinatePair>> getCoordinates(String key) {

		return map.get(key);
	}

	/**
	 *
	 * Updates hashmap for player that tracks which moves result in piece flips
	 * in which coordinates, as well as possible moves the player may make.
	 *
	 * @param Board
	 *            board state (2D char array).
	 */
	public void analyzeMoves(char[][] board) {

		// scans board for type of piece
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {

				if (board[row][col] == piece) {

					//searches in all directions for potential moves
					searchAllDirections(board, row, col);
				}
			}
		}
	}

	/**
	 *
	 * Clears hashmap that tracks which moves are possible and which piece are
	 * to be flipped by a move.
	 *
	 */
	public void clearMap() {

		map.clear();
	}

	/**
	 *
	 * Overloaded method. Checks to see if there is a possible move at given
	 * coordinate.
	 *
	 * @param row
	 *            row of a board.
	 * @param col
	 *            column of a board.
	 */
	public boolean hasKey(int row, int col) {

		CoordinatePair temp = new CoordinatePair(row, col);

		return map.containsKey(temp.toString());
	}

	/**
	 *
	 * Overloaded method. Checks to see if there is a possible move at a given
	 * coordinate.
	 *
	 * @param coordinatePair
	 *            object of type CoordinatePair that contains the row and column
	 *            to check.
	 */
	public boolean hasKey(CoordinatePair coordinatePair) {

		return map.containsKey(coordinatePair.toString());
	}

	/**
	 *
	 * Checks to see if player has possible moves.
	 */
	public boolean hasMoves() {

		if (map.isEmpty()) {

			return false;
		}

		return true;
	}

	/**
	 *
	 * Returns possible moves that a player can make.
	 *
	 * @return Array of Strings that are the row and columns for possible moves.
	 */
	public String[] getPossibleMoves() {

		Set<String> s = map.keySet();
		String[] temp = new String[s.size()];

		s.toArray(temp);

		return temp;
	}

	/**
	 * 
	 * Calls other method that performs the search with proper offset in the x
	 * and y directions
	 * 
	 * @param board
	 *            board state
	 * @param currRow
	 *            row location of a player's piece on the board
	 * @param currCol
	 *            column location of a player's piece on the board
	 */
	private void searchAllDirections(char[][] board, int currRow, int currCol){
		
		for (int i = -1; i < 2; i ++){
			for (int j = -1; j < 2; j ++){
				
				if (i == 0 && j == 0){
					
					continue;
				}
				
				try{
					search(board, currRow, currCol, i, j);
				} catch (ArrayIndexOutOfBoundsException oob){
					
				}
			}
		}
	}

	/**
	 * 
	 * Searches in all 8 directions around a player's piece for an unbroken
	 * chain of opponent's pieces
	 * 
	 * @param board
	 *            board state
	 * @param currRow
	 *            row location of a player's piece on the board
	 * @param currCol
	 *            column location of a player's piece on the board
	 * @param xOffset
	 *            which direction in the x coordinate plane should the search go
	 * @param yOffset
	 *            which direction in the y coordinate plane should the search go
	 * @throws ArrayIndexOutOfBoundsException
	 */
	private void search(char[][] board, int currRow, int currCol, int xOffset, int yOffset) throws ArrayIndexOutOfBoundsException{
		
		if (board[currRow + xOffset][currCol + yOffset] == otherPiece) {

			int i = currRow;
			int j = currCol;

			List <CoordinatePair> toFlip = new ArrayList<CoordinatePair>();

			while (true) {

				i += xOffset;
				j += yOffset;
				
				// same piece type in that direction
				if (board[i][j] == piece) {

					break;
				}

				/*
				 * finds end of chain and pairs key to the value(array of coords
				 * to flip)
				 */
				if (board[i][j] != piece && board[i][j] != otherPiece) {

					CoordinatePair key = new CoordinatePair(i, j);

					// already exists a key of this type
					if (hasKey(key)) {

						map.get(key.toString()).add(toFlip);
						break;
					}

					// no key of this type
					else {

						List<List<CoordinatePair>> outerArray = new ArrayList<List<CoordinatePair>>();
						outerArray.add(toFlip);
						map.put(key.toString(), outerArray);
						break;
					}
				}

				CoordinatePair cp = new CoordinatePair(i, j);
				toFlip.add(cp);
			}
		}
	}
	
}
