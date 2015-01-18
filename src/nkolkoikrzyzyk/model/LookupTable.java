package nkolkoikrzyzyk.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class LookupTable 
{
	private static int counter = 1; // is one ahead
	private String name;
	private double alpha=0.5;
	private final Map<Double, Double> probabilityMap;

	public LookupTable()
	{
		this( "Table " + counter );		
	}

	public LookupTable( String name )
	{
		if( name != null)
			this.name = name;
		else
			this.name = "Table " + counter;

		counter++;
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
	}

	public double get( int[] board)
	{
		double hash = GameModel.hash(board);
		Double probability = probabilityMap.get(hash);  
		if( probability == null)
		{
			probability = 0.5;
			probabilityMap.put(hash, probability);	
		}
		return probability;
	}

	public TrainingData getAfterstateTrainingData()
	{
		String dataName = name + " data";
		List<float[]> inputs = new LinkedList<float[]>();
		List<float[]> outputs = new LinkedList<float[]>();

		for( Map.Entry<Double, Double> entry : probabilityMap.entrySet() )
		{
			int[] unhashed = GameModel.unhash(entry.getKey());
			float[] fUnhashed = new float[unhashed.length]; 
			for( int i = 0; i<unhashed.length; i++)
			{
				fUnhashed[i] = (float)unhashed[i];
			}
			inputs.add(fUnhashed);
			outputs.add( new float[]{ entry.getValue().floatValue() } );
		}

		float[][] inputsArray = new float[inputs.size()][];
		inputs.toArray( inputsArray );
		float[][] outputsArray = new float[outputs.size()][];
		outputs.toArray( outputsArray );

		return new TrainingData(dataName, inputsArray, outputsArray);
	}

	public TrainingData getBeforestatesTrainingData()
	{
		String dataName = name + " data";
		List<float[]> inputs = new LinkedList<float[]>();
		List<float[]> outputs = new LinkedList<float[]>();
		
		//TODO: wypelnic beforestates
		
		float[][] inputsArray = new float[inputs.size()][];
		inputs.toArray( inputsArray );
		float[][] outputsArray = new float[outputs.size()][];
		outputs.toArray( outputsArray );
		return new TrainingData(dataName, inputsArray, outputsArray);
	}

	@Override
	public String toString()
	{
		return name + " [" + probabilityMap.size() + "]";
	}
}
