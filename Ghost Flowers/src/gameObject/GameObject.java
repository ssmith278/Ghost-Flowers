package gameObject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import tileMap.Tile;
import tileMap.TileMap;
import main.GamePanel;

public abstract class GameObject implements Serializable{

	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -7999109935768265707L;
	protected TileMap tilemap;
	protected int tileSize;
	
	protected double xmap, ymap;
	protected double x, y, dx, dy, xdest, ydest, xtemp, ytemp, moveSpeed, maxSpeed, stopSpeed, dashSpeed, fallSpeed, maxFallSpeed, jumpStart, stopJumpSpeed;	
	protected int width, height, cwidth, cheight, currRow, currCol, currentAction, previousAction;	
	protected boolean topLeft, topRight, bottomLeft, bottomRight, facingRight, left, right, up, down, dashing, jumping, falling, grounded;
	protected Animation animation;
	
	public GameObject(TileMap tilemap){
		this.tilemap = tilemap;
		this.tileSize = tilemap.getTileSize();
	}
	
	public boolean intersects(GameObject o){
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		
		return r1.intersects(r2);
	}
	
	public Rectangle getRectangle(){
		return new Rectangle((int)x - cwidth/2, (int)y - cheight/2, cwidth, cheight);
	}
	
	public void calculateCorners(double x, double y){
		
		int leftTile = (int)(x - cwidth / 2) / tileSize;
		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int topTile = (int)(y - cheight /2) / tileSize;
		int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
		
		 if(topTile < 0 || bottomTile >= tilemap.getNumRows() ||
	                leftTile < 0 || rightTile >= tilemap.getNumCols()) {
	                topLeft = topRight = bottomLeft = bottomRight = false;
	                return;
	        }
		 
        int tl = tilemap.getType(topTile, leftTile);
        int tr = tilemap.getType(topTile, rightTile);
        int bl = tilemap.getType(bottomTile, leftTile);
        int br = tilemap.getType(bottomTile, rightTile);
        
        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;
	}
	
	public void checkTileMapCollision(){
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest);
		if (dy < 0){
			if (topLeft || topRight){
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else{
				ytemp += dy;
			}
		}
		if (dy > 0){
			if (bottomLeft || bottomRight){
				dy = 0;
				falling = false;
				grounded = true;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if (dx < 0){
			if (topLeft || bottomLeft){
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		if (dx > 0){
			if (topRight || bottomRight){
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2; 
			}
			else{
				xtemp += dx;
			}
		}
		
		if (!falling){
			calculateCorners(x, ydest + 1);
			if (!bottomLeft && !bottomRight)
				falling = true;
				grounded = false;
		}
	}
	
	public double getX() { return x; }
	public double getY() { return y; }
	public double getDx() { return dx; }
	public double getDy() { return dy; }
	public double getSpeedPercent(double percent){
		return maxSpeed*percent/100;
	}
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCWidth() { return cwidth; }
	public int getCHeight() { return cheight; }
	
	public void setPosition(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public void setVector(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition(){
		xmap = tilemap.getX();
		ymap = tilemap.getY();
	}
	
	public boolean notOnScreen(){
		return x + xmap + width < 0 || x + xmap - width > GamePanel.WIDTH || y + ymap + height < 0 || y + ymap - height > GamePanel.HEIGHT;
	}
	
	public void setLeft(boolean b){ left = b; }
	public void setRight(boolean b){ right = b; }
	public void setUp(boolean b){ up = b; }
	public void setDown(boolean b){ down = b; }
	public void setDashing(boolean b){ dashing = b; }
	public void setJumping(boolean b){ jumping = b;}
	
	public boolean isLeft(){ return left;}
	public boolean isRight(){ return right; }
	public boolean isUp(){ return up; }
	public boolean isDown(){ return down; }
	public boolean isDashing(){ return dashing; }
	public boolean isJumping(){ return jumping; }
	public boolean isFacingRight(){ return facingRight; }
}
