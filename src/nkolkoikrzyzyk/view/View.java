/**
 * View.java
 */
package nkolkoikrzyzyk.view;

import java.util.concurrent.BlockingQueue;

import javax.swing.SwingUtilities;

import nkolkoikrzyzyk.commons.GameData;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.model.NeuralNetwork;
import nkolkoikrzyzyk.view.game.GameWindow;
import nkolkoikrzyzyk.view.game.NewGameWindow;
import nkolkoikrzyzyk.view.neuralnetworks.NeuralNetworksWindow;

/**
 * @author elohhim
 *
 */
public class View {

	private final BlockingQueue<ProgramEvent> blockingQueue;

	private AppFrame mainFrame = null;
	private GameWindow gameWindow = null;
	private NewGameWindow newGameWindow = null;
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
	
	public void setAppWindowVisible(boolean b) {
		this.mainFrame.setVisible(b);
	}
	
	public GameWindow invokeGameWindow(final BlockingQueue<ProgramEvent> blockingQueue, final GameData gameData)
	{
		this.gameWindow = new GameWindow(blockingQueue, gameData);
		return this.gameWindow;
	}

	public void invokeNewGameWindow( NeuralNetwork[] networkList )
	{
		if( this.newGameWindow == null)
			this.newGameWindow = new NewGameWindow(this.blockingQueue);
		this.newGameWindow.setVisible(true);
		this.newGameWindow.populateNetworkList( networkList );
	}
	
	public void invokeNeuralNetworksWindow() 
	{
		if( this.getNeuralNetworksWindow() == null)
			this.neuralNetworksWindow = new NeuralNetworksWindow(this.blockingQueue);
		this.getNeuralNetworksWindow().setVisible(true);
	}

	/**
	 * @return the neuralNetworksWindow
	 */
	public NeuralNetworksWindow getNeuralNetworksWindow()
	{
		return neuralNetworksWindow;
	}	
}
