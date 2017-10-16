package my.project.othello;

import java.util.*;

/**
 *
 * Abstract class that contains methods used by all players of a game of reversi
 *
 * @author Aleksandr Popov
 */
public abstract class Player{

	//stores which pieces a possible move would have to flip
	private HashMap<String, ArrayList<ArrayList<CoordinatePair>>> map = new HashMap <String, ArrayList<ArrayList<CoordinatePair>>>();

	protected char piece;
    protected char otherPiece;

	public abstract String getInput();

	/**
	* Getter for type of piece a player has control over
	*
	* @return Type of piece a player owns
	*/
        public char getPiece(){

                return piece;
        }

	/**
	*
	* Getter for piece other player has
	*
	* @return Type of piece the other player owns
	*/
        public char getOtherPiece(){

                return otherPiece;
        }


	/**
 	* 
 	* Getter for the list of locations to flip associated with a move
 	*
 	* @return Coordinates of piece that are to be flipped to the other piece than the current player
 	*/
	public ArrayList<ArrayList<CoordinatePair>> getCoordinates(String key){

		return map.get(key);
	}

	/**
	*
	* Updates hashmap for player that tracks which moves result in piece flips
	* in which coordinates, as well as possible moves the player may make
	*
	* @param Board state (2D char array)
	*/
	public void analyzeMoves(char[][] board){

		//scans board for type of piece
		for (int row = 0; row < board.length; row++){
			for (int col = 0; col < board[0].length; col++){

				if (board[row][col] == piece){

					ArrayList<CoordinatePair> toFlip = new ArrayList<CoordinatePair>();

					//searches right
					try{
						
					searchRight(board, row, col, toFlip);
					} catch(ArrayIndexOutOfBoundsException aie){

					}
					
					//searches left
					try{
						searchLeft(board, row, col, toFlip);
					} catch(ArrayIndexOutOfBoundsException aie){

					}
					
					//searches above
					try{
						searchAbove(board, row, col, toFlip);
					} catch(ArrayIndexOutOfBoundsException aie){

					}
					
					//searches below
					try{
						searchBelow(board, row, col, toFlip);
					} catch(ArrayIndexOutOfBoundsException aie){

					}
					//searches above right diagonal
					try{
				
						searchAboveRight(board, row, col, toFlip);
					} catch(ArrayIndexOutOfBoundsException aie){

					}

					//searches above left diagonal
					try{

						searchAboveLeft(board, row, col, toFlip);
					} catch(ArrayIndexOutOfBoundsException aie){

					}

					//searches below right diagonal
					try{

						searchBelowRight(board, row, col, toFlip);
					} catch(ArrayIndexOutOfBoundsException aie){

					}

					//searches below left diagonal
 					try{

						searchBelowLeft(board, row, col, toFlip);
 					} catch(ArrayIndexOutOfBoundsException aie){

                    }
				}
			}
		}
	}

	/**
	*
	* Clears hashmap that tracks which moves are possible 
	* and which piece are to be flipped by a move
	*
	*/
	public void clearMap(){

		map.clear();
	}

	/**
	*
	* Overloaded method. Checks to see if there is a possible move at given coordinate
	*
	* @param row row of a board
	* @param col column of a board 
	*/
	public boolean hasKey(int row, int col){

		CoordinatePair temp = new CoordinatePair(row, col);

		return map.containsKey(temp.toString());
	}
	
	/**
	*
	* Overloaded method. Checks to see if there is a possible move at a given coordinate
	*
	* @param coordinatePair object of type CoordinatePair that contains the row and column to check
	*/	
	public boolean hasKey(CoordinatePair coordinatePair){

		return map.containsKey(coordinatePair.toString());
	}

	/**
	*
	* Checks to see if player has possible moves
	*/
	public boolean hasMoves(){

		if (map.isEmpty()){

			return false;
		}
		

		return true;
	}

	/**
	*
	* Returns possible moves that a player can make
	*
	* @return Array of Strings that are the row and columns for possible moves
	*/
	public String[] getPossibleMoves(){

		Set<String> s = map.keySet();
        String[] temp = new String[s.size()];
            	
		s.toArray(temp);

		return temp;
	}
	
