package jp.critique.andtetris;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import jp.critique.andtetris.block.BarBlock;
import jp.critique.andtetris.block.Block;
import jp.critique.andtetris.block.LShapeBlock;
import jp.critique.andtetris.block.ReverseLShapeBlock;
import jp.critique.andtetris.block.ReverseZShapeBlock;
import jp.critique.andtetris.block.SquareBlock;
import jp.critique.andtetris.block.TShapeBlock;
import jp.critique.andtetris.block.ZShapeBlock;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.util.Log;
import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class MainActivity extends SimpleBaseGameActivity implements OnClickListener{
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int CAMERA_WIDTH = 416;
	public static final int CAMERA_HEIGHT = 256;

	// ===========================================================
	// Fields
	// ===========================================================

	private ITexture mTexture;
	private ITextureRegion mFaceTextureRegion;
	private Scene scene;
	private Board board;
    
    private Block block;
    private Random rand;
	private Entity entity;
	private ArrayList<Entity> arrayList;
	private ArrayList<Entity> fixedBlocks;
	private Line[] horizontalLineArray;
	private Rectangle[] rectArray;
	private float buttonSize = 32f;
	private Line[] verticalLineArray;
	
	
    

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		try {
			this.mTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/tinybutton.png");
				}
			});

			this.mTexture.load();
			this.mFaceTextureRegion = TextureRegionFactory.extractFromTexture(this.mTexture);
			
			rand = new Random();
			
		} catch (IOException e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onCreateScene() {
		
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		createScene();
		
		board = new Board(scene, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		block = createBlock(board);
		block.draw(scene, mFaceTextureRegion, this.getVertexBufferObjectManager());
		entity = block.getEntity();
		
		createButtons();
		
		horizontalLineArray = board.getHorizontalLineArray();
		verticalLineArray = board.getVerticalLineArray();
		rectArray = block.getRectArray();
		
		scene.registerUpdateHandler(new IUpdateHandler() {
			
			private int posX = 0;

			@Override
			public void reset() {}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				posX++;
				entity.setX(posX);
				
				
				// check collision current block
				for (int j = 0; j < rectArray.length; j++) {
					for (int i = 0; i < horizontalLineArray.length; i++) {
						if (rectArray[j].collidesWith(horizontalLineArray[i])) {
							horizontalLineArray[i].setColor(Color.BLUE);
							break;
						} else {
							horizontalLineArray[i].setColor(Color.RED);
						}
					}
					
					for (int j2 = 0; j2 < verticalLineArray.length; j2++) {
						if (rectArray[j].collidesWith(verticalLineArray[j2])) {
							verticalLineArray[j2].setColor(Color.BLUE);
							break;
						} else {
							verticalLineArray[j2].setColor(Color.RED);
						}
						
					}
				}
				
				for (int j = 0; j < rectArray.length; j++) {
						if (rectArray[j].collidesWith(horizontalLineArray[horizontalLineArray.length - 1])) {
							arrayList.add(entity);
							block = createBlock(board);
							block.draw(scene, mFaceTextureRegion, MainActivity.this.getVertexBufferObjectManager());
							rectArray = block.getRectArray();
							entity = block.getEntity();
							posX = 0;
							break;
						}
				}
				
				
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO: handle exception
				}
				
			}
		});

		return scene;
	}

	/**
	 * 
	 */
	private void createButtons() {
		final Sprite leftButton = new ButtonSprite(CAMERA_WIDTH-100, -buttonSize, this.mFaceTextureRegion, this.getVertexBufferObjectManager(), this);
		leftButton.setRotation(270f);
		
		final Sprite rightButton = new ButtonSprite(CAMERA_WIDTH-100, CAMERA_HEIGHT - 60, this.mFaceTextureRegion, this.getVertexBufferObjectManager(), this);
		rightButton.setRotation(270f);
		
		final Sprite rotateButton = new ButtonSprite(CAMERA_WIDTH-20, CAMERA_HEIGHT / 2 - 80, this.mFaceTextureRegion, this.getVertexBufferObjectManager(), this);
		rotateButton.setRotation(270f);
		
		scene.registerTouchArea(leftButton);
		scene.registerTouchArea(rightButton);
		scene.registerTouchArea(rotateButton);
		
		scene.attachChild(leftButton);
		scene.attachChild(rightButton);
		scene.attachChild(rotateButton);
		scene.setTouchAreaBindingOnActionDownEnabled(true);
	}

	/**
	 * 
	 */
	private void createScene() {
		scene = new Scene();
		scene.setBackground(new Background(Color.BLACK));
		scene.setPosition(0, 32);
	}

	
	/**
	 * create block
	 * @param reference board
	 * @return block
	 */
	public Block createBlock(Board board) {
		int blockNo = rand.nextInt(7);
		switch (blockNo) {
		case Block.BAR:
			return new BarBlock(board);
		case Block.Z_SHAPE:
			return new ZShapeBlock(board);
		case Block.SQUARE:
			return new SquareBlock(board);
		case Block.L_SHAPE:
			return new LShapeBlock(board);
		case Block.REVERSE_Z_SHAPE:
			return new ReverseZShapeBlock(board);
		case Block.T_SHAPE:
			return new TShapeBlock(board);
		case Block.REVERSE_L_SHAPE:
			return new ReverseLShapeBlock(board);
		}
		
		return null;
	}
	
	private void moveBlock(final String direction) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				final Entity block = MainActivity.this.entity;
				block.clearEntityModifiers();
				
				final float x = block.getX();
				final float y = block.getY();
				final float degree = block.getRotation();
				if(direction == "right")
					block.registerEntityModifier(new MoveModifier(0.3f, x, x, y, y - Board.TILE_SIZE));
				else if(direction == "left")
					block.registerEntityModifier(new MoveModifier(0.3f, x, x, y, y + Board.TILE_SIZE));
				else if(direction == "rotate")
					block.registerEntityModifier(new RotationModifier(0.3f, degree, degree + 90));
			}
		});
	}

	final private String TAG = "MainActivity.onClick";
	@Override
	public void onClick(final ButtonSprite pButtonSprite, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Log.v(TAG,String.valueOf(pButtonSprite.getY()));
				if(pButtonSprite.getY()>CAMERA_HEIGHT - 61) {
					MainActivity.this.moveBlock("left");
				}else if(pButtonSprite.getY()<0) {
					MainActivity.this.moveBlock("right");
				}else {
					MainActivity.this.moveBlock("rotate");
				}
			}
		});
		
	}
	

}
