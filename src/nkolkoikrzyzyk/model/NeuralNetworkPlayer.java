package nkolkoikrzyzyk.model;

public class NeuralNetworkPlayer extends Player 
{
	public static neurons siecX = new neurons(0.5, Mark.CROSS.value());
	public static neurons siecO = new neurons(0.5, Mark.NOUGHT.value());
	
	private neurons siec;
	
	private NeuralNetwork network;
		
	public NeuralNetworkPlayer(String name, Mark markType, NeuralNetwork network) {
		super(name, markType);
		this.network = network;
		siec = markType==Mark.CROSS?siecX:siecO;
	}

	@Override
	public int[] makeMove( int[] board, int position ) {
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
