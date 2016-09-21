package chess;

import java.util.ArrayList;
import java.util.Random;

public class AIOtherTeam {
	
	private ArrayList<Move> validMoves;
	private ArrayList<Integer> moveValues;
	private boolean foundWinningMove;
	private boolean active;
	private Move winningMove;
	private Move move1;
	private Move move2;
	private int size;
	private int player_or_AI;
	private int count12;
	private int count15;
	private int countW;
	private int countWLR;
	private int countWRR;
	private int countB;
	private int countBLR;
	private int countBRR;
	private int doCastling;
	
	public AIOtherTeam() {
		validMoves = new ArrayList<Move>();
		moveValues = new ArrayList<Integer>();
		size = 8;
		player_or_AI = 0;
		count12 = 0;
		count15 = 0;
		countW = 0;
		countWLR = 0;
		countWRR = 0;
		countB = 0;
		countBLR = 0;
		countBRR = 0;
		doCastling = 0;
		foundWinningMove = false;
	}

	/*******************************************************************
	 * Generates all valid moves the AI can make for the current turn.
	 ******************************************************************/
	public void moveGenerator(ChessModel model, IChessPiece[][] board) {
		validMoves.clear();
		moveValues.clear();

		int countValidMoves = 0;
		int countIfRisk = 0;

		for (int row1 = 0; row1 < size; row1++)
			for (int col1 = 0; col1 < size; col1++)
				if (model.pieceAt(row1, col1) != null && model.pieceAt(row1, col1).player() == model.currentPlayer())
					for (int row2 = 0; row2 < size; row2++)
						for (int col2 = 0; col2 < size; col2++) {
							Move move = new Move(row1, col1, row2, col2);
							player_or_AI = 0;
							if (isValidMove(model, board, move)) {
								validMoves.add(countValidMoves, move);

								// **********************
								if (isValid_PutPlayerInCheckMate(model, board, move) == true) {
									foundWinningMove = true;
									winningMove = move;
								}
								// **********************

								for (int row3 = 0; row3 < size; row3++) {
									for (int col3 = 0; col3 < size; col3++)
										if (board[row3][col3] != null && model.pieceAt(row3, col3).player() != model.currentPlayer()) {
											Move move2 = new Move(row3, col3, row2, col2);
											player_or_AI = 0;
											if (isValidMove_AtRisk(model, board, move2) == true)
												countIfRisk++;
										}
								}
								if (countIfRisk == 0)
									moveValues.add(countValidMoves, 0);
								else
									moveValues.add(countValidMoves, 1);

								countIfRisk = 0;
								countValidMoves++;
							}
						}
	}

	public boolean isValidMove(ChessModel model, IChessPiece[][] board, Move move) {
		IChessPiece spotFrom = board[move.fromRow][move.fromColumn];
		IChessPiece spotTo = board[move.toRow][move.toColumn];

		// If checking player move.
		if (player_or_AI == 0) {
			if (spotFrom == null || spotFrom.player() != model.currentPlayer())
				return false;

			// If AI is doing a certain computation.
		} else if (player_or_AI == 1) {
			if (spotFrom == null || spotFrom.player() == model.currentPlayer())
				return false;
		}

		// If in check, makes sure the move removes you from check.
		if (inCheck(model, board, model.currentPlayer()) == true) {
			if (spotFrom.isValidMove(move, board) == true) {
				return isValidMove_InCheck(model, board, move);
			}
			return false;

			// Checks that the player's move will not place themselves in check.
		} else if (isValidMove_SelfToCheck(model, board, move) == false) {
			return false;

			// Checks if the move is a valid castling move.
		} else if (spotFrom != null && spotTo != null && spotFrom.player() == model.currentPlayer() && spotTo.player() == model.currentPlayer())
			if (isValidMove_Castling(model, board, move) == false)
				return false;

		return model.pieceAt(move.fromRow, move.fromColumn).isValidMove(move, board);
	}

