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
import nkolkoikrzyzyk.events.TrainNNEvent;
import nkolkoikrzyzyk.view.game.NewGameWindow;

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
		this.setBounds(100, 100, 600, 600);
		this.setResizable( false );
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout( new GridLayout(0,2) );
		this.setVisible( true );
		
		fill();
	}

	private void fill() {
		JButton newGameButton = new JButton("New game");
		newGameButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				blockingQueue.add( new StartGameModuleEvent());
			}
		});
		this.add(newGameButton);
		
		JButton neuralNetworksButton = new JButton("Neural Networks");
		neuralNetworksButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				blockingQueue.add( new StartNeuralNetworksModuleEvent());
			}
		});
		this.add(neuralNetworksButton);
		
		JButton trainNNButton = new JButton("Train NN");
		trainNNButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				blockingQueue.add( new TrainNNEvent() );
			}
		});
		this.add(trainNNButton);
	}
}
