/**
 * 
 */
package nkolkoikrzyzyk.view.neuralnetworks;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nkolkoikrzyzyk.commons.NeuralNetworkMockup;
import nkolkoikrzyzyk.events.CloseNeuralNetworksModuleEvent;
import nkolkoikrzyzyk.events.LoadNetworkEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.events.SaveNetworkEvent;
import nkolkoikrzyzyk.model.NeuralNetwork;
import nkolkoikrzyzyk.view.ViewUtilities;

/**
 * @author Johhny
 *
 */
public class NeuralNetworksWindow extends JFrame implements WindowListener 
{	
	private BlockingQueue<ProgramEvent> blockingQueue;
	private NeuralNetworkDrawPanel drawPanel;
	private JList<NeuralNetwork> networkList;
	private JFileChooser fileChooserNetwork;
	private JFileChooser fileChooserTrain;
	
	public NeuralNetworksWindow(BlockingQueue<ProgramEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
		this.initialize();
	}

	private void initialize()
	{
		this.addWindowListener(this);
		this.setBounds(100, 100, 800, 600);
		this.setTitle("Neural Networks Module");
		this.setResizable( true );
		JPanel padding = new JPanel();
		padding.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		this.setContentPane(padding);
		this.setLayout( new BorderLayout() );
		
		drawPanel = new NeuralNetworkDrawPanel();
		initializeNetworkList();
		fileChooserNetwork = new JFileChooser();
		fileChooserTrain = new JFileChooser();
		
		fill();
		
		this.setVisible( true );
	}

	private void initializeNetworkList() 
	{
		networkList = new JList<NeuralNetwork>();
		networkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		networkList.addListSelectionListener( new ListSelectionListener() 
		{
		    @Override
			public void valueChanged(ListSelectionEvent e) 
		    {
		    	if (e.getValueIsAdjusting() == false) 
		    	{
		    		if(NeuralNetworksWindow.this.networkList.isSelectionEmpty() == false)
		            {
		                NeuralNetworksWindow.this.drawPanel.setMockup(
		                		NeuralNetworksWindow.this.networkList.getSelectedValue().getMockup());
		                NeuralNetworksWindow.this.drawPanel.revalidate();
		                NeuralNetworksWindow.this.repaint();
		            } 
		        }
			}
		});
	}

	private void fill() 
	{
		fillCentralPanel();
		fillLeftPanel();
		fillBottomPanel();
	}

	private void fillCentralPanel()
	{	
		JScrollPane drawPanelScroll = new JScrollPane(drawPanel);
		drawPanelScroll.setBorder(ViewUtilities.titledBorder("Selected Artificial Neural Network"));
		this.add(drawPanelScroll, BorderLayout.CENTER);
	}
	
	private void fillLeftPanel()
	{
		JPanel leftPanel = new JPanel() {
			@Override
			public Dimension getPreferredSize()
			{
				return new Dimension(250,0);
			}
		};
		leftPanel.setLayout( new BorderLayout() );
		leftPanel.setBorder(ViewUtilities.titledBorder("Select Artificial Neural Network"));	
		
		JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
		JButton loadNetworkButton = new JButton("Load Neural Network");
		loadNetworkButton.setMnemonic('L');
		loadNetworkButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				int returnVal = fileChooserNetwork.showOpenDialog(NeuralNetworksWindow.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fileChooserNetwork.getSelectedFile();
		            if(file.exists())
		            {
		            	blockingQueue.add( new LoadNetworkEvent(file));
		            }
		            else
		            {
		            	JOptionPane.showMessageDialog(null, "File: " + file.getAbsolutePath() + " does not exist!");
		            }
		            			            
		        } else {
		            System.out.println("Failed to choose file.");
		        }
			}
		});
		buttonPanel.add(loadNetworkButton);
		
		JButton saveNetworkButton = new JButton("Save Neural Network");
		saveNetworkButton.setMnemonic('S');
		saveNetworkButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(NeuralNetworksWindow.this.networkList.isSelectionEmpty() == false)
				{
					int returnVal = fileChooserNetwork.showSaveDialog(NeuralNetworksWindow.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fileChooserNetwork.getSelectedFile();
			            //TODO 	To mogloby byc w sumie na poziomie GUIa ale 
			            //		nie wiem czy robic wyjatek 
			            blockingQueue.add( new SaveNetworkEvent(file,
			            		NeuralNetworksWindow.this.networkList.getSelectedValue()));		            
			        } else {
			            System.out.println("Failed to choose file.");
			        }
				}
				
			}
		});
		buttonPanel.add(saveNetworkButton);
		
		leftPanel.add(buttonPanel, BorderLayout.PAGE_END);
				
		JScrollPane networkListScroll = new JScrollPane(networkList);
		leftPanel.add(networkListScroll, BorderLayout.CENTER);
		
		this.add(leftPanel, BorderLayout.LINE_START);
	}

	private void fillBottomPanel()
	{
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout( new GridLayout(1,2));
		
		JPanel bottomLeftPanel = new JPanel();
		bottomLeftPanel.setBorder(ViewUtilities.titledBorder("Create new Artificial Neural Network"));
		
		bottomPanel.add(bottomLeftPanel);
		
		JPanel bottomRightPanel = new JPanel();
		bottomRightPanel.setBorder(ViewUtilities.titledBorder("Train Artificial Neural Network"));
		//bottomRightPanel.setLayout( new )
		
		
		JList trainDataList = new JList();
		JScrollPane trainDataListScroll = new JScrollPane(trainDataList); 
		
		bottomRightPanel.add(trainDataListScroll);
		
		JButton showDataButton = new JButton("Show");
		showDataButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
			}
		});
		bottomRightPanel.add(showDataButton);
		
		JButton loadDataButton = new JButton("Load");
		loadDataButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				
			}
		});
		bottomRightPanel.add(loadDataButton);
		
		JSpinner epochNumber = new JSpinner();
		bottomRightPanel.add(epochNumber);
		
		JButton trainButton = new JButton("Train");
		trainButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				
			}
		});
		bottomRightPanel.add(trainButton);
		
		
		JProgressBar progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
		progressBar.setValue(0);
		bottomRightPanel.add(progressBar);
		
		bottomPanel.add(bottomRightPanel);
		
		this.add(bottomPanel, BorderLayout.PAGE_END);
	}
	
	public void populateNetworkList(NeuralNetwork[] networkListModel) 
	{
		this.networkList.setListData(networkListModel);		
	}
	
	public void setDrawPanelMockup(NeuralNetworkMockup mockup)
	{
		this.drawPanel.setMockup(mockup);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {
		blockingQueue.add( new CloseNeuralNetworksModuleEvent());
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}
