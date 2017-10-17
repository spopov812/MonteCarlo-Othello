package my.project.othello;

/**
 *
 * Represents coordinates on a board. Stores a row and a column.
 * 
 * @author Aleksandr Popov
 */
public class CoordinatePair {

	private int row;
	private int col;

	/**
	 *
	 * Constructor for a CoordinatePair object that represents coordinates on
	 * the board.
	 *
	 * @param row
	 *            row number of a coordinate on the board.
	 * @param col
	 *            column number of a coordinate on the board.
	 */
	public CoordinatePair(int row, int col) {

		this.row = row;
		this.col = col;
	}

	/**
	 *
	 * Returns a string representation of the coordinate on the board where the
	 * row and column are combined together.
	 *
	 * @return string representation of the coordinate on the board where the
	 *         row and column are combined together.
	 */
	public String toString() {

		String str = "";
		str += row;
		str += col;

		return str;

	}

	/**
	 *
	 * Returns the row value encoded in a string where the row and column are
	 * combined together.
	 *
	 * @param position
	 *            string representation of a coordinate on the board where the
	 *            row and column are combined together.
	 *
	 * @return the row of a coordinate on the board.
	 */
	public static int decodeRow(String position) {

		return Integer.parseInt(position) / 10;
	}

	/**
	 *
	 * Returns the column value encoded in a string where the row and column are
	 * combined together.
	 *
	 * @param position
	 *            string representation of a coordinate on the board where the
	 *            row and column are combined together.
	 *
	 * @return the column of a coordinate on the board.
	 */
	public static int decodeCol(String position) {

		return Integer.parseInt(position) % 10;
	}

	/**
	 *
	 * Returns the row of a coordinate pair.
	 *
	 * @return row of a coordinate pair on the board.
	 */
	public int getRow() {

		return row;
	}

	/**
	 *
	 * Returns the column of a coordinate pair.
	 *
	 * @return column of a coordinate pair on the board.
	 */
	public int getCol() {

		return col;
	}
}
