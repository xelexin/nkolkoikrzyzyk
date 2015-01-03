/**
 * AppFrame.java
 */
package nkolkoikrzyzyk.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;

import nkolkoikrzyzyk.commons.BoardMockup;
import nkolkoikrzyzyk.events.ProgramEvent;

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
		this.setBounds(100, 100, 200, 600);
		this.setResizable( false );
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout( new GridLayout(0,1) );
		this.setVisible( true );
		
		fill();
	}

	private void fill() {
		JButton newGameButton = new JButton("New game");
		newGameButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new NewGameWindow(blockingQueue);
			}
		});
		this.add(newGameButton);
		
		JButton newNeuralNetworkButton = new JButton("New NN");
		this.add(newNeuralNetworkButton);
	}
}
