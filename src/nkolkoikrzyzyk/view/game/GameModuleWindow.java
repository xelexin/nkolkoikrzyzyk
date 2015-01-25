/**
 * 
 */
package nkolkoikrzyzyk.view.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JPanel;

import nkolkoikrzyzyk.commons.TestResult;
import nkolkoikrzyzyk.controller.players.Player;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.model.LookupTable;
import nkolkoikrzyzyk.model.NeuralNetwork;

/**
 * @author elohhim
 *
 */
public class GameModuleWindow extends JFrame implements WindowListener 
{
	//outside
	private final BlockingQueue<ProgramEvent> blockingQueue;
	//inside
	private PlayersPanel playersPanel;
	private TestPlayerPanel testPanel;

	public GameModuleWindow( BlockingQueue<ProgramEvent> blockingQueue)
	{
		this.blockingQueue = blockingQueue;
		initialize();
	}


	private void initialize() 
	{
		this.addWindowListener(this);
		this.setTitle("Tic-Tac-Toe Game Module");
		JPanel padding = new JPanel();
		padding.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		this.setContentPane(padding);
		this.setLayout( new BorderLayout(5,5));
		this.playersPanel = new PlayersPanel(blockingQueue);
		this.testPanel = new TestPlayerPanel(blockingQueue);
		
		fill();

		this.pack();

		Rectangle  bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		Dimension mSize =new Dimension(
				Math.min( this.getSize().width, bounds.width),
				Math.min( this.getSize().height, bounds.height-10)
				);
		this.setMinimumSize(mSize);
		this.setResizable( true );

		this.setVisible( true );
	}


	private void fill() 
	{
		this.add(playersPanel, BorderLayout.PAGE_START);
		this.add(testPanel, BorderLayout.PAGE_END);
	}


	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	public void populateLists(
			NeuralNetwork[] afterstatesNetworkListModel,
			NeuralNetwork[] beforestatesNetworkListModel, 
			LookupTable[] lookupTableListModel)
	{
		this.playersPanel.populateNetworkCombos(afterstatesNetworkListModel, beforestatesNetworkListModel);
		this.playersPanel.populateLookupTableCombo(lookupTableListModel);
	}


	public void populatePlayerList(Player[] playerListModel) 
	{
		this.playersPanel.populateList(playerListModel);
		this.testPanel.populatePlayerCombos(playerListModel);
	}


	public void setResults(TestResult testResult) 
	{
		this.testPanel.setResults(testResult);
	}
}
