/**
 * Controller.java
 */
package nkolkoikrzyzyk.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import nkolkoikrzyzyk.commons.GameData;
import nkolkoikrzyzyk.events.CloseNeuralNetworksModuleEvent;
import nkolkoikrzyzyk.events.LoadNetworkEvent;
import nkolkoikrzyzyk.events.NewGameEvent;
import nkolkoikrzyzyk.events.NewNetworkEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.events.SaveNetworkEvent;
import nkolkoikrzyzyk.events.StartGameModuleEvent;
import nkolkoikrzyzyk.events.StartNeuralNetworksModuleEvent;
import nkolkoikrzyzyk.events.TrainNNEvent;
import nkolkoikrzyzyk.model.GameModel;
import nkolkoikrzyzyk.model.Mark;
import nkolkoikrzyzyk.model.Model;
import nkolkoikrzyzyk.model.NeuralNetwork;
import nkolkoikrzyzyk.model.NeuralNetworkPlayer;
import nkolkoikrzyzyk.model.Player;
import nkolkoikrzyzyk.view.View;
import nkolkoikrzyzyk.view.game.GameWindow;

/**
 * @author elohhim
 *
 */
public class AppController 
{

	/** reference to MVC View*/
	private final View view;
	/** reference to MVC Model*/
	private final Model model;
	/**queue for ProgramEvent.*/
	private final BlockingQueue<ProgramEvent> blockingQueue;
	/**mapping of objects ProgramEvent to objects ProgramAction*/
	private final Map<Class<? extends ProgramEvent>, ProgramAction> eventActionMap;

	/**
	 * creates object of Controller type
	 *
	 * @param view reference to view
	 * @param model reference to model
	 * @param blockingQueue reference to blockingQueue for communication
	 */
	public AppController(final View view, final  Model model, final BlockingQueue<ProgramEvent> blockingQueue) {
		this.view = view;
		this.model = model;
		this.blockingQueue = blockingQueue;
		this.eventActionMap = new HashMap<Class<? extends ProgramEvent>, ProgramAction>();
		this.fillEventActionMap();
	}

	/**
	 * infinite loop to intercept events from view
	 * <br> it takes events from blockingQueue and using eventActionMap
	 * starts action handling this event
	 * @throws InterruptedException
	 */
	public void work() throws InterruptedException
	{
		ProgramEvent event = null;
		while(true)
		{
			try
			{
				event = this.blockingQueue.take();
				ProgramAction action = this.eventActionMap.get(event.getClass());
				action.go(event);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * fills container eventActionMap
	 */
	private void fillEventActionMap()
	{
		this.eventActionMap.put( NewGameEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event ) {
				NewGameEvent nGE = ( NewGameEvent ) event;
				System.out.println("New game button clicked");
				
				BlockingQueue<ProgramEvent> gameQueue = new LinkedBlockingQueue<ProgramEvent>();
				GameModel model = new GameModel();
				GameData gameData = new GameData(model.getId(), nGE.player1.getName(), nGE.player2.getName());
				GameWindow view = AppController.this.view.invokeGameWindow(gameQueue, gameData);
				System.out.println(view);
				GameController controller = new GameController(view, model, gameQueue, blockingQueue, nGE.player1, nGE.player2);
				AppController.this.view.setAppWindowVisible( false );
				try {
					controller.work();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AppController.this.view.setAppWindowVisible( true );
			}
		});
		
		this.eventActionMap.put( TrainNNEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event ) {
				TrainNNEvent tNNE = ( TrainNNEvent ) event;
				System.out.println("Train NN button clicked");
				Player player1 = new NeuralNetworkPlayer("Siec X", Mark.CROSS, null);
				Player player2 = new NeuralNetworkPlayer("Siec O", Mark.NOUGHT, null);
				for(int i = 0; i <100; i++)
				{
					new GameModel().fastPlay(player1, player2);
				}
			}
		});
		
		this.eventActionMap.put( StartNeuralNetworksModuleEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event ) {
				StartNeuralNetworksModuleEvent sNNME = ( StartNeuralNetworksModuleEvent ) event;
				AppController.this.view.invokeNeuralNetworksWindow();
				AppController.this.view.setAppWindowVisible( false );
				
				//TODO: Brzydki hack z public neuralNetworksWindow
				AppController.this.view.getNeuralNetworksWindow().populateNetworkList(
						AppController.this.model.getNetworkListModel());
			}
		});
		
		this.eventActionMap.put( CloseNeuralNetworksModuleEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event ) {
				CloseNeuralNetworksModuleEvent eNNME = ( CloseNeuralNetworksModuleEvent ) event;
				AppController.this.view.setAppWindowVisible( true );
			}
		});
		
		this.eventActionMap.put( LoadNetworkEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event ) {
				LoadNetworkEvent lNE = ( LoadNetworkEvent ) event;
				AppController.this.model.addNetwork( new NeuralNetwork(lNE.file) );
				AppController.this.view.getNeuralNetworksWindow().populateNetworkList(
						AppController.this.model.getNetworkListModel());
			}
		});
		
		this.eventActionMap.put( SaveNetworkEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event ) {
				SaveNetworkEvent sNE = ( SaveNetworkEvent ) event;
				sNE.network.saveToFile(sNE.file);
			}
		});
		
		this.eventActionMap.put( StartGameModuleEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event )
			{
				StartGameModuleEvent sGME = (StartGameModuleEvent) event;
				AppController.this.view.invokeNewGameWindow(
						AppController.this.model.getNetworkListModel());				
			}
		});
		
		this.eventActionMap.put( NewNetworkEvent.class, new ProgramAction()
		{
			@Override
			public void go(ProgramEvent event)
			{
				NewNetworkEvent nNE = (NewNetworkEvent) event;
				NeuralNetwork network = new NeuralNetwork(nNE.name, nNE.inputSize, nNE.layersSize, nNE.variance, nNE.isSigmoid);
				AppController.this.model.addNetwork(network);
				AppController.this.view.getNeuralNetworksWindow().populateNetworkList(
						AppController.this.model.getNetworkListModel());
			}
		});
	}
}
