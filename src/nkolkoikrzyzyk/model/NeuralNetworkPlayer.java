package nkolkoikrzyzyk.model;

public class NeuralNetworkPlayer extends Player 
{
	private neurons siec;
	private NeuralNetwork network;
		
	public NeuralNetworkPlayer(String name, Mark markType, NeuralNetwork network) {
		super(name, markType);
		this.network = network;
		siec = new neurons(0.5, markType.value());
	}

	@Override
	public int[] makeMove( int[] board, int position) {
		board = siec.outputSignal(board);
		return board;
	}

	@Override
	public void youWin(int[] board) {
		siec.youWin(board);
	}

	@Override
	public void youLost() {
		siec.youLost();
	}
}
