package chess;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ChessGUI {

	/**********************************************************************
	 * 
	 * creates the JFrame and adds the chess game as a panel
	 *********************************************************************/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ChessPanel panel = new ChessPanel();
		frame.getContentPane().add(panel);

		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Menu");
		JMenu verses = new JMenu("Verses");
		JMenuItem pvp = new JMenuItem("Player vs. Player");
		pvp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.setVerses("pvp");
			}
		});
		JMenuItem pve = new JMenuItem("Player vs. Bot");
		pve.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.setVerses("pve");
			}
		});
		JMenuItem eve = new JMenuItem("Bot vs. Bot");
		eve.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.setVerses("eve");
			}
		});
		JMenuItem reset = new JMenuItem("Reset");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.reset();
			}
		});
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		verses.add(pvp);
		verses.add(pve);
		verses.add(eve);
		menu.add(verses);
		menu.add(reset);
		menu.add(quit);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
		Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
		frame.setIconImage(icon);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
}
