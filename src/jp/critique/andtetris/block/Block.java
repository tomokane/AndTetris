/**
 * 
 */
package jp.critique.andtetris.block;

import java.util.Random;

import jp.critique.andtetris.Board;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.graphics.Point;

/**
 * @author tomokane
 *
 */
public abstract class Block {
	
	public static final int MAX_Y = 4;
	public static final int MAX_X = 4;
	
	private static final int TILE_SIZE = Board.TILE_SIZE;
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	
	public static final int BAR = 0;
	public static final int Z_SHAPE = 1;
	public static final int SQUARE = 2;
	public static final int L_SHAPE = 3;
	public static final int REVERSE_Z_SHAPE = 4;
	public static final int T_SHAPE = 5;
	public static final int REVERSE_L_SHAPE = 6;
	public static final int WALL = 7;
	
	protected int[][] block;
	protected int imageNo;
	
	protected Point pos;
	
	protected Board board;
	
	private Entity blockGroup;
	private Entity entity;
	private Rectangle[] rectArray;

	public Rectangle[] getRectArray() {
		return rectArray;
	}

	public void setRectArray(Rectangle[] rectArray) {
		this.rectArray = rectArray;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * constructer
	 * @param board scene
	 */
	public Block(Board board) {
		block = new int[MAX_Y][MAX_X];
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				block[y][x] = 0;
			}
		}
		
		imageNo = 6;
		pos = new Point(4, 4);
		
		this.board = board;
		
	}
	
	public void draw(Scene s, ITextureRegion mFaceTextureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
		
		int gradientCounter = 0;
		
		rectArray = new Rectangle[4];
		
		entity = new Entity(pos.x * TILE_SIZE,pos.y * TILE_SIZE);
		
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				if (block[y][x] == 1) {
					rectArray[gradientCounter] = new Rectangle((x - 2) * TILE_SIZE,(y - 2) * TILE_SIZE
							,TILE_SIZE,TILE_SIZE, vertexBufferObjectManager);
					rectArray[gradientCounter].setColor(0,(float) (0.5 + 0.1 * gradientCounter),0);
					entity.attachChild(rectArray[gradientCounter]);
					gradientCounter++;
				}
			}
		}
		s.attachChild(entity);
	}
	
	/**
	 * move block to direction
	 * @param dir direction
	 * @return return true if fixed block
	 */
	public boolean move(int dir) {
		switch (dir) {
		case LEFT:
			Point newPos = new Point(pos.x - 1, pos.y);
			if(board.isMovable(newPos, block)) {
				pos = newPos;
			}
			break;
		case RIGHT:
			newPos = new Point(pos.x + 1, pos.y);
			if(board.isMovable(newPos, block)) {
				pos = newPos;
			} else {
				board.fixBlock(pos, block, imageNo);
				return true;
			}
			break;
		}
		return false;
	}
	
	/**
	 * rotate block
	 */
	public void turn() {
		int[][] turnedBlock = new int[MAX_Y][MAX_X];
		
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				turnedBlock[x][MAX_Y - 1 - y] = block[y][x];
			}
		}
		
		if (board.isMovable(pos, turnedBlock)) {
			block = turnedBlock;
		}
	}
}