	private boolean isValidMove_Castling(ChessModel model,
			IChessPiece[][] board, Move move) {
		return false;
//		IChessPiece spotFrom = board[move.fromRow][move.fromColumn];
//		IChessPiece spotTo = board[move.toRow][move.toColumn];
//		int row = 0;
//		int kingCol = 0;
//		int rookCol = 0;
//		boolean check = false;
//		boolean able = false;
//		IChessPiece storage1 = null;
//		IChessPiece storage2 = null;
//
//		// If the pieces selected are king and rook, respectively.
//		if (spotFrom != null && spotFrom.type() == "King" && spotTo != null && spotTo.type() == "Rook")
//			if (model.currentPlayer() == Player.WHITE) {
//
//				// If the white king has not moved yet.
//				if (countW == 0)
//
//					// If the king is moving to the bottom left corner and that
//					// corners rook hasn't moved.
//					if (spotTo == board[7][0] && countWLR == 0) {
//						able = true;
//
//						row = 7;
//						kingCol = 2;
//						rookCol = 3;
//						storage1 = board[7][2];
//						storage2 = board[7][3];
//
//						// If the king is moving to the bottom right corner and
//						// that corners rook hasn't moved.
//					} else if (spotTo == board[7][7] && countWRR == 0) {
//						able = true;
//
//						row = 7;
//						kingCol = 6;
//						rookCol = 5;
//						storage1 = board[7][6];
//						storage2 = board[7][5];
//					}
//
//				// If the black king has not moved yet.
//			} else if (countB == 0)
//
//				// If the king is moving to the top left corner and that corners
//				// rook hasn't moved.
//				if (spotTo == board[0][0] && countBLR == 0) {
//					able = true;
//
//					row = 0;
//					kingCol = 2;
//					rookCol = 3;
//					storage1 = board[0][2];
//					storage2 = board[0][3];
//
//					// If the king is moving to the top right corner and that
//					// corners rook hasn't moved.
//				} else if (spotTo == board[0][7] && countBRR == 0) {
//					able = true;
//
//					row = 0;
//					kingCol = 6;
//					rookCol = 5;
//					storage1 = board[0][6];
//					storage2 = board[0][5];
//				}
//
//		// If castling conditions are meet.
//		if (able == true) {
//
//			// Execute castling maneuver, store previous board position.
//			board[row][kingCol] = spotFrom;
//			board[row][rookCol] = spotTo;
//			board[move.toRow][move.toColumn] = null;
//			board[move.fromRow][move.fromColumn] = null;
//
//			// If castling has not been selected yet, check that castling will
//			// not place the player in check.
//			if (doCastling == 0) {
//
//				check = inCheck(model, board, model.currentPlayer());
//
//				// Reset the board to previous positionn.
//				board[move.fromRow][move.fromColumn] = board[row][kingCol];
//				board[move.toRow][move.toColumn] = board[row][rookCol];
//				board[row][kingCol] = storage1;
//				board[row][rookCol] = storage2;
//			}
//
//			doCastling = 0;
//
//			// Return if castling places you in check or not.
//			if (check == true)
//				return false;
//			else
//				return true;
//		} else {
//			doCastling = 0;
//			return false;
//		}
	}

	private boolean isValidMove_SelfToCheck(ChessModel model,
			IChessPiece[][] board, Move move) {
		IChessPiece storage1 = null;
		IChessPiece storage2 = null;
		boolean check = false;

		if (inCheck(model, board, model.currentPlayer()) == false) {

			if (board[move.fromRow][move.fromColumn] != null
					&& board[move.fromRow][move.fromColumn].player() == model.currentPlayer()) {

				// player_or_AI = 0;
				if (model.pieceAt(move.fromRow, move.fromColumn).isValidMove(move, board) == true) {

					// Stores the current board position.
					storage1 = board[move.toRow][move.toColumn];
					storage2 = board[move.fromRow][move.fromColumn];
					board[move.toRow][move.toColumn] = board[move.fromRow][move.fromColumn];
					board[move.fromRow][move.fromColumn] = null;

					check = inCheck(model, board, model.currentPlayer());

					// Reset board to previous position;
					board[move.toRow][move.toColumn] = null;
					board[move.fromRow][move.fromColumn] = storage2;
					board[move.toRow][move.toColumn] = storage1;

					if (check == true)
						return false;
					else
						return true;
				}
			}
		}
		return true;
	}

