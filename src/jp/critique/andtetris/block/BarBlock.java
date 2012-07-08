/**
 * 
 */
package jp.critique.andtetris.block;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import jp.critique.andtetris.Board;

/**
 * @author tomokane
 *
 */
public class BarBlock extends Block {

	/**
	 * @param board
	 * @param vertexBufferObjectManager 
	 * @param mFaceTextureRegion 
	 * @param scene 
	 */
	public BarBlock(Scene s, VertexBufferObjectManager vertexBufferObjectManager) {
		super(s, vertexBufferObjectManager);
        // □■□□
        // □■□□
        // □■□□
        // □■□□
        block[0][1] = 1;
        block[1][1] = 1;
        block[2][1] = 1;
        block[3][1] = 1;
        
        this.createBlocks();
//        
//        imageNo = Block.BAR;
	}

}
