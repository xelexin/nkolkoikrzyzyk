/**
 * AppFrame.java
 */
package nkolkoikrzyzyk.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;

import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.events.StartGameModuleEvent;
import nkolkoikrzyzyk.events.StartNeuralNetworksModuleEvent;
import nkolkoikrzyzyk.events.StartTestEvent;

/**
 * @author elohhim
 *
 */
@SuppressWarnings("serial")
public class AppFrame extends JFrame {
	private BlockingQueue<ProgramEvent> blockingQueue;
		
	public AppFrame(BlockingQueue<ProgramEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
		this.initialize();
	}

	private void initialize()
	{
		this.setBounds(100, 100, 900, 300);
		this.setResizable( false );
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout( new GridLayout(0,3) );
		this.setVisible( true );
		
		fill();
	}

	private void fill() {
		JButton newGameButton = new JButton("<html><center>Play<br>Tic-Tac-Toe</center></html>");
		newGameButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				blockingQueue.add( new StartGameModuleEvent());
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
	}
}
