package chess;

import java.util.ArrayList;

public class ChessModel implements IChessModel {

	/** the chess board of of pieces */
	private IChessPiece[][] board;

	/** the white pieces that have been eliminated */
	private IChessPiece[] deadWhite;

	/** the black pieces that have been eliminated */
	private IChessPiece[] deadBlack;

	/** the pieces that have been eliminated */
	private ArrayList<IChessPiece> allDeletedPieces;

	/** the last fifty pieces that have been moved */
	private ArrayList<IChessPiece> lastFiftyPieces;

	/**
	 * the positions of pieces that have been promoted and the turn they were
	 * promoted on
	 */
	private ArrayList<int[]> promotedPosition;

	/** the moves that have occurred and have not been undone */
	private ArrayList<int[]> allMovePositions;

	/** determines which player has the current turn */
	private Player player;

	/** tracks all of the tiles that are highlighted */
	private int highlightedTiles[][];

	/** tracks the total number of moves that have not been undone */
	private int turnCounter;

	/** tracks last time a piece was captured for fifty move rule */
	private int lastTimeDeletedAPiece;

	/** holds the row position of the white king */
	private int whiteKingX;

	/** holds the column position of the white king */
	private int whiteKingY;

	/** holds the row position of the black king */
	private int blackKingX;

	/** holds the column position of the black king */
	private int blackKingY;

