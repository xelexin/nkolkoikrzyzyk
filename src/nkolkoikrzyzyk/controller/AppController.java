/**
 * Controller.java
 */
package nkolkoikrzyzyk.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

import nkolkoikrzyzyk.commons.GameData;
import nkolkoikrzyzyk.controller.players.ChaoticNeutralPlayer;
import nkolkoikrzyzyk.controller.players.LookupTablePlayer;
import nkolkoikrzyzyk.events.DeleteLookupTableEvent;
import nkolkoikrzyzyk.events.DeletePlayerEvent;
import nkolkoikrzyzyk.events.GenerateTrainingDataEvent;
import nkolkoikrzyzyk.events.LoadNetworkEvent;
import nkolkoikrzyzyk.events.LoadTrainingDataEvent;
import nkolkoikrzyzyk.events.NewGameEvent;
import nkolkoikrzyzyk.events.NewLookupTableEvent;
import nkolkoikrzyzyk.events.NewNetworkEvent;
import nkolkoikrzyzyk.events.NewPlayerEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.events.SaveNetworkEvent;
import nkolkoikrzyzyk.events.SaveTrainingDataEvent;
import nkolkoikrzyzyk.events.StartGameModuleEvent;
import nkolkoikrzyzyk.events.StartNeuralNetworksModuleEvent;
import nkolkoikrzyzyk.events.StartTestEvent;
import nkolkoikrzyzyk.events.StartTrainingEvent;
import nkolkoikrzyzyk.events.TestEndedEvent;
import nkolkoikrzyzyk.events.TrainingEndedEvent;
import nkolkoikrzyzyk.model.GameModel;
import nkolkoikrzyzyk.model.LookupTable;
import nkolkoikrzyzyk.model.Mark;
import nkolkoikrzyzyk.model.Model;
import nkolkoikrzyzyk.model.NeuralNetwork;
import nkolkoikrzyzyk.model.TrainingData;
import nkolkoikrzyzyk.model.TrainingData.TrainingDataType;
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
	public AppController(final View view, final  Model model, final BlockingQueue<ProgramEvent> blockingQueue) 
	{
		this.view = view;
		this.model = model;
		this.blockingQueue = blockingQueue;
		this.eventActionMap = 
				new HashMap<Class<? extends ProgramEvent>, ProgramAction>();
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
		startTestEventHandler();
		startNewGameEventHandler();	
		startTrainingEventHandler();
		trainingEndedEventHandler();
		startNeuralNetworksModuleEventHandler();
		loadNetworkEventHandler();
		saveNetworkEventHandler();
		newNetworkEventHandler();
		newLookupTableEventHandler();
		startGameModuleEventHandler();
		generateTrainingDataEventHandler();
		deleteLookupTableEventHandler();
		loadTrainingDataEventHandler();
		saveTrainingDataEventHandler();
		deletePlayerEventHandler();
		newPlayerEventHandler();
		testEndedEventHandler();
	}

	private void testEndedEventHandler() 
	{
		this.eventActionMap.put( TestEndedEvent.class, new ProgramAction() 
		{	
			@Override
			public void go(ProgramEvent event) 
			{
				TestEndedEvent tEE = ( TestEndedEvent ) event;
				try {
					AppController.this.view.getGameModuleWindow().setResults(tEE.tester.get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}

	private void newPlayerEventHandler() 
	{
		this.eventActionMap.put(NewPlayerEvent.class, new ProgramAction() 
		{	
			@Override
			public void go(ProgramEvent event) 
			{
				NewPlayerEvent nPE = (NewPlayerEvent)event;
				AppController.this.model.addPlayer(nPE.player);
				AppController.this.refreshPlayerList();
			}
		});
	}

	private void deletePlayerEventHandler() 
	{
		this.eventActionMap.put(DeletePlayerEvent.class, new ProgramAction()
		{	
			@Override
			public void go(ProgramEvent event) 
			{
				DeletePlayerEvent dPE = (DeletePlayerEvent) event;
				AppController.this.model.deletePlayer(dPE.player);	
				refreshPlayerList();
			}
		});
		
	}

	private void startGameModuleEventHandler()
	{
		this.eventActionMap.put( StartGameModuleEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event )
			{
				StartGameModuleEvent sGME = (StartGameModuleEvent) event;
				AppController.this.view.getGameModuleWindow().populateLists(
						model.getFilteredNetworkListModel(9, 1),
						model.getFilteredNetworkListModel(9, 9),
						model.getLookupTableListModel()
						);
			}
		});
	}
	
	private void startTestEventHandler()
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
				LookupTable tableXO = new LookupTable("Table XO");
				//gracze treningowi
				LookupTablePlayer p1 = new LookupTablePlayer("Player X", Mark.CROSS, tableX);
				LookupTablePlayer p2 = new LookupTablePlayer("Player O", Mark.NOUGHT, tableO);

				//LookupTablePlayer p3 = new LookupTablePlayer("Player XO-X", Mark.CROSS, tableXO);
				//LookupTablePlayer p4 = new LookupTablePlayer("Player XO-O", Mark.NOUGHT, tableXO);

				//rozgrywanie gier
				System.out.println("Learning LUTs...");
				int games = 500000;
				long start = System.currentTimeMillis();
				for( int i = 0; i<games; i++)
					new GameModel().fastPlay( p1, p2);
				System.out.println("Playing " + games + " games took " + (System.currentTimeMillis()-start)/1000 + "s.");

				System.out.println("Player 1: Win: " + p1.getWinCounter() + 
						" Draw: " + p1.getDrawCounter() + " Lost: " + p1.getLostCounter());
				System.out.println("Player 2: Win: " + p2.getWinCounter() + 
						" Draw: " + p2.getDrawCounter() + " Lost: " + p2.getLostCounter());
				
				// TODO: remove
				TrainingData beforeStatesData = p1.getLookupTable().getBeforestatesTrainingData();
				beforeStatesData.saveToFile(new File("before.txt"));
				System.out.println("Player 1 LUT: " + p1.getLookupTable().toString());
				System.out.println("Player 2 LUT: " + p2.getLookupTable().toString());
				
				System.out.println("Playing LUTs vs random...");
				p1.resetCounters();
				p2.resetCounters();
				p1.setTrainingInProgress(false);
				p2.setTrainingInProgress(false);
				start = System.currentTimeMillis();
				for( int i = 0; i<2*games; i++)
				{
					new GameModel().fastPlay( p1, new ChaoticNeutralPlayer("Chaos", Mark.NOUGHT));
					new GameModel().fastPlay( new ChaoticNeutralPlayer("Chaos", Mark.CROSS), p2 );
				}
					
				System.out.println("Playing " + 2*games + " games took " + (System.currentTimeMillis()-start)/1000 + "s.");
				System.out.println("Player 1: Win: " + p1.getWinCounter() + 
						" Draw: " + p1.getDrawCounter() + " Lost: " + p1.getLostCounter());
				System.out.println("Player 2: Win: " + p2.getWinCounter() + 
						" Draw: " + p2.getDrawCounter() + " Lost: " + p2.getLostCounter());
				
				
				System.out.println("Playing LUT 1 vs LUT 2...");
				p1.resetCounters();
				p2.resetCounters();
				start = System.currentTimeMillis();
				for( int i = 0; i<games; i++)
				{
					new GameModel().fastPlay( p1, p2);
				}
					
				System.out.println("Playing " + games + " games took " + (System.currentTimeMillis()-start)/1000 + "s.");
				System.out.println("Player 1: Win: " + p1.getWinCounter() + 
						" Draw: " + p1.getDrawCounter() + " Lost: " + p1.getLostCounter());
				System.out.println("Player 2: Win: " + p2.getWinCounter() + 
						" Draw: " + p2.getDrawCounter() + " Lost: " + p2.getLostCounter());
			}

			private File String(String string)
			{
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

	private void newLookupTableEventHandler()
	{
		this.eventActionMap.put( NewLookupTableEvent.class, new ProgramAction()
		{
			@Override
			public void go(ProgramEvent event)
			{
				NewLookupTableEvent nLTE = (NewLookupTableEvent) event;
				LookupTable table = new LookupTable(nLTE.name);
				AppController.this.model.addLookupTable(table);
				AppController.this.refreshLookupTableList();
			}
		});
	}

	private void deleteLookupTableEventHandler()
	{
		this.eventActionMap.put( DeleteLookupTableEvent.class, new ProgramAction()
		{
			@Override
			public void go(ProgramEvent event)
			{
				DeleteLookupTableEvent dLTE = (DeleteLookupTableEvent)event;
				AppController.this.model.deleteLookupTable( dLTE.table);
				AppController.this.refreshLookupTableList();
				
			}
		});
		
	}

	private void newNetworkEventHandler()
	{
		this.eventActionMap.put( NewNetworkEvent.class, new ProgramAction()
		{
			@Override
			public void go(ProgramEvent event)
			{
				NewNetworkEvent nNE = (NewNetworkEvent) event;
				NeuralNetwork network = new NeuralNetwork(nNE.name, nNE.inputSize, nNE.layersSize, nNE.variance, nNE.isSigmoid);
				AppController.this.model.addNetwork(network);
				refreshNetworkList();
			}
		});
	}

	private void loadNetworkEventHandler()
	{
		this.eventActionMap.put( LoadNetworkEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event ) {
				LoadNetworkEvent lNE = ( LoadNetworkEvent ) event;
				AppController.this.model.addNetwork( new NeuralNetwork(lNE.file) );
				refreshNetworkList();
			}
		});
	}

	private void saveNetworkEventHandler()
	{
		this.eventActionMap.put( SaveNetworkEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event ) {
				SaveNetworkEvent sNE = ( SaveNetworkEvent ) event;
				sNE.network.saveToFile(sNE.file);
			}
		});
	}

	private void startNeuralNetworksModuleEventHandler()
	{
		this.eventActionMap.put( StartNeuralNetworksModuleEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event ) {
				StartNeuralNetworksModuleEvent sNNME = ( StartNeuralNetworksModuleEvent ) event;
				refreshNetworkList();
				refreshTrainingDataList();
				refreshLookupTableList();
			}
		});
	}

	private void loadTrainingDataEventHandler()
	{
		this.eventActionMap.put( LoadTrainingDataEvent.class, new ProgramAction()
		{
			@Override
			public void go(ProgramEvent event)
			{
				LoadTrainingDataEvent lTDE = (LoadTrainingDataEvent)event;
				AppController.this.model.addTrainingData(new TrainingData(lTDE.file) );
				refreshTrainingDataList();
			}
		});
	}

	private void saveTrainingDataEventHandler()
	{
		this.eventActionMap.put( SaveTrainingDataEvent.class, new ProgramAction()
		{
			@Override
			public void go(ProgramEvent event)
			{
				SaveTrainingDataEvent sTDE = (SaveTrainingDataEvent)event;
				sTDE.data.saveToFile(sTDE.file);
			}
		});
	}
	
	private void generateTrainingDataEventHandler()
	{
		this.eventActionMap.put( GenerateTrainingDataEvent.class, new ProgramAction()
		{
			@Override
			public void go(ProgramEvent event)
			{
				GenerateTrainingDataEvent gTDE = (GenerateTrainingDataEvent)event;
				AppController.this.model.addTrainingData( 
						gTDE.type == TrainingDataType.AFTERSTATES?
								gTDE.table.getAfterstateTrainingData()
								:
								gTDE.table.getBeforestatesTrainingData()	
						);
				refreshTrainingDataList();
			}
		});		
	}

	private void startTrainingEventHandler()
	{
		this.eventActionMap.put( StartTrainingEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event ) {
				StartTrainingEvent sTE = ( StartTrainingEvent ) event;				
			}
		});
	}

	private void trainingEndedEventHandler()
	{
		this.eventActionMap.put( TrainingEndedEvent.class, new ProgramAction() 
		{	
			@Override
			public void go(ProgramEvent event) 
			{
				TrainingEndedEvent tEE = ( TrainingEndedEvent ) event;
				try {
					AppController.this.model.addNetwork(tEE.trainer.get());
					refreshNetworkList();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void startNewGameEventHandler()
	{
		this.eventActionMap.put( NewGameEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event ) 
			{
				NewGameEvent nGE = ( NewGameEvent ) event;
				BlockingQueue<ProgramEvent> gameQueue = new LinkedBlockingQueue<ProgramEvent>();
				GameModel model = new GameModel();
				GameData gameData = new GameData(model.getId(), nGE.player1.getName(), nGE.player2.getName());
				GameWindow gameView = new GameWindow(gameQueue, gameData);
				AppController.this.view.addGameWindow(gameView);
				GameController controller = new GameController(gameView, model, gameQueue, blockingQueue, nGE.player1, nGE.player2);
				new Thread(controller).start();
			}
		});
	}
	
	private void refreshLookupTableList()
	{
		this.view.getNeuralNetworksWindow().populateLookupTableList(
				this.model.getLookupTableListModel());
	}

	private void refreshNetworkList()
	{
		AppController.this.view.getNeuralNetworksWindow().populateNetworkList(
				AppController.this.model.getNetworkListModel()
				);
	}

	private void refreshPlayerList() 
	{
		AppController.this.view.getGameModuleWindow().populatePlayerList(
				AppController.this.model.getPlayerListModel()
				);
		
	}

	private void refreshTrainingDataList()
	{
		AppController.this.view.getNeuralNetworksWindow().populateTrainingDataList(
				AppController.this.model.getTrainingDataListModel()
				);
	}
}
