package nkolkoikrzyzyk.model;

import java.util.Scanner;

public class HumanPlayer extends Player 
{
	private Scanner reader;
	
	public HumanPlayer(String name, Mark markType) {
		super(name, markType);
		this.reader = new Scanner(System.in);
		
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

}
