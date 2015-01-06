/**
 * 
 */
package nkolkoikrzyzyk.view.neuralnetworks;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.concurrent.BlockingQueue;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;
import javax.swing.text.View;

import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.view.LabeledForm;
import nkolkoikrzyzyk.view.ViewUtilities;

/**
 * @author Johhny
 *
 */
public class NewNetworkPanel extends JPanel
{
	private static final int MIN_WIDTH = 350;
	private static final int MIN_HEIGHT= 200;
	private static final int MAX_WIDTH = 350;
	private static final int MAX_HEIGHT = 200;
	
	private static final int MAX_INPUT = 20;
	private static final int MAX_OUTPUT = 20;
	private static final int MAX_HIDDEN_LAYERS = 10;

	//outside
	private BlockingQueue<ProgramEvent> blockingQueue;
	//inside
	private JSpinner inputSize;
	private JSpinner hiddenLayers;
	private JSpinner outputSize;
	private JSlider variance;

	public NewNetworkPanel( BlockingQueue<ProgramEvent> blockingQueue)
	{
		this.blockingQueue = blockingQueue;

		initialize();
	}

	private void initialize()
	{
		this.setBorder(ViewUtilities.titledBorder("Create new Artificial Neural Network"));

		initializeInputSize();
		initializeHiddenLayers();
		initializeOutputSize();
		initializeVariance();

		fill();
	}

	private void initializeHiddenLayers()
	{
		this.hiddenLayers = new JSpinner();
		hiddenLayers.setModel(new SpinnerNumberModel(1, 1, MAX_HIDDEN_LAYERS, 1));
	}

	private void initializeVariance()
	{
		this.variance = new JSlider(1, 10);
	}

	private void initializeOutputSize()
	{
		this.outputSize = new JSpinner();
		outputSize.setModel(new SpinnerNumberModel(9, 1, MAX_OUTPUT, 1));
		outputSize.setEnabled(false);		
	}

	private void initializeInputSize()
	{
		this.inputSize = new JSpinner();
		inputSize.setModel(new SpinnerNumberModel(9, 1, MAX_INPUT, 1));
		inputSize.setEnabled(false);
		
	}

	private void fill()
	{
		this.setLayout(new BorderLayout(5,5));

		this.add(leftPanel(), BorderLayout.LINE_START);
		this.add(ViewUtilities.scroll(rightPanel()), BorderLayout.LINE_END);
		this.add(bottomPanel(), BorderLayout.PAGE_END);
	}

	private JPanel leftPanel()
	{
		Component[] fields = { inputSize, hiddenLayers, outputSize};
		String[] labels = {"Input size", "Hidden layers", "Output size"};
		char[] mnemonics = {'I', 'H', 'O'};
		return new LabeledForm(fields, labels, mnemonics);
	}

	private JPanel rightPanel()
	{
		JPanel rightPanel = new JPanel();
		return rightPanel;
	}

	private JPanel bottomPanel()
	{
		JPanel bottomPanel = new JPanel( );
		bottomPanel.setLayout(new BorderLayout(5,5));
		JButton createButton = new JButton("Create ANN");
		createButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				
			}
		});
		bottomPanel.add(createButton, BorderLayout.LINE_START);
		
		bottomPanel.add(variance, BorderLayout.CENTER);
		return bottomPanel;
	}

	//	@Override
	//	public Dimension getMinimumSize()
	//	{
	//		return new Dimension(MIN_WIDTH, MIN_HEIGHT);
	//	}

	@Override
	public Dimension getMaximumSize()
	{
		return new Dimension(MAX_WIDTH, MAX_HEIGHT);
	}
}
