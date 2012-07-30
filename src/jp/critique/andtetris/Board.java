/**
 * 
 */
package jp.critique.andtetris;

import jp.critique.andtetris.block.Block;

import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.vbo.LowMemoryLineVertexBufferObject;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;

import android.graphics.Color;
import android.graphics.Point;

/**
 * @author tomokane
 *
 */
public class Board {
	
	public static final int MAX_X = 10;
	public static final int MAX_Y = 25;
	
	public static final int TILE_SIZE = 30;
	
	
	private int[][] board;
	private int[][] boardImage;
	
	private VertexBufferObjectManager vertexBufferObjectManager;
	private Scene scene;
	private Line[] horizontalLineArray;
	private Line[] verticalLineArray;
	public static Line leftLine;
	public static Line rightLine;
	public static Line bottomLine;
	
	public Line[] getHorizontalLineArray() {
		return horizontalLineArray;
	}

	public Line[] getVerticalLineArray() {
		return verticalLineArray;
	}

	/**
	 * Constructor
	 */
	public Board(Scene scene, VertexBufferObjectManager vertexBufferObjectManager) {
		board = new int[MAX_X][MAX_Y];
		boardImage = new int[MAX_X][MAX_Y];
		
		this.vertexBufferObjectManager = vertexBufferObjectManager;
		this.scene = scene;
		
		createBottomLine();
	}
	
	/**
	 * draw wall.
	 */
	private void createBottomLine() {
		
		bottomLine = new Line(0, AndrisActivity.CAMERA_HEIGHT - 1, AndrisActivity.CONTAINER_WIDTH, AndrisActivity.CAMERA_HEIGHT - 1, this.vertexBufferObjectManager);
		bottomLine.setColor(1, 0, 0);
		bottomLine.setAlpha(0);
		bottomLine.setLineWidth(3f);
		this.scene.attachChild(bottomLine);
	}
	
	
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
