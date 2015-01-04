/**
 * 
 */
package nkolkoikrzyzyk.events;

import java.io.File;

import nkolkoikrzyzyk.model.NeuralNetwork;

/**
 * @author elohhim
 *
 */
public class SaveNetworkEvent extends ProgramEvent 
{
	public File file;
	public NeuralNetwork network;
	

	public SaveNetworkEvent(File file, NeuralNetwork network) 
	{
		this.file = file;
		this.network = network;
	}
}
