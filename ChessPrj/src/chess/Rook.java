package chess;

public class Rook extends ChessPiece {

	/** tracks how many times the rook has moved */
	private int count;

	/**********************************************************************
	 * 
	 * knight constructor to call super class constructor
	 * 
	 * @param player
	 *            indicates which side the piece belongs to
	 *********************************************************************/
	protected Rook(Player player) {
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
		return "Rook";
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
		if (move.toRow - move.fromRow != 0
				&& move.toColumn - move.fromColumn != 0) {
			return false;
		}
		if (move.fromRow - move.toRow == 0 && move.toColumn > move.fromColumn) {
			for (int j = move.fromColumn + 1; j < move.toColumn; j++) {
				if (board[move.fromRow][j] != null) {
					return false;
				}
			}
		} else if (move.fromRow - move.toRow == 0
				&& move.toColumn < move.fromColumn) {
			for (int j = move.fromColumn - 1; j > move.toColumn; j--) {
				if (board[move.fromRow][j] != null) {
					return false;
				}
			}
		} else if (move.fromColumn - move.toColumn == 0
				&& move.toRow > move.fromRow) {
			for (int i = move.fromRow + 1; i < move.toRow; i++) {
				if (board[i][move.fromColumn] != null) {
					return false;
				}
			}
		} else if (move.fromColumn - move.toColumn == 0
				&& move.toRow < move.fromRow) {
			for (int i = move.fromRow - 1; i > move.toRow; i--) {
				if (board[i][move.fromColumn] != null) {
					return false;
				}
			}
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
	 * @return count number of times rook has moved
	 *********************************************************************/
	public int getCount() {
		return count;
	}
}
