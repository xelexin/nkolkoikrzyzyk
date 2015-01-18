/**
 * 
 */
package nkolkoikrzyzyk.view.lookup;

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

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import nkolkoikrzyzyk.controller.Filler;
import nkolkoikrzyzyk.controller.LookupTablePlayer;
import nkolkoikrzyzyk.controller.Trainer;
import nkolkoikrzyzyk.events.DeleteLookupTableEvent;
import nkolkoikrzyzyk.events.GenerateTrainingDataEvent;
import nkolkoikrzyzyk.events.NewLookupTableEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.model.LookupTable;
import nkolkoikrzyzyk.model.Mark;
import nkolkoikrzyzyk.model.NeuralNetwork;
import nkolkoikrzyzyk.model.TrainingData;
import nkolkoikrzyzyk.model.TrainingData.TrainingDataType;
import nkolkoikrzyzyk.view.LabeledForm;
import nkolkoikrzyzyk.view.SteppedComboBox;
import nkolkoikrzyzyk.view.ViewUtilities;
import nkolkoikrzyzyk.view.neuralnetworks.NewNetworkPanel;
import nkolkoikrzyzyk.view.neuralnetworks.TrainNetworkPanel;

/**
 * @author Johhny
 *
 */
public class LookupTablePanel extends JPanel implements PropertyChangeListener 
{
	private static final int MIN_WIDTH =600;
	private static final int MIN_HEIGHT= 200;
	private static final int MAX_WIDTH = 600;
	private static final int MAX_HEIGHT = 200;

	//outside
	private BlockingQueue<ProgramEvent> blockingQueue;

	//inside
	private JList<LookupTable> tableList;
	private SteppedComboBox<LookupTable> table1ComboBox;
	private SteppedComboBox<LookupTable> table2ComboBox;
	private JSpinner alphaFactor;
	private JSpinner games;
	private JTextField tableName;
	private JProgressBar progressBar;
	private JButton fillButton;

	public LookupTablePanel(
			BlockingQueue<ProgramEvent> blockingQueue)
	{
		this.blockingQueue = blockingQueue;		
		initialize();
	}

	private void initialize()
	{
		this.setBorder( ViewUtilities.titledBorder("Create and fill lookup table") );
		this.setLayout(new BorderLayout(5,5));

		initializeTableList();
		initializeSpinners();
		initializeComboBoxes();
		initializeTextFields();
		initializeProgressBar();
		initializeFillButton();

		fill();
	}

	private void initializeComboBoxes()
	{
		this.table1ComboBox = new SteppedComboBox<LookupTable>();
		Dimension d = table1ComboBox.getPreferredSize();
		table1ComboBox.setPreferredSize(new Dimension(120, d.height));
		table1ComboBox.setPopupWidth(240);
		this.table2ComboBox = new SteppedComboBox<LookupTable>();
		d = table2ComboBox.getPreferredSize();
		table2ComboBox.setPreferredSize(new Dimension(120, d.height));
		table2ComboBox.setPopupWidth(240);
	}

