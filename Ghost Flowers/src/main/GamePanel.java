package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import gameState.GameStateManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener{

	private GameStateManager gsm;
	
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static int screenWidth = (int) screenSize.getWidth();
	private static int screenHeight = (int) screenSize.getHeight();
	
	public static final int WIDTH = screenWidth/3;
	public static final int HEIGHT = screenHeight/3;
	//public static final int WIDTH = 960;
	//public static final int HEIGHT = 540;
	public static final int SCALE = 2;
	
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	private BufferedImage image;
	private Graphics2D g;
	
	public GamePanel(){
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify(){
		super.addNotify();
		if (thread == null){
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	public void run(){
		
		init();
		
		long start;
		long elapsed;
		long wait;
		
		//game loop
		
		while(running){
			
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 0;
						
			try{
				Thread.sleep(wait);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void init(){
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
		gsm = new GameStateManager();
	}
	
	private void update(){
		gsm.update();
	}
	private void draw(){
		gsm.draw(g);
	}
	private void drawToScreen(){
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}
	public void keyPressed(KeyEvent event){
		gsm.keyPressed(event.getKeyCode());
	}
	public void keyReleased(KeyEvent event){
		gsm.keyReleased(event.getKeyCode());
	}
	public void keyTyped(KeyEvent event){}

}
