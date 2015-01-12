package nkolkoikrzyzyk.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class LookupTable 
{
	private double alpha=0.5;
	
	private final Map<Double, Double> probabilityMap;

	public LookupTable()
	{
		this.probabilityMap = new HashMap<Double, Double>();
	}

	/*
	 * c - current
	 * p - previous
	 * As - Afterstate
	 * P_ - probability of
	 * H - hash
	 */
	public void updateProcedure( Stack<int[]> historyStack, double reward )
	{
		int[] cAs = historyStack.pop();
		double cAsH = GameModel.hash(cAs);
		probabilityMap.put(cAsH, reward);
		
		while( !historyStack.isEmpty() )
		{
			int[] pAs = historyStack.pop();
			double pAsH = GameModel.hash(pAs);
			
			if( probabilityMap.get(pAsH) == null )
			{
				probabilityMap.put(pAsH, 0.5);
			}
			
			double P_pAs = probabilityMap.get(pAsH);
			double P_cAs = probabilityMap.get(cAsH);
			
			P_pAs = P_pAs + alpha*(P_cAs - P_pAs);
			probabilityMap.put(pAsH, P_pAs);	
			cAs = pAs;
		}
		//TODO trzeba to dopracować, jak ta alpha powinna się zmieniać
		alpha*=0.9999;
		System.out.println("Number of unique afterstates in table: " + probabilityMap.size());
	}
	
	public double get( int[] board)
	{
		Double probability = probabilityMap.get(GameModel.hash(board));  
		if( probability == null)
			probability = 0.5;
		return probability;
	}
}
