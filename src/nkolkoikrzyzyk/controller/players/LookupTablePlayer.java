/**
 * 
 */
package nkolkoikrzyzyk.controller.players;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import nkolkoikrzyzyk.model.LookupTable;
import nkolkoikrzyzyk.model.Mark;

/**
 * @author elohhim
 *
 */
public class LookupTablePlayer extends Player 
{
	private static final double RANDOM_PROBABILITY = 0.15; 
	
	private LookupTable lookupTable;
	private static Random random = new Random(System.nanoTime());
	private boolean trainingInProgress = false;
	
	public LookupTablePlayer(String name, Mark markType, LookupTable lookupTable) 
	{
		super(name, markType);
		this.lookupTable = lookupTable;
	}

	/* (non-Javadoc)
	 * @see nkolkoikrzyzyk.controller.Player#makeMove(int[], int)
	 */
	@Override
	public int[] makeMove(int[] board, int position) 
	{
		int choice;
		if( random.nextDouble() < RANDOM_PROBABILITY && trainingInProgress)
		{
			choice = makeExploratoryMove(board);
		}
		else
		{
			choice = makeNormalMove(board);
		}
		board[choice] = markType.value();
		return board;
	}

	private int makeExploratoryMove(int[] board) {
		int choice;
		//Exploratory move
		choice = random.nextInt(9);
		while(board[choice]!=0)
		{
			choice=random.nextInt(9);
		}
		//TODO: print
		//System.out.println(name + " selected " + choice + " as a exploratory move");
		return choice;
	}

	private int makeNormalMove(int[] board) {
		//Normal move
		int choice = -1;
		double choiceP = 0.0;
		for( int i = 0; i<9 ; i++)
		{
			int[] temp = Arrays.copyOf(board, board.length);
			if( board[i] == 0 )
			{
				temp[i] = markType.value();
				Double tempP = lookupTable.get(temp);
				//TODO: print
//				System.out.print(tempP + "\t");
				if( tempP >= choiceP)
				{
					choice = i;
					choiceP = tempP; 
				}
			}
			else
			{
				//TODO: print
				//System.out.print("_\t");
			}
		}
		//TODO: print
		//System.out.println("");
		//System.out.println(name + " selected " + choice + " as a best move");
		return choice;
	}

	/* (non-Javadoc)
	 * @see nkolkoikrzyzyk.controller.Player#youWin(java.util.Stack)
	 */
	@Override
	public void youWin(Stack<int[]> historyStack) 
	{
		super.youWin(historyStack);
		if(trainingInProgress) lookupTable.updateProcedure(historyStack, 1.0);
	}

	/* (non-Javadoc)
	 * @see nkolkoikrzyzyk.controller.Player#youDraw(java.util.Stack)
	 */
	@Override
	public void youDraw(Stack<int[]> historyStack) 
	{
		super.youDraw(historyStack);
		if(trainingInProgress) lookupTable.updateProcedure(historyStack, 0.7);
	}

	/* (non-Javadoc)
	 * @see nkolkoikrzyzyk.controller.Player#youLost(java.util.Stack)
	 */
	@Override
	public void youLost(Stack<int[]> historyStack) 
	{
		super.youLost(historyStack);
		if(trainingInProgress) lookupTable.updateProcedure(historyStack, 0.0);
	}

	public boolean isTrainingInProgress() 
	{
		return trainingInProgress;
	}

	public void setTrainingInProgress(boolean trainingInProgress) {
		this.trainingInProgress = trainingInProgress;
	}

	public LookupTable getLookupTable() 
	{
		return this.lookupTable;
	}
	
	@Override
	public String getMnemo()
	{
		return "LUT";
	}
}
