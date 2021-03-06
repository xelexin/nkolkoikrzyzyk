package nkolkoikrzyzyk.model;

import java.util.Arrays;
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
		String dataName = name + "-ADATA";
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
		String dataName = name + "-BDATA";
		HashMap<Double, float[]> converted = convertMap();
		List<float[]> inputs = new LinkedList<float[]>();
		List<float[]> outputs = new LinkedList<float[]>();
		for( Map.Entry<Double, float[]> entry : converted.entrySet() )
		{
			int[] unhashed = GameModel.unhash(entry.getKey());
			float[] fUnhashed = GameModel.toFloat(unhashed);
			inputs.add(fUnhashed);
			outputs.add( Arrays.copyOf(entry.getValue(), entry.getValue().length) );
		}

		float[][] inputsArray = new float[inputs.size()][];
		inputs.toArray( inputsArray );
		float[][] outputsArray = new float[outputs.size()][];
		outputs.toArray( outputsArray );
		return new TrainingData(dataName, inputsArray, outputsArray);
	}
	
	private HashMap<Double, float[]> convertMap()
	{
		HashMap<Double, float[]> converted = new HashMap<Double, float[]>();
		for(Map.Entry<Double, Double> afterstateEntry : probabilityMap.entrySet())
		{
			Double afterstateHash = afterstateEntry.getKey();
			int[] afterstate = GameModel.unhash(afterstateHash);
			//System.out.println("#########Afterstate#########");
			//GameModel.printBoard(afterstate);
			
			//checking all possibilities of beforestates
			for( int i = 0; i<afterstate.length; i++)
			{
				if( afterstate[i] != 0 )
				{
					int[] tempBeforestate = Arrays.copyOf(afterstate, afterstate.length);
					tempBeforestate[i] = 0;
					int boardSum = 0;
					for( int value : tempBeforestate) boardSum+=value;
					//System.out.println(i + ". Beforestate - bsum: " + boardSum);
					//GameModel.printBoard(tempBeforestate);
					//if legal state
					if( (boardSum == 0 || boardSum == 1) && !GameModel.isWin(tempBeforestate) )
					{
						//System.out.println("Legal beforestate");
						
						double beforestateHash = GameModel.hash(tempBeforestate);
						float[] probabilities = converted.get(beforestateHash);
						if( probabilities == null)
						{
							probabilities = new float[afterstate.length];
							converted.put(beforestateHash, probabilities);
						}
						//System.out.println( "Probs for beforestate " + Arrays.toString(probabilities) );
						probabilities[i] = afterstateEntry.getValue().floatValue();
						//System.out.println( "Modified probs for beforestate " + Arrays.toString(probabilities) );
					}
					else
					{
						//System.out.println("Illegal beforestate");
						continue;
					}
				}
			}
		}
		return converted;
	}

	public TrainingData getBeforestatesTrainingData2()
	{
		String dataName = name + "-BDATA";
		List<float[]> inputs = new LinkedList<float[]>();
		List<float[]> outputs = new LinkedList<float[]>();
		
		// generate first move beforestate
		{
			float[] output = new float [9];
			float[] beforeState = new float[9];
	
			for (int i = 0; i<9; i++)
			{
				int[] afterState = new int[9];
				afterState[i] = 1;
				
				double hash = GameModel.hash(afterState);
				Double probability = probabilityMap.get(hash);  
				if( probability == null)
					continue;
				output[i] = probability.floatValue();
			}
		
			inputs.add(beforeState);
			outputs.add(output);
		}
	
		for( Map.Entry<Double, Double> entry : probabilityMap.entrySet() )
		{
			int emptyFields = 0;
			int[] iBeforeState = GameModel.unhash(entry.getKey());
			float[] beforeState = new float[iBeforeState.length];
			int[] emptyFieldsIdx = new int[9];
			int[] afterState = new int[9];
			for( int i = 0; i<iBeforeState.length; i++)
			{
				beforeState[i] = (float)iBeforeState[i];
				if (iBeforeState[i] == 0)
				{
					emptyFieldsIdx[emptyFields] = i;
					emptyFields++;
				}
			}

			if (emptyFields > 0 && emptyFields % 2 == 1)
			{
				float[] output = new float [9];

				for (int i = 0; i<emptyFields; i++)
				{
					System.arraycopy(iBeforeState, 0, afterState, 0, iBeforeState.length);
					afterState[emptyFieldsIdx[i]] = 1;

					double hash = GameModel.hash(afterState);
					Double probability = probabilityMap.get(hash);  
					if( probability == null)
						continue;
					output[emptyFieldsIdx[i]] = probability.floatValue();
				}
				
				inputs.add(beforeState);
				outputs.add(output);
			}
		}
		
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
