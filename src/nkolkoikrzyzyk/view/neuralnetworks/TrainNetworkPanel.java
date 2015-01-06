/**
 * 
 */
package nkolkoikrzyzyk.view.neuralnetworks;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.peer.ComponentPeer;
import java.io.File;
import java.util.concurrent.BlockingQueue;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import nkolkoikrzyzyk.events.LoadNetworkEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.model.NeuralNetwork;
import nkolkoikrzyzyk.view.LabeledForm;
import nkolkoikrzyzyk.view.ViewUtilities;

/**
 * @author Johhny
 *
 */
public class TrainNetworkPanel extends JPanel
{
	private static final int MIN_WIDTH = 500;
	private static final int MIN_HEIGHT= 200;
	private static final int MAX_WIDTH = 500;
	private static final int MAX_HEIGHT = 200;
	
	//outside
	private BlockingQueue<ProgramEvent> blockingQueue;
	private JList<NeuralNetwork> networkList;
	private NeuralNetworkDrawPanel drawPanel;
	//inside
	private JFileChooser fileChooser;
	private JSpinner momentum;
	private JSpinner learningRatio;
	private JSpinner epoches;
	private JList dataList;
	
	public TrainNetworkPanel(
			BlockingQueue<ProgramEvent> blockingQueue,
			JList<NeuralNetwork> networkList,
			NeuralNetworkDrawPanel drawPanel)
	{
		this.blockingQueue = blockingQueue;
		this.networkList = networkList;
		this.drawPanel = drawPanel;
		
		initialize();
	
	}

	private void initialize()
	{
		this.setBorder(ViewUtilities.titledBorder("Train Artificial Neural Network"));
		this.setLayout(new BorderLayout(5,5));
		
		initializeSpinners();
		
		this.dataList = new JList();
		fill();
	}

	private void initializeSpinners()
	{
		this.fileChooser = new JFileChooser();
		
		this.momentum = new JSpinner();
		this.momentum.setModel(new SpinnerNumberModel(0.50, 0.0, 1.0, 0.01));
		
		this.learningRatio = new JSpinner();
		this.learningRatio.setModel(new SpinnerNumberModel(0.50, 0.0, 1.0, 0.01));
		
		this.epoches = new JSpinner();
		this.epoches.setModel(new SpinnerNumberModel(1000, 100, 10000, 100));
	}

	private void fill()
	{
		this.add(leftPanel(), BorderLayout.LINE_START);
		this.add(rightPanel(), BorderLayout.LINE_END);
		this.add(bottomPanel(), BorderLayout.PAGE_END);
		
	}
	
	private JPanel leftPanel()
	{
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout(5,5));
		
		leftPanel.add(new JLabel("Training data sets"), BorderLayout.PAGE_START);	
		leftPanel.add(ViewUtilities.scroll(dataList), BorderLayout.CENTER);
		leftPanel.add(dataButtons(), BorderLayout.PAGE_END);
		
		return leftPanel;
	}

	private JPanel dataButtons()
	{
		JPanel buttons = new JPanel( new GridLayout(1,2));
		buttons.add(createLoadButton());
		buttons.add(createShowButton());
		return buttons;
	}

	private JButton createLoadButton()
	{
		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int returnVal = fileChooser.showOpenDialog(TrainNetworkPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fileChooser.getSelectedFile();
		            if(file.exists())
		            {
		            }
		            else
		            {
		            	JOptionPane.showMessageDialog(null, "File: " + file.getAbsolutePath() + " does not exist!");
		            }
				}
			}
		});
		return loadButton;
	}
	
	private JButton createShowButton()
	{
		JButton showButton = new JButton("Show");
		showButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
			}
		});
		return showButton;
	}
	
	private JPanel rightPanel()
	{
		String[] labels = { "Momentum", "Learning ratio", "Epoches"};
	    char[] mnemonics = {'M','R','E'};
	    Component[] fields = {momentum, learningRatio, epoches};
		LabeledForm form = new LabeledForm(fields, labels, mnemonics);		
		return form;
	}
	
	private JPanel bottomPanel()
	{
		JPanel bottomPanel = new JPanel( );
		bottomPanel.setLayout(new BorderLayout(5,5));
		JButton trainButton = new JButton("Train ANN");
		trainButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				
			}
		});
		bottomPanel.add(trainButton, BorderLayout.LINE_START);
		JProgressBar progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		bottomPanel.add(progressBar, BorderLayout.CENTER);
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
