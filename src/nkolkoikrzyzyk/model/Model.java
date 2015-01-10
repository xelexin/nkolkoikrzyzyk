/**
 * Model.java
 */
package nkolkoikrzyzyk.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import nkolkoikrzyzyk.events.ProgramEvent;

/**
 * @author elohhim
 *
 */
public class Model {

	private final BlockingQueue<ProgramEvent> blockingQueue;
	private final List<NeuralNetwork> networkList;
	private final List<TrainingData> trainingDataList;
	
	public List<NeuralNetwork> getNetworkList() {
		return networkList;
	}
	
	public Model( BlockingQueue<ProgramEvent> blockingQueue ) {
		this.blockingQueue = blockingQueue;
		this.networkList = new LinkedList<NeuralNetwork>();
		this.trainingDataList = new LinkedList<TrainingData>();
		//TEST
		NeuralNetwork mlp1 = new NeuralNetwork(9, new int[]{ 9, 9, 9  }, 2.0f);
		mlp1.getLayer(2).setIsSigmoid(false);
		networkList.add(mlp1);
		
		NeuralNetwork mlp2 = new NeuralNetwork(2, new int[]{ 3,3,3,3,3 }, 1.0f);
		networkList.add(mlp2);
		
		NeuralNetwork mlp3 = new NeuralNetwork(15, new int[]{ 10, 4 }, 1.0f); 
		mlp3.getLayer(1).setIsSigmoid(false);
		networkList.add(mlp3);
		
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
		
		trainingDataList.add(new TrainingData("Dane testowe", inputs, outputs));
		
		//TEST END
	}

	public void addNetwork(NeuralNetwork neuralNetwork) {
		this.networkList.add(neuralNetwork);
	}

	public NeuralNetwork[] getNetworkListModel()
	{
		NeuralNetwork[] array = new NeuralNetwork[networkList.size()];
		networkList.toArray(array);
		return array;
	}
	
	public TrainingData[] getTrainingDataListModel()
	{
		TrainingData[] array = new TrainingData[trainingDataList.size()];
		trainingDataList.toArray(array);
		return array;
	}
}
