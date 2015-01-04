/**
 * 
 */
package nkolkoikrzyzyk.view.neuralnetworks;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListModel;

import nkolkoikrzyzyk.commons.NeuralNetworkMockup;
import nkolkoikrzyzyk.events.CloseNeuralNetworksModuleEvent;
import nkolkoikrzyzyk.events.LoadNetworkEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.events.SaveNetworkEvent;
import nkolkoikrzyzyk.model.NeuralNetwork;

/**
 * @author Johhny
 *
 */
public class NeuralNetworksWindow extends JFrame implements WindowListener 
{	
	private BlockingQueue<ProgramEvent> blockingQueue;
	private NeuralNetworkDrawPanel drawPanel;
	private JList<NeuralNetwork> networkList;
	private JFileChooser fileChooser;
	
	public NeuralNetworksWindow(BlockingQueue<ProgramEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
		this.initialize();
	}

	private void initialize()
	{
		this.addWindowListener(this);
		this.setBounds(100, 100, 800, 600);
		this.setTitle("Neural Networks Module");
		this.setResizable( false );
		this.setLayout( new BorderLayout() );
		this.setVisible( true );
		drawPanel = new NeuralNetworkDrawPanel();
		networkList = new JList<NeuralNetwork>();
		fileChooser = new JFileChooser();
		
		fill();
	}

	private void fill() {
		JScrollPane drawPanelScroll = new JScrollPane(drawPanel);
		this.add(drawPanelScroll, BorderLayout.CENTER);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout( new BorderLayout() );
		leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Networks"),
                BorderFactory.createEmptyBorder(5,5,5,5)));	
		JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
		
		JButton loadNetworkButton = new JButton("Load Neural Network");
		loadNetworkButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				int returnVal = fileChooser.showOpenDialog(NeuralNetworksWindow.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fileChooser.getSelectedFile();
		            blockingQueue.add( new LoadNetworkEvent(file));		            
		        } else {
		            System.out.println("Failed to choose file.");
		        }
			}
		});
		buttonPanel.add(loadNetworkButton);
		
		JButton saveNetworkButton = new JButton("Save Neural Network");
		saveNetworkButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if(!NeuralNetworksWindow.this.networkList.isSelectionEmpty())
				{
					int returnVal = fileChooser.showSaveDialog(NeuralNetworksWindow.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fileChooser.getSelectedFile();
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
		leftPanel.add(buttonPanel, BorderLayout.PAGE_START);
				
		JScrollPane networkListScroll = new JScrollPane(networkList);
		
		leftPanel.add(networkListScroll, BorderLayout.CENTER);
		
		this.add(leftPanel, BorderLayout.LINE_START);
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

	public void refreshList(List<NeuralNetwork> networkList) 
	{
		NeuralNetwork[] array = new NeuralNetwork[networkList.size()];
		networkList.toArray(array);
		this.networkList.setListData(array);		
	}
}
