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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.concurrent.BlockingQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;

import nkolkoikrzyzyk.controller.Trainer;
import nkolkoikrzyzyk.events.CancelTrainingEvent;
import nkolkoikrzyzyk.events.LoadTrainingDataEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.events.SaveTrainingDataEvent;
import nkolkoikrzyzyk.model.NeuralNetwork;
import nkolkoikrzyzyk.model.TrainingData;
import nkolkoikrzyzyk.view.LabeledForm;
import nkolkoikrzyzyk.view.SteppedComboBox;
import nkolkoikrzyzyk.view.ViewUtilities;

/**
 * @author Johhny
 *
 */
public class TrainNetworkPanel extends JPanel implements PropertyChangeListener 
{
	private static final int MIN_WIDTH = 500;
	private static final int MIN_HEIGHT= 200;
	private static final int MAX_WIDTH = 500;
	private static final int MAX_HEIGHT = 200;

	//outside
	private BlockingQueue<ProgramEvent> blockingQueue;
	private NeuralNetworkDrawPanel drawPanel;

	//inside
	private JFileChooser fileChooser;
	private SteppedComboBox<NeuralNetwork> networkComboBox;
	private JSpinner momentum;
	private JSpinner learningRatio;
	private JSpinner epoches;
	private JList<TrainingData> dataList;
	private JProgressBar progressBar;
	private JButton trainButton;
	private Trainer currentTrainer=null;

	public TrainNetworkPanel(
			BlockingQueue<ProgramEvent> blockingQueue,
			NeuralNetworkDrawPanel drawPanel)
	{
		this.blockingQueue = blockingQueue;
		this.drawPanel = drawPanel;

		initialize();

	}

	private void initialize()
	{
		this.setBorder(ViewUtilities.titledBorder("Train Artificial Neural Network"));
		this.setLayout(new BorderLayout(5,5));

		initializeSpinners();
		initializeComboBox();
		this.dataList = new JList<TrainingData>();
		initializeTrainButton();

		fill();
	}

	private void initializeComboBox()
	{
		this.networkComboBox = new SteppedComboBox<NeuralNetwork>();
		Dimension d = networkComboBox.getPreferredSize();
		networkComboBox.setPreferredSize(new Dimension(150, d.height));
		networkComboBox.setPopupWidth(240);
	}

	private void initializeSpinners()
	{
		this.fileChooser = new JFileChooser();

		this.momentum = ViewUtilities.spinner(0.5f, 0.0f, 1.0f, 0.01f, "0.00");

		this.learningRatio = ViewUtilities.spinner(0.5f, 0.0f, 1.0f, 0.01f, "0.00");

		this.epoches = ViewUtilities.spinner(1000, 100, 10000, 100, "0");
	}

	private void initializeTrainButton()
	{
		trainButton = new JButton("Train ANN");
		trainButton.setMnemonic('T');
		trainButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if( TrainNetworkPanel.this.dataList.isSelectionEmpty() )
				{
					JOptionPane.showMessageDialog(null,"You must select training data!");
				}
				else if( TrainNetworkPanel.this.networkComboBox.getSelectedIndex() == -1)
				{
					JOptionPane.showMessageDialog(null,"You must select ANN!");
				}
				else
				{
					TrainNetworkPanel.this.trainButton.setEnabled(false);
					progressBar.setValue(0);
					currentTrainer = getTrainer();
					currentTrainer.addPropertyChangeListener( TrainNetworkPanel.this );
					currentTrainer.execute();
				}
			}
		});
	}

	private void initializeProgressBar()
	{
		progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
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
		buttons.add(createSaveButton());
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
						TrainNetworkPanel.this.blockingQueue.add(
								new LoadTrainingDataEvent( file )
								);
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

	private JButton createSaveButton()
	{
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(TrainNetworkPanel.this.dataList.isSelectionEmpty() == false)
				{
					int returnVal = fileChooser.showSaveDialog(TrainNetworkPanel.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						blockingQueue.add( new SaveTrainingDataEvent(file,
								TrainNetworkPanel.this.dataList.getSelectedValue())
								);		            
					} 
					else 
					{
						JOptionPane.showMessageDialog(null,"Failed to choose file.");
					}
				}
			}
		});
		return saveButton;
	}

	private JPanel rightPanel()
	{
		String[] labels = { "ANN", "Momentum", "Learning ratio", "Epoches"};
		char[] mnemonics = { '0', 'M','R','E'};
		Component[] fields = { networkComboBox, momentum, learningRatio, epoches};
		LabeledForm form = new LabeledForm(fields, labels, mnemonics);		
		return form;
	}

	private JPanel bottomPanel()
	{
		JPanel bottomPanel = new JPanel( );
		bottomPanel.setLayout(new BorderLayout(5,5));

		initializeProgressBar();

		bottomPanel.add(trainButton, BorderLayout.LINE_START);
		bottomPanel.add(progressBar, BorderLayout.CENTER);
		
		bottomPanel.add(createCancelButton(), BorderLayout.LINE_END);
		return bottomPanel;
	}

	private JButton createCancelButton()
	{
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(currentTrainer!=null)
					TrainNetworkPanel.this.blockingQueue.add(
							new CancelTrainingEvent(currentTrainer)
							);
			}
		});
		return cancelButton;
	}

	private Trainer getTrainer()
	{
		return new Trainer(
				this.blockingQueue,
				this.networkComboBox.getItemAt(this.networkComboBox.getSelectedIndex()),
				this.dataList.getSelectedValue(),
				(Float)this.learningRatio.getValue(),
				(Float)this.momentum.getValue(),
				(Integer)this.epoches.getValue(),
				this.trainButton
				);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(MIN_WIDTH, MIN_HEIGHT);
	}

	@Override
	public Dimension getMaximumSize()
	{
		return new Dimension(MAX_WIDTH, MAX_HEIGHT);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		if( "progress" == evt.getPropertyName()) 
		{
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

	public void populateList(TrainingData[] trainingDataListModel) 
	{
		this.dataList.setListData(trainingDataListModel);
	}

	public void populateNetworkCombo(NeuralNetwork[] networkList)
	{
		this.networkComboBox.setModel( new DefaultComboBoxModel<>(networkList));
	}
}