	private boolean isValidMove_InCheck(ChessModel model,
			IChessPiece[][] board, Move move) {
		IChessPiece storage1 = null;
		IChessPiece storage2 = null;
		boolean check = false;

		// Stores the current board position.
		storage1 = board[move.toRow][move.toColumn];
		storage2 = board[move.fromRow][move.fromColumn];
		board[move.toRow][move.toColumn] = board[move.fromRow][move.fromColumn];
		board[move.fromRow][move.fromColumn] = null;

		check = inCheck(model, board, model.currentPlayer());

		// Reset board to previous position;
		board[move.toRow][move.toColumn] = null;
		board[move.fromRow][move.fromColumn] = storage2;
		board[move.toRow][move.toColumn] = storage1;

		if (check == true)
			return false;
		else
			return true;
	}

	public boolean isValidMove_AtRisk(ChessModel model, IChessPiece[][] board, Move move) { // isvalidmove2
		boolean valid = false;
		IChessPiece storage1 = board[move.toRow][move.toColumn];

		if (model.currentPlayer() == Player.WHITE)
			board[move.toRow][move.toColumn] = new Pawn(Player.WHITE);
		else
			board[move.toRow][move.toColumn] = new Pawn(Player.BLACK);

		if (model.pieceAt(move.fromRow, move.fromColumn) == null
				|| model.pieceAt(move.fromRow, move.fromColumn).player() == model.currentPlayer()) {
			board[move.toRow][move.toColumn] = storage1;
			return false;
		} else {
			player_or_AI = 0;
			valid = model.pieceAt(move.fromRow, move.fromColumn).isValidMove(move, board);
			board[move.toRow][move.toColumn] = storage1;
			return valid;
		}
	}

	public boolean isValid_PutPlayerInCheckMate(ChessModel model, IChessPiece[][] board, Move move) {
		IChessPiece storage1 = null;
		IChessPiece storage2 = null;
		boolean check = false;

		storage1 = board[move.toRow][move.toColumn];
		storage2 = board[move.fromRow][move.fromColumn];
		board[move.toRow][move.toColumn] = board[move.fromRow][move.fromColumn];
		board[move.fromRow][move.fromColumn] = null;

		check = isCheckMate(model, board, model.currentPlayer().next());

		board[move.toRow][move.toColumn] = null;
		board[move.fromRow][move.fromColumn] = storage2;
		board[move.toRow][move.toColumn] = storage1;

		return check;
	}

	public boolean isCheckMate(ChessModel model, IChessPiece[][] board, Player p) {
		IChessPiece storage1 = null;
		IChessPiece storage2 = null;
		int count2 = 0;

		if (inCheck(model, board, p) == true) {
			for (int rowS = 0; rowS < size; rowS++)
				for (int colS = 0; colS < size; colS++)
					if (model.pieceAt(rowS, colS) != null && model.pieceAt(rowS, colS).player() == p) {
						for (int rowE = 0; rowE < size; rowE++)
							for (int colE = 0; colE < size; colE++) {
								move2 = new Move(rowS, colS, rowE, colE);
								player_or_AI = 0;
								if (model.pieceAt(move2.fromRow, move2.fromColumn).isValidMove(move2, board) == true) {
									storage1 = board[move2.toRow][move2.toColumn];
									storage2 = board[move2.fromRow][move2.fromColumn];
									board[move2.toRow][move2.toColumn] = model.pieceAt(move2.fromRow, move2.fromColumn);
									board[move2.fromRow][move2.fromColumn] = null;
									if (inCheck(model, board, p) == false) {
										board[move2.toRow][move2.toColumn] = null;
										board[move2.fromRow][move2.fromColumn] = storage2;
										board[move2.toRow][move2.toColumn] = storage1;
										count2++;
									}
									board[move2.toRow][move2.toColumn] = null;
									board[move2.fromRow][move2.fromColumn] = storage2;
									board[move2.toRow][move2.toColumn] = storage1;
								}

							}
					}
			if (count2 > 0)
				return false;
			else
				return true;
		}
		return false;
	}

