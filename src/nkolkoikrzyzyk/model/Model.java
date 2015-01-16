/**
 * Model.java
 */
package nkolkoikrzyzyk.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import nkolkoikrzyzyk.controller.HumanPlayer;
import nkolkoikrzyzyk.controller.LookupTablePlayer;
import nkolkoikrzyzyk.events.NewGameEvent;
import nkolkoikrzyzyk.events.ProgramEvent;

/**
 * @author elohhim
 *
 */
public class Model {

	private final BlockingQueue<ProgramEvent> blockingQueue;
	private final List<NeuralNetwork> networkList;
	private final List<TrainingData> trainingDataList;
	private final List<LookupTable> lookupTableList;

	public List<NeuralNetwork> getNetworkList() 
	{
		return networkList;
	}

	public Model( BlockingQueue<ProgramEvent> blockingQueue ) 
	{
		this.blockingQueue = blockingQueue;
		this.networkList = new LinkedList<NeuralNetwork>();
		this.trainingDataList = new LinkedList<TrainingData>();
		this.lookupTableList = new LinkedList<LookupTable>();
		
		//TEST
		NeuralNetwork mlp3 = new NeuralNetwork("15dots ANN" ,15, new int[]{ 10, 4 }, 1.0f); 
		mlp3.getLayer(1).setIsSigmoid(false);
		networkList.add(mlp3);

		NeuralNetwork afterstates = new NeuralNetwork( "AfterstatesANN", 9, new int[]{27,81,27,9,1}, 1.0f);
		networkList.add(afterstates);
		// inputs
		float[][] inputs = new float[][]
				{
				new float[]{ 1, 1, 1,  1, 0, 1,  1, 0, 1,  1, 0, 1,  1, 1, 1 }, // 0
				new float[]{ 0, 0, 1,  0, 0, 1,  0, 0, 1,  0, 0, 1,  0, 0, 1 }, // 1
				new float[]{ 1, 1, 1,  0, 0, 1,  1, 1, 1,  1, 0, 0,  1, 1, 1 }, // 2
				new float[]{ 1, 1, 1,  0, 0, 1,  1, 1, 1,  0, 0, 1,  1, 1, 1 }, // 3
				new float[]{ 1, 0, 1,  1, 0, 1,  1, 1, 1,  0, 0, 1,  0, 0, 1 }, // 4
				new float[]{ 1, 1, 1,  1, 0, 0,  1, 1, 1,  0, 0, 1,  1, 1, 1 }, // 5
				new float[]{ 1, 1, 1,  1, 0, 0,  1, 1, 1,  1, 0, 1,  1, 1, 1 }, // 6
				new float[]{ 1, 1, 1,  0, 0, 1,  0, 0, 1,  0, 0, 1,  0, 0, 1 }, // 7
				new float[]{ 1, 1, 1,  1, 0, 1,  1, 1, 1,  1, 0, 1,  1, 1, 1 }, // 8
				new float[]{ 1, 1, 1,  1, 0, 1,  1, 1, 1,  0, 0, 1,  1, 1, 1 }, // 9
				};

		// outputs
		float[][] outputs = new float[][]
				{
				new float[]{ 0, 0, 0, 0 },
				new float[]{ 0, 0, 0, 1 },
				new float[]{ 0, 0, 1, 0 },
				new float[]{ 0, 0, 1, 1 },
				new float[]{ 0, 1, 0, 0 },
				new float[]{ 0, 1, 0, 1 },
				new float[]{ 0, 1, 1, 0 },
				new float[]{ 0, 1, 1, 1 },
				new float[]{ 1, 0, 0, 0 },
				new float[]{ 1, 0, 0, 1 },
				};

		trainingDataList.add(new TrainingData("15dots data set", inputs, outputs));

		//tworzenie tabel
		LookupTable tableX = new LookupTable("Table X");
		addLookupTable(tableX);
		LookupTable tableO = new LookupTable("Table O");
		addLookupTable(tableO);		
		//TEST END
	}

	public void addNetwork(NeuralNetwork neuralNetwork) 
	{
		this.networkList.add(neuralNetwork);
	}
	
	public void addTrainingData(TrainingData trainingData) 
	{
		this.trainingDataList.add(trainingData);
	}
	
	public void addLookupTable( LookupTable lookupTable)
	{
		this.lookupTableList.add(lookupTable);
	}
	

	public NeuralNetwork[] getNetworkListModel()
	{
		NeuralNetwork[] array = new NeuralNetwork[networkList.size()];
		networkList.toArray(array);
		return array;
	}

	public NeuralNetwork[] getFilteredNetworkListModel(int inputSize, int outputSize)
	{
		LinkedList<NeuralNetwork> filteredList = new LinkedList<NeuralNetwork>();
		Iterator<NeuralNetwork> iterator = networkList.iterator();
		while( iterator.hasNext())
		{
			NeuralNetwork element = iterator.next();
			System.out.println(element.getInputSize() + " " + element.getOutputSize());

			if ( element.getInputSize() == inputSize && element.getOutputSize() == outputSize)
			{
				filteredList.add(element);
			}
		}
		NeuralNetwork[] array = new NeuralNetwork[filteredList.size()];
		filteredList.toArray(array);
		return array;
	}

	public TrainingData[] getTrainingDataListModel()
	{
		TrainingData[] array = new TrainingData[trainingDataList.size()];
		trainingDataList.toArray(array);
		return array;
	}

	public LookupTable[] getLookupTableListModel()
	{
		LookupTable[] array = new LookupTable[lookupTableList.size()];
		lookupTableList.toArray(array);
		return array;
	}
}
