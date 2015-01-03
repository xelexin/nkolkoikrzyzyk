/**
 * 
 */
package nkolkoikrzyzyk.model;

/**
 * @author Johhny
 *
 */
public abstract class Player 
{
	protected String name;
	protected Mark markType;
		
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
	
	public abstract int[] makeMove(int[] board, int position);
	public abstract void youWin(int[] board);
	public abstract void youLost();
}