	//checks if a piece can be placed to the right of each piece
	private void searchRight(char[][] board, int row, int col, ArrayList<CoordinatePair> toFlip) {

		if (board[row][col + 1] == otherPiece) {

			toFlip = new ArrayList<CoordinatePair>();

			for (int i = col + 1; i < 8; i++) {

				// piece has same colored piece to the right of it
				if (board[row][i] == piece) {

					break;
				}

				// finds end of chain and pairs key to the value(array of coords
				// to flip)
				if (board[row][i] != piece && board[row][i] != otherPiece) {

					CoordinatePair key = new CoordinatePair(row, i);

					// already exists a key of this type
					if (hasKey(key)) {

						map.get(key.toString()).add(toFlip);
						break;
					}

					// no key of this type
					else {

						ArrayList<ArrayList<CoordinatePair>> outerArray = new ArrayList<ArrayList<CoordinatePair>>();
						outerArray.add(toFlip);
						map.put(key.toString(), outerArray);
						break;
					}
				}

				CoordinatePair cp = new CoordinatePair(row, i);
				toFlip.add(cp);
			}
		}
	}
	
	//checks if a piece can be placed to the left of each piece
	private void searchLeft(char[][] board, int row, int col, ArrayList<CoordinatePair> toFlip) {

		if (board[row][col - 1] == otherPiece) {

			toFlip = new ArrayList<CoordinatePair>();

			for (int i = col - 1; i > -1; i--) {

				// piece has same colored piece to the left of it
				if (board[row][i] == piece) {

					break;
				}

				// finds end of chain and pairs key to the value(array of coords
				// to flip)
				if (board[row][i] != piece && board[row][i] != otherPiece) {

					CoordinatePair key = new CoordinatePair(row, i);

					// already exists a key of this type
					if (hasKey(key)) {

						map.get(key.toString()).add(toFlip);
						break;
					}

					// no key of this type
					else {

						ArrayList<ArrayList<CoordinatePair>> outerArray = new ArrayList<ArrayList<CoordinatePair>>();
						outerArray.add(toFlip);
						map.put(key.toString(), outerArray);
						break;
					}
				}

				CoordinatePair cp = new CoordinatePair(row, i);
				toFlip.add(cp);
			}
		}
	}
	
	//checks if a piece can be placed above each piece
	private void searchAbove(char[][] board, int row, int col, ArrayList<CoordinatePair> toFlip) {

		if (board[row - 1][col] == otherPiece) {

			toFlip = new ArrayList<CoordinatePair>();

			for (int i = row - 1; i < 8; i--) {

				// piece has same colored piece above it
				if (board[i][col] == piece) {

					break;
				}

				// finds end of chain and pairs key to the value(array of coords
				// to flip)
				if (board[i][col] != piece && board[i][col] != otherPiece) {

					CoordinatePair key = new CoordinatePair(i, col);

					// already exists a key of this type
					if (hasKey(key)) {

						map.get(key.toString()).add(toFlip);
						break;
					}

					// no key of this type
					else {

						ArrayList<ArrayList<CoordinatePair>> outerArray = new ArrayList<ArrayList<CoordinatePair>>();
						outerArray.add(toFlip);
						map.put(key.toString(), outerArray);
						break;
					}
				}

				CoordinatePair cp = new CoordinatePair(i, col);
				toFlip.add(cp);
			}
		}
	}
	
	//checks if a piece can be placed below each piece
	private void searchBelow(char[][] board, int row, int col, ArrayList<CoordinatePair> toFlip) {

		if (board[row + 1][col] == otherPiece) {

			toFlip = new ArrayList<CoordinatePair>();

			for (int i = row + 1; i < 8; i++) {

				// piece has same colored piece below it
				if (board[i][col] == piece) {

					break;
				}

				// finds end of chain and pairs key to the value(array of coords
				// to flip)
				if (board[i][col] != piece && board[i][col] != otherPiece) {

					CoordinatePair key = new CoordinatePair(i, col);

					// already exists a key of this type
					if (hasKey(key)) {

						map.get(key.toString()).add(toFlip);
						break;
					}

					// no key of this type
					else {

						ArrayList<ArrayList<CoordinatePair>> outerArray = new ArrayList<ArrayList<CoordinatePair>>();
						outerArray.add(toFlip);
						map.put(key.toString(), outerArray);
						break;
					}
				}

				CoordinatePair cp = new CoordinatePair(i, col);
				toFlip.add(cp);
			}
		}
	}
	
