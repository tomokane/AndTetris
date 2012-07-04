/**
 * 
 */
package jp.critique.andtetris.block;

import jp.critique.andtetris.Board;

/**
 * @author tomokane
 *
 */
public class SquareBlock extends Block {

	/**
	 * @param board
	 */
	public SquareBlock(Board board) {
		super(board);
        // □□□□
        // □■■□
        // □■■□
        // □□□□
        block[1][1] = 1;
        block[1][2] = 1;
        block[2][1] = 1;
        block[2][2] = 1;
        
        imageNo = Block.SQUARE;
	}

}
