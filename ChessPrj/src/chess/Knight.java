package chess;

public class Knight extends ChessPiece {

	/**********************************************************************
	 * 
	 * knight constructor to call super class constructor
	 * 
	 * @param player
	 *            indicates which side the piece belongs to
	 *********************************************************************/
	protected Knight(Player player) {
		super(player);
	}

	/**********************************************************************
	 * 
	 * returns the type of the piece as a String
	 * 
	 * @return String the type of the piece
	 *********************************************************************/
	@Override
	public String type() {
		return "Knight";
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
		if (!(Math.abs(move.toRow - move.fromRow) == 2 && Math
				.abs(move.toColumn - move.fromColumn) == 1)
				&& !(Math.abs(move.toRow - move.fromRow) == 1 && Math
						.abs(move.toColumn - move.fromColumn) == 2)) {
			return false;
		}
		return true;
	}
}
