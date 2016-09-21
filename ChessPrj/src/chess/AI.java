package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AI {

	/** the player that the AI is playing for */
	private Player player;
	
	/** the starting moves the AI will always take depending on which side it is on */
	private ArrayList<Move> firstMoves;

	/** the weighted point values for each of the different pieces */
	private HashMap<String, Integer> weighting;

	/** the list of all moves that are viable for the current turn */
	private ArrayList<Move> viableMoves;

	/** creates a random number when needed for equally ranked decisions */
	private Random random;

	/** determines whether the AI is playing or not */
	private boolean active;

	/**********************************************************************
	 * 
	 * constructor to initialize instance variables
	 * 
	 * @param p
	 *            the player the AI is playing for
	 *********************************************************************/
	public AI(Player p) {
		player = p;
		firstMoves = new ArrayList<Move>();
		if (p == Player.WHITE) {
			firstMoves.add(new Move(6, 4, 4, 4));
			firstMoves.add(new Move(7, 6, 5, 5));
		}else {
			firstMoves.add(new Move(1, 4, 2, 4));
			firstMoves.add(new Move(1, 3, 3, 3));
		}
		weighting = new HashMap<String, Integer>();
		weighting.put("Pawn", 1);
		weighting.put("Knight", 3);
		weighting.put("Bishop", 3);
		weighting.put("Rook", 5);
		weighting.put("Queen", 9);
		weighting.put("King", 15);
		viableMoves = new ArrayList<Move>();
		random = new Random();
		active = false;
	}

	/**********************************************************************
	 * 
	 * determines what move to do based on point system
	 * 
	 * @param model
	 *            the model the AI is performing a move on
	 * @param board
	 *            the board that the AI is performing a move on
	 *********************************************************************/
	public void doMove(ChessModel model, IChessPiece[][] board) {
		// determine what moves provide the best statistical advantage
		// in the long run
		if (model.getTurnCounter() < 4) {
			//use initial moves provided
			if (model.isValidMove(firstMoves.get(model.getTurnCounter() / 2))) {
				model.move(firstMoves.get(model.getTurnCounter() / 2));
				return;
			}
		}
		Move move, nextMove;
		int topPointMoves = -100;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == null) {
					continue;
				}
				if (board[i][j].player() != player) {
					continue;
				}
				for (int x = 0; x < 8; x++) {
					for (int y = 0; y < 8; y++) {
						int value = 0;
						move = new Move(i, j, x, y);
						if (model.isValidMove(move)) {
							if (board[x][y] != null) {
								value += weighting.get(board[x][y].type());
							}
							model.move(move);
							int opposingValue = -1;
							Move opposingMove = null;
							for (int i2 = 0; i2 < 8; i2++) {
								for (int j2 = 0; j2 < 8; j2++) {
									if (board[i2][j2] == null) {
										continue;
									}
									if (board[i2][j2].player() == player) {
										continue;
									}
									for (int x2 = 0; x2 < 8; x2++) {
										for (int y2 = 0; y2 < 8; y2++) {
											nextMove = new Move(i2, j2, x2, y2);
											if (model.isValidMove(nextMove)) {
												if (board[x2][y2] != null) {
													if (opposingMove == null) {
														opposingMove = nextMove;
														opposingValue = weighting.get(board[x2][y2].type());
													}
													if (weighting.get(board[x2][y2].type()) > opposingValue) {
														opposingValue = weighting.get(board[x2][y2].type());
														opposingMove = nextMove;
													}
												}else {
													if (opposingMove == null) {
														opposingMove = nextMove;
														opposingValue = 0;
													}
													if (0 > opposingValue) {
														opposingValue = 0;
														opposingMove = nextMove;
													}
												}
											}
										}
									}
								}
							}
							if (opposingMove == null) {
								//assuming checkmate
								model.undoLastMove();
								model.move(move);
								return;
							}
							value -= opposingValue;
							model.move(opposingMove);
							///////////////////////////////////////////////////////////////
							int nextValue = 0;
							for (int i3 = 0; i3 < 8; i3++) {
								for (int j3 = 0; j3 < 8; j3++) {
									int nextTempValue = 0;
									if (board[i3][j3] == null) {
										continue;
									}
									if (board[i3][j3].player() == player) {
										continue;
									}
									for (int x3 = 0; x3 < 8; x3++) {
										for (int y3 = 0; y3 < 8; y3++) {
											nextMove = new Move(i3, j3, x3, y3);
											if (model.isValidMove(nextMove)) {
												if (board[x3][y3] != null) {
													nextTempValue = weighting.get(board[x3][y3].type());
												}
												model.move(nextMove);
												int nextOpposingValue = 0;
												for (int i4 = 0; i4 < 8; i4++) {
													for (int j4 = 0; j4 < 8; j4++) {
														if (board[i4][j4] == null) {
															continue;
														}
														if (board[i4][j4].player() == player) {
															continue;
														}
														for (int x4 = 0; x4 < 8; x4++) {
															for (int y4 = 0; y4 < 8; y4++) {
																nextMove = new Move(i4, j4, x4, y4);
																if (model.isValidMove(nextMove)) {
																	if (board[x4][y4] != null) {
																		if (weighting.get(board[x4][y4].type()) > nextOpposingValue) {
																			nextOpposingValue = weighting.get(board[x4][y4].type());
																		}
																	}
																}
															}
														}
													}
												}
												model.undoLastMove();
												if (nextTempValue - nextOpposingValue > nextValue) {
													nextValue = nextTempValue - nextOpposingValue;
												}
											}
										}
									}
								}
							}
							///////////////////////////////////////////////////////////////
							model.undoLastMove();
							model.undoLastMove();
							if (value + nextValue > topPointMoves) {
								viableMoves.clear();
								viableMoves.add(move);
								topPointMoves = value + nextValue;
							} else if (value + nextValue == topPointMoves) {
								viableMoves.add(move);
							}
						}
					}
				}
			}
		}
		if (viableMoves.size() > 1) {
			int executedMove = random.nextInt(viableMoves.size());
			model.move(viableMoves.get(executedMove));
		} else {
			model.move(viableMoves.get(0));
		}
	}

	/**********************************************************************
	 * 
	 * returns the player the AI is playing for
	 * 
	 * @return Player the player the AI is playing for
	 *********************************************************************/
	public Player player() {
		return player;
	}

	/**********************************************************************
	 * 
	 * sets the active boolean of the AI determining whether the AI is playing
	 * or not
	 * 
	 * @param a
	 *            the new value for the active boolean
	 *********************************************************************/
	public void setActive(boolean a) {
		active = a;
	}

	/**********************************************************************
	 * 
	 * returns the active instance variable which determines whether the AI is
	 * playing or not
	 * 
	 * @return boolean the active instance variable
	 *********************************************************************/
	public boolean active() {
		return active;
	}
}
