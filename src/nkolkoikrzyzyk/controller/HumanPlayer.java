package nkolkoikrzyzyk.controller;

import java.util.Stack;

import nkolkoikrzyzyk.model.Mark;

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
	public void youWin(Stack<int[]> history) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void youLost(Stack<int[]> history) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void youDraw(Stack<int[]> history)
	{
		// TODO Auto-generated method stub
	}

}
