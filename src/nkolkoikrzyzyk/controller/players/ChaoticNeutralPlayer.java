package nkolkoikrzyzyk.controller.players;

import java.util.Random;
import java.util.Stack;

import nkolkoikrzyzyk.model.Mark;

public class ChaoticNeutralPlayer extends Player
{
	private Random rand = new Random(System.currentTimeMillis());
	
	public ChaoticNeutralPlayer(String name, Mark markType)
	{
		super(name, markType);
	}

	@Override
	public int[] makeMove(int[] board, int position)
	{
		int input;
		input = rand.nextInt(9);
		while(board[input]!=0)
		{
			input=rand.nextInt(9);
		}
		board[input] = markType.value();
		return board;
	}
}
