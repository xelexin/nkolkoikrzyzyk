package nkolkoikrzyzyk.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import nkolkoikrzyzyk.commons.NeuralNetworkMockup;
import nkolkoikrzyzyk.commons.NeuralNetworkMockup.LayerMockup;

public class NeuralNetwork
{
	/*
	 * Neural network layer
	 */
	public static class Layer
	{
		float[] output;
		float[] input;
		float[] weights;
		
		public float[] getWeights() {
			return weights;
		}

		public void setWeights(float[] weights) {
			this.weights = weights;
		}

		public boolean isSigmoid() {
			return isSigmoid;
		}

		public void setSigmoid(boolean isSigmoid) {
			this.isSigmoid = isSigmoid;
		}

		float[] dweights; // used for learning
		boolean isSigmoid = true; // apply sigmoid function for the neuron outputs?

		public Layer(int inputSize, int outputSize)
		{
			output = new float[outputSize];
			input = new float[inputSize];
			weights = new float[(inputSize + 1) * outputSize];
			dweights = new float[weights.length];
		}
	
		public void initWeights(Random r)
		{
			for (int i = 0; i < weights.length; i++)
				weights[i] = (float)r.nextGaussian() * 2.0f;
		}

		public float[] run(float[] in)
		{
			System.arraycopy(in, 0, input, 0, in.length);
			int offs = 0;
			for (int i = 0; i < output.length; i++)
			{
				float val = weights[offs + input.length];
				for (int j = 0; j < input.length; j++)
					val += weights[offs + j] * input[j];
				if (isSigmoid)
					val = (float)(1.0f / (1.0f + Math.exp(-val)));

				output[i] = val;
				offs += input.length + 1;
			}
			return Arrays.copyOf(output, output.length);
		}

		public float[] train(float[] error, float learningRate, float momentum)
		{
			int offs = 0;
			float[] nextError = new float[input.length + 1];
			for (int i = 0; i < output.length; i++)
			{
				float d = error[i];
				if (isSigmoid)
					d *= output[i] * (1.0f - output[i]);

				for (int j = 0; j < input.length; j++)
				{
					int idx = offs + j;
					nextError[j] += weights[idx] * d;
					float dw = input[j] * d * learningRate;
					weights[idx] += dweights[idx] * momentum + dw;
					dweights[idx] = dw;
				}

				{
					int idx = offs + input.length;
					nextError[input.length] += weights[idx] * d;
					float dw = d * learningRate;
					weights[idx] += dweights[idx] * momentum + dw;
					dweights[idx] = dw;
				}

				offs += input.length + 1;
			}
			return nextError;
		}
	}

	Layer[] layers = null;
	private String name;

	/**
	 * Create multi-layer neural network
	 * @param inputSize		Number of inputs
	 * @param layersSize	Number of neurons in each layer
	 * 						(last one is output layer, so it can't be empty)
	 */
	public void init(int inputSize, int[] layersSize)
	{
		layers = new Layer[layersSize.length];
		Random r = new Random(1234);
		int prevLayerSize = inputSize;

		for (int i = 0; i < layersSize.length; i++)
		{
			layers[i] = new Layer(prevLayerSize, layersSize[i]);
			layers[i].initWeights(r);
			prevLayerSize = layersSize[i];
		}
	}

	public Layer getLayer(int idx)
	{
		return layers[idx];
	}

	/**
	 * Calculate neural network output based on input
	 * @param input Input values
	 * @return		Output values
	 */
	public float[] run(float[] input)
	{
		float[] actIn = input;
		for (int i = 0; i < layers.length; i++)
			actIn = layers[i].run(actIn);
		return actIn;
	}

	/**
	 * Train the neural network
	 * @param input			Input vector
	 * @param targetOutput	Expected output vector
	 * @param learningRate
	 * @param momentum
	 */
	public void train(float[] input, float[] targetOutput, float learningRate, float momentum)
	{
		float[] calcOut = run(input);
		float[] error = new float[calcOut.length];
		for (int i = 0; i < error.length; i++)
		{
			error[i] = targetOutput[i] - calcOut[i]; // negative error
		}
		for (int i = layers.length - 1; i >= 0; i--)
		{
			error = layers[i].train(error, learningRate, momentum);
		}
	}

