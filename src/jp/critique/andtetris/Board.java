/**
 * 
 */
package jp.critique.andtetris;

import jp.critique.andtetris.block.Block;

import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.graphics.Color;
import android.graphics.Point;

/**
 * @author tomokane
 *
 */
public class Board {
	
	public static final int MAX_X = 12;
	public static final int MAX_Y = 26;
	
	public static final int TILE_SIZE = 16;
	
	
	private int[][] board;
	private int[][] boardImage;
	
	private ITextureRegion mFaceTextureRegion;
	private VertexBufferObjectManager vertexBufferObjectManager;
	private Scene scene;
	private Line[] horizontalLineArray;
	private Line[] verticalLineArray;

	public Line[] getHorizontalLineArray() {
		return horizontalLineArray;
	}

	public void setHorizontalLineArray(Line[] lineArray) {
		this.horizontalLineArray = lineArray;
	}

	public Line[] getVerticalLineArray() {
		return verticalLineArray;
	}

	public void setVerticalLineArray(Line[] verticalLineArray) {
		this.verticalLineArray = verticalLineArray;
	}

	/**
	 * Constructor
	 */
	public Board(Scene scene, ITextureRegion mFaceTextureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
		board = new int[MAX_Y][MAX_X];
		boardImage = new int[MAX_Y][MAX_X];
		
		this.mFaceTextureRegion = mFaceTextureRegion;
		this.vertexBufferObjectManager = vertexBufferObjectManager;
		this.scene = scene;
		
		init();
	}
	
	/**
	 * draw wall.
	 */
	private void init() {
		verticalLineArray = new Line[MAX_X + 1];
		horizontalLineArray = new Line[MAX_Y + 1];
		
		for (int i = 0; i < verticalLineArray.length + 1; i++) {
			verticalLineArray[i] = new Line(0, i * 16, MainActivity.CAMERA_WIDTH, i * 16, this.vertexBufferObjectManager);
			verticalLineArray[i].setColor(1f,0,0);
			verticalLineArray[i].setLineWidth(3f);
			this.scene.attachChild(verticalLineArray[i]);
		}
		for (int i = 0; i < horizontalLineArray.length; i++) {
			horizontalLineArray[i] = new Line(i * 16, 0, i * 16, 192 , this.vertexBufferObjectManager);
			horizontalLineArray[i].setColor(1f,0,0);
			horizontalLineArray[i].setLineWidth(3f);
			this.scene.attachChild(horizontalLineArray[i]);
		}
	}
	
	/**
	 * insert sprite
	 * @param s Scene
	 * @param mFaceTextureRegion sprite
	 * @param vertexBufferObjectManager manager
	 */
//	public void draw(Scene s, ITextureRegion mFaceTextureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
//		for (int y = 0; y < MAX_Y; y++) {
//			for (int x = 0; x < MAX_X; x++) {
//				if(board[y][x] == 1) {
////					Sprite blockSprite = new Sprite(x * TILE_SIZE, y * TILE_SIZE, mFaceTextureRegion, vertexBufferObjectManager);
//					Rectangle blockRect = new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, vertexBufferObjectManager);
//					blockRect.setColor(0.9f, 0.1f, 0.1f);
//					s.attachChild(blockRect);
//				}
//			}
//		}
//	}
	
	/**
	 * check block movable
	 * @param newPos next position
	 * @param block block
	 * @return if block is able to move is true
	 */
	public boolean isMovable(Point newPos, int[][] block) {
		for (int y = 0; y < Block.MAX_Y; y++) {
			for (int x = 0; x < Block.MAX_X; x++) {
				if (block[y][x] == 1) {
					if(newPos.y + y < 0) {
						if (newPos.x + x <= 0 || newPos.x + x >= MAX_X - 1) {
							return false;
						}
					} else if (board[newPos.y + y][newPos.x + x] == 1) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * freeze block when touch floor.
	 * @param pos position of block
	 * @param block block
	 * @param color blocks color
	 */
	public void fixBlock(Point pos, int[][] block, int imageNo) {
		for (int y = 0; y < Block.MAX_Y; y++) {
			for (int x = 0; x < Block.MAX_Y; x++) {
				if (block[y][x] == 1) {
					if (pos.y + y < 0) continue;
					board[pos.y + y][pos.x + x] = 1;
					boardImage[pos.y + y][pos.x + x] = imageNo;
				}
			}
		}
	}
	
	/**
	 * delete filled line
	 */
	public void deleteFilledLine() {
		for (int y = 0; y < MAX_Y - 1; y++) {
			int count = 0;
			for (int x = 1; x < MAX_X - 1; x++) {
				if (board[y][x] == 1) count++;
			}
			
			if (count == Board.MAX_X - 2) {
				for (int x = 1; x < MAX_X - 1; x++) {
					board[y][x] = 0;
				}
				
				for (int ty = y; ty > 0; ty--) {
					for (int tx = 1; tx < MAX_X - 1; tx++) {
						board[ty][tx] = board[ty - 1][tx];
						boardImage[ty][tx] = boardImage[ty - 1][tx - 1];
					}
				}
				
			}
		}
	}
	
	/**
	 * check filled over blocks
	 * @return if filled over, return true.
	 */
	public boolean isStacked() {
		for (int x = 1; x < MAX_X; x++) {
			if (board[0][x] == 1) {
				return true;
			}
		}
		return false;
	}

}
