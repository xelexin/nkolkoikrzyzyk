/**
 * View.java
 */
package nkolkoikrzyzyk.view;

import java.util.concurrent.BlockingQueue;

import javax.swing.SwingUtilities;

import nkolkoikrzyzyk.commons.GameData;
import nkolkoikrzyzyk.events.ProgramEvent;

/**
 * @author elohhim
 *
 */
public class View {

	private final BlockingQueue<ProgramEvent> blockingQueue;

	private AppFrame mainFrame = null;
	private GameWindow gameWindow = null;
	private NeuralNetworksWindow neuralNetworksWindow = null;

	/**
	 * Creates new View
	 *
	 * @param blockingQueue - queue for communication with controller
	 * @param mockup - lightweight representation of model
	 */
	public View(final BlockingQueue<ProgramEvent> blockingQueue){
		this.blockingQueue = blockingQueue;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				View.this.mainFrame = new AppFrame(blockingQueue);
			}
		});
	}
	
	public GameWindow invokeGameWindow(final BlockingQueue<ProgramEvent> blockingQueue, final GameData gameData)
	{
		this.gameWindow = new GameWindow(blockingQueue, gameData);
		return this.gameWindow;
	}

	public void setAppWindowVisible(boolean b) {
		this.mainFrame.setVisible(b);
	}

	public void invokeNeuralNetworksWindow() 
	{
		if ( this.neuralNetworksWindow == null)
			this.neuralNetworksWindow = new NeuralNetworksWindow(this.blockingQueue);
		this.neuralNetworksWindow.setVisible(true);
	}
}
