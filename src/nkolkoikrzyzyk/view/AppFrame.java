/**
 * AppFrame.java
 */
package nkolkoikrzyzyk.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.events.StartGameModuleEvent;
import nkolkoikrzyzyk.events.StartNewGameModuleEvent;
import nkolkoikrzyzyk.events.StartNeuralNetworksModuleEvent;
import nkolkoikrzyzyk.events.StartTestEvent;

/**
 * @author elohhim
 *
 */
@SuppressWarnings("serial")
public class AppFrame extends JFrame {
	private BlockingQueue<ProgramEvent> blockingQueue;
		
	public AppFrame(BlockingQueue<ProgramEvent> blockingQueue) 
	{
		this.blockingQueue = blockingQueue;
		this.initialize();
	}

	private void initialize()
	{
		this.setBounds(100, 100, 1200, 300);
		this.setResizable( true );
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout( new GridLayout(0,4) );
		this.setVisible( true );
		
		fill();
	}

	private void fill() 
	{
		createMenuBar();
		
//		JDesktopPane desktop = new JDesktopPane();
//		this.setContentPane(desktop);
		
		JButton newGameButton = new JButton("<html><center>Play<br>Tic-Tac-Toe</center></html>");
		newGameButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				blockingQueue.add( new StartNewGameModuleEvent());
			}
		});
		this.add(newGameButton);
		
		JButton neuralNetworksButton = 
				new JButton("<html><center>Manage Artificial<br>Neural Networks</center></html>");
		neuralNetworksButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				blockingQueue.add( new StartNeuralNetworksModuleEvent());
			}
		});
		this.add(neuralNetworksButton);
		
		JButton startTestButton = new JButton("Start test WARNING: long");
		startTestButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				blockingQueue.add( new StartTestEvent() );
			}
		});
		this.add(startTestButton);
		
		JButton gameModuleButton = new JButton("<html><center>Tic-Tac-Toe<br>Game Module</center></html>");
		gameModuleButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				blockingQueue.add( new StartGameModuleEvent());
			}
		});
		this.add(gameModuleButton);
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu module = new JMenu("Select module");
		module.getAccessibleContext().setAccessibleDescription(
		        "Select one of the application modules."
				);
		
		JMenuItem aNNModule = 
				new JMenuItem("Artificial Neural Networks Module");
		module.add(aNNModule);
		
		JMenuItem tTTGameModule = 
				new JMenuItem("Tic-Tac-Toe Game Module");
		module.add(tTTGameModule);
		menuBar.add(module);
		
		this.setJMenuBar(menuBar);
	}
}