	/*******************************************************************
	 * Calculates the best possible move the AI can make.
	 ******************************************************************/
	public Move moveCalculator(ChessModel model, IChessPiece[][] board) {
		Move move;

		int myPvalue = 0;
		int opPvalue = 0;
		int count13 = 0;

		int moveValue = 0;
		for (int a = 0; a < validMoves.size(); a++) {
			move = validMoves.get(a);

			if (board[move.fromRow][move.fromColumn] == null)
				myPvalue = 0;
			else if (board[move.fromRow][move.fromColumn].type() == "Pawn")
				myPvalue = 1;
			else if (board[move.fromRow][move.fromColumn].type() == "Knight")
				myPvalue = 3;
			else if (board[move.fromRow][move.fromColumn].type() == "Bishop")
				myPvalue = 3;
			else if (board[move.fromRow][move.fromColumn].type() == "Rook")
				myPvalue = 5;
			else if (board[move.fromRow][move.fromColumn].type() == "Queen")
				myPvalue = 9;
			else if (board[move.fromRow][move.fromColumn].type() == "King")
				myPvalue = 1000;

			if (board[move.toRow][move.toColumn] == null)
				opPvalue = 0;
			else if (board[move.toRow][move.toColumn].type() == "Pawn")
				opPvalue = 1;
			else if (board[move.toRow][move.toColumn].type() == "Knight")
				opPvalue = 3;
			else if (board[move.toRow][move.toColumn].type() == "Bishop")
				opPvalue = 3;
			else if (board[move.toRow][move.toColumn].type() == "Rook")
				opPvalue = 5;
			else if (board[move.toRow][move.toColumn].type() == "Queen")
				opPvalue = 9;
			else if (board[move.toRow][move.toColumn].type() == "King")
				opPvalue = 1000;

			if (opPvalue == 0 && moveValues.get(a) == 0)
				moveValue = 0;
			else if (opPvalue == 0 && moveValues.get(a) == 1)
				moveValue = -(myPvalue) * 3;
			else if (opPvalue > 0 && moveValues.get(a) == 0)
				moveValue = opPvalue * 10;
			else if (opPvalue > 0 && moveValues.get(a) == 1)
				moveValue = (opPvalue * 3 - myPvalue * 3) + 1;

			count13 = 0;
			for (int row1 = 0; row1 < size; row1++) {
				for (int col1 = 0; col1 < size; col1++) {
					// count13 = 0;
					if (board[row1][col1] != null && board[row1][col1].player() != model.currentPlayer()
							&& board[move.fromRow][move.fromColumn].type() != "Pawn") {
						Move move7 = new Move(row1, col1, move.fromRow, move.fromColumn);

						player_or_AI = 1;
						if (isValidMove(model, board, move7) == true) {
							if (count13 < 1) {
								moveValue = moveValue + (myPvalue * 2) - 1;
								count13++;
							}
						}
					}
				}
			}
			moveValues.set(a, moveValue);
		}
		int max = -10000;
		int maxPos = -1;
		for (int c = 0; c < moveValues.size(); c++) {
			int value = moveValues.get(c);
			if (value > max) {
				max = value;
				maxPos = c;
			}
		}
		if (max > 0) {
			return validMoves.get(maxPos);
		} else if (max == 0 || maxPos == -1 || max == -10000)
			return bestRandomMove(model, board);
		else
			return validMoves.get(maxPos);
	}

