package chess;

public class Pawn extends ChessPiece {

	/**********************************************************************
	 * 
	 * pawn constructor to call super class constructor
	 * 
	 * @param player
	 *            indicates which side the piece belongs to
	 *********************************************************************/
	protected Pawn(Player player) {
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
		return "Pawn";
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
		// set allowed move equal to 2 or 1 depending on what it can do
		int allowedMove;
		if (this.player() == Player.WHITE) {
			allowedMove = (move.fromRow == 6) ? 2 : 1;
		} else {
			allowedMove = (move.fromRow == 1) ? 2 : 1;
		}
		// if the column isn't changing make sure that the pawn is
		// traveling in the right direction
		if (move.toColumn - move.fromColumn == 0) {
			if (this.player() == Player.WHITE) {
				if (move.toRow - move.fromRow < -allowedMove
						|| move.toRow - move.fromRow > 0) {
					return false;
				}
			} else {
				if (move.toRow - move.fromRow > allowedMove
						|| move.toRow - move.fromRow < 0) {
					return false;
				}
			}
		} else {
			// if it is trying to move diagonal
			// make sure that there is a piece to capture
			int directionMoving = (this.player() == Player.WHITE) ? -1 : 1;
			if (board[move.toRow][move.toColumn] != null) {
				if (!(move.toRow - move.fromRow == directionMoving
						&& Math.abs(move.toColumn - move.fromColumn) == 1 && this
							.player() != board[move.toRow][move.toColumn]
						.player())) {
					return false;
				}
			} else {
				return false;
			}
		}
		// make sure it isn't trying to kill another piece head on
		if (move.toColumn == move.fromColumn) {
			if (board[move.toRow][move.toColumn] != null) {
				return false;
			}
		}
		// if there is a piece in the way, make sure that it is not
		// jumping over it, it has to be either attacking diagonally
		// or it fails
		if (allowedMove == 2) {
			if (this.player() == Player.WHITE) {
				if (board[move.fromRow - 1][move.fromColumn] != null) {
					if (move.fromColumn - 1 >= 0 && move.fromColumn - 1 < 8) {
						if (board[move.fromRow - 1][move.fromColumn - 1] != null) {
							if (board[move.fromRow - 1][move.fromColumn - 1]
									.player() != Player.WHITE) {
								if (move.toRow == move.fromRow - 1
										&& move.toColumn == move.fromColumn - 1) {
									return true;
								}
							}
						}
					}
					if (move.fromColumn + 1 >= 0 && move.fromColumn + 1 < 8) {
						if (board[move.fromRow - 1][move.fromColumn + 1] != null) {
							if (board[move.fromRow - 1][move.fromColumn + 1]
									.player() != Player.WHITE) {
								if (move.toRow == move.fromRow - 1
										&& move.toColumn == move.fromColumn + 1) {
									return true;
								}
							}
						}
					}
					return false;
				}
			} else {
				if (board[move.fromRow + 1][move.fromColumn] != null) {
					if (move.fromColumn - 1 >= 0 && move.fromColumn - 1 < 8) {
						if (board[move.fromRow + 1][move.fromColumn - 1] != null) {
							if (board[move.fromRow + 1][move.fromColumn - 1]
									.player() != Player.BLACK) {
								if (move.toRow == move.fromRow + 1
										&& move.toColumn == move.fromColumn - 1) {
									return true;
								}
							}
						}
					}
					if (move.fromColumn + 1 >= 0 && move.fromColumn + 1 < 8) {
						if (board[move.fromRow + 1][move.fromColumn + 1] != null) {
							if (board[move.fromRow + 1][move.fromColumn + 1]
									.player() != Player.BLACK) {
								if (move.toRow == move.fromRow + 1
										&& move.toColumn == move.fromColumn + 1) {
									return true;
								}
							}
						}
					}
					return false;
				}
			}
		}
		return true;
	}
}
