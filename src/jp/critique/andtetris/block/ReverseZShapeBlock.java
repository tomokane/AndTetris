/**
 * 
 */
package jp.critique.andtetris.block;

import jp.critique.andtetris.Board;

/**
 * @author tomokane
 *
 */
public class ReverseZShapeBlock extends Block {

	/**
	 * @param board
	 */
	public ReverseZShapeBlock(Board board) {
		super(board);
        // □□□□
        // □■□□
        // □■■□
        // □□■□
        block[1][1] = 1;
        block[2][1] = 1;
        block[2][2] = 1;
        block[3][2] = 1;
        
        imageNo = Block.REVERSE_Z_SHAPE;
	}

}
