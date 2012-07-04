/**
 * 
 */
package jp.critique.andtetris.block;

import jp.critique.andtetris.Board;

/**
 * @author tomokane
 *
 */
public class ReverseLShapeBlock extends Block {

	/**
	 * @param board
	 */
	public ReverseLShapeBlock(Board board) {
		super(board);
        // □□□□
        // □■■□
        // □■□□
        // □■□□
        block[1][1] = 1;
        block[1][2] = 1;
        block[2][1] = 1;
        block[3][1] = 1;
        
        imageNo = Block.REVERSE_L_SHAPE;
	}

}