	//checks if a piece can be placed on the above right diagonal of each piece
	private void searchAboveRight(char[][] board, int row, int col, ArrayList<CoordinatePair> toFlip){
		
		if (board[row - 1][col + 1] == otherPiece){

			int i = row;
			int j = col;

			toFlip = new ArrayList<CoordinatePair>();

			while(true){

				i--;
				j++;
				//piece has same colored piece right above diagonal
				if (board[i][j] == piece){

        				break;
				}

				//finds end of chain and pairs key to the value(array of coords to flip)
				if (board[i][j] != piece && board[i][j] != otherPiece){

        				CoordinatePair key = new CoordinatePair(i, j);
	
						//already exists a key of this type
						if (hasKey(key)){

						 	map.get(key.toString()).add(toFlip);
							break;
						}

						//no key of this type
						else{

							ArrayList<ArrayList<CoordinatePair>> outerArray = new ArrayList<ArrayList<CoordinatePair>>();
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
	
	//checks if a piece can be placed on the above left diagonal of each piece
	private void searchAboveLeft(char[][] board, int row, int col, ArrayList<CoordinatePair> toFlip){
		
		if (board[row - 1][col -1] == otherPiece){

			int i = row;
			int j = col;

			toFlip = new ArrayList<CoordinatePair>();

			while (true){

				i--;
				j--;
				//piece has same colored piece left above diagonal
				if (board[i][j] == piece){

        				break;
				}

				//finds end of chain and pairs key to the value(array of coords to flip)
				if (board[i][j] != piece && board[i][j] != otherPiece){

					CoordinatePair key = new CoordinatePair(i, j);

					//already exists a key of this type
					if (hasKey(key)){

		 				map.get(key.toString()).add(toFlip);
			 			break;
				 	}

					//no key of this type
					else{

						ArrayList<ArrayList<CoordinatePair>> outerArray = new ArrayList<ArrayList<CoordinatePair>>();
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
	
	//checks if a piece can be placed on the below right diagonal of each piece
	private void searchBelowRight(char[][] board, int row, int col, ArrayList<CoordinatePair> toFlip){
		
		if (board[row + 1][col + 1] == otherPiece){

			int i = row;
			int j = col;

			toFlip = new ArrayList<CoordinatePair>();

			while(true){

				i++;
				j++;

				//piece has same colored piece right below diagonal
				if (board[i][j] == piece){

        				break;
				}

				//finds end of chain and pairs key to the value(array of coords to flip)
				if (board[i][j] != piece && board[i][j] != otherPiece){

        				CoordinatePair key = new CoordinatePair(i, j);

						//already exists a key of this type
						if (hasKey(key)){

							map.get(key.toString()).add(toFlip);
							break;
						}

						//no key of this type
						else{

							ArrayList<ArrayList<CoordinatePair>> outerArray = new ArrayList<ArrayList<CoordinatePair>>();
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
	
	//checks if a piece can be placed on the below left diagonal of each piece
	private void searchBelowLeft(char[][] board, int row, int col, ArrayList<CoordinatePair> toFlip){
		
		if(board[row + 1][col - 1] == otherPiece) {

			int i = row;
			int j = col;

			toFlip = new ArrayList<CoordinatePair>();

			while(true){

				i++;
				j--;

				//piece has same colored piece right above diagonal
				if (board[i][j] == piece){

					break;
				}

				//finds end of chain and pairs key to the value(array of coords to flip)
				if (board[i][j] != piece && board[i][j] != otherPiece){

					CoordinatePair key = new CoordinatePair(i, j);

					//already exists a key of this type
					if (hasKey(key)){

						map.get(key.toString()).add(toFlip);
						break;
					}

					//no key of this type
					else{

						ArrayList<ArrayList<CoordinatePair>> outerArray = new ArrayList<ArrayList<CoordinatePair>>();
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
