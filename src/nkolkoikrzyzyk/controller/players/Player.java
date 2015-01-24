/**
 * 
 */
package nkolkoikrzyzyk.controller.players;

import java.util.Stack;

import nkolkoikrzyzyk.model.Mark;

/**
 * @author Johhny
 *
 */
public abstract class Player 
{
	protected String name;
	protected Mark markType;
	
	protected long winCounter = 0;
	protected long drawCounter = 0;
	protected long lostCounter = 0;
		
	public Player(String name, Mark markType) {
		this.name = name;
		this.markType = markType;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public Mark getMarkType() {
		return markType;
	}
	
	/**
	 * Make move
	 * @param board		Current board state.
	 * @param position	Where mark is being put? (used only by Human player)
	 * @return			Board state after executing a move.
	 */
	public abstract int[] makeMove(int[] board, int position);
	
	public void youWin(Stack<int[]> history)
	{
		winCounter++;
	}
	
	public void youDraw(Stack<int[]> history)
	{
		drawCounter++;
	}
	
	public void youLost(Stack<int[]> history)
	{
		lostCounter++;
	}

	public final long getWinCounter()
	{
		return winCounter;
	}

	public final long getDrawCounter()
	{
		return drawCounter;
	}

	public final long getLostCounter()
	{
		return lostCounter;
	}

	public final void resetCounters()
	{
		winCounter=0;
		drawCounter=0;
		lostCounter=0;
	}

	
}
