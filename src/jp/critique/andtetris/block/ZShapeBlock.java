/**
 * 
 */
package jp.critique.andtetris.block;

import jp.critique.andtetris.Board;

/**
 * @author tomokane
 *
 */
public class ZShapeBlock extends Block {

	/**
	 * @param board
	 */
	public ZShapeBlock(Board board) {
		super(board);
        // □□□□
        // □□■□
        // □■■□
        // □■□□
        block[1][2] = 1;
        block[2][1] = 1;
        block[2][2] = 1;
        block[3][1] = 1;
        
        imageNo = Block.Z_SHAPE;
	}

}
