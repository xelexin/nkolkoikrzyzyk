package nkolkoikrzyzyk.model;

public class HumanPlayer extends Player 
{	
	public HumanPlayer(String name, Mark markType) {
		super(name, markType);		
	}

	@Override
	public int[] makeMove( int[] board, int position) 
	{
		board[position]=markType.value();
		return board;
	}

	@Override
	public void youWin(int[] board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void youLost() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void youDraw(int[] board)
	{
		// TODO Auto-generated method stub
		
	}

}
