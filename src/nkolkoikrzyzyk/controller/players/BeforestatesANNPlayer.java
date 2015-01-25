/**
 * 
 */
package nkolkoikrzyzyk.controller.players;

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
		return board;
	}

	@Override
	public String getMnemo() 
	{
		return "BANN";
	}
}
