package chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ChessPanel extends JPanel {

	/** the JPanel that holds the chess board */
	private JPanel chessPanel;

	/** the JPanel with the miscellaneous display items */
	private JPanel menuPanel;

	/** the JPanel that holds the dead white pieces */
	private JPanel whitePanel;

	/** the JPanel that holds the dead black pieces */
	private JPanel blackPanel;

	/** the JButton array for tiles on the board */
	private JButton[][] board;

	/** the JButton array for tiles on the deadWhite */
	private JButton[] whiteBoard;

	/** the JButton array for tiles on the deadBlack */
	private JButton[] blackBoard;

	/** the ChessModel object */
	private ChessModel model;

	/** the AI object */
	//private OldAI ultraBot;
	private AIOtherTeam ultraBot;
	
	//private AIOtherTeam alphaBot;
	private OldAI alphaBot;

	/** the timer to control the AI wait time for moves */
	private javax.swing.Timer botTimer;
	
	/** the timer to control the time of both the white and the black player */
	private javax.swing.Timer gameTimer;

	/** boolean to determine if the game is completed */
	private boolean gameCompleted;

	/** boolean to determine if a player is undergoing promotion */
	private boolean choose;

	/** button to undo last move */
	private JButton undoButton;

	/** label to show white time */
	private JLabel whiteTime;

	/** label to show global message */
	private JLabel globalMessage;

	/** label to show turn color */
	private JLabel turnColor;

	/** label to show black time */
	private JLabel blackTime;

	/** font used by all text display */
	private static Font globalFont = new Font("Verdana", Font.PLAIN, 20);

	/** the ImageIcon for a white pawn */
	private static ImageIcon whitePawn = new ImageIcon("white_pawn.png");

	/** the ImageIcon for a black pawn */
	private static ImageIcon blackPawn = new ImageIcon("black_pawn.png");

	/** the ImageIcon for a white rook */
	private static ImageIcon whiteRook = new ImageIcon("white_rook.png");

	/** the ImageIcon for a black rook */
	private static ImageIcon blackRook = new ImageIcon("black_rook.png");

	/** the ImageIcon for a white knight */
	private static ImageIcon whiteKnight = new ImageIcon("white_knight.png");

	/** the ImageIcon for a black knight */
	private static ImageIcon blackKnight = new ImageIcon("black_knight.png");

	/** the ImageIcon for a white bishop */
	private static ImageIcon whiteBishop = new ImageIcon("white_bishop.png");

	/** the ImageIcon for a black bishop */
	private static ImageIcon blackBishop = new ImageIcon("black_bishop.png");

	/** the ImageIcon for a white queen */
	private static ImageIcon whiteQueen = new ImageIcon("white_queen.png");

	/** the ImageIcon for a black queen */
	private static ImageIcon blackQueen = new ImageIcon("black_queen.png");

	/** the ImageIcon for a white king */
	private static ImageIcon whiteKing = new ImageIcon("white_king.png");

	/** the ImageIcon for a black king */
	private static ImageIcon blackKing = new ImageIcon("black_king.png");

	/** the ImageIcon for white color */
	private static ImageIcon whiteColor = new ImageIcon("white_color.png");

	/** the ImageIcon for black color */
	private static ImageIcon blackColor = new ImageIcon("black_color.png");

	/** the ImageIcon for eighty squared */
	private static ImageIcon eightySquared = new ImageIcon("eighty_squared.png");

	/** the ImageIcon for one sixty */
	private static ImageIcon oneSixty = new ImageIcon("one_sixty.png");

	/** the ImageIcon for two forty */
	private static ImageIcon twoForty = new ImageIcon("two_forty.png");

	/** the ImageIcon for chess graphic */
	private static ImageIcon chessGraphic = new ImageIcon("chess_graphic.png");

	/**
	 * holds all of the ImageIcons and sorts them by the player color and name
	 * of piece
	 */
	private HashMap<String, ImageIcon> icons;

	/**
	 * holds the row position of the user's first clicked piece during a move
	 */
	private int startX;

	/**
	 * holds the column position of the user's first clicked piece during a move
	 */
	private int startY;

	/** the ButtonListener used for all of the JButtons */
	private ButtonListener buttonListener = new ButtonListener();

	/**********************************************************************
	 * 
	 * constructor to initialize instance variables and create visuals
	 *********************************************************************/
	public ChessPanel() {

		ultraBot = new AIOtherTeam();
		//ultraBot = new OldAI(Player.BLACK);
		ultraBot.setActive(false);
		//alphaBot = new AIOtherTeam();
		alphaBot = new OldAI(Player.WHITE);
		alphaBot.setActive(false);
		gameCompleted = false;
		choose = false;

		icons = new HashMap<String, ImageIcon>();
		icons.put("WHITEPawn", whitePawn);
		icons.put("WHITERook", whiteRook);
		icons.put("WHITEKnight", whiteKnight);
		icons.put("WHITEBishop", whiteBishop);
		icons.put("WHITEQueen", whiteQueen);
		icons.put("WHITEKing", whiteKing);
		icons.put("BLACKPawn", blackPawn);
		icons.put("BLACKRook", blackRook);
		icons.put("BLACKKnight", blackKnight);
		icons.put("BLACKBishop", blackBishop);
		icons.put("BLACKQueen", blackQueen);
		icons.put("BLACKKing", blackKing);

		this.setLayout(new BorderLayout());

		// chessPanel
		chessPanel = new JPanel();
		chessPanel.setLayout(new GridLayout(8, 8, 0, 0));
		chessPanel.setBackground(new Color(52, 33, 20));
		chessPanel.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
		model = new ChessModel();
		board = new JButton[8][8];

		// set the darker and lighter colors
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = new JButton();
				if (i % 2 == 0) {
					if (j % 2 == 0) {
						board[i][j].setBackground(new Color(222, 173, 115));
					} else {
						board[i][j].setBackground(new Color(106, 66, 40));
					}
				} else {
					if (j % 2 == 0) {
						board[i][j].setBackground(new Color(106, 66, 40));
					} else {
						board[i][j].setBackground(new Color(222, 173, 115));
					}
				}
				board[i][j].addActionListener(buttonListener);
				board[i][j].setSize(new Dimension(70, 70));
				chessPanel.add(board[i][j]);
			}
		}

		// menuPanel
		menuPanel = new JPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
		whiteTime = new JLabel("30.0");
		whiteTime.setFont(globalFont);
		whiteTime.setVerticalTextPosition(JLabel.CENTER);
		whiteTime.setHorizontalTextPosition(JLabel.CENTER);
		whiteTime.setForeground(new Color(255, 255, 255));
		whiteTime.setIcon(oneSixty);
		menuPanel.add(whiteTime);
		globalMessage = new JLabel("Welcome to Chess!");
		globalMessage.setFont(globalFont);
		globalMessage.setVerticalTextPosition(JLabel.CENTER);
		globalMessage.setHorizontalTextPosition(JLabel.CENTER);
		globalMessage.setForeground(new Color(255, 255, 255));
		globalMessage.setIcon(twoForty);
		menuPanel.add(globalMessage);
		JLabel centerGraphic = new JLabel();
		centerGraphic.setIcon(chessGraphic);
		menuPanel.add(centerGraphic);
		undoButton = new JButton("Undo");
		undoButton.setFont(globalFont);
		undoButton.setVerticalTextPosition(JButton.CENTER);
		undoButton.setHorizontalTextPosition(JButton.CENTER);
		undoButton.setForeground(new Color(255, 255, 255));
		undoButton.addActionListener(buttonListener);
		undoButton.setIcon(eightySquared);
		undoButton.setMargin(new Insets(-3, -3, -3, -3));
		undoButton.setBorderPainted(false);
		menuPanel.add(undoButton);
		JLabel turnText = new JLabel("Turn:");
		turnText.setFont(globalFont);
		turnText.setVerticalTextPosition(JLabel.CENTER);
		turnText.setHorizontalTextPosition(JLabel.CENTER);
		turnText.setForeground(new Color(255, 255, 255));
		turnText.setIcon(eightySquared);
		menuPanel.add(turnText);
		turnColor = new JLabel();
		turnColor.setIcon(whiteColor);
		menuPanel.add(turnColor);
		blackTime = new JLabel("30.0");
		blackTime.setFont(globalFont);
		blackTime.setVerticalTextPosition(JLabel.CENTER);
		blackTime.setHorizontalTextPosition(JLabel.CENTER);
		blackTime.setForeground(new Color(255, 255, 255));
		blackTime.setIcon(oneSixty);
		menuPanel.add(blackTime);

		// whitePanel
		whitePanel = new JPanel();
		whitePanel.setLayout(new GridLayout(8, 2, 0, 0));
		whiteBoard = new JButton[16];

		for (int i = 0; i < 16; i++) {
			whiteBoard[i] = new JButton();
			whiteBoard[i].setBackground(new Color(250, 200, 140));
			whiteBoard[i].setBorderPainted(false);
			whiteBoard[i].addActionListener(buttonListener);
			whiteBoard[i].setSize(new Dimension(70, 70));
			whitePanel.add(whiteBoard[i]);
		}

		// blackPanel
		blackPanel = new JPanel();
		blackPanel.setLayout(new GridLayout(8, 2, 0, 0));
		blackBoard = new JButton[16];

		for (int i = 0; i < 16; i++) {
			blackBoard[i] = new JButton();
			blackBoard[i].setBackground(new Color(150, 110, 84));
			blackBoard[i].setBorderPainted(false);
			blackBoard[i].addActionListener(buttonListener);
			blackBoard[i].setSize(new Dimension(70, 70));
			blackPanel.add(blackBoard[i]);
		}

		// add everything together
		this.add(chessPanel, BorderLayout.CENTER);
		this.add(whitePanel, BorderLayout.WEST);
		this.add(blackPanel, BorderLayout.EAST);
		this.add(menuPanel, BorderLayout.NORTH);
		menuPanel.setPreferredSize(new Dimension(980, 70));
		chessPanel.setPreferredSize(new Dimension(700, 700));
		whitePanel.setPreferredSize(new Dimension(140, 700));
		blackPanel.setPreferredSize(new Dimension(140, 700));
		startX = -1;
		startY = -1;

		botTimer = new javax.swing.Timer(700, new BotTimeManager());
		gameTimer = new javax.swing.Timer(1000, new GameTimeManager());
		
		botTimer.start();

		displayBoard();
	}

	/**********************************************************************
	 * 
	 * updates the visuals for all of the panels to correspond with the current
	 * game progress
	 *********************************************************************/
	private void displayBoard() {
		// update turn color
		if (model.currentPlayer() == Player.WHITE) {
			turnColor.setIcon(whiteColor);
		} else {
			turnColor.setIcon(blackColor);
		}
		// update outlines of tiles
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				switch (model.highlighted(i, j)) {
				case 5:
					board[i][j].setBackground(new Color(255, 30, 30));
					board[i][j].setBorder(BorderFactory.createLineBorder(
							new Color(100, 0, 0), 1));
					break;
				case 4:
					board[i][j].setBorder(BorderFactory.createLineBorder(
							new Color(255, 255, 255), 3));
					this.helperDisplay(i, j);
					break;
				case 3:
					board[i][j].setBorder(BorderFactory.createLineBorder(
							new Color(0, 0, 255), 3));
					this.helperDisplay(i, j);
					break;
				case 2:
					board[i][j].setBorder(BorderFactory.createLineBorder(
							new Color(250, 0, 0), 3));
					this.helperDisplay(i, j);
					break;
				case 1:
					board[i][j].setBorder(BorderFactory.createLineBorder(
							new Color(250, 250, 120), 3));
					this.helperDisplay(i, j);
					break;
				default:
					this.helperDisplay(i, j);
					break;
				}
			}
		}
		// set the piece icons for the chess board
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (model.pieceAt(i, j) != null) {
					board[i][j].setIcon(icons.get(model.pieceAt(i, j).player()
							+ model.pieceAt(i, j).type()));
				} else {
					board[i][j].setIcon(null);
				}
			}
		}
		// set the piece icons for the dead white board
		for (int i = 0; i < 16; i++) {
			if (model.deadWhiteAt(i) != null) {
				whiteBoard[i].setIcon(icons.get("WHITE"
						+ model.deadWhiteAt(i).type()));
			} else {
				whiteBoard[i].setIcon(null);
			}
		}
		// set the piece icons for the dead black board
		for (int i = 0; i < 16; i++) {
			if (model.deadBlackAt(i) != null) {
				blackBoard[i].setIcon(icons.get("BLACK"
						+ model.deadBlackAt(i).type()));
			} else {
				blackBoard[i].setIcon(null);
			}
		}
	}

	private void helperDisplay(int i, int j) {
		if (model.highlighted(i, j) != 5) {
			if (model.highlighted(i, j) == 0) {
				board[i][j].setBorder(null);
			}
			if (i % 2 == 0) {
				if (j % 2 == 0) {
					board[i][j].setBackground(new Color(222, 173, 115));
				}else {
					board[i][j].setBackground(new Color(106, 66, 40));
				}
			}else {
				if (j % 2 == 0) {
					board[i][j].setBackground(new Color(106, 66, 40));
				}else {
					board[i][j].setBackground(new Color(222, 173, 115));
				}
			}
		}
	}
	
	/**********************************************************************
	 * 
	 * sets the current verses state (pvp, pve, or eve)
	 * 
	 * @param s
	 *            String representing state
	 *********************************************************************/
	public void setVerses(String s) {
		if (s.equals("pvp")) {
			if (ultraBot.active() || alphaBot.active()) {
				this.reset();
			}
			ultraBot.setActive(false);
			alphaBot.setActive(false);
		} else if (s.equals("pve")) {
			if (!ultraBot.active() || alphaBot.active()) {
				this.reset();
			}
			ultraBot.setActive(true);
			alphaBot.setActive(false);
		} else if (s.equals("eve")) {
			if (!ultraBot.active() || !alphaBot.active()) {
				this.reset();
			}
			ultraBot.setActive(true);
			alphaBot.setActive(true);
		}
	}

	/**********************************************************************
	 * 
	 * resets the game
	 *********************************************************************/
	public void reset() {
		gameCompleted = false;
		startX = -1;
		startY = -1;
		model.reset();
		this.displayBoard();
	}

	/**********************************************************************
	 * 
	 * inner class that represents action listener for buttons
	 *********************************************************************/
	private class ButtonListener implements ActionListener {

		/**********************************************************************
		 * 
		 * handles all of the JButton and MenuItem click events
		 * 
		 * @param e
		 *            the event is invoked
		 *********************************************************************/
		@Override
		public void actionPerformed(ActionEvent e) {
			// if game is done don't run
			if (gameCompleted) {
				return;
			}
			// undo
			if (e.getSource() == undoButton) {
				if (choose) {
					return;
				}
				model.undoLastMove();
				model.clearTiles();
				model.showTheCheck();
				displayBoard();
				botTimer.restart();
				gameTimer.restart();
				whiteTime.setText("30.0");
				blackTime.setText("30.0");
				return;
			}
			// don't let user click on bot player pieces if they are active
			if (model.currentPlayer() == ultraBot.player() && ultraBot.active() && !choose) {
				return;
			}
			if (model.currentPlayer() == alphaBot.player() && alphaBot.active() && !choose) {
				return;
			}
			// loop through every tile on chess board
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (board[i][j] == e.getSource()) {
						// if undergoing promotion, do promotion stuff
						if (choose) {
							IChessPiece w = null;
							w = model.pieceAt(i, j);
							if (w == null
									|| w.player() == model.currentPlayer()
									|| w instanceof Pawn || w instanceof King) {
								return;
							}
							model.switchPawn(w);
							choose = false;
							model.clearTiles();
							if (model.stalemate()) {
								globalMessage.setText("Stalemate!");
								gameCompleted = true;
								gameTimer.stop();
							} else if (model.isComplete()) {
								globalMessage.setText("CHECK MATE!");
								model.showCheckMate();
								displayBoard();
								gameCompleted = true;
								gameTimer.stop();
							} else if (model.inCheck(model.currentPlayer())) {
								globalMessage.setText(model.currentPlayer()
										+ " is in check!");
								model.clearTiles();
								model.showTheCheck();
								displayBoard();
							} else {
								globalMessage.setText("");
							}
							displayBoard();
							return;
						}
						// if startX, startY are not a piece yet, assign them
						// to the button that was clicked
						if (startX == -1) {
							startX = i;
							startY = j;
							// highlight the pieces avaiable moves
							model.highlightMoves(startX, startY);
							// reset piece assignment if it is not a valid one
							// for the move that needs to be done
							if (model.pieceAt(startX, startY) == null
									|| model.pieceAt(startX, startY).player() != model
											.currentPlayer()) {
								startX = -1;
								startY = -1;
								model.clearTiles();
							}else {
								gameTimer.start();
							}
						} else {
							if (model.pieceAt(startX, startY) == null) {
								startX = -1;
								startY = -1;
								return;
							}
							// if the player clicks on his own piece,
							// instead of attempting a move, reset the
							// starting piece to the newly clicked piece
							if (model.pieceAt(i, j) != null) {
								if (model.pieceAt(startX, startY).player() == model
										.pieceAt(i, j).player()) {
									startX = i;
									startY = j;
									// highlight available moves
									model.highlightMoves(startX, startY);
									displayBoard();
									return;
								}
							} else { // clear tiles because player is attempting
										// a move
								model.clearTiles();
							}
							// if the piece is a king, determine if the king
							// is trying to castle (if it is clicking 2
							// to the left or right of it in the same row)
							if (model.pieceAt(startX, startY) instanceof King) {
								if (i - startX == 0
										&& Math.abs(j - startY) == 2) {
									// the user wants to castle, so attempt
									// a castle
									boolean temp;
									if (j - startY > 0) {
										temp = model.castle(new Move(startX,
												startY, i, j), startX, 7);
									} else {
										temp = model.castle(new Move(startX,
												startY, i, j), startX, 0);
									}
									if (temp) {
										startX = -1;
										startY = -1;
										displayBoard();
										return;
									}
								}
							}
							// check if the move being attempted is valid
							if (model
									.isValidMove(new Move(startX, startY, i, j))) {

								model.move(new Move(startX, startY, i, j));
								// restart timer so that it takes a full second
								// for AI to make a move
								botTimer.restart();
								gameTimer.restart();
								whiteTime.setText("30.0");
								blackTime.setText("30.0");
								// clear highlighted tiles
								model.clearTiles();
								// if promotion occurs, set choose to true
								// and wait for next input
								if (model.promotion()) {
									choose = true;
									globalMessage.setText("Promote Pawn!");
									model.highlightPromotions(model
											.currentPlayer().next());
									displayBoard();
									startX = -1;
									startY = -1;
									return;
								}
								displayBoard();
								// check the status of the game
								if (model.stalemate()) {
									globalMessage.setText("Stalemate!");
									gameCompleted = true;
									gameTimer.stop();
								} else if (model.isComplete()) {
									globalMessage.setText("CHECK MATE!");
									model.showCheckMate();
									displayBoard();
									gameCompleted = true;
									gameTimer.stop();
								} else if (model.inCheck(model.currentPlayer())) {
									globalMessage.setText(model.currentPlayer()
											+ " is in check!");
									model.clearTiles();
									model.showTheCheck();
									displayBoard();
								} else {
									globalMessage.setText("");
								}
							}
							startX = -1;
							startY = -1;
						}
					}
				}
			}
			// if choose and it did not find the button on the chess board
			// check the dead white and the dead black
			if (choose) {
				IChessPiece w = null;
				for (int x = 0; x < 16; x++) {

					if (e.getSource() == whiteBoard[x]) {

						w = model.deadWhiteAt(x);
						if (w == null || w.player() == model.currentPlayer()
								|| w instanceof Pawn || w instanceof King) {
							return;
						}
						model.switchPawn(w);
					}

					if (e.getSource() == blackBoard[x]) {
						w = model.deadBlackAt(x);
						if (w == null || w.player() == model.currentPlayer()
								|| w instanceof Pawn || w instanceof King) {
							return;
						}
						model.switchPawn(w);
					}
				}
				choose = false;
				model.clearTiles();
			}
			displayBoard();
		}
	}

	/**********************************************************************
	 * 
	 * inner class that manages the time for AI
	 *********************************************************************/
	public class BotTimeManager implements ActionListener {

		/**********************************************************************
		 * 
		 * handles all of the AI decisions
		 * 
		 * @param e
		 *            the event that is invoked
		 *********************************************************************/
		@Override
		public void actionPerformed(ActionEvent e) {
			saveStats();
			// exit if game is done
			if (gameCompleted) {
				return;
			}
			if (choose) {
				return;
			}
			// if ultraBot is active and it is his turn then execute a turn
			if (model.currentPlayer() == ultraBot.player() && ultraBot.active()) {
				model.clearTiles();
				//ultraBot.doMove(model, model.getBoard());
				ultraBot.moveGenerator(model, model.getBoard());
				Move move = ultraBot.moveCalculator(model, model.getBoard());
				model.move(move);
				// promote to queen if needed
				if (model.promotion()) {
					IChessPiece w = new Queen(ultraBot.player());
					model.switchPawn(w);
					if (model.stalemate()) {
						globalMessage.setText("Stalemate!");
						gameCompleted = true;
						gameTimer.stop();
						saveStats();
					} else if (model.isComplete()) {
						globalMessage.setText("CHECK MATE!");
						model.showCheckMate();
						displayBoard();
						gameCompleted = true;
						gameTimer.stop();
						saveStats();
					} else if (model.inCheck(model.currentPlayer())) {
						globalMessage.setText(model.currentPlayer()
								+ " is in check!");
						model.clearTiles();
						model.showTheCheck();
						displayBoard();
					} else {
						globalMessage.setText("");
					}
				}
				displayBoard();
				// check current status of game
				if (model.stalemate()) {
					globalMessage.setText("Stalemate!");
					gameCompleted = true;
					gameTimer.stop();
					saveStats();
				} else if (model.isComplete()) {
					globalMessage.setText("CHECK MATE!");
					model.showCheckMate();
					displayBoard();
					gameCompleted = true;
					gameTimer.stop();
					saveStats();
				} else if (model.inCheck(model.currentPlayer())) {
					globalMessage.setText(model.currentPlayer()
							+ " is in check!");
					model.clearTiles();
					model.showTheCheck();
					displayBoard();
				} else {
					globalMessage.setText("");
				}
				whiteTime.setText("30.0");
				blackTime.setText("30.0");
				return;
			}
			// if alphaBot is active and it is his turn then execute a turn
			else if (model.currentPlayer() == alphaBot.player() && alphaBot.active()) {
				model.clearTiles();
				alphaBot.doMove(model, model.getBoard());
				//alphaBot.moveGenerator(model, model.getBoard());
				//Move move = alphaBot.moveCalculator(model, model.getBoard());
				//model.move(move);
				// promote to queen if needed
				if (model.promotion()) {
					IChessPiece w = new Queen(alphaBot.player());
					model.switchPawn(w);
					if (model.stalemate()) {
						globalMessage.setText("Stalemate!");
						gameCompleted = true;
						gameTimer.stop();
						saveStats();
					} else if (model.isComplete()) {
						globalMessage.setText("CHECK MATE!");
						model.showCheckMate();
						displayBoard();
						gameCompleted = true;
						gameTimer.stop();
						saveStats();
					} else if (model.inCheck(model.currentPlayer())) {
						globalMessage.setText(model.currentPlayer()
								+ " is in check!");
						model.clearTiles();
						model.showTheCheck();
						displayBoard();
					} else {
						globalMessage.setText("");
					}
				}
				displayBoard();
				// check current status of game
				if (model.isComplete()) {
					globalMessage.setText("CHECK MATE!");
					model.showCheckMate();
					displayBoard();
					gameCompleted = true;
					gameTimer.stop();
					saveStats();
				} else if (model.stalemate()) {
					globalMessage.setText("Stalemate!");
					gameCompleted = true;
					gameTimer.stop();
					saveStats();
				} else if (model.inCheck(model.currentPlayer())) {
					globalMessage.setText(model.currentPlayer()
							+ " is in check!");
					model.clearTiles();
					model.showTheCheck();
					displayBoard();
				} else {
					globalMessage.setText("");
				}
				whiteTime.setText("30.0");
				blackTime.setText("30.0");
				return;
			}

		}

	}
	
	private void saveStats() {
		if (gameCompleted || model.getTurnCounter() >= 300) {
			int winsWhite = 0;
			int winsBlack = 0;
			try {
	            Scanner reader = new Scanner(new File("stats.txt"));
	            String firstString = reader.nextLine();
	            String secondString = reader.nextLine();
	            winsBlack = Integer.parseInt(firstString);
	            winsWhite = Integer.parseInt(secondString);
	            reader.close();
	        }catch (IOException exception) {
	            System.out.println("File not found");
	        }
	        try {
	            PrintWriter saver = new PrintWriter(new BufferedWriter
	                    (new FileWriter("stats.txt")));
	            if (model.inCheck(Player.WHITE)) {
	            	winsBlack++;
	            }
	            if (model.inCheck(Player.BLACK)) {
	            	winsWhite++;
	            }
	            saver.println(winsBlack);
	            saver.println(winsWhite);
		        saver.close();
	        }catch (IOException exception) {
	            exception.printStackTrace();
	        }
	        reset();
		}
	}
	
	/**********************************************************************
	 * 
	 * inner class that manages the time for game
	 *********************************************************************/
	public class GameTimeManager implements ActionListener {

		/**********************************************************************
		 * 
		 * handles the game time
		 * 
		 * @param e
		 *            the event that is invoked
		 *********************************************************************/
		@Override
		public void actionPerformed(ActionEvent e) {
			if (model.currentPlayer() == Player.WHITE) {
				whiteTime.setText(Double.toString(Math.max(0.0, (Double.parseDouble(whiteTime.getText()) - 1.0))));
			}else {
				blackTime.setText(Double.toString(Math.max(0.0, (Double.parseDouble(blackTime.getText()) - 1.0))));
			}
		}
	}
}
