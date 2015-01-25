/**
 * View.java
 */
package nkolkoikrzyzyk.view;

import java.util.concurrent.BlockingQueue;

import javax.swing.SwingUtilities;

import nkolkoikrzyzyk.commons.GameData;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.view.game.GameModule;
import nkolkoikrzyzyk.view.game.GameWindow;
import nkolkoikrzyzyk.view.neuralnetworks.ANNModule;

/**
 * @author elohhim
 *
 */
public class View {

	private final BlockingQueue<ProgramEvent> blockingQueue;

	private AppFrame mainFrame = null;
	private GameWindow gameWindow = null;
	private ANNModule neuralNetworksWindow = null;
	private GameModule gameModuleWindow = null;

	/**
	 * Creates new View
	 *
	 * @param blockingQueue - queue for communication with controller
	 * @param mockup - lightweight representation of model
	 */
	public View(final BlockingQueue<ProgramEvent> blockingQueue){
		this.blockingQueue = blockingQueue;
		invokeNeuralNetworksWindow();
		invokeGameModuleWindow();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				View.this.mainFrame = new AppFrame(
						blockingQueue, 
						neuralNetworksWindow,
						gameModuleWindow);
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
	
	public void invokeNeuralNetworksWindow() 
	{
		if( this.getNeuralNetworksWindow() == null)
			this.neuralNetworksWindow = new ANNModule(this.blockingQueue);
		this.getNeuralNetworksWindow().setVisible(true);
	}

	/**
	 * @return the neuralNetworksWindow
	 */
	public ANNModule getNeuralNetworksWindow()
	{
		return neuralNetworksWindow;
	}

	public void invokeGameModuleWindow() 
	{
		if( this.gameModuleWindow == null)
			this.gameModuleWindow = new GameModule(this.blockingQueue);
		this.gameModuleWindow.setVisible(true);		
	}

	public GameModule getGameModuleWindow() 
	{	
		return gameModuleWindow;
	}	
	
	public void addGameWindow( GameWindow game )
	{
		this.gameModuleWindow.addGameWindow(game);
	}
}
