/**
 * Controller.java
 */
package nkolkoikrzyzyk.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
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
import nkolkoikrzyzyk.events.StartTestEvent;
import nkolkoikrzyzyk.events.StartTrainingEvent;
import nkolkoikrzyzyk.events.TrainingEndedEvent;
import nkolkoikrzyzyk.model.GameModel;
import nkolkoikrzyzyk.model.LookupTable;
import nkolkoikrzyzyk.model.Mark;
import nkolkoikrzyzyk.model.Model;
import nkolkoikrzyzyk.model.NeuralNetwork;
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
		this.eventActionMap.put( StartTestEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event ) 
			{
				StartTestEvent sTE = ( StartTestEvent ) event;
				
				//tworzenie tabel
				LookupTable tableX = new LookupTable("Table X");
				model.addLookupTable(tableX);
				LookupTable tableO = new LookupTable("Table O");
				model.addLookupTable(tableO);
				
				//gracze treningowi
				LookupTablePlayer p1 = new LookupTablePlayer("Player X", Mark.CROSS, tableX);
				LookupTablePlayer p2 = new LookupTablePlayer("Player O", Mark.NOUGHT, tableO);
				
				//rozgrywanie gier
				int games = 200000;
				long start = System.currentTimeMillis();
				for( int i = 0; i<games; i++)
					new GameModel().fastPlay( p1, p2);
				System.out.println("Playing " + games + " games took " + (System.currentTimeMillis()-start)/1000 + "s.");
				
				//generowanie danych testowych
				AppController.this.model.addTrainingData( 
						tableX.getAfterstateTrainingData(false));
				AppController.this.model.addTrainingData( 
						tableO.getAfterstateTrainingData(false));
				
				p2.setTrainingInProgress(false);
				
				blockingQueue.add( new NewGameEvent(new HumanPlayer("Human human", Mark.CROSS), p2 ));
			}
		});
		
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
		
		this.eventActionMap.put( StartTrainingEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event ) {
				StartTrainingEvent sTE = ( StartTrainingEvent ) event;				
			}
		});
		
		this.eventActionMap.put( TrainingEndedEvent.class, new ProgramAction() 
		{	
			@Override
			public void go(ProgramEvent event) 
			{
				TrainingEndedEvent tEE = ( TrainingEndedEvent ) event;
				try {
					AppController.this.model.addNetwork(tEE.trainer.get());
					AppController.this.view.getNeuralNetworksWindow().populateNetworkList(
							AppController.this.model.getNetworkListModel());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
				AppController.this.view.getNeuralNetworksWindow().populateNetworkList(
						AppController.this.model.getNetworkListModel());
				AppController.this.view.getNeuralNetworksWindow().populateTrainingDataList(
						AppController.this.model.getTrainingDataListModel());
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
						AppController.this.model.getFilteredNetworkListModel(9, 1));				
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
