package chess;

public class Bishop extends ChessPiece {

	/**********************************************************************
	 * 
	 * knight constructor to call super class constructor
	 * 
	 * @param player
	 *            indicates which side the piece belongs to
	 *********************************************************************/
	protected Bishop(Player player) {
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
		return "Bishop";
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
		if (Math.abs((move.fromColumn - move.toColumn)
				/ (double) (move.fromRow - move.toRow)) != 1.0) {
			return false;
		}
		if (move.toColumn > move.fromColumn && move.toRow < move.fromRow) {
			for (int i = 1; i < move.fromRow - move.toRow; i++) {
				if (board[move.fromRow - i][move.fromColumn + i] != null) {
					return false;
				}
			}
		} else if (move.toColumn < move.fromColumn && move.toRow < move.fromRow) {
			for (int i = 1; i < move.fromRow - move.toRow; i++) {
				if (board[move.fromRow - i][move.fromColumn - i] != null) {
					return false;
				}
			}
		} else if (move.toColumn < move.fromColumn && move.toRow > move.fromRow) {
			for (int i = 1; i < move.toRow - move.fromRow; i++) {
				if (board[move.fromRow + i][move.fromColumn - i] != null) {
					return false;
				}
			}
		} else if (move.toColumn > move.fromColumn && move.toRow > move.fromRow) {
			for (int i = 1; i < move.toRow - move.fromRow; i++) {
				if (board[move.fromRow + i][move.fromColumn + i] != null) {
					return false;
				}
			}
		}
		return true;
	}
}
