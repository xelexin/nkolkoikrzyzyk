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

	private AppFrame frame;
	private GameWindow gameWindow;

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
				View.this.frame = new AppFrame(blockingQueue);
			}
		});
	}
	
	public GameWindow invokeGameWindow(final BlockingQueue<ProgramEvent> blockingQueue, final GameData gameData)
	{
		this.gameWindow = new GameWindow(blockingQueue, gameData);
		return this.gameWindow;
	}

	public void setAppWindowVisible(boolean b) {
		this.frame.setVisible(b);
	}
}
