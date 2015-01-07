/**
 * 
 */
package nkolkoikrzyzyk.view.neuralnetworks;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import javax.swing.text.View;
import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;

import nkolkoikrzyzyk.events.NewNetworkEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.view.LabeledForm;
import nkolkoikrzyzyk.view.ViewUtilities;

/**
 * @author Johhny
 *
 */
public class NewNetworkPanel extends JPanel
{
	private static final int PREF_WIDTH = 450;
	private static final int PREF_HEIGHT= 200;
	private static final int MAX_WIDTH = 450;
	private static final int MAX_HEIGHT = 200;
	
	private static final int MAX_INPUT = 20;
	private static final int MAX_OUTPUT = 20;
	private static final int MAX_HIDDEN_LAYERS = 10;
	private static final int MAX_LAYER_SIZE = 20;

	//outside
	private BlockingQueue<ProgramEvent> blockingQueue;
	//inside
	private JTextField name;
	private JSpinner inputSize;
	private JSpinner hiddenLayers;
	private JSpinner outputSize;
	private JSpinner variance;
	private JScrollPane layersScrollPane;
	
	private JSpinner[] layers = new JSpinner[0];
	private JCheckBox[] sigmoidChecks = new JCheckBox[0];
	
	private JCheckBox sigmoidOutput;

	public NewNetworkPanel( BlockingQueue<ProgramEvent> blockingQueue)
	{
		this.blockingQueue = blockingQueue;

		initialize();
	}

	private void initialize()
	{
		this.setBorder(ViewUtilities.titledBorder("Create new Artificial Neural Network"));

		this.name = new JTextField();
		this.name.setColumns(10);
		initializeInputSize();
		initializeHiddenLayers();
		initializeOutputSize();
		initializeVariance();
		this.sigmoidOutput = new JCheckBox();
		fill();
	}

	private void initializeHiddenLayers()
	{
		this.hiddenLayers = ViewUtilities.spinner(1, 1, MAX_HIDDEN_LAYERS, 1, "0");
		this.hiddenLayers.addChangeListener( new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				NewNetworkPanel.this.layersScrollPane.setViewportView(layersPanel());
			}
		});
	}

	private void initializeVariance()
	{
		this.variance = ViewUtilities.spinner(1.0f, 1.0f, 2.0f, 0.001f, "0.000");
	}

	private void initializeOutputSize()
	{
		this.outputSize = ViewUtilities.spinner(9, 1, MAX_OUTPUT, 1, "0");
		outputSize.setEnabled(false);		
	}

	private void initializeInputSize()
	{
		this.inputSize = ViewUtilities.spinner(9, 1, MAX_INPUT, 1, "0");
		inputSize.setEnabled(false);
		
	}

	private void fill()
	{
		this.setLayout(new BorderLayout(5,5));

		this.add(leftPanel(), BorderLayout.LINE_START);
		this.add(rightPanel(), BorderLayout.CENTER);
		this.add(bottomPanel(), BorderLayout.PAGE_END);
	}

	private JPanel leftPanel()
	{
		Component[] fields = { name, inputSize, outputSize, sigmoidOutput, variance};
		String[] labels = {"Name", "Input size", "Output size", "Sigmoid output", "Variance"};
		char[] mnemonics = {'N', 'I', 'O', '.', 'V'};
		return new LabeledForm(fields, labels, mnemonics);
	}

	private JPanel rightPanel()
	{
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout(5,5));
		
		Component[] fields = {hiddenLayers};
		String[] labels = {"Hidden layers"};
		char[] mnemonics = {'H'};
		rightPanel.add(new LabeledForm(fields, labels, mnemonics), BorderLayout.PAGE_START);
		
		this.layersScrollPane = new JScrollPane(layersPanel());
		this.layersScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.layersScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		rightPanel.add(this.layersScrollPane);
		return rightPanel;
	}
	
	protected LabeledForm layersPanel()
	{
		int count = (Integer)this.hiddenLayers.getValue();
		
		JSpinner layersTemp[] = Arrays.copyOf(layers, count);
		JCheckBox sigmoidTemp[] = Arrays.copyOf(sigmoidChecks, count);
				
		for( int i = layers.length; i<count;i++)
		{
			layersTemp[i] = ViewUtilities.spinner(1, 1, MAX_LAYER_SIZE, 1, "0");
			sigmoidTemp[i] = new JCheckBox("sigmoid",true);
		}
		layers = layersTemp;
		sigmoidChecks = sigmoidTemp;
		
		//fill
		String[] labels = new String[count];
		Box[] boxes = new Box[count];
		for( int i = 0; i < count; i++)
		{
			labels[i] = "Layer " + String.format("%02d", i+1);
			boxes[i] = new Box(BoxLayout.LINE_AXIS);
			boxes[i].add(layers[i]);
			boxes[i].add(sigmoidChecks[i]);
		}//for
		LabeledForm form = new LabeledForm(boxes, labels);	
		return form;
	}

	private JPanel bottomPanel()
	{
		JPanel bottomPanel = new JPanel( );
		bottomPanel.setLayout(new BorderLayout(5,5));
		JButton createButton = new JButton("Create ANN");
		createButton.setMnemonic('C');
		createButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//TODO: wynieœæ logikê poza listener, zostawiæ wywo³ania
				String name = NewNetworkPanel.this.name.getText();
				if (name.trim().length() == 0)
					name = null;
				int inputSize = (Integer)NewNetworkPanel.this.inputSize.getValue();
				int count = (Integer)NewNetworkPanel.this.hiddenLayers.getValue();
				int[] layersSize = new int[count+1];
				boolean[] isSigmoid = new boolean[count+1];
				for( int i = 0; i<count; i++)
				{
					layersSize[i] = (Integer)NewNetworkPanel.this.layers[i].getValue();
					isSigmoid[i] = NewNetworkPanel.this.sigmoidChecks[i].isSelected();
				}
				layersSize[count] = (Integer)NewNetworkPanel.this.outputSize.getValue();
				isSigmoid[count] = NewNetworkPanel.this.sigmoidOutput.isSelected();
				
				float variance = (Float)NewNetworkPanel.this.variance.getValue();
				blockingQueue.add( new NewNetworkEvent(name, inputSize, layersSize, isSigmoid, variance));
			}
		});
		bottomPanel.add(createButton, BorderLayout.LINE_START);
		return bottomPanel;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(PREF_WIDTH, PREF_HEIGHT);
	}

	@Override
	public Dimension getMaximumSize()
	{
		return new Dimension(MAX_WIDTH, MAX_HEIGHT);
	}
}
