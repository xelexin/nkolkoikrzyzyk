/**
 * 
 */
package nkolkoikrzyzyk.model;

/**
 * @author elohhim
 *
 */
public class TrainingData 
{
	private final String name;
	private final float[][] inputs;
	private final float[][] outputs;
	
	public TrainingData( String name, float[][] inputs, float[][] outputs)
	{
		this.name = name;
		this.inputs = inputs;
		this.outputs = outputs;
	}
	
	public float[][] getInputs() {
		return inputs;
	}
	
	public float[][] getOutputs() {
		return outputs;
	}
	
	public float[] getInput( int idx )
	{
		return inputs[idx];
	}

	public float[] getOutput( int idx )
	{
		return outputs[idx];
	}

	@Override
	public String toString()
	{
		return this.name + 
				"[i:" + this.inputs[0].length + 
				" o:" + this.outputs[0].length + 
				" s:" + this.inputs.length + "]";
	}

	public String getName() 
	{
		return name;
	}
	
	
}
