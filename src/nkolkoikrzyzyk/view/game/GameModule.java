/**
 * 
 */
package nkolkoikrzyzyk.view.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyVetoException;
import java.util.concurrent.BlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;

import nkolkoikrzyzyk.commons.TestResult;
import nkolkoikrzyzyk.controller.players.Player;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.model.LookupTable;
import nkolkoikrzyzyk.model.NeuralNetwork;
import nkolkoikrzyzyk.view.ViewUtilities;

/**
 * @author elohhim
 *
 */
public class GameModule extends JPanel 
{
	//outside
	private final BlockingQueue<ProgramEvent> blockingQueue;
	//inside
	private PlayersPanel playersPanel;
	private TestPlayerPanel testPanel;
	private GamePanel gamePanel;
	private JDesktopPane desktop;

	public GameModule( BlockingQueue<ProgramEvent> blockingQueue)
	{
		this.blockingQueue = blockingQueue;
		initialize();
	}


	private void initialize() 
	{
//		this.setTitle("Tic-Tac-Toe Game Module");
		JPanel padding = new JPanel();
		padding.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
//		this.setContentPane(padding);
		this.setLayout( new BorderLayout(5,5));
		this.playersPanel = new PlayersPanel(blockingQueue);
		this.testPanel = new TestPlayerPanel(blockingQueue);
		this.gamePanel = new GamePanel(blockingQueue);
		initializeDesktop();

		fill();

//		Rectangle  bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
//		Dimension mSize =new Dimension(
//				Math.min( this.getSize().width, bounds.width),
//				Math.min( this.getSize().height, bounds.height-10)
//				);
//		this.setMinimumSize(mSize);
//		this.pack();
//		this.setResizable( true );
//		this.setVisible( true );
	}


	private void initializeDesktop() 
	{
		this.desktop = new JDesktopPane();

		desktop.setMinimumSize( new Dimension(600,600));
	}


	private void fill() 
	{
		this.add(leftPanel(), BorderLayout.LINE_START);
		this.add(centerPanel(), BorderLayout.CENTER);
	}

	private JPanel leftPanel()
	{
		JPanel leftPanel = new JPanel( /*new BorderLayout(5,5)*/);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		leftPanel.add(playersPanel);
		leftPanel.add(testPanel);
		return leftPanel;
	}


	private JPanel centerPanel()
	{
		JPanel centerPanel = new JPanel( new BorderLayout(5,5))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize()
			{
				return new Dimension(600,600);
			}
		};
		centerPanel.setBorder(ViewUtilities.titledBorder("Tic-Tac-Toe Games"));
		centerPanel.add(desktop, BorderLayout.CENTER);
		centerPanel.add(gamePanel, BorderLayout.PAGE_END);
		return centerPanel;
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
		this.gamePanel.populatePlayerCombos(playerListModel);
	}


	public void setResults(TestResult testResult) 
	{
		this.testPanel.setResults(testResult);
	}


	public void addGameWindow(GameWindow game) 
	{
		this.desktop.add(game);
		game.setVisible(true);
		try 
		{
			game.setSelected(true);
		} 
		catch (PropertyVetoException e) {};
	}
}
