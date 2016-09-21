package chess;

public class King extends ChessPiece {

	/** tracks how many times the king has moved */
	private int count;

	/**********************************************************************
	 * 
	 * king constructor to call super class constructor
	 * 
	 * @param player
	 *            indicates which side the piece belongs to
	 *********************************************************************/
	protected King(Player player) {
		super(player);
		count = 0;
	}

	/**********************************************************************
	 * 
	 * returns the type of the piece as a String
	 * 
	 * @return String the type of the piece
	 *********************************************************************/
	@Override
	public String type() {
		return "King";
	}

	/**********************************************************************
	 * 
	 * determines whether the piece can move a certain way or not
	 * 
	 * @param move
	 *            the move trying to be executed
	 * @param board
	 *            the chess board the move is being made on
	 * 
	 * @return boolean true if valid move, false if not
	 *********************************************************************/
	public boolean isValidMove(Move move, IChessPiece[][] board) {
		if (!super.isValidMove(move, board)) {
			return false;
		}
		if (Math.abs(move.toRow - move.fromRow) > 1
				|| Math.abs(move.toColumn - move.fromColumn) > 1) {
			return false;
		}
		return true;
	}

	/**********************************************************************
	 * 
	 * increments count by one
	 *********************************************************************/
	public void incrementCount() {
		count++;
	}

	/**********************************************************************
	 * 
	 * decrements count by one
	 *********************************************************************/
	public void decrementCount() {
		count--;
	}

	/**********************************************************************
	 * 
	 * returns count
	 * 
	 * @return count number of times king has moved
	 *********************************************************************/
	public int getCount() {
		return count;
	}
}
