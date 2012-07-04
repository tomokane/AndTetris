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
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
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

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class MainActivity extends SimpleBaseGameActivity {
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
	private float centerX;
	private float centerY;

    private Thread gameLoop;
    
    private Board board;
    
    private Block block;
    private Block nextBlock;
    
    private Random rand;
	private Entity entity;
	private ArrayList<Entity> arrayList;
	private Line[] lineArray;
	private Rectangle[] rectArray;
    

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
					return getAssets().open("gfx/face_box.png");
				}
			});

			this.mTexture.load();
			this.mFaceTextureRegion = TextureRegionFactory.extractFromTexture(this.mTexture);
			
			rand = new Random();
			
//			block = createBlock(board);
			
		} catch (IOException e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onCreateScene() {
		
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
		
//		Sprite blockSprite = new Sprite(centerX, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
//		Sprite blockSprite2 = new Sprite(centerX, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		
		scene = new Scene();
		scene.setBackground(new Background(Color.BLACK));
		scene.setPosition(0, 32);
		
		board = new Board(scene, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		block = createBlock(board);
		block.draw(scene, mFaceTextureRegion, this.getVertexBufferObjectManager());
		entity = block.getEntity();
		
		lineArray = board.getLineArray();
		rectArray = block.getRectArray();
		
		scene.registerUpdateHandler(new IUpdateHandler() {
			
			private int posX = 0;

			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				posX++;
				entity.setX(posX);
				
				for (int j = 0; j < rectArray.length; j++) {
					for (int i = 0; i < lineArray.length; i++) {
						if (rectArray[j].collidesWith(lineArray[i])) {
							lineArray[i].setColor(Color.BLUE);
							break;
						} else {
							lineArray[i].setColor(Color.RED);
						}
					}
				}
				
				for (int j = 0; j < rectArray.length; j++) {
						if (rectArray[j].collidesWith(lineArray[lineArray.length - 1])) {
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

	
	private void repaint() {
		block.draw(scene, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
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

}
