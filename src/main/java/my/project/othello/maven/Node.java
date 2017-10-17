package my.project.othello;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Represents a hypothetical (but possible) board state. Used to implement Monte
 * Carlo Tree Search Algorithm. Needs to know the hypothetical board state, its
 * parent node, the piece that is supposed to move, and the previous move made
 * to get to this board state.
 *
 * @author Aleksandr Popov
 */
public class Node {

	private char[][] boardState;
	private String prevMove;
	private List<Node> nodeChain = new ArrayList<Node>();
	private Node parentNode = null;
	private int numWins;
	private int numSimulations;
	private boolean wasUsed = false;
	private char nextMovePiece;
	private PseudoPlayer p = null;

	/**
	 *
	 * Constructor for a node object.
	 *
	 * @param board
	 *            hypothetical board state.
	 * @param parentNode
	 *            parent of current node, through which this current node can be
	 *            accessed.
	 * @param piece
	 *            piece type that is next to move.
	 * @param prevMove
	 *            previous move made to get to this board state.
	 */
	public Node(char[][] board, Node parentNode, char piece, String prevMove) {

		boardState = board;
		this.parentNode = parentNode;
		nextMovePiece = piece;
		this.prevMove = prevMove;
		p = new PseudoPlayer(piece);

		p.analyzeMoves(boardStateCopy());
	}

	/**
	 *
	 * Generates possible nodes (potential board states) from this current node.
	 *
	 */
	public void buildChain() {

		wasUsed = true;
		String[] boardMoves = p.getPossibleMoves();
		char[][] updatedBoard = null;
		List<List<CoordinatePair>> list = null;

		// generates new node in node chain
		for (int i = 0; i < boardMoves.length; i++) {

			updatedBoard = boardStateCopy();

			updatedBoard[CoordinatePair.decodeRow(boardMoves[i])][CoordinatePair
					.decodeCol(boardMoves[i])] = nextMovePiece;

			list = p.getCoordinates(boardMoves[i]);

			for (int j = 0; j < list.size(); j++) {
				for (int k = 0; k < list.get(j).size(); k++) {

					updatedBoard[list.get(j).get(k).getRow()][list.get(j).get(k).getCol()] = nextMovePiece;
				}
			}

			nodeChain.add(new Node(updatedBoard, this, generateChildPiece(nextMovePiece), boardMoves[i]));
		}
	}

	/**
	 *
	 * Updates child nodes stemming from current node. Used only when it is not
	 * possible to generate child nodes. Updates piece type that is next to
	 * move and tries to build a node chain.
	 */
	public void regenerateChain() {

		nextMovePiece = generateChildPiece(nextMovePiece);
		buildChain();
	}

	/**
	 *
	 * Returns a copy of the hypothetical board state stored in this node.
	 *
	 * @return Board state that this node is tracking.
	 */
	public char[][] boardStateCopy() {

		char[][] copy = new char[8][8];

		for (int i = 0; i < boardState.length; i++) {
			for (int j = 0; j < boardState.length; j++) {

				copy[i][j] = boardState[i][j];
			}
		}

		return copy;
	}

	/**
	 *
	 * Returns a random node that is a child of the current node.
	 *
	 * @return random node that is a child of the current node.
	 */
	public Node getRandomNode() {

		Random r = new Random();

		int randNum = 0;

		randNum = r.nextInt(nodeChain.size());

		return nodeChain.get(randNum);
	}

	/**
	 * Returns whether or not there are possible child nodes from current node.
	 *
	 * @return returns true if there are possible child nodes from the current
	 *         node, false otherwise.
	 */
	public boolean hasNodes() {

		if (nodeChain.size() > 0) {

			return true;
		}

		return false;
	}

	/**
	 *
	 * Returns parent node of current node.
	 *
	 * @return node that is the parent of the current node.
	 */
	public Node getParent() {

		return parentNode;
	}

	/**
	 *
	 * Returns whether or not a node has a parent.
	 *
	 * @return returns true if current node has a parent, false otherwise.
	 */
	public boolean hasParent() {

		if (parentNode == null) {

			return false;
		}

		else {
			return true;
		}
	}

	/**
	 *
	 * Sets the current node as the parent to all nodes. Represents the real
	 * board state from which all other nodes will stem.
	 *
	 */
	public void setParent() {

		parentNode = null;
	}

	/**
	 *
	 * Updates number of simulations run and wins when running simulations
	 * through this node. Used in backpropagation phase.
	 *
	 * @param win
	 *            whether or not a simulation going through this node has
	 *            resulted in a win.
	 */
	public void updateStats(boolean win) {

		if (win) {

			numWins++;
		}

		numSimulations++;
	}

	/**
	 * Compares board states of two nodes and returns if they are the same.
	 *
	 * @param node
	 *            node whose board state will be compared to calling objects'.
	 *
	 * @return whether or not the board states of two nodes are identical.
	 */
	public boolean equals(Node node) {

		char[][] thisBoard = this.boardStateCopy();
		char[][] nodeBoard = node.boardStateCopy();

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				if (thisBoard[i][j] != nodeBoard[i][j]) {

					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Compares node's board state and a board state.
	 *
	 * @param board
	 *            board that will be compared to the board state within a node.
	 * 
	 * @return whether or not the board states node's and argument's are
	 *         identical.
	 */

	public boolean equalsGameState(char[][] board) {

		char[][] thisBoard = this.boardStateCopy();

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				if (thisBoard[i][j] != board[i][j]) {

					return false;
				}
			}
		}

		return true;
	}

	/**
	 *
	 * Returns whether or not this node has had any child nodes generated.
	 *
	 * @return returns true if this node has had any child nodes generated,
	 *         returns false otherwise.
	 */
	public boolean wasUsed() {

		return wasUsed;
	}

	/**
	 *
	 * Returns the children of this node (possible board states stemming from
	 * this board state).
	 *
	 * @return children of this node (possible board states stemming from this
	 *         board state).
	 */
	public List<Node> getChildren() {

		return nodeChain;
	}

	/**
	 * Returns the number of wins made when simulations went through this node.
	 *
	 * @return number of wins made when simulations went through this node.
	 */
	public int getWins() {

		return numWins;
	}

	/**
	 * Returns the number of simulations that went through this node.
	 *
	 * @return number of simulations that went through this node.
	 */
	public int getSimulations() {

		return numSimulations;
	}

	/**
	 *
	 * Returns the move made from the parent node to get to this node.
	 *
	 * @return string representation of the move made from the parent node to
	 *         get to this node.
	 */
	public String getPrevMove() {

		return prevMove;
	}

	/**
	 * Returns a piece of the opposite type as the one given.
	 *
	 * @param piece
	 *            a piece that a node has to make a move with.
	 *
	 * @return a piece of the opposite type.
	 */
	private char generateChildPiece(char piece) {

		if (piece == 'X') {

			return 'O';
		}

		else {

			return 'X';
		}
	}
}
