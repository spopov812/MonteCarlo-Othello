package my.project.othello;

/**
 *
 * Represents a player that inherits necessary methods from player class but
 * used only for simulation purposes in IntelligentComputerPlayer.
 *
 * @author Aleksandr Popov
 */
public class PseudoPlayer extends Player {

	/**
	 *
	 * Constructor for a PseudoPlayer object.
	 *
	 * @param piece
	 *            piece that the player will be placing on the board also
	 *            deduced the piece that the opponent will be playing as.
	 */
	public PseudoPlayer(char piece) {

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
	 * Implements abstract method defined in Player class, but returns null as
	 * this player will not be making any real moves on the real game board.
	 *
	 * @return null.
	 */
	public String getInput() {

		return null;
	}
}
