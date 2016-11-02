package tileMap;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.Serializable;

import main.GamePanel;
import javax.imageio.ImageIO;

public class Background implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3418637289656037282L;

	transient private BufferedImage image;
	
	private final String FILE_LOCATION;
	
	private int width;
	private int height;	
	private double xmap, ymap;
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(String s, double ms, int width, int height){
		FILE_LOCATION = s;
		
		importBackground();
		
		moveScale = ms;
		this.width = width;
		this.height = height;
	}
	
	public void importBackground(){
		try{
			image = ImageIO.read(getClass().getResourceAsStream(FILE_LOCATION));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y){		 
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;		
	}
	
	public void setVector(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
		
	}
	
	public void update(){		
		x += dx;
		while(x <= -GamePanel.WIDTH) x += GamePanel.WIDTH;
		while(x >= GamePanel.WIDTH) x -= GamePanel.WIDTH;
		y += dy;
		while(y <= -GamePanel.HEIGHT) y += GamePanel.HEIGHT;
		while(y >= GamePanel.HEIGHT) y -= GamePanel.HEIGHT;
	}
	
	public void draw(Graphics2D g){	
		BufferedImage beforeImage = image;		
		AffineTransform at = new AffineTransform();
			at.scale(width/image.getWidth(),height/image.getHeight());
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage afterImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		afterImage = scaleOp.filter(beforeImage, afterImage);
		
		
		g.drawImage(afterImage, (int)x, (int)y, null);
		
		if (x - xmap < 0)
			g.drawImage(afterImage, (int)xmap + GamePanel.WIDTH, (int)y, null);
		if (x + xmap > 0)
			g.drawImage(afterImage, (int)xmap - GamePanel.WIDTH, (int)y, null);
		if(y - ymap < 0) 
			g.drawImage(afterImage, (int)x, (int)ymap + GamePanel.HEIGHT, null);
		if(y + ymap > 0) 
			g.drawImage(afterImage, (int)x, (int)ymap - GamePanel.HEIGHT, null);
	}

}
