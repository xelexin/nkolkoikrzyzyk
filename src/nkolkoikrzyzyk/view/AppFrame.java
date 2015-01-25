/**
 * AppFrame.java
 */
package nkolkoikrzyzyk.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.events.StartGameModuleEvent;
import nkolkoikrzyzyk.events.StartNeuralNetworksModuleEvent;
import nkolkoikrzyzyk.events.StartTestEvent;
import nkolkoikrzyzyk.view.game.GameModule;
import nkolkoikrzyzyk.view.neuralnetworks.ANNModule;

/**
 * @author elohhim
 *
 */
@SuppressWarnings("serial")
public class AppFrame extends JFrame 
{
	//outside
	private final BlockingQueue<ProgramEvent> blockingQueue;
	private final ANNModule aNNModule;
	private final GameModule gameModule;
	//inside
	private JPanel mainPanel;
	

	public AppFrame(
			BlockingQueue<ProgramEvent> blockingQueue,
			ANNModule aNNModule, 
			GameModule gameModule) 
	{
		this.blockingQueue = blockingQueue;
		this.aNNModule = aNNModule;
		this.gameModule = gameModule;
		this.initialize();
	}

	private void initialize()
	{
		this.setTitle("ANNPTTT (Artificial Neural Networks playing Tic-Tac-Toe)");
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setResizable( true );
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initializeMainPanel();
		fill();
		pack();
		this.setVisible( true );
	}

	private void initializeMainPanel() 
	{
		mainPanel = new JPanel( new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
	}

	private void fill() 
	{
		createMenuBar();
		this.add(ViewUtilities.scroll(mainPanel));
	}

	private void createMenuBar() 
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu module = new JMenu("Select module");
		module.getAccessibleContext().setAccessibleDescription(
				"Select one of the application modules."
				);

		JMenuItem aNNModuleMenu = 
				new JMenuItem("Artificial Neural Networks Module");
		module.add(aNNModuleMenu);
		aNNModuleMenu.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				mainPanel.removeAll();
				mainPanel.add(aNNModule);
				blockingQueue.add( new StartNeuralNetworksModuleEvent());
			}
		});
		
		JMenuItem tTTGameModuleMenu = 
				new JMenuItem("Tic-Tac-Toe Game Module");
		tTTGameModuleMenu.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				mainPanel.removeAll();
				mainPanel.add(gameModule);
				blockingQueue.add( new StartGameModuleEvent());
			}
		});
		module.add(tTTGameModuleMenu);

		JMenuItem startTest = new JMenuItem("Start test WARNING: long");
		startTest.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				blockingQueue.add( new StartTestEvent() );
			}
		});
		module.add(startTest);

		
		
		menuBar.add(module);

		this.setJMenuBar(menuBar);
	}
}
