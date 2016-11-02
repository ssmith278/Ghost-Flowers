package tileMap;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Tile implements Serializable{
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 5554572710921361272L;
	transient private BufferedImage image;
	private int type;
	
	// tile types
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	
	public Tile(BufferedImage image, int type) {
		this.image = image;
		this.type = type;
	}
	
	public BufferedImage getImage() { return image; }
	public int getType() { return type; }
	
}
