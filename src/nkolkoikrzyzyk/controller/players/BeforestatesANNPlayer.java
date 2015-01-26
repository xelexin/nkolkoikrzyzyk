/**
 * 
 */
package nkolkoikrzyzyk.controller.players;

import nkolkoikrzyzyk.model.GameModel;
import nkolkoikrzyzyk.model.Mark;
import nkolkoikrzyzyk.model.NeuralNetwork;

/**
 * @author elohhim
 *
 */
public class BeforestatesANNPlayer extends NeuralNetworkPlayer 
{
	public BeforestatesANNPlayer(String name, Mark markType,
			NeuralNetwork network) {
		super(name, markType, network);
	}

	@Override
	public int[] makeMove( int[] board, int position)
	{
		int bestMove = -1;
		if (network != null) 
		{	
			float[] input = GameModel.toFloat(board);
			float[] output = network.run(input);
			float bestProb = Float.NEGATIVE_INFINITY;
			for( int i = 0 ; i < output.length; i++)
			{
				if( output[i] > bestProb && board[i] == 0 )
				{
					bestProb = output[i];
					bestMove = i;
				}
			}
		}
		//TODO print
		//GameModel.printBoard(board);
		//System.out.println("Best move is:" + bestMove);
		//System.out.println("");
		if (bestMove == -1)
		{
			do 
			{
				bestMove = random.nextInt(9);
			} while (board[bestMove] == 0);
		}

		board[bestMove] = this.markType.value();
		return board;
	}

	@Override
	public String getMnemo() 
	{
		return "BANN";
	}
}
