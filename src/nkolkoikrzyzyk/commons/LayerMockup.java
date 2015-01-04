/**
 * 
 */
package nkolkoikrzyzyk.commons;

/**
 * @author elohhim
 *
 */
public class LayerMockup {
	
	public int outputSize;
	public int inputSize;
	public float[] weights;
	public boolean isSigmoid;
	
	public LayerMockup( int outputSize, int inputSize, float[] weights, boolean isSigmoid)
	{
		this.weights=weights;
		this.isSigmoid=isSigmoid;
		this.outputSize = outputSize;
		this.inputSize = inputSize;
	}
};