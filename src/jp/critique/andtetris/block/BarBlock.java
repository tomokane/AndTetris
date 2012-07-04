/**
 * 
 */
package jp.critique.andtetris.block;

import jp.critique.andtetris.Board;

/**
 * @author tomokane
 *
 */
public class BarBlock extends Block {

	/**
	 * @param board
	 */
	public BarBlock(Board board) {
		super(board);
        // □■□□
        // □■□□
        // □■□□
        // □■□□
        block[0][1] = 1;
        block[1][1] = 1;
        block[2][1] = 1;
        block[3][1] = 1;
        
        imageNo = Block.BAR;
	}

}
