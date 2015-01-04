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
	//TODO: testowe
	public NeuralNetwork testowa;
	
	public List<NeuralNetwork> getNetworkList() {
		return networkList;
	}
	
	public Model( BlockingQueue<ProgramEvent> blockingQueue ) {
		this.blockingQueue = blockingQueue;
		this.networkList = new LinkedList<NeuralNetwork>();
		NeuralNetwork mlp = new NeuralNetwork();
		mlp.init(9, new int[]{ 3, 3, 9 });
		networkList.add(mlp);
		testowa = mlp;
	}

	public void addNetwork(NeuralNetwork neuralNetwork) {
		this.networkList.add(neuralNetwork);
	}
}
