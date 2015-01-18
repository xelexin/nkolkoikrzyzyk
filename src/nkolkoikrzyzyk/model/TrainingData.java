/**
 * 
 */
package nkolkoikrzyzyk.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;

/**
 * @author elohhim
 *
 */
public class TrainingData 
{
	public enum TrainingDataType
	{
		BEFORESTATES,
		AFTERSTATES
	};

	private String name;
	private float[][] inputs;
	private float[][] outputs;

	public TrainingData( String name, float[][] inputs, float[][] outputs)
	{
		this.name = name;
		this.inputs = inputs;
		this.outputs = outputs;
	}

	public TrainingData(File file)
	{
		this(null, null, null);
		this.loadFromFile(file);
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
		if( inputs.length > 0 && outputs.length > 0)
			return this.name + 
					"[i:" + this.inputs[0].length + 
					" o:" + this.outputs[0].length + 
					" s:" + this.inputs.length + "]";
		else
			return this.name + "[i:- o:- s:"+ this.inputs.length + "]";
	}

	public String getName() 
	{
		return name;
	}

	/**
	 * Serialize training data to a file
	 */
	public boolean saveToFile(File file)
	{
		try 
		{
			PrintWriter out = new PrintWriter(file);

			out.println(this.name);
			out.println(this.inputs.length);
			out.println(this.inputs[0].length);
			out.println(this.outputs[0].length);

			for (int i = 0; i<inputs.length; i++)
			{
				for( Float element : inputs[i] )
				{
					out.print(element);
					out.print(" ");
				}
				out.println("");
				for( Float element : outputs[i] )
				{
					out.print(element);
					out.print(" ");
				}
				out.println("");
			}
			out.close();
		}
		catch(IOException ex) 
		{
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Deserialize network from a file
	 */
	public boolean loadFromFile(File file)
	{
		try 
		{
			FileInputStream fin = new FileInputStream(file);
			Scanner scanner = new Scanner(fin);
			scanner.useLocale(Locale.ENGLISH); 	
			//read name
			this.name = scanner.nextLine();
			//read dataSize
			int dataSize = scanner.nextInt();
			System.out.println("Data size: " + dataSize);
			scanner.nextLine();
			//read input size
			int inputSize = scanner.nextInt();
			scanner.nextLine();
			//read output size
			int outputSize = scanner.nextInt();
			scanner.nextLine();
			
			this.inputs = new float[dataSize][inputSize];
			this.outputs = new float[dataSize][outputSize];
			for( int i = 0; i<dataSize; i++)
			{
				for( int j = 0; j<inputSize ; j++)
				{
					inputs[i][j] = scanner.nextFloat();
				}
				scanner.nextLine();
				for( int j = 0; j<outputSize; j++)
				{
					outputs[i][j] = scanner.nextFloat();
				}
				scanner.nextLine();
			}
			scanner.close();
		}
		catch(IOException ex) 
		{
			ex.printStackTrace();
			return false;
		}
		return true;
	}

}
