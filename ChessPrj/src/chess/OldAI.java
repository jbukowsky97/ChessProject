package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class OldAI {
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
	 * @param p the player the AI is playing for
	 *********************************************************************/
	public OldAI(Player p) {
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
		weighting.put("King", 50);
		viableMoves = new ArrayList<Move>();
		random = new Random();
		active = false;
	}
	
	/**********************************************************************
	 * 
	 * determines what move to do based on point system
	 * 
	 * @param model the model the AI is performing a move on
	 * @param board the board that the AI is performing a move on
	 *********************************************************************/
	public void doMove(ChessModel model, IChessPiece[][] board) {
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
						int maxValue = -1;
						move = new Move(i, j, x, y);
						if (model.isValidMove(move)) {
							if (board[x][y] != null) {
								value += weighting.get(board[x][y].type());
							}
							model.move(move);
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
													if (weighting.get(board[x2][y2].type()) > maxValue) {
														maxValue = weighting.get(board[x2][y2].type());
													}
												}else {
													if (0 > maxValue) {
														maxValue = 0;
													}
												}
											}
										}
									}
								}
							}
							model.undoLastMove();
							value -= maxValue;
							if (value > topPointMoves) {
								viableMoves.clear();
								viableMoves.add(move);
								topPointMoves = value;
							}else if (value == topPointMoves) {
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
		}else {
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
	 * sets the active boolean of the AI determining whether
	 * the AI is playing or not
	 * 
	 * @param a the new value for the active boolean
	 *********************************************************************/
	public void setActive(boolean a) {
		active = a;
	}
	
	/**********************************************************************
	 * 
	 * returns the active instance variable which determines whether
	 * the AI is playing or not
	 * 
	 * @return boolean the active instance variable
	 *********************************************************************/
	public boolean active() {
		return active;
	}
}
