/**
 * 
 */
package nkolkoikrzyzyk.controller;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingWorker;

import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.events.TrainingEndedEvent;
import nkolkoikrzyzyk.model.NeuralNetwork;
import nkolkoikrzyzyk.model.TrainingData;
import nkolkoikrzyzyk.view.LabeledForm;
import nkolkoikrzyzyk.view.ViewUtilities;

/**
 * @author elohhim
 *
 */

public class Trainer extends SwingWorker<NeuralNetwork, Void>
{
	//outside
	private BlockingQueue<ProgramEvent> blockingQueue;
	private final JButton trainButton;
	
	//inside
	private NeuralNetwork student;
	private TrainingData data;
	private float learningRate;
	private float momentum;
	private int epoches;
	private float meanTime = 0.0f;
	private float globalMaxError = 0.0f;

	public Trainer( BlockingQueue<ProgramEvent> blockingQueue, NeuralNetwork student, TrainingData data, float learnigRare, float momentum, int epoches, JButton trainButton)
	{
		this.blockingQueue = blockingQueue;
		this.trainButton = trainButton;
		try {
			this.student = (NeuralNetwork) student.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.student.setName( student.getName() + " [" + data.getName() + "] ");
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

			if (epoch % (epoches/100) == 0)
			{
				setProgress((100*epoch)/epoches);
			}
		}
		this.meanTime  = (float)time / (float)epoches / 1000000.0f;
	}
	
	private float calculateMeanError()
	{
		float meanError = 0.0f;
		for (int i = 0; i < data.getOutputs().length; i++)
		{
//			System.out.println("Data " + i );
			meanError += calculateInputError(data.getOutputs()[i], student.run(data.getInputs()[i]));			
		}
		meanError/=data.getOutputs().length;
		return meanError;
	}
	
	private float calculateInputError(float[] output, float[] studentOutput)
	{
		float maxError = 0.0f;
		for(int i = 0; i<output.length; i++)
		{
			//TODO print
			//System.out.println("o: " + output[i] + " so: " + studentOutput[i] );
			float tempError = Math.abs((output[i] - studentOutput[i]));
			if( tempError > maxError)
				maxError = tempError;
			if( tempError > globalMaxError)
				globalMaxError = tempError;
		}
		//TODO print
//		System.out.println("Max error for input: " + maxError);
		return maxError;
	}
	
	@Override
	protected NeuralNetwork doInBackground() throws Exception 
	{
		do
		{
			train();
		} while( askForDecision() );
		return student;
	}
	
	private boolean askForDecision()
	{
		
		JPanel msgPanel = new JPanel( new BorderLayout(5,5));
		JLabel msg = new JLabel("Max Error: " + globalMaxError +
				" Mean max error: " + calculateMeanError() +  
				". Learning time per epoch: " + meanTime + "ms.");
		msgPanel.add(msg, BorderLayout.PAGE_START);
		String[] labels = {"Momentum", "Learning ratio", "Epoches"};
		JSpinner momentum = ViewUtilities.spinner(this.momentum, 0.0f, 1.0f, 0.01f, "0.00");
		JSpinner learningRatio = ViewUtilities.spinner(this.learningRate, 0.0f, 1.0f, 0.01f, "0.00");
		JSpinner epoches = ViewUtilities.spinner(this.epoches, 100, 10000, 100, "0");	
		Component[] fields = {momentum, learningRatio, epoches};
		LabeledForm form  = new LabeledForm(fields, labels);
		msgPanel.add(form, BorderLayout.CENTER);
		
		Object[] options = {"Continue",
        "End"};
		Toolkit.getDefaultToolkit().beep();
		int decision = JOptionPane.showOptionDialog(
				null, 
				msgPanel, 
				"Continue training?",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null, options, null);
		if( decision == JOptionPane.YES_OPTION )
		{
			setProgress(0);
			this.momentum = (Float)momentum.getValue();
			this.learningRate = (Float)learningRatio.getValue();
			this.epoches = (Integer)epoches.getValue();
			return true;
		}
		else
		{
			return false;
		}
	}
	@Override 
	public void done()
	{
		Toolkit.getDefaultToolkit().beep();
		trainButton.setEnabled(true);
		blockingQueue.add( new TrainingEndedEvent(this));
	}
}
