/**
 * 
 */
package nkolkoikrzyzyk.events;

/**
 * @author Johhny
 *
 */
public class NewNetworkEvent extends ProgramEvent
{
	public final String name;
	public final int inputSize;
	public final int[] layersSize;
	public final boolean[] isSigmoid;
	public final float variance;
	
	public NewNetworkEvent(String name, int inputSize, int[] layersSize, boolean[] isSigmoid, float variance)
	{
		this.name = name;
		this.inputSize = inputSize;
		this.layersSize = layersSize;
		this.isSigmoid = isSigmoid;
		this.variance = variance;
	}

}