	/*******************************************************************
	 * Determines the best random move the AI can make if the is no actual best
	 * move.
	 ******************************************************************/
	public Move bestRandomMove(ChessModel model, IChessPiece[][] board) {
		Move bmove = null;
		int count11 = 0;

		if (count15 == 0) {

			for (int a = 0; a < validMoves.size(); a++) {
				bmove = validMoves.get(a);
				count11 = 0;

				for (int row3 = 0; row3 < size; row3++) {
					for (int col3 = 0; col3 < size; col3++)

						if (board[row3][col3] != null && model.pieceAt(row3, col3).player() != model.currentPlayer()) {
							Move move2 = new Move(row3, col3, bmove.toRow, bmove.toColumn);
							player_or_AI = 0;
							if (isValidMove_AtRisk(model, board, move2) == true)
								count11++;
						}
				}
				if (board[bmove.toRow][bmove.toColumn] != null && board[bmove.toRow][bmove.toColumn].player() == model.currentPlayer())
					count11++;
				if (count11 == 0 && isValid_PutPlayerInCheck(model, board, bmove) == true) {
					return bmove;
				}
			}
			count15++;
		}

		Random randomGenerator = new Random();
		int randomInt;
		if (validMoves.size() > 0) {
			randomInt = randomGenerator.nextInt(validMoves.size());
		}else {
			randomInt = 0;
		}
		bmove = validMoves.get(randomInt);
		count11 = 0;

		if (count12 > 150 && (board[bmove.toRow][bmove.toColumn] == null || (board[bmove.toRow][bmove.toColumn] != null
				&& board[bmove.toRow][bmove.toColumn].player() != model.currentPlayer()))) {
			count12 = 0;
			count15 = 0;
			return bmove;
		}

		for (int row3 = 0; row3 < size; row3++) {
			for (int col3 = 0; col3 < size; col3++)

				if (board[row3][col3] != null && model.pieceAt(row3, col3).player() != model.currentPlayer()) {
					Move move2 = new Move(row3, col3, bmove.toRow, bmove.toColumn);
					player_or_AI = 0;
					if (isValidMove_AtRisk(model, board, move2) == true)
						count11++;
				}
		}

		if (board[bmove.toRow][bmove.toColumn] != null && board[bmove.toRow][bmove.toColumn].player() == model.currentPlayer())
			count11++;

		if (count11 == 0 && (board[bmove.toRow][bmove.toColumn] == null || (board[bmove.toRow][bmove.toColumn] != null
				&& board[bmove.toRow][bmove.toColumn].player() != model.currentPlayer()))) {
			count12 = 0;
			count15 = 0;
			return bmove;
		}
		count12++;
		return bestRandomMove(model, board);
	}

	public boolean isValid_PutPlayerInCheck(ChessModel model, IChessPiece[][] board, Move move) {
		IChessPiece storage1 = null;
		IChessPiece storage2 = null;
		boolean check = false;

		storage1 = board[move.toRow][move.toColumn];
		storage2 = board[move.fromRow][move.fromColumn];
		board[move.toRow][move.toColumn] = board[move.fromRow][move.fromColumn];
		board[move.fromRow][move.fromColumn] = null;

		check = inCheck(model, board, model.currentPlayer().next());

		board[move.toRow][move.toColumn] = null;
		board[move.fromRow][move.fromColumn] = storage2;
		board[move.toRow][move.toColumn] = storage1;

		return check;
	}

	public boolean inCheck(ChessModel model, IChessPiece[][] board, Player p) {

		// Loops through board for players king piece.
		for (int rowK = 0; rowK < size; rowK++) {
			for (int colK = 0; colK < size; colK++)
				if (model.pieceAt(rowK, colK) != null && model.pieceAt(rowK, colK).player() == p
						&& model.pieceAt(rowK, colK).type() == "King")

					// Loops through board again for opponent player pieces.
					for (int row = 0; row < size; row++)
						for (int col = 0; col < size; col++)
							if (model.pieceAt(row, col) != null && model.pieceAt(row, col).player() != p) {

								// Creates a new move from the opponent piece to
								// the players king, if the new move
								// is valid the players king is in check.
								move1 = new Move(row, col, rowK, colK);
								player_or_AI = 0;
								if (model.pieceAt(move1.fromRow, move1.fromColumn).isValidMove(move1, board) == true)
									return true;
							}
		}
		return false;
	}

	public void setActive(boolean a) {
		active = a;
	}
	
	public boolean active() {
		return active;
	}
	
	public Player player() {
		return Player.BLACK;
	}
}
