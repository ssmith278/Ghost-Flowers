package main;

import javax.swing.JFrame;

public class Game {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Ghost Flowers");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		frame.getContentPane().add(new GamePanel());
		frame.pack();
		frame.setVisible(true);

	}

}