	private void initializeTableList()
	{
		tableList = new JList<LookupTable>();
		tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	private void initializeSpinners()
	{		
		this.alphaFactor = ViewUtilities.spinner(0.9995f, 0.9990f, 1.0f, 0.00001f, "0.00000");
		//TODO: nie dziala
		this.alphaFactor.setEnabled(false);
		this.games = ViewUtilities.spinner(100000, 100000, 500000, 10000, "0");
	}

	private void initializeTextFields()
	{
		tableName = new JTextField();
		tableName.setColumns(10);
	}

	private void initializeProgressBar()
	{
		progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
	}

	private void initializeFillButton()
	{
		fillButton = new JButton("Play games");
		fillButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if( table1ComboBox.getSelectedIndex() == -1 || table1ComboBox.getSelectedIndex() == -1)
				{
					noTableSelected();
				}
				else
				{
					Filler filler = getFiller();
					filler.addPropertyChangeListener( LookupTablePanel.this );
					filler.execute();
					fillButton.setEnabled(false);
				}
			}
		});
	}

	private void fill()
	{
		this.add(leftPanel(), BorderLayout.LINE_START);
		this.add(centralPanel(), BorderLayout.CENTER);
		this.add(rightPanel(), BorderLayout.LINE_END);
	}

	private JPanel centralPanel()
	{
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout(5,5));

		leftPanel.add(new JLabel("Lookup tables"), BorderLayout.PAGE_START);
		JScrollPane scrollPane = ViewUtilities.scroll(tableList);
		leftPanel.add(scrollPane, BorderLayout.CENTER);
		leftPanel.add(bottomLeftPanel(), BorderLayout.PAGE_END);

		return leftPanel;
	}

	private JPanel bottomLeftPanel()
	{
		JPanel leftBottomPanel = new JPanel();
		leftBottomPanel.setLayout( new BoxLayout(leftBottomPanel,BoxLayout.PAGE_AXIS)); 
		leftBottomPanel.add(newDeleteButtons());

		JPanel namePanel = new JPanel();
		JLabel nameLabel = new JLabel("Table name");
		nameLabel.setLabelFor(tableName);
		namePanel.add(nameLabel);
		namePanel.add(tableName);
		leftBottomPanel.add(namePanel);
		return leftBottomPanel;
	}

	private JPanel newDeleteButtons()
	{
		JPanel buttons = new JPanel( new GridLayout(1,2));
		buttons.add(createNewButton());
		buttons.add(createDeleteButton());
		return buttons;
	}

	private JButton createDeleteButton()
	{
		JButton deleteTableButton = new JButton("Delete");
		deleteTableButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if( tableList.isSelectionEmpty() )
				{
					noTableSelected();
				}
				else
				{
					LookupTablePanel.this.blockingQueue.add( 
							new DeleteLookupTableEvent(
									LookupTablePanel.this.tableList.getSelectedValue()
									));
				}
			}


		});
		return deleteTableButton;
	}

	private JButton createNewButton()
	{
		JButton newTableButton = new JButton("New");
		newTableButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String name = LookupTablePanel.this.tableName.getText();
				if (name.trim().length() == 0)
					name = null;
				blockingQueue.add( new NewLookupTableEvent(name) );
			}
		});
		return newTableButton;
	}

	private JPanel leftPanel()
	{
		JPanel centerPanel = new JPanel(new BorderLayout(5,5));
		JLabel generateLabel = new JLabel("<html>Generate training<br>data based on:</html>");
		JPanel generateButtons = new JPanel( new GridLayout(3,1));

		generateButtons.add(afterstatesButton());
		generateButtons.add(beforestatesButton());

		centerPanel.add(generateLabel, BorderLayout.PAGE_START);
		centerPanel.add(generateButtons, BorderLayout.CENTER);

		return centerPanel;
	}

	private JButton beforestatesButton()
	{
		JButton beforestatesButton = new JButton("<html><center>Beforestates<br>(i:9 o:9)</center></html>");
		beforestatesButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{

				LookupTablePanel.this.blockingQueue.add( 
						new GenerateTrainingDataEvent(
								LookupTablePanel.this.tableList.getSelectedValue(),
								TrainingDataType.BEFORESTATES
								));				
			}
		});
		//TODO: 
		beforestatesButton.setEnabled(false);
		return beforestatesButton;
	}

	private JButton afterstatesButton()
	{
		JButton afterstatesButton = new JButton("<html><center>Afterstates<br>(i:9 o:1)</center></html>");
		afterstatesButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if( tableList.isSelectionEmpty() )
				{
					noTableSelected();
				}
				else
				{
					LookupTablePanel.this.blockingQueue.add( 
							new GenerateTrainingDataEvent(
									LookupTablePanel.this.tableList.getSelectedValue(),
									TrainingDataType.AFTERSTATES
									));	
				}
			}
		});
		return afterstatesButton;
	}

	private JPanel rightPanel()
	{
		JPanel rightPanel = new JPanel(new BorderLayout());
		String[] labels = { "Table X", "Table O", "Games", "Alpha factor"};
		char[] mnemonics = new char[0];
		Component[] fields = {table1ComboBox, table2ComboBox, games, alphaFactor};
		LabeledForm form = new LabeledForm(fields, labels, mnemonics);
		rightPanel.add(form,BorderLayout.CENTER);
		rightPanel.add(bottomRightPanel(), BorderLayout.PAGE_END);
		return rightPanel;
	}

	private JPanel bottomRightPanel()
	{
		JPanel bottomPanel = new JPanel( );
		bottomPanel.setLayout(new BorderLayout(5,5));
		bottomPanel.add(fillButton, BorderLayout.LINE_START);
		bottomPanel.add(progressBar, BorderLayout.CENTER);
		return bottomPanel;
	}

	protected Filler getFiller()
	{
		return new Filler(
				blockingQueue, 
				new LookupTablePlayer(
						"Table 1",
						Mark.CROSS,
						table1ComboBox.getItemAt(table1ComboBox.getSelectedIndex()) ), 
						new LookupTablePlayer(
								"Table 2",
								Mark.NOUGHT,
								table2ComboBox.getItemAt(table2ComboBox.getSelectedIndex()) ),
								(Integer)games.getValue(),
								fillButton
				);
	}

	private void noTableSelected()
	{
		JOptionPane.showMessageDialog(null, "You must select lookup table to perform operation!");
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
			this.repaint();
		}
	}

	public void populateList(LookupTable[] lookupTableListModel) 
	{
		this.tableList.setListData(lookupTableListModel);
		this.table1ComboBox.setModel(
				new DefaultComboBoxModel<>(lookupTableListModel));
		this.table2ComboBox.setModel(
				new DefaultComboBoxModel<>(lookupTableListModel));
	}
}
