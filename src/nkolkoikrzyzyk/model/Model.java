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
	private List<NeuralNetwork> networkList;
	
	public List<NeuralNetwork> getNetworkList() {
		return networkList;
	}
	
	public Model( BlockingQueue<ProgramEvent> blockingQueue ) {
		this.blockingQueue = blockingQueue;
		this.networkList = new LinkedList<NeuralNetwork>();
		NeuralNetwork mlp1 = new NeuralNetwork();
		mlp1.init(9, new int[]{ 9, 9, 9  }, 2.0f);
		mlp1.getLayer(2).setIsSigmoid(false);
		networkList.add(mlp1);
		
		NeuralNetwork mlp2 = new NeuralNetwork();
		mlp2.init(2, new int[]{ 3,3,3,3,3 }, 1.0f);
		networkList.add(mlp2);
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
}