	/**********************************************************************
	 * 
	 * constructor to initialize instance variables and create virtual boards
	 *********************************************************************/
	public ChessModel() {
		turnCounter = 0;
		allDeletedPieces = new ArrayList<IChessPiece>();
		lastTimeDeletedAPiece = 0;
		lastFiftyPieces = new ArrayList<IChessPiece>();
		allMovePositions = new ArrayList<int[]>();
		promotedPosition = new ArrayList<int[]>();
		whiteKingX = 7;
		whiteKingY = 4;
		blackKingX = 0;
		blackKingY = 4;
		highlightedTiles = new int[8][8];
		board = new IChessPiece[8][8];
		deadWhite = new IChessPiece[16];
		deadBlack = new IChessPiece[16];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = null;
				highlightedTiles[i][j] = 0;
			}
		}
		// black pieces
		player = Player.BLACK;
		for (int i = 0; i < 8; i++) {
			board[1][i] = new Pawn(player);
		}
		board[0][0] = new Rook(player);
		board[0][1] = new Knight(player);
		board[0][2] = new Bishop(player);
		board[0][3] = new Queen(player);
		board[0][4] = new King(player);
		board[0][5] = new Bishop(player);
		board[0][6] = new Knight(player);
		board[0][7] = new Rook(player);
		// white pieces
		player = Player.WHITE;
		for (int i = 0; i < 8; i++) {
			board[6][i] = new Pawn(player);
		}
		board[7][0] = new Rook(player);
		board[7][1] = new Knight(player);
		board[7][2] = new Bishop(player);
		board[7][3] = new Queen(player);
		board[7][4] = new King(player);
		board[7][5] = new Bishop(player);
		board[7][6] = new Knight(player);
		board[7][7] = new Rook(player);
	}

	/**********************************************************************
	 * 
	 * determines if one of the players is in checkmate
	 * 
	 * @return boolean true if a player is in checkmate, false if not
	 *********************************************************************/
	public boolean isComplete() {
		Move move;
		// first check if the white player is in checkmate
		boolean whiteCheckMate = true;
		if (!this.inCheck(player.WHITE)) {
			whiteCheckMate = false;
		}
		// check the eight spots around the white king, attempt to move the
		// king to those positions and if successful check if it is in check
		// at its new position. if it isnt, then checkmate is false.
		// undo the move afterwards
		for (int i = Math.max(0, whiteKingX - 1); i < Math.min(8,
				whiteKingX + 2); i++) {
			for (int j = Math.max(0, whiteKingY - 1); j < Math.min(8,
					whiteKingY + 2); j++) {
				move = new Move(whiteKingX, whiteKingY, i, j);
				if (this.isValidMove(move)) {
					this.move(move);
					if (!this.inCheck(player.WHITE)) {
						whiteCheckMate = false;
					}
					this.undoLastMove();
				}
			}
		}
		// if the king is unable to move out of checkmate, check for blocking
		// with all of the white pieces
		if (whiteCheckMate) {
			// check for all of the blocking
			outerloop: for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (board[i][j] == null) {
						continue;
					}
					if (board[i][j].player() != Player.WHITE) {
						continue;
					}
					if (board[i][j].type().equals("King")) {
						continue;
					}
					for (int x = 0; x < 8; x++) {
						for (int y = 0; y < 8; y++) {
							move = new Move(i, j, x, y);
							// if move is valid, commit it and see if the king
							// gets out of checkmate. afterwards undo the move
							if (this.isValidMove(move)) {
								this.move(move);
								if (!this.inCheck(Player.WHITE)) {
									whiteCheckMate = false;
									this.undoLastMove();
									break outerloop;
								}
								this.undoLastMove();
							}
						}
					}
				}
			}
		}
		// first check if the black player is in checkmate
		boolean blackCheckMate = true;
		if (!this.inCheck(player.BLACK)) {
			blackCheckMate = false;
		}
		// check the eight spots around the black king, attempt to move the
		// king to those positions and if successful check if it is in check
		// at its new position. if it isnt, then checkmate is false.
		// undo the move afterwards
		for (int i = Math.max(0, blackKingX - 1); i < Math.min(8,
				blackKingX + 2); i++) {
			for (int j = Math.max(0, blackKingY - 1); j < Math.min(8,
					blackKingY + 2); j++) {
				move = new Move(blackKingX, blackKingY, i, j);
				if (this.isValidMove(move)) {
					this.move(move);
					if (!this.inCheck(player.BLACK)) {
						blackCheckMate = false;
					}
					this.undoLastMove();
				}
			}
		}
		// if the king is unable to move out of checkmate, check for blocking
		// with all of the black pieces
		if (blackCheckMate) {
			// check for all of the blocking
			outerloop: for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (board[i][j] == null) {
						continue;
					}
					if (board[i][j].player() != Player.BLACK) {
						continue;
					}
					if (board[i][j].type().equals("King")) {
						continue;
					}
					for (int x = 0; x < 8; x++) {
						for (int y = 0; y < 8; y++) {
							move = new Move(i, j, x, y);
							// if move is valid, commit it and see if the king
							// gets out of checkmate. afterwards undo the move
							if (this.isValidMove(move)) {
								this.move(move);
								if (!this.inCheck(Player.BLACK)) {
									blackCheckMate = false;
									this.undoLastMove();
									break outerloop;
								}
								this.undoLastMove();
							}
						}
					}
				}
			}
		}
		return (whiteCheckMate || blackCheckMate);
	}

	/**********************************************************************
	 * 
	 * determines if a castle can be done with the king and rook selected by the
	 * user
	 * 
	 * @param move
	 *            the castling move being attempted
	 * @param cX
	 *            the row position of the rook
	 * @param cY
	 *            the column position of the rook
	 * 
	 * @return boolean true if a castle can be done with the given inputs
	 *********************************************************************/
	public boolean castle(Move move, int cX, int cY) {
		// cX and cY are the position of where the Rook would be if a player
		// tried to initiate such a move
		if (!(board[cX][cY] instanceof Rook)) {
			return false;
		}
		if (!(board[move.fromRow][move.fromColumn] instanceof King)) {
			return false;
		}
		Player sideCastling = board[move.fromRow][move.fromColumn].player();
		Move tempMove;
		// check to see if rook and king have not moved yet
		if (((Rook) board[cX][cY]).getCount() != 0) {
			return false;
		}
		if (((King) board[move.fromRow][move.fromColumn]).getCount() != 0) {
			return false;
		}
		// loop through empty tiles between King and Rook to see if they are
		// empty
		for (int i = 1; i < 7; i++) {
			if ((cY < i && i < move.fromColumn)
					|| (move.fromColumn < i && i < cY)) {
				if (board[move.fromRow][i] != null) {
					return false;
				}
			}
		}
		// make sure every position king will be in/pass through is not in check
		if (this.inCheck(sideCastling)) {
			return false;
		}
		// directionalCount represents which way the king is traveling
		int directionalCount = 0;
		if (move.fromColumn > cY) {
			directionalCount = -1;
		} else {
			directionalCount = 1;
		}
		// move the king one time in the direction he is traveling and
		// check if he is in check there
		tempMove = new Move(move.fromRow, move.fromColumn, move.fromRow,
				move.fromColumn + directionalCount);
		this.move(tempMove);
		if (sideCastling == Player.WHITE) {
			this.updateWhiteKing(move.fromRow, move.fromColumn
					+ directionalCount);
		} else {
			this.updateBlackKing(move.fromRow, move.fromColumn
					+ directionalCount);
		}
		if (this.inCheck(sideCastling)) {
			if (sideCastling == Player.WHITE) {
				this.updateWhiteKing(move.fromRow, move.fromColumn);
			} else {
				this.updateBlackKing(move.fromRow, move.fromColumn);
			}
			this.undoLastMove();
			return false;
		}
		// do the same thing as previous but 2 spaces in the direction
		// that it is traveling in
		tempMove = new Move(move.fromRow, move.fromColumn, move.fromRow,
				move.fromColumn + (2 * directionalCount));
		this.move(tempMove);
		if (sideCastling == Player.WHITE) {
			this.updateWhiteKing(move.fromRow, move.fromColumn
					+ (2 * directionalCount));
		} else {
			this.updateBlackKing(move.fromRow, move.fromColumn
					+ (2 * directionalCount));
		}
		if (this.inCheck(sideCastling)) {
			if (sideCastling == Player.WHITE) {
				this.updateWhiteKing(move.fromRow, move.fromColumn);
			} else {
				this.updateBlackKing(move.fromRow, move.fromColumn);
			}
			this.undoLastMove();
			this.undoLastMove();
			return false;
		} else {
			if (sideCastling == Player.WHITE) {
				this.updateWhiteKing(move.fromRow, move.fromColumn);
			} else {
				this.updateBlackKing(move.fromRow, move.fromColumn);
			}
			this.undoLastMove();
			this.undoLastMove();
		}
		// all the tests are done and have not failed, commit to the move
		this.move(move);
		if (move.fromColumn > cY) {
			board[move.fromRow][3] = board[move.fromRow][0];
			board[move.fromRow][0] = null;
			((Rook) board[move.fromRow][3]).incrementCount();
		} else {
			board[move.fromRow][5] = board[move.fromRow][7];
			board[move.fromRow][7] = null;
			((Rook) board[move.fromRow][5]).incrementCount();
		}
		return true;
	}

	/**********************************************************************
	 * 
	 * determines if the game is in a stalemate
	 * 
	 * @return boolean true if a stalemate has occurred, false if not
	 *********************************************************************/
	public boolean stalemate() {
		Move move;
		// first check if white is in stalemate
		boolean noWhiteMoves = true;
		if (this.inCheck(Player.WHITE)) {
			noWhiteMoves = false;
		}
		// check to see if the white player can make any move that is valid
		outerloop: for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (!noWhiteMoves) {
					break outerloop;
				}
				if (board[i][j] == null) {
					continue;
				}
				if (board[i][j].player() != Player.WHITE) {
					continue;
				}
				for (int x = Math.max(0, i - 1); x < Math.min(8, i + 2); x++) {
					for (int y = Math.max(0, j - 1); y < Math.min(8, j + 2); y++) {
						move = new Move(i, j, x, y);
						if (this.isValidMove(move)) {
							noWhiteMoves = false;
						}
					}
				}
			}
		}
		// now check if black is in stalemate
		boolean noBlackMoves = true;
		if (this.inCheck(Player.BLACK)) {
			noBlackMoves = false;
		}
		// check to see if the black player can make any move that is valid
		outerloop: for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (!noBlackMoves) {
					break outerloop;
				}
				if (board[i][j] == null) {
					continue;
				}
				if (board[i][j].player() != Player.BLACK) {
					continue;
				}
				for (int x = Math.max(0, i - 1); x < Math.min(8, i + 2); x++) {
					for (int y = Math.max(0, j - 1); y < Math.min(8, j + 2); y++) {
						move = new Move(i, j, x, y);
						if (this.isValidMove(move)) {
							noBlackMoves = false;
						}
					}
				}
			}
		}
		return (noWhiteMoves || noBlackMoves);
	}

	/**********************************************************************
	 * 
	 * determines if a piece needs to be promoted
	 * 
	 * @return boolean true if a piece needs to be promoted, false if not
	 *********************************************************************/
	public boolean promotion() {
		// search the 0 and 7 rows to see if a pawn is in them, if so
		// then promotion must occur
		for (int i = 0; i < 8; i++) {
			if (board[0][i] instanceof Pawn) {
				return true;
			} else {
				if (board[7][i] instanceof Pawn) {
					return true;
				}
			}
		}
		return false;
	}

	/**********************************************************************
	 * 
	 * switches a pawn being promoted with the piece selected by the user
	 * 
	 * @param chosen
	 *            ([one] Harry Potter) the piece selected by the user
	 *********************************************************************/
	public void switchPawn(IChessPiece chosen) {
		// search for the pawn that is in need of promotion
		// and then promote it with selected piece
		// add the promotion to the promotedPosition ArrayList
		// so that it can be udone later on
		for (int l = 0; l < 8; l++) {
			if (player == Player.BLACK) {
				if (board[0][l] instanceof Pawn) {
					board[0][l] = null;
					board[0][l] = chosen;
					int[] temp = { turnCounter - 1, 0, l };
					promotedPosition.add(temp);
				}
			} else {
				if (board[7][l] instanceof Pawn) {
					board[7][l] = null;
					board[7][l] = chosen;
					int[] temp = { turnCounter - 1, 7, l };
					promotedPosition.add(temp);
				}
			}
		}

	}

	/**********************************************************************
	 * 
	 * determines if a move being attempted is valid with the rules of chess
	 * including check
	 * 
	 * @param move
	 *            the move being attempted
	 * 
	 * @return boolean true if move is valid, false if not
	 *********************************************************************/
	public boolean isValidMove(Move move) {
		// check if valid move
		if (board[move.fromRow][move.fromColumn].isValidMove(move, board)) {
			// if valid, make sure that the player is not putting his own
			// king in check, or if he did not fix a check
			this.move(move);
			if (this.inCheck(board[move.toRow][move.toColumn].player())) {
				this.undoLastMove();
				return false;
			}
			this.undoLastMove();
			return true;
		} else {
			return false;
		}
	}

	/**********************************************************************
	 * 
	 * determines if a move being attempted is valid with the rules of chess
	 * without including check
	 * 
	 * @param move
	 *            the move being attempted
	 * 
	 * @return boolean true if move is valid, false if not
	 *********************************************************************/
	public boolean isValidMoveWithoutCheck(Move move) {
		// check if valid move but do not check if it puts its own
		// king in check. used for inCheck when it doesn't need to be tested
		// to prevent uncontrollable looping
		return board[move.fromRow][move.fromColumn].isValidMove(move, board);
	}

	/**********************************************************************
	 * 
	 * undoes the last move that was successfully executed
	 *********************************************************************/
	public void undoLastMove() {
		if (turnCounter > 0) {
			turnCounter--;
		}
		// un-update the fifty pieces array, deleting the last value
		int indexFiftyCheck = lastFiftyPieces.size();
		if (lastFiftyPieces.size() > 0) {
			lastFiftyPieces.remove(indexFiftyCheck - 1);
		}
		// check to see if a promotion occured on this turn
		// if so, undo the promotion
		for (int i = 0; i < promotedPosition.size(); i++) {
			if (turnCounter == promotedPosition.get(i)[0]) {
				board[promotedPosition.get(i)[1]][promotedPosition.get(i)[2]] = new Pawn(
						player.next());
				promotedPosition.remove(i);
				break;
			}
		}
		// if there are no moves that have happened previously, return
		if (allMovePositions.size() < 1) {
			return;
		}
		// undo the last move that happened
		int index = allMovePositions.size();
		int positions[] = allMovePositions.get(index - 1);
		board[positions[0]][positions[1]] = board[positions[2]][positions[3]];
		board[positions[2]][positions[3]] = allDeletedPieces.get(index - 1);
		allMovePositions.remove(index - 1);
		allDeletedPieces.remove(index - 1);
		// if a king moved last time, set the king position variables
		// back to where he used to be
		if (board[positions[0]][positions[1]] instanceof King) {
			if (board[positions[0]][positions[1]].player() == Player.WHITE) {
				this.updateWhiteKing(positions[0], positions[1]);
			} else {
				this.updateBlackKing(positions[0], positions[1]);
			}
			((King) board[positions[0]][positions[1]]).decrementCount();
			if (positions[2] - positions[0] == 0
					&& Math.abs(positions[3] - positions[1]) == 2) {
				// The King castled this time, so undo the rook moving
				if (positions[3] - positions[1] > 0) {
					board[positions[0]][7] = board[positions[0]][5];
					board[positions[0]][5] = null;
					((Rook) board[positions[0]][7]).decrementCount();
				} else {
					board[positions[0]][0] = board[positions[0]][3];
					board[positions[0]][3] = null;
					((Rook) board[positions[0]][0]).decrementCount();
				}
			}
		}
		// decrement rook count to keep track of whether it has moved
		// or not even with undoing
		if (board[positions[0]][positions[1]] instanceof Rook) {
			((Rook) board[positions[0]][positions[1]]).decrementCount();
		}
		// if a piece was captured on the move being undone,
		// remove the piece that got captured from the array
		// of captured pieces
		if (board[positions[2]][positions[3]] != null) {
			if (board[positions[2]][positions[3]].player() == Player.WHITE) {
				for (int i = 15; i >= 0; i--) {
					if (deadWhite[i] != null) {
						deadWhite[i] = null;
						break;
					}
				}
			} else {
				for (int i = 15; i >= 0; i--) {
					if (deadBlack[i] != null) {
						deadBlack[i] = null;
						break;
					}
				}

			}
		}
		this.nextPlayer();
	}

	/**********************************************************************
	 * 
	 * executes a move that has already been validated
	 * 
	 * @param move
	 *            the move being executed
	 *********************************************************************/
	public void move(Move move) {
		// if it was a king, update the kings positions and increment count
		// for castling tracking
		if (board[move.fromRow][move.fromColumn] instanceof King) {
			if (board[move.fromRow][move.fromColumn].player() == Player.WHITE) {
				this.updateWhiteKing(move.toRow, move.toColumn);
			} else {
				this.updateBlackKing(move.toRow, move.toColumn);
			}
			((King) board[move.fromRow][move.fromColumn]).incrementCount();
		}
		// if it was a rook, increment the count for castling tracking
		if (board[move.fromRow][move.fromColumn] instanceof Rook) {
			((Rook) board[move.fromRow][move.fromColumn]).incrementCount();
		}
		// add the piece that was captured to the deletedPieces
		// ArrayList, even if it is null
		allDeletedPieces.add(board[move.toRow][move.toColumn]);
		if (board[move.toRow][move.toColumn] != null) {
			lastTimeDeletedAPiece = turnCounter;
		}
		// add the positions of the pieces that were in the move to the
		// allMovePositions ArrayList for undo capability
		int positions[] = { move.fromRow, move.fromColumn, move.toRow,
				move.toColumn };
		allMovePositions.add(positions);
		// if a piece was captured, at it to the correct array of dead
		// pieces
		if (board[move.toRow][move.toColumn] != null) {
			if (board[move.toRow][move.toColumn].player() == Player.WHITE) {
				for (int i = 0; i < 16; i++) {
					if (deadWhite[i] == null) {
						deadWhite[i] = board[move.toRow][move.toColumn];
						break;
					}
				}
			} else {
				for (int i = 0; i < 16; i++) {
					if (deadBlack[i] == null) {
						deadBlack[i] = board[move.toRow][move.toColumn];
						break;
					}
				}

			}
		}
		// add the moved piece to fifty pieces array for fifty move rule
		// tracking
		lastFiftyPieces.add(board[move.fromRow][move.fromColumn]);
		// commit move
		board[move.toRow][move.toColumn] = board[move.fromRow][move.fromColumn];
		board[move.fromRow][move.fromColumn] = null;
		turnCounter++;
		// cycle players
		this.nextPlayer();
	}

	/**********************************************************************
	 * 
	 * determines if a given player is in check
	 * 
	 * @param p
	 *            the given player
	 * 
	 * @return boolean true if the given player is in check, false if not
	 *********************************************************************/
	public boolean inCheck(Player p) {
		Move move;
		// loop through every piece on board
		// if it is the opposite piece of the King being checked,
		//
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == null) {
					continue;
				}
				if (board[i][j].player() == p) {
					continue;
				}
				if (p == Player.WHITE) {
					move = new Move(i, j, whiteKingX, whiteKingY);
				} else {
					move = new Move(i, j, blackKingX, blackKingY);
				}
				if (this.isValidMoveWithoutCheck(move)) {
					return true;
				}
			}
		}
		return false;
	}

	/**********************************************************************
	 * 
	 * handles the fifty move rule
	 * 
	 * @return boolean true if fifty move rule occurs, false if not
	 *********************************************************************/
	public boolean fiftyMoveRule() {
		if (turnCounter >= 50) {
			if (lastFiftyPieces.size() > 49) {
				if (Math.abs(lastTimeDeletedAPiece - turnCounter) < 50)
					for (int i = lastFiftyPieces.size() - 1; i >= 50; i--) {
						if (lastFiftyPieces.get(i) instanceof Pawn) {
							return true;
						}
					}
			}
		}
		return false;
	}

	/**********************************************************************
	 * 
	 * handles the threefold repetition rule *TO BE COMPLETED*
	 * 
	 * @return boolean true if threefold repetition move rule occurs, false if
	 *         not
	 *********************************************************************/
	public boolean threefoldRepetition() {
		// to be completed
		return false;
	}

	/**********************************************************************
	 * 
	 * clears all of the highlighted tiles
	 *********************************************************************/
	public void clearTiles() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				highlightedTiles[i][j] = 0;
			}
		}
	}
	
	public void showTheCheck() {
		if (player == Player.WHITE) {
			this.showChecks(whiteKingX, whiteKingY);
		}else {
			this.showChecks(blackKingX, blackKingY);
		}
	}
	
	private void showChecks(int kingX, int kingY) {
		Move move;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == null) {
					continue;
				}
				if (board[i][j].player() == player) {
					continue;
				}
				if (player == Player.WHITE) {
					move = new Move(i, j, kingX, kingY);
				} else {
					move = new Move(i, j, kingX, kingY);
				}
				if (this.isValidMoveWithoutCheck(move)) {
					highlightedTiles[i][j] = 5;
					if (player == Player.WHITE) {
						highlightedTiles[whiteKingX][whiteKingY] = 5;
					}else {
						highlightedTiles[blackKingX][blackKingY] = 5;						
					}
				}
			}
		}
	}
	
	public void showCheckMate() {
		this.clearTiles();
		if (player == Player.WHITE) {
			for (int i = Math.max(0, whiteKingX - 1); i < Math.min(8, whiteKingX + 2); i++) {
				for (int j = Math.max(0, whiteKingY - 1); j < Math.min(8, whiteKingY + 2); j++) {
					this.showChecks(i, j);
				}
			}
		}else {
			for (int i = Math.max(0, blackKingX - 1); i < Math.min(8, blackKingX + 2); i++) {
				for (int j = Math.max(0, blackKingY - 1); j < Math.min(8, blackKingY + 2); j++) {
					this.showChecks(i, j);
				}
			}
		}
	}

	/**********************************************************************
	 * 
	 * highlights every valid move for a piece on the board at row: pieceI and
	 * column: pieceJ
	 * 
	 * @param pieceI
	 *            the row position of the piece
	 * @param pieceJ
	 *            the column position of the piece
	 *********************************************************************/
	public void highlightMoves(int pieceI, int pieceJ) {
		clearTiles();
		if (board[pieceI][pieceJ] == null) {
			return;
		}
		Move move;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				move = new Move(pieceI, pieceJ, i, j);
				if (this.isValidMove(move)) {
					if (board[i][j] == null) {
						highlightedTiles[i][j] = 1;
					} else {
						highlightedTiles[i][j] = 2;
					}
				}
			}
		}
		if (board[pieceI][pieceJ] instanceof King) {
			if (board[pieceI][pieceJ].player() == Player.WHITE) {
				if (this.castle(new Move(7, 4, 7, 2), 7, 0)) {
					highlightedTiles[7][2] = 3;
					this.undoLastMove();
				}
				if (this.castle(new Move(7, 4, 7, 6), 7, 7)) {
					highlightedTiles[7][6] = 3;
					this.undoLastMove();
				}
			} else {
				if (this.castle(new Move(0, 4, 0, 2), 0, 0)) {
					highlightedTiles[0][2] = 3;
					this.undoLastMove();
				}
				if (this.castle(new Move(0, 4, 0, 6), 0, 7)) {
					highlightedTiles[0][6] = 3;
					this.undoLastMove();
				}
			}
		}
	}

	/**********************************************************************
	 * 
	 * highlights every valid move for a piece on the board at row: pieceI and
	 * column: pieceJ
	 * 
	 * @param pieceI
	 *            the row position of the piece
	 * @param pieceJ
	 *            the column position of the piece
	 *********************************************************************/
	public void highlightPromotions(Player p) {
		clearTiles();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == null) {
					continue;
				}
				if (board[i][j].type().equals("Pawn")) {
					continue;
				}
				if (board[i][j].type().equals("King")) {
					continue;
				}
				if (board[i][j].player() == p) {
					highlightedTiles[i][j] = 4;
				}
			}
		}
	}

	/**********************************************************************
	 * 
	 * returns the player that has the current turn
	 * 
	 * @Player WHITE if it is the white player's turn, BLACK if it is the black
	 *         player's turn
	 *********************************************************************/
	public Player currentPlayer() {
		return player;
	}

	/**********************************************************************
	 * 
	 * switches the current player to the next player
	 *********************************************************************/
	public void nextPlayer() {
		player = player.next();
	}

	/**********************************************************************
	 * 
	 * returns the number of rows for the chess board
	 * 
	 * @return int the number of rows for the chess board
	 *********************************************************************/
	public int numRows() {
		return 8;
	}

	/**********************************************************************
	 * 
	 * returns the number of columns for the chess board
	 * 
	 * @return int the number of columns for the chess board
	 *********************************************************************/
	public int numColumns() {
		return 8;
	}
	
	/**********************************************************************
	 * 
	 * returns the number of turns that have happened
	 * 
	 * @return int the number of turns that have happened
	 *********************************************************************/
	public int getTurnCounter() {
		return turnCounter;
	}

	/**********************************************************************
	 * 
	 * returns the value of the highlightedTiles array at the given row and
	 * column
	 * 
	 * @param row
	 *            the row position of the tile in the array
	 * @param column
	 *            the column position of the tile in the array
	 * 
	 * @return int 0 if not highlighted, 1 if yellow highlight, 2 if red, and
	 *         three if blue
	 *********************************************************************/
	public int highlighted(int row, int column) {
		return highlightedTiles[row][column];
	}

	/**********************************************************************
	 * 
	 * returns the IChessPiece at the given row and column in the board
	 * 
	 * @param row
	 *            the row position of the board
	 * @param column
	 *            the column position of the board
	 * 
	 * @return IChessPiece the piece on the board at the given row and column
	 *********************************************************************/
	public IChessPiece pieceAt(int row, int column) {
		return board[row][column];
	}

	/**********************************************************************
	 * 
	 * returns the IChessPiece at the given row and column in the deadWhite
	 * 
	 * @param row
	 *            the row position of the deadWhite array
	 * @param column
	 *            the column position of the deadWhite array
	 * 
	 * @return IChessPiece the piece on the deadWhite at the given row and
	 *         column
	 *********************************************************************/
	public IChessPiece deadWhiteAt(int index) {
		return deadWhite[index];
	}

	/**********************************************************************
	 * 
	 * returns the IChessPiece at the given row and column in the deadBlack
	 * 
	 * @param row
	 *            the row position of the deadBlack array
	 * @param column
	 *            the column position of the deadBlack array
	 * 
	 * @return IChessPiece the piece on the deadBlack at the given row and
	 *         column
	 *********************************************************************/
	public IChessPiece deadBlackAt(int index) {
		return deadBlack[index];
	}

	/**********************************************************************
	 * 
	 * updates the position of the white king
	 * 
	 * @param newX
	 *            the new row position of the white king
	 * @param newY
	 *            the new column position of the white king
	 *********************************************************************/
	public void updateWhiteKing(int newX, int newY) {
		whiteKingX = newX;
		whiteKingY = newY;
	}

	/**********************************************************************
	 * 
	 * updates the position of the black king
	 * 
	 * @param newX
	 *            the new row position of the black king
	 * @param newY
	 *            the new column position of the black king
	 *********************************************************************/
	public void updateBlackKing(int newX, int newY) {
		blackKingX = newX;
		blackKingY = newY;
	}

	/**********************************************************************
	 * 
	 * returns the chess board
	 * 
	 * @return IChessPiece[][] the chess board
	 *********************************************************************/
	public IChessPiece[][] getBoard() {
		return board;
	}

	/**********************************************************************
	 * 
	 * resets the game
	 *********************************************************************/
	public void reset() {
		this.clearTiles();
		int max = allMovePositions.size();
		for (int i = 0; i < max; i++) {
			this.undoLastMove();
		}
	}
}
