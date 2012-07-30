/**
 * 
 */
package jp.critique.andtetris.block;

import java.util.ArrayList;
import java.util.Random;

import jp.critique.andtetris.Board;
import jp.critique.andtetris.AndrisActivity;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
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
	
	public static final int ZERO_DEGREE = 0;
	public static final int NINTY_DEGREE = 1;
	public static final int ONEEIGHTY_DEGREE = 2;
	public static final int TWOSEVENTY_DEGREE = 3;
	
	public static ArrayList<Rectangle> fixedBlocks = new ArrayList<Rectangle>();
	
	protected int[][] block = new int[4][4];
	protected int imageNo;
	
	protected Point pos;
	
	protected Board board;
	
	private Entity blockGroup;
	private Entity entity;
	private Rectangle[] rectArray;
	private Rectangle[] rectangleGroup;
	public MoveYModifier moveYModifier;
	public PhysicsHandler physicsHandler;
	private VertexBufferObjectManager vertexBufferObjectManager;
	public int[][] blockArray;
	public static final float START_POINT = 1.5f * 30;

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
	public Block(Scene s, VertexBufferObjectManager vertexBufferObjectManager) {
		rectangleGroup = new Rectangle[4];
		this.vertexBufferObjectManager = vertexBufferObjectManager;
		entity = new Entity(165f,START_POINT);
//		physicsHandler = new PhysicsHandler(entity);
//		entity.registerUpdateHandler(physicsHandler);
//		
//		physicsHandler.setVelocityY(30);
		
		s.attachChild(entity);
	}
	
	protected void createBlocks() {
		int i = 0;
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				if (block[y][x] == 1 && i < rectangleGroup.length) {
					rectangleGroup[i] = new Rectangle(-15,(i - 1.5f ) * 30,30,30, this.vertexBufferObjectManager);
					rectangleGroup[i].setColor(0.2f + (0.1f * i),0,0);
					rectangleGroup[i].setZIndex(1);
					entity.attachChild(rectangleGroup[i]);
					i++;
				}
			}
		}
	}
	
}
