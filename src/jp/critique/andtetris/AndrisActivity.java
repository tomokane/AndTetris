package jp.critique.andtetris;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import jp.critique.andtetris.block.BarBlock;
import jp.critique.andtetris.block.Block;

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
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.entity.util.ScreenCapture;
import org.andengine.entity.util.ScreenCapture.IScreenCaptureCallback;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.FileUtils;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.util.Log;
import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga
 * 
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class AndrisActivity extends SimpleBaseGameActivity implements
        OnClickListener {
    // ===========================================================
    // Constants
    // ===========================================================
    
    private final class IUpdateHandlerImplementation implements IUpdateHandler {
        private final Scene tetrisContainer;
        
        private IUpdateHandlerImplementation(Scene tetrisContainer) {
            this.tetrisContainer = tetrisContainer;
        }
        
        @Override
        public void onUpdate(float pSecondsElapsed) {
            
            if (AndrisActivity.this.block != null) {
                Entity block = AndrisActivity.this.block.getEntity();
                boolean isCollide = false;
                for (int i = 0; i < block.getChildCount(); i++) {
                    // check collide fixed block
                    for (int j = 0; j < Block.fixedBlocks.size(); j++) {
                        if (Block.fixedBlocks.get(j).collidesWith(
                                (IShape) block.getChildByIndex(i))) {
                            isCollide = true;
                            break;
                        }
                    }
                    //
                    // check collide bottom line
                    if (Board.bottomLine.collidesWith((IShape) block
                            .getChildByIndex(i))) {
                        isCollide = true;
                        break;
                    }
                }
                
                Log.v(TAG, "block pos y : " + block.getY());
                
                // collision with block
                if (isCollide) {
                    // MainActivity.this.block.physicsHandler.setVelocity(0f);
                    for (int i = 0; i < block.getChildCount(); i++) {
                        Block.fixedBlocks.add((Rectangle) block
                                .getChildByIndex(i));
                    }
                    AndrisActivity.this.block = new BarBlock(
                                                             tetrisContainer,
                                                             getVertexBufferObjectManager());
                } else {
                    float posY = block.getY();
                    block.setY(posY + 10);
                }
                
            }
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        @Override
        public void reset() {
            // TODO Auto-generated method stub
            Log.v(TAG, "reset");
            
        }
    }

    private final String              TAG             = "MainActivity";
    
    public static final int           CAMERA_WIDTH    = 400;
    public static final int           CAMERA_HEIGHT   = 750;
    public static final int           CONTAINER_WIDTH = 300;
    
    // ===========================================================
    // Fields
    // ===========================================================
    
    private ITexture                  mTexture;
    private ITextureRegion            backgroundTextureRegion;
    private Scene                     scene;
    private Board                     board;
    
    private Block                     block;
    private Random                    rand;
    private Entity                    entity;
    private ArrayList<Entity>         arrayList;
    private ArrayList<Entity>         fixedBlocks;
    private Line[]                    horizontalLineArray;
    private Rectangle[]               rectArray;
    private float                     buttonSize      = 32f;
    private Line[]                    verticalLineArray;
    private VertexBufferObjectManager vertexBufferObjectManager;
    private BitmapTextureAtlas        backgroundBitmapTextureAtlas;
    private BitmapTextureAtlas        arrowRightBitmapTextureAtlas;
    public TextureRegion              arrowRightTextureRegion;
    private BitmapTextureAtlas        rotateBitmapTextureAtlas;
    private TextureRegion             rotateTextureRegion;
    private BitmapTextureAtlas        arrowLeftBitmapTextureAtlas;
    private TextureRegion             arrowLeftTextureRegion;
    
    // ===========================================================
    // Constructors
    // ===========================================================
    
    // ===========================================================
    // Getter & Setter
    // ===========================================================
    
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    
    /**
     * Setting Render engine option and screen options.
     * 
     * @return EngineOptions
     */
    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        
        return new EngineOptions(
                                 true,
                                 ScreenOrientation.PORTRAIT_FIXED,
                                 new RatioResolutionPolicy(
                                                           CAMERA_WIDTH,
                                                           CAMERA_HEIGHT),
                                 camera);
    }
    
    /**
     * Load images.
     * 
     */
    @Override
    public void onCreateResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        
        // Background image
        this.backgroundBitmapTextureAtlas = new BitmapTextureAtlas(
                                                                   this.getTextureManager(),
                                                                   300,
                                                                   750,
                                                                   TextureOptions.BILINEAR);
        this.backgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(this.backgroundBitmapTextureAtlas, this,
                        "bg.png", 0, 0);
        this.backgroundBitmapTextureAtlas.load();
        
        // Arrow right image
        this.arrowRightBitmapTextureAtlas = new BitmapTextureAtlas(
                                                                   this.getTextureManager(),
                                                                   120,
                                                                   120,
                                                                   TextureOptions.BILINEAR);
        this.arrowRightTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(this.arrowRightBitmapTextureAtlas, this,
                        "arrow-right.png", 0, 0);
        this.arrowRightBitmapTextureAtlas.load();
        
        // Arrow left image
        this.arrowLeftBitmapTextureAtlas = new BitmapTextureAtlas(
                                                                  this.getTextureManager(),
                                                                  120,
                                                                  120,
                                                                  TextureOptions.BILINEAR);
        this.arrowLeftTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(this.arrowLeftBitmapTextureAtlas, this,
                        "arrow-left.png", 0, 0);
        this.arrowLeftBitmapTextureAtlas.load();
        
        // Rotate button image
        this.rotateBitmapTextureAtlas = new BitmapTextureAtlas(
                                                               this.getTextureManager(),
                                                               120,
                                                               120,
                                                               TextureOptions.BILINEAR);
        this.rotateTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(this.rotateBitmapTextureAtlas, this,
                        "rotate.png", 0, 0);
        this.rotateBitmapTextureAtlas.load();
        
    }
    
    /**
     * initialize scene.
     * 
     * @return Scene
     */
    @Override
    public Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());
        
        // create scene.
        final Scene scene = new Scene();
        scene.setBackground(new Background(Color.BLACK));
        final Scene tetrisContainer = new Scene();
        tetrisContainer.setX(50);
        scene.setChildScene(tetrisContainer);
        
        // draw background image.
        final Sprite backgroundSprite = new Sprite(
                                                   0,
                                                   0,
                                                   backgroundTextureRegion,
                                                   this.getVertexBufferObjectManager());
        
        tetrisContainer.attachChild(backgroundSprite);
        
        // draw board container.
        board = new Board(tetrisContainer, this.getVertexBufferObjectManager());
        
        // draw first block.
        createBlock(tetrisContainer);
        
        // create game controllers(arrow key and rotate key).
        createGameControllers(tetrisContainer);
        
        // register loop function.
        tetrisContainer.registerUpdateHandler(new IUpdateHandlerImplementation(tetrisContainer));
        
        return scene;
    }
    
    /**
     * @param tetrisContainer
     */
    private void createGameControllers(final Scene tetrisContainer) {
        // create buttons
        final Sprite leftArrowSprite = new ButtonSprite(
                                                        -(this.arrowLeftTextureRegion
                                                                .getWidth() * 2) / 3,
                                                        AndrisActivity.CAMERA_HEIGHT
                                                                - this.arrowLeftTextureRegion
                                                                        .getWidth()
                                                                / 3 - 20,
                                                        this.arrowLeftTextureRegion,
                                                        this.getVertexBufferObjectManager(),
                                                        this);
        leftArrowSprite.setZIndex(Integer.MAX_VALUE);
        tetrisContainer.attachChild(leftArrowSprite);
        
        final Sprite rightArrowSprite = new ButtonSprite(
                                                         AndrisActivity.CONTAINER_WIDTH
                                                                 - (this.arrowRightTextureRegion.getWidth() * 1)
                                                                 / 3,
                                                         AndrisActivity.CAMERA_HEIGHT
                                                                 - this.arrowRightTextureRegion
                                                                         .getWidth()
                                                                 / 3 - 20,
                                                         this.arrowRightTextureRegion,
                                                         this.getVertexBufferObjectManager(),
                                                         this);
        rightArrowSprite.setZIndex(Integer.MAX_VALUE);
        tetrisContainer.attachChild(rightArrowSprite);
        
        final Sprite rotateArrowSprite = new ButtonSprite(
                                                          AndrisActivity.CONTAINER_WIDTH
                                                                  / 2
                                                                  - this.rotateTextureRegion.getWidth()
                                                                  / 2,
                                                          AndrisActivity.CAMERA_HEIGHT
                                                                  - (this.rotateTextureRegion
                                                                          .getWidth())
                                                                  / 3 - 10,
                                                          this.rotateTextureRegion,
                                                          this.getVertexBufferObjectManager(),
                                                          this);
        rotateArrowSprite.setZIndex(Integer.MAX_VALUE);
        tetrisContainer.attachChild(rotateArrowSprite);
        
        tetrisContainer.registerTouchArea(leftArrowSprite);
        tetrisContainer.registerTouchArea(rightArrowSprite);
        tetrisContainer.registerTouchArea(rotateArrowSprite);
        
        tetrisContainer.setTouchAreaBindingOnActionDownEnabled(true);
    }
    
    /**
     * @param scene
     */
    public void createBlock(final Scene scene) {
        block = new BarBlock(scene, this.getVertexBufferObjectManager());
    }
    
    /**
     * create block
     * 
     * @param reference
     *            board
     * @return block
     */
    public Block createBlock(Board board) {
        int blockNo = rand.nextInt(7);
        
        blockNo = Block.BAR;
        // switch (blockNo) {
        // case Block.BAR:
        // return new BarBlock(board);
        // case Block.Z_SHAPE:
        // return new ZShapeBlock(board);
        // case Block.SQUARE:
        // return new SquareBlock(board);
        // case Block.L_SHAPE:
        // return new LShapeBlock(board);
        // case Block.REVERSE_Z_SHAPE:
        // return new ReverseZShapeBlock(board);
        // case Block.T_SHAPE:
        // return new TShapeBlock(board);
        // case Block.REVERSE_L_SHAPE:
        // return new ReverseLShapeBlock(board);
        // }
        
        return null;
    }
    
    private void moveBlock(final String direction) {
        runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                final Entity block = AndrisActivity.this.block.getEntity();
                // block.clearEntityModifiers();
                
                final float x = block.getX();
                final float y = block.getY();
                final float degree = block.getRotation();
                if (direction == "right")
                    block.registerEntityModifier(new MoveModifier(0.3f, x, x
                            + Board.TILE_SIZE, y, y));
                else if (direction == "left")
                    block.registerEntityModifier(new MoveModifier(0.3f, x, x
                            - Board.TILE_SIZE, y, y));
                else if (direction == "rotate")
                    block.registerEntityModifier(new RotationModifier(
                                                                      0.3f,
                                                                      degree,
                                                                      degree + 90));
            }
        });
    }
    
    @Override
    public void onClick(final ButtonSprite pButtonSprite,
            float pTouchAreaLocalX, float pTouchAreaLocalY) {
        runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                Log.v(TAG, String.valueOf(pButtonSprite.getY()));
                if (pButtonSprite.getX() > AndrisActivity.CONTAINER_WIDTH
                        - AndrisActivity.this.arrowRightTextureRegion
                                .getWidth()) {
                    AndrisActivity.this.moveBlock("right");
                } else if (pButtonSprite.getX() < 0) {
                    AndrisActivity.this.moveBlock("left");
                } else {
                    AndrisActivity.this.moveBlock("rotate");
                }
            }
        });
        
    }
    
}
