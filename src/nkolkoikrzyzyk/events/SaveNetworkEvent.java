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
	public final File file;
	public final NeuralNetwork network;
	

	public SaveNetworkEvent(File file, NeuralNetwork network) 
	{
		this.file = file;
		this.network = network;
	}
}
