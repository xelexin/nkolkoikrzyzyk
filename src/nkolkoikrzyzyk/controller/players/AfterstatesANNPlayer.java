/**
 * 
 */
package nkolkoikrzyzyk.controller.players;

import java.util.Arrays;

import nkolkoikrzyzyk.model.GameModel;
import nkolkoikrzyzyk.model.Mark;
import nkolkoikrzyzyk.model.NeuralNetwork;

/**
 * @author elohhim
 *
 */
public class AfterstatesANNPlayer extends NeuralNetworkPlayer 
{
	public AfterstatesANNPlayer(String name, Mark markType,
			NeuralNetwork network) 
	{
		super(name, markType, network);
	}

	@Override
	public int[] makeMove( int[] board, int position)
	{
		int bestMove = -1;

		if (network != null) 
		{	
			//sprawdzanie wszystkich afterstates
			float bestProbability = Float.NEGATIVE_INFINITY;
			for( int i = 0; i < 9; i++)
			{
				int[] temp = Arrays.copyOf(board, board.length);
				if( board[i] == 0 )
				{
					temp[i] = markType.value();
					System.out.println("Testing");
					GameModel.printBoard(temp);
					float[] input = GameModel.toFloat(temp);
					float[] output = network.run(input);
					System.out.println("Network output: " + output[0] + " Current best probability: " + bestProbability);
					if( output[0] > bestProbability )
					{
						bestMove = i;
						bestProbability = output[0];
						System.out.println("Now best move is: " + bestMove);
					}
				}
			}
		} 
		else
		{
			System.out.println("network == null, doing random move...");
		}

		if (bestMove == -1)
		{
			do 
			{
				bestMove = random.nextInt(9);
			} while (board[bestMove] == 0);
		}
			
		System.out.println("Afterstates ANN player selected " + bestMove + " as a best move");
		board[bestMove] = markType.value();
		return board;
	}
}