	/**
	 * Serialize network to a file
	 */
	public boolean saveToFile(String fileName)
	{
		try {
			PrintWriter out = new PrintWriter(fileName);

			out.println(layers.length);

			out.print(layers[0].input.length);	
			for (Layer layer : layers)
			{
				out.print(" ");
				out.print(layer.output.length);	
			}

			for (Layer layer : layers)
			{
				out.println();
				out.print(layer.isSigmoid);
				for (int i = 0; i < layer.weights.length; i++)
				{
					out.print(" ");
					out.print(layer.weights[i]);
				}
			}

			out.close();
		}
		catch(IOException ex) {
			ex.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Deserialize network from a file
	 */
	public boolean loadFromFile(String fileName)
	{
		try {
			FileInputStream fin = new FileInputStream(new File(fileName));
			java.util.Scanner scanner = new java.util.Scanner(fin);
			scanner.useLocale(Locale.ENGLISH); 

			int numLayers = scanner.nextInt();
			System.out.println("Num layers = " + numLayers);

			if (numLayers > 100)
			{
				scanner.close();
				return false;
			}

			// read layer sizes
			int[] layersSize = new int [numLayers];
			int inputSize = scanner.nextInt();
			for (int i = 0; i < numLayers; i++)
				layersSize[i] = scanner.nextInt();

			// create network structure
			this.init(inputSize, layersSize);

			// load weights
			for (int i = 0; i < numLayers; i++)
			{
				layers[i].isSigmoid = scanner.nextBoolean();
				for (int j = 0; j < layers[i].weights.length; j++)
				{
					layers[i].weights[j] = scanner.nextFloat();
				}
			}

			scanner.close();
		}
		catch(IOException ex) {
			ex.printStackTrace();
			return false;
		}

		return true;
	}

	public String getName() {
		return name;
	}
	
	public NeuralNetworkMockup mockup()
	{
		NeuralNetworkMockup mockup = new NeuralNetworkMockup(this.layers.length);
		for(Layer element : layers)
		{
			mockup.addLayer(element.getWeights(), element.isSigmoid());
		}
		return mockup;
	}

	// EXAMPLE USAGE
	/*
	public static void main(String[] args) throws Exception
	{
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

		NeuralNetwork mlp = new NeuralNetwork();
		mlp.init(15, new int[]{ 7, 4 }); // 15 inputs, 7 neurons in hidden layer, 4 outpus
		mlp.getLayer(1).setIsSigmoid(false);

//		mlp.loadFromFile("ann.txt");
//		for (int i = 0; i < outputs.length; i++)
//		{
//			float[] t = inputs[i];
//			float[] ret = mlp.run(t);
//			System.out.printf("%s => %s\n", Arrays.toString(t), Arrays.toString(ret));
//		}

		int en = 1000;
		for (int e = 1; e <= en; e++)
		{
			for (int i = 0; i < outputs.length; i++)
				mlp.train(inputs[i], outputs[i], 0.1f, 0.6f);

			if (e % 100 == 0)
			{
				System.out.println();
				System.out.printf("%d epoch:\n", e);
				for (int i = 0; i < outputs.length; i++)
				{
					float[] t = inputs[i];
					float[] ret = mlp.run(t);
					System.out.printf("%s => %s\n", Arrays.toString(t), Arrays.toString(ret));
				}

				float[] t = new float[]{ 0.97f, 1.01f, 0.93f,  1.01f, 0.04f, 0.08f,  0.94f, 0.98f, 1.1f,  0.91f, 0.04f, 1.03f,  0.96f, 0.98f, 0.91f };
				float[] ret = mlp.run(t);
				System.out.printf("%s => %s\n", Arrays.toString(t), Arrays.toString(ret));
			}
		}

		mlp.saveToFile("ann.txt");
	}
	 */
}