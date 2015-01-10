/**
 * 
 */
package nkolkoikrzyzyk.model;

import java.awt.Toolkit;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

import javax.swing.SwingWorker;

import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.events.TrainingEndedEvent;

/**
 * @author elohhim
 *
 */

public class Trainer extends SwingWorker<NeuralNetwork, Void>
{
	private BlockingQueue<ProgramEvent> blockingQueue;
	private NeuralNetwork student;
	private TrainingData data;
	private float learningRate;
	private float momentum;
	private int epoches;

	public Trainer( BlockingQueue<ProgramEvent> blockingQueue, NeuralNetwork student, TrainingData data, float learnigRare, float momentum, int epoches)
	{
		this.blockingQueue = blockingQueue;
		try {
			this.student = (NeuralNetwork) student.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.data = data;
		this.learningRate = learnigRare;
		this.momentum = momentum;
		this.epoches =  epoches;
	}
	public void train()
	{
		long time = 0;

		for (int epoch = 1; epoch <= epoches; epoch++)
		{
			long startTime = System.nanoTime();

			for (int i = 0; i < data.getOutputs().length; i++)
			{
				student.train(data.getInput(i), data.getOutput(i),learningRate, momentum);
			}
				
			time += System.nanoTime() - startTime;

			if (epoch % 100 == 0)
			{
				setProgress((100*epoch)/epoches);
				System.out.println();
				System.out.printf("%d epoch:\n", epoch);
				for (int i = 0; i < data.getOutputs().length; i++)
				{
					float[] t = data.getInputs()[i];
					float[] ret = student.run(t);

					float error = 0.0f;
					for (int j = 0; j < ret.length; j++)
					{
						error += Math.abs(ret[j] - data.getOutputs()[i][j]);
					}
					System.out.printf("%s => %s (tot. error = %f)\n", Arrays.toString(t), Arrays.toString(ret), error);
				}
			}
		}
		System.out.printf("Learning time per epoch: %f ms\n", (float)time / (float)epoches / 1000000.0f);
	}
	@Override
	protected NeuralNetwork doInBackground() throws Exception 
	{
		train();
		return student;
	}
	
	@Override 
	public void done()
	{
		Toolkit.getDefaultToolkit().beep();
		blockingQueue.add( new TrainingEndedEvent(this));
	}
	
	
}
